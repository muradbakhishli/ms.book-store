package az.ingress.controller

import az.ingress.exception.ErrorHandler
import az.ingress.model.enums.UserRole
import az.ingress.model.request.RegistrationRequest
import az.ingress.service.abstraction.UserService
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static az.ingress.model.enums.UserRole.AUTHOR
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class UserControllerTest extends Specification {

    UserService userService
    UserController userController
    MockMvc mockMvc

    def setup() {
        userService = Mock()
        userController = new UserController(userService)
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(ErrorHandler.class)
                .build()
    }

    def "Test Registration User success case"() {
        given:
        def url = "/v1/users"
        def registrationRequest = RegistrationRequest.builder()
                .firstName("Murad")
                .lastName("Baxisli")
                .email("muradbaxisli202@gmail.com")
                .password("murad12345")
                .age(24)
                .role(AUTHOR)
                .build()
        def jsonRequest = """
                                    {
                                        "firstName": "Murad",
                                        "lastName": "Baxisli",
                                        "email": "muradbaxisli202@gmail.com",
                                        "age": 24,
                                        "password": "murad12345",
                                        "role": "AUTHOR"
                                    }
                                 """

        when:
        mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
        ).andReturn()

        then:
        1 * userService.registrationUser(registrationRequest)


    }
}
