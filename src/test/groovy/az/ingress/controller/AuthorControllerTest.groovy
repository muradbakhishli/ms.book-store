package az.ingress.controller

import az.ingress.exception.ErrorHandler
import az.ingress.service.abstraction.AuthorService
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

class AuthorControllerTest extends Specification {

    AuthorService authorService
    AuthorController authorController
    MockMvc mockMvc

    def setup() {
        authorService = Mock()
        authorController = new AuthorController(authorService)
        mockMvc = MockMvcBuilders.standaloneSetup(authorController)
                .setControllerAdvice(ErrorHandler.class)
                .build()
    }

    def "Test subscribe success case"() {

        given:
        def id = 1L
        def studentId = 1L
        def url = "/v1/authors/$id/subscribe?studentId=$studentId"

        when:
        mockMvc.perform(
                put(url)
                        .contentType(APPLICATION_JSON)
        )

        then:
        1 * authorService.subscribe(id, studentId)
    }

}
