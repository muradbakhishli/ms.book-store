package az.ingress.util;

import az.ingress.exception.AuthenticationException;
import az.ingress.logger.ApplicationLogger;
import az.ingress.model.jwt.AccessTokenClaimsSet;
import az.ingress.model.jwt.RefreshTokenClaimsSet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;

import static az.ingress.exception.ErrorMessage.USER_UNAUTHORIZED;
import static az.ingress.mapper.factory.ObjectMapperFactory.OBJECT_MAPPER_FACTORY;
import static az.ingress.model.constant.AuthConstant.KEY_SIZE;
import static az.ingress.model.constant.AuthConstant.RSA;
import static com.nimbusds.jose.JWSAlgorithm.RS256;

@Slf4j
@Component
public class JwtUtil {

    ApplicationLogger applicationLogger = ApplicationLogger.getLogger(JwtUtil.class);
    private static ObjectMapper objectMapper = OBJECT_MAPPER_FACTORY.createObjectMapper();

    public KeyPair generateKeyPair() {
        try {
            var keyPairGen = KeyPairGenerator.getInstance(RSA);
            keyPairGen.initialize(KEY_SIZE);
            return keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.error("ActionLog.generateKeyPair.error", e);
            throw new AuthenticationException(USER_UNAUTHORIZED.getMessage(), 401);
        }
    }

    public <T> String generateToken(T tokenClaimsSet, PrivateKey privateKey) {
        SignedJWT signedJWT;
        try {
            signedJWT = generateSignedJWT(objectMapper.writeValueAsString(tokenClaimsSet), privateKey);
            return signedJWT.serialize();
        } catch (Exception e) {
            log.error("ActionLog.generateToken.error", e);
            throw new AuthenticationException(USER_UNAUTHORIZED.getMessage(), 401);
        }
    }

    public void verifyToken(String token, RSAPublicKey publicKey) {
        try {
            var signedJwt = SignedJWT.parse(token);
            var verifier = new RSASSAVerifier(publicKey);

            if (!signedJwt.verify(verifier)) {
                log.error("ActionLog.verifyToken.error can't verify signedJwt");
                throw new AuthenticationException(USER_UNAUTHORIZED.getMessage(), 401);
            }
        } catch (ParseException | JOSEException e) {
            log.error("ActionLog.verifyToken.error can't parse token ", e);
            throw new AuthenticationException(USER_UNAUTHORIZED.getMessage(), 401);
        }
    }

    public Date generateSessionExpirationTime(Integer expirationMinutes) {
        return new Date(System.currentTimeMillis() + expirationMinutes * 60 * 1_000);
    }

    public boolean isRefreshTokenTimeExpired(RefreshTokenClaimsSet refreshTokenClaimsSet) {
        return refreshTokenClaimsSet.getExpirationTime().before(new Date());
    }

    public boolean isRefreshTokenCountExpired(RefreshTokenClaimsSet refreshTokenClaimsSet) {
        return refreshTokenClaimsSet.getCount() <= 0;
    }

    public AccessTokenClaimsSet getClaimsFromAccessToken(String token) {

        AccessTokenClaimsSet claimsSet;
        try {
            claimsSet = objectMapper.readValue(getClaimsFromToken(token).toString(), AccessTokenClaimsSet.class);
        } catch (IOException e) {
            log.error("ActionLog.getClaimsFromAccessToken.error can't parse access token", e);
            throw new AuthenticationException(USER_UNAUTHORIZED.getMessage(), 401);
        }
        return claimsSet;
    }

    public RefreshTokenClaimsSet getClaimsFromRefreshToken(String token) {
        RefreshTokenClaimsSet claimsSet;
        try {
            var claimsAsText = getClaimsFromToken(token).toString();
            log.info(claimsAsText);
            claimsSet = objectMapper.readValue(claimsAsText, RefreshTokenClaimsSet.class);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.error("ActionLog.getClaimsFromRefreshToken.error can't parse refresh token", e);
            throw new AuthenticationException(USER_UNAUTHORIZED.getMessage(), 401);
        }
        return claimsSet;
    }


    public boolean isTokenExpired(Date expirationTime) {
        return expirationTime.before(new Date());
    }

    @SneakyThrows
    private JWTClaimsSet getClaimsFromToken(String token) {
        return SignedJWT.parse(token).getJWTClaimsSet();
    }

    @SneakyThrows
    private SignedJWT generateSignedJWT(String tokenClaimsSetJson,
                                        PrivateKey privateKey) {
        var claimsSet = JWTClaimsSet.parse(tokenClaimsSetJson);
        var header = new JWSHeader(RS256);
        var signedJWT = new SignedJWT(header, claimsSet);
        var signer = new RSASSASigner(privateKey);
        signedJWT.sign(signer);
        return signedJWT;
    }
}
