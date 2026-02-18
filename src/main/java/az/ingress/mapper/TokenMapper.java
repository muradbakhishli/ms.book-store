package az.ingress.mapper;

import az.ingress.model.constant.AuthConstant;
import az.ingress.model.jwt.AccessTokenClaimsSet;
import az.ingress.model.jwt.AuthPayloadDto;
import az.ingress.model.jwt.RefreshTokenClaimsSet;
import az.ingress.model.jwt.RefreshTokenRequest;

import java.util.Date;

import static az.ingress.model.constant.AuthConstant.ISSUER;

public enum TokenMapper {

    TOKEN_MAPPER;

    public AccessTokenClaimsSet toAccessTokenClaimsSet(AuthPayloadDto dto, Date expirationTime) {
        return AccessTokenClaimsSet.builder()
                .username(dto.getUsername())
                .userId(dto.getUserId())
                .expirationTime(expirationTime)
                .createdTime(new Date())
                .iss(ISSUER)
                .build();
    }

    public RefreshTokenClaimsSet toRefreshTokenRequest(AuthPayloadDto dto, Date expirationTime, int count) {
        return RefreshTokenClaimsSet.builder()
                .username(dto.getUsername())
                .userId(dto.getUserId())
                .expirationTime(expirationTime)
                .count(count)
                .iss(ISSUER)
                .build();
    }
}
