package az.ingress.mapper

import az.ingress.model.constant.AuthConstant
import az.ingress.model.jwt.AuthPayloadDto
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.mapper.TokenMapper.TOKEN_MAPPER
import static az.ingress.model.constant.AuthConstant.ISSUER

class TokenMapperTest extends Specification {

    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "TestCreateAccessTokenClaimsSet"() {
        given:
        def authPayloadDto = random.nextObject(AuthPayloadDto)
        def expirationDate = random.nextObject(Date)

        when:
        def accessTokenClaimsSet =
                TOKEN_MAPPER.toAccessTokenClaimsSet(authPayloadDto, expirationDate)

        then:
        accessTokenClaimsSet.userId == authPayloadDto.userId
        accessTokenClaimsSet.username == authPayloadDto.username
        accessTokenClaimsSet.expirationTime == expirationDate
    }

    def "TestCreateRefreshTokenClaimsSet"() {
        given:
        def authPayLoadDto = random.nextObject(AuthPayloadDto)
        def expirationDate = random.nextObject(Date)
        def count = random.nextInt()

        when:
        def refreshTokenClaimsSet = TOKEN_MAPPER.toRefreshTokenRequest(authPayLoadDto, expirationDate, count)

        then:
        refreshTokenClaimsSet.username == authPayLoadDto.username
        refreshTokenClaimsSet.userId == authPayLoadDto.userId
        refreshTokenClaimsSet.expirationTime == expirationDate
        refreshTokenClaimsSet.count == count
    }
}
