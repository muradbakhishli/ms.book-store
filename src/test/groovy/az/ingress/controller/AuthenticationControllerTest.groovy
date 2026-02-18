package az.ingress.controller

import az.ingress.exception.ErrorHandler
import az.ingress.model.jwt.RefreshTokenRequest
import az.ingress.model.request.LoginRequest
import az.ingress.model.response.LoginResponse
import az.ingress.service.abstraction.AuthenticationService
import az.ingress.service.concrete.AuthenticationServiceHandler
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class AuthenticationControllerTest extends Specification {

    AuthenticationService authenticationService
    MockMvc mockMvc

    def setup() {
        authenticationService = Mock()
        AuthenticationController authenticationController = new AuthenticationController(authenticationService)
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setControllerAdvice(ErrorHandler.class)
                .build()
    }

    def "Test Login success case"() {
        given:
        def url = "/v1/auth/login"
        def loginRequest = new LoginRequest("muradbaxisli202@gmail.com", "murad2002")
        def loginResponse = LoginResponse.of("accessToken", "refreshToken")
        def jsonRequest = """
                                      {
                                        "email": "muradbaxisli202@gmail.com",
                                        "password": "murad2002"                                 
                                      }
                                 """

        def expectedResponseJson = """
                                              {
                                                 "accessToken": "accessToken",
                                                 "refreshToken": "refreshToken"
                                              }
                                          """

        when:
        def jsonResponse = mockMvc.perform(
                post(url)
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest)
        ).andReturn()

        then:
        1 * authenticationService.login(loginRequest) >> loginResponse
        jsonResponse.response.status == OK.value()
        JSONAssert.assertEquals(expectedResponseJson.toString(), jsonResponse.response.contentAsString.toString(), true)
    }

    def "Test Refresh success case"() {
        given:
        def url = "/v1/auth/refresh"
        def refreshTokenRequest = new RefreshTokenRequest("refreshToken")
        def loginResponse = LoginResponse.of("accessToken", "refreshToken")
        def jsonRequest = """
                                     {
                                       "refreshToken": "refreshToken"
                                     }
                                 """
        def expectedResponseJson = """
                                              {
                                                 "accessToken": "accessToken",
                                                 "refreshToken": "refreshToken"
                                              }
                                          """

        when:
        def jsonResponse = mockMvc.perform(
                post(url)
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest)
        ).andReturn()

        then:
        1 * authenticationService.refresh(refreshTokenRequest) >> loginResponse
        jsonResponse.response.status == OK.value()
        JSONAssert.assertEquals(expectedResponseJson.toString(), jsonResponse.response.contentAsString.toString(), true)
    }


}
