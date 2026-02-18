package az.ingress.service

import az.ingress.model.jwt.AuthPayloadDto
import az.ingress.model.jwt.RefreshTokenRequest
import az.ingress.model.request.LoginRequest
import az.ingress.model.response.LoginResponse
import az.ingress.model.response.UserResponse
import az.ingress.service.abstraction.AuthenticationService
import az.ingress.service.abstraction.TokenService
import az.ingress.service.abstraction.UserService
import az.ingress.service.concrete.AuthenticationServiceHandler
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import spock.lang.Specification

class AuthenticationServiceTest extends Specification {

    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    UserService userService
    TokenService tokenService
    AuthenticationManager authenticationManager
    AuthenticationService authenticationService

    def setup() {
        userService = Mock()
        tokenService = Mock()
        authenticationManager = Mock()
        authenticationService = new AuthenticationServiceHandler(userService, tokenService, authenticationManager)
    }

    def "Test Login success case"() {
        given:
        def loginRequest = random.nextObject(LoginRequest)
        def userResponse = random.nextObject(UserResponse)
        def authPayload = AuthPayloadDto.of(userResponse.userId as String, userResponse.email)
        def loginResponse = random.nextObject(LoginResponse)

        when:
        def actual = authenticationService.login(loginRequest)

        then:
        1 * authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()
                )
        )
        1 * userService.getUserByEmail(loginRequest.getEmail()) >> userResponse
        1 * tokenService.generateToken(authPayload) >> loginResponse

        loginResponse.accessToken == actual.accessToken
        loginResponse.refreshToken == actual.refreshToken
    }

    def "Test Refresh method success case" () {
        given:
        def refreshTokenRequest = random.nextObject(RefreshTokenRequest)
        def loginResponse = random.nextObject(LoginResponse)

        when:
        def actual = authenticationService.refresh(refreshTokenRequest)

        then:
        1 * tokenService.refreshToken(refreshTokenRequest.getRefreshToken()) >> loginResponse

        loginResponse.accessToken == actual.accessToken
        loginResponse.refreshToken == actual.refreshToken

    }

}
