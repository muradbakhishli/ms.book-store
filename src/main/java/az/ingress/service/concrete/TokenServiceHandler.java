package az.ingress.service.concrete;

import az.ingress.annotation.Log;
import az.ingress.exception.AuthenticationException;
import az.ingress.logger.ApplicationLogger;
import az.ingress.model.cache.AuthCacheData;
import az.ingress.model.jwt.AuthPayloadDto;
import az.ingress.model.response.LoginResponse;
import az.ingress.service.abstraction.TokenService;
import az.ingress.util.CacheUtil;
import az.ingress.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import static az.ingress.exception.ErrorMessage.REFRESH_TOKEN_COUNT_EXPIRED;
import static az.ingress.exception.ErrorMessage.REFRESH_TOKEN_EXPIRED;
import static az.ingress.exception.ErrorMessage.TOKEN_EXPIRED;
import static az.ingress.exception.ErrorMessage.USER_UNAUTHORIZED;
import static az.ingress.mapper.TokenMapper.TOKEN_MAPPER;
import static az.ingress.model.constant.AuthConstant.AUTH_CACHE_DATA_PREFIX;
import static az.ingress.model.constant.AuthConstant.RSA;
import static az.ingress.model.constant.AuthConstant.TOKEN_EXPIRE_DAY_COUNT;
import static java.time.temporal.ChronoUnit.DAYS;
import static jodd.util.Base64.decode;
import static jodd.util.Base64.encodeToString;

@Service
@Log
@RequiredArgsConstructor
public class TokenServiceHandler implements TokenService {

    ApplicationLogger log = ApplicationLogger.getLogger(TokenServiceHandler.class);

    private final CacheUtil cacheUtil;
    private final JwtUtil jwtUtil;

    @Value("${jwt.access-token.expiration.time}")
    private int accessTokenExpirationTime;

    @Value("${jwt.refresh-token.expiration.time}")
    private int refreshTokenExpirationTime;

    @Value("${jwt.refresh-token.expiration.count}")
    private int refreshTokenExpirationCount;

    @Override
    public LoginResponse generateToken(AuthPayloadDto authPayloadDto) {
        var keyPair = jwtUtil.generateKeyPair();
        var accessTokenClaimsSet = TOKEN_MAPPER.
                toAccessTokenClaimsSet(authPayloadDto, jwtUtil.generateSessionExpirationTime(accessTokenExpirationTime));
        var refreshTokenClaimsSet = TOKEN_MAPPER.toRefreshTokenRequest(
                authPayloadDto,
                jwtUtil.generateSessionExpirationTime(refreshTokenExpirationTime),
                refreshTokenExpirationCount
        );

        var authCacheData = AuthCacheData.builder()
                .accessTokenClaimsSet(accessTokenClaimsSet)
                .publicKey(encodeToString(keyPair.getPublic().getEncoded()))
                .build();

        cacheUtil.saveToCache(
                AUTH_CACHE_DATA_PREFIX + authPayloadDto.getUserId(),
                authCacheData,
                TOKEN_EXPIRE_DAY_COUNT,
                DAYS);

        var accessToken = jwtUtil.generateToken(accessTokenClaimsSet, keyPair.getPrivate());
        var refreshToken = jwtUtil.generateToken(refreshTokenClaimsSet, keyPair.getPrivate());
        return LoginResponse.of(accessToken, refreshToken);
    }


    @Override
    public LoginResponse refreshToken(String refreshToken) {
        var refreshTokenClaimsSet = jwtUtil.getClaimsFromRefreshToken(refreshToken);
        var userId = refreshTokenClaimsSet.getUserId();
        var username = refreshTokenClaimsSet.getUsername();
        var refreshTokenCount = refreshTokenClaimsSet.getCount() - 1;
        try {
            AuthCacheData authCacheData = cacheUtil.getBucket(AUTH_CACHE_DATA_PREFIX + userId);
            if (authCacheData == null) throw new AuthenticationException(USER_UNAUTHORIZED.getMessage(), 401);
            var publicKey = KeyFactory.getInstance(RSA).generatePublic(
                    new X509EncodedKeySpec(decode(authCacheData.getPublicKey()))
            );
            jwtUtil.verifyToken(refreshToken, (RSAPublicKey) publicKey);
            if (jwtUtil.isRefreshTokenTimeExpired(refreshTokenClaimsSet)) {
                throw new AuthenticationException(REFRESH_TOKEN_EXPIRED.getMessage(), 401);
            }
            if (jwtUtil.isRefreshTokenCountExpired(refreshTokenClaimsSet)) {
                throw new AuthenticationException(REFRESH_TOKEN_COUNT_EXPIRED.getMessage(), 401);
            }
            return generateToken(AuthPayloadDto.of(userId, username));
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AuthenticationException(USER_UNAUTHORIZED.getMessage(), 401);
        }
    }

    @Override
    public AuthPayloadDto validateToken(String accessToken) {
        try {
            var userId = jwtUtil.getClaimsFromAccessToken(accessToken).getUserId();
            var username = jwtUtil.getClaimsFromAccessToken(accessToken).getUsername();
            AuthCacheData authCacheData = cacheUtil.getBucket(AUTH_CACHE_DATA_PREFIX + userId);
            if (authCacheData == null) {
                throw new AuthenticationException(TOKEN_EXPIRED.getMessage(), 406);
            }
            var publicKey = KeyFactory.getInstance(RSA).generatePublic(
                    new X509EncodedKeySpec(decode(authCacheData.getPublicKey()))
            );
            jwtUtil.verifyToken(accessToken, (RSAPublicKey) publicKey);
            if (jwtUtil.isTokenExpired(authCacheData.getAccessTokenClaimsSet().getExpirationTime())) {
                throw new AuthenticationException(TOKEN_EXPIRED.getMessage(), 406);
            }
            return AuthPayloadDto.of(userId, username);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error(String.valueOf(ex));
            throw new AuthenticationException(USER_UNAUTHORIZED.getMessage(), 401);
        }
    }
}
