package az.ingress.controller

import az.ingress.exception.ErrorHandler
import az.ingress.model.request.BookRequest
import az.ingress.model.response.BookResponse
import az.ingress.service.BookService
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

class BookControllerTest extends Specification {

    BookService bookService
    BookController bookController
    MockMvc mockMvc

    def setup() {
        bookService = Mock()
        bookController = new BookController(bookService)
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setControllerAdvice(ErrorHandler.class)
                .build()
    }

    def "Test DeleteBook success case"() {
        given:
        def id = 1L
        def authorId = 1L
        def url = "/v1/books/$id?authorId=$authorId"

        when:
        mockMvc.perform(
                delete(url)
                        .contentType(APPLICATION_JSON)
        ) andReturn()

        then:
        1 * bookService.deleteBook(id, authorId)
    }

    def "Test ReadBook success case"() {
        given:
        def id = 1L
        def studentId = 2L
        def url = "/v1/books/$id/read?studentId=$studentId"

        when:
        mockMvc.perform(
                put(url)
                        .contentType(APPLICATION_JSON)
        ).andReturn()

        then:
        1 * bookService.readBook(id, studentId)
    }

    def "Test UpdateBook success case"() {
        given:
        def id = 1L
        def url = "/v1/books/$id"
        def bookRequest = BookRequest.builder()
                .name("Book")
                .isbn("12345678910")
                .build()

        def jsonBookRequest = """ 
                                           {
                                              "name": "Book",
                                              "isbn": "12345678910"                                           
                                           }
                                     """

        when:
        mockMvc.perform(
                put(url)
                        .contentType(APPLICATION_JSON)
                        .content(jsonBookRequest)
        ).andReturn()

        then:
        1 * bookService.updateBook(id, bookRequest)
    }

    def "Test GetBook success case"() {
        given:
        def id = 1L
        def url = "/v1/books/$id"
        def bookResponse = BookResponse.builder()
                .id(id)
                .name("Book")
                .isbn("12345678910")
                .build()

        def jsonBookResponse = """
                                           {
                                              "id": $id,
                                              "name": "Book",
                                              "isbn": "12345678910"
                                           }
                                      """
        when:
        def actual = mockMvc.perform(
                get(url)
                        .contentType(APPLICATION_JSON)
        ).andReturn()

        then:
        1 * bookService.getBook(id) >> bookResponse
        actual.response.status == OK.value()
        JSONAssert.assertEquals(jsonBookResponse.toString(),
                                actual.response.contentAsString.toString(),
                                true
        )
    }

}
