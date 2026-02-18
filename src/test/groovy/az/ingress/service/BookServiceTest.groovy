package az.ingress.service

import az.ingress.dao.entity.AuthorEntity
import az.ingress.dao.entity.BookEntity
import az.ingress.dao.entity.StudentEntity
import az.ingress.dao.repository.BookRepository
import az.ingress.exception.ErrorMessage
import az.ingress.exception.NotFoundException
import az.ingress.model.request.BookRequest
import az.ingress.model.response.BookResponse
import az.ingress.service.abstraction.AuthorService
import az.ingress.service.abstraction.StudentService
import az.ingress.service.concrete.BookServiceHandler
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.exception.ErrorMessage.AUTHOR_NOT_FOUND
import static az.ingress.exception.ErrorMessage.AUTHOR_NOT_FOUND
import static az.ingress.exception.ErrorMessage.BOOKS_NOT_FOUND
import static az.ingress.exception.ErrorMessage.STUDENT_NOT_FOUND

class BookServiceTest extends Specification {

    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    BookRepository bookRepository
    AuthorService authorService
    StudentService studentService
    BookService bookService

    def setup() {
        bookRepository = Mock()
        authorService = Mock()
        studentService = Mock()
        bookService = new BookServiceHandler(bookRepository, authorService, studentService)
    }

    def "Test create book method success case"() {
        given:
        def authorId = random.nextLong()
        def bookRequest = random.nextObject(BookRequest)
        def authorEntity = random.nextObject(AuthorEntity)

        when:
        bookService.createBook(authorId, bookRequest)

        then:
        1 * authorService.findAuthorByIdIfExists(authorId) >> authorEntity
        1 * bookRepository.save(_)
    }

    def "Test create book method when author not found case"() {
        given:
        def authorId = random.nextLong()
        def bookRequest = random.nextObject(BookRequest)

        when:
        bookService.createBook(authorId, bookRequest)

        then:
        1 * authorService.findAuthorByIdIfExists(authorId) >> { throw new NotFoundException(AUTHOR_NOT_FOUND.getMessage()) }
        thrown(NotFoundException)
    }

    def "Test delete book method success case"() {
        given:
        def id = random.nextLong()
        def authorId = random.nextLong()
        def authorEntity = random.nextObject(AuthorEntity)
        def bookEntity = random.nextObject(BookEntity)

        when:
        bookService.deleteBook(id, authorId)

        then:
        1 * authorService.findAuthorByIdIfExists(authorId) >> authorEntity
        1 * bookRepository.findBookByIdAndAuthor(id, authorEntity) >> Optional.of(bookEntity)
        1 * bookRepository.save(_)
    }

    def "Test delete book method when author not found case"() {
        given:
        def id = random.nextLong()
        def authorId = random.nextLong()

        when:
        bookService.deleteBook(id, authorId)

        then:
        1 * authorService.findAuthorByIdIfExists(authorId) >> { throw new NotFoundException(AUTHOR_NOT_FOUND.getMessage()) }
        thrown(NotFoundException)
        0 * bookRepository.findBookByIdAndAuthor(_, _)
        0 * bookRepository.save(_)
    }

    def "Test delete book method when author and book not found case"() {
        given:
        def id = random.nextLong()
        def authorId = random.nextLong()
        def authorEntity = random.nextObject(AuthorEntity)

        when:
        bookService.deleteBook(id, authorId)

        then:
        1 * authorService.findAuthorByIdIfExists(authorId) >> authorEntity
        1 * bookRepository.findBookByIdAndAuthor(id, authorEntity) >> Optional.empty()
        thrown(NotFoundException)
        0 * bookRepository.save(_)
    }

    def "Test get book method success case"() {
        given:
        def id = random.nextLong()
        def bookEntity = random.nextObject(BookEntity)

        when:
        def actual = bookService.getBook(id)

        then:
        1 * bookRepository.findById(id) >> Optional.of(bookEntity)
        actual.id == bookEntity.id
        actual.name == bookEntity.name
        actual.isbn == bookEntity.isbn
    }

    def "Test readBook method success case"() {
        given:
        def id = random.nextLong()
        def studentId = random.nextLong()
        def student = random.nextObject(StudentEntity)
        def bookEntity = random.nextObject(BookEntity)

        when:
        bookService.readBook(id, studentId)

        then:
        1 * studentService.findStudentByIdIfExists(studentId) >> student
        1 * bookRepository.findById(id) >> Optional.of(bookEntity)
        1 * bookRepository.save(_)
    }

    def "Test readBook method student not found case"() {
        given:
        def id = random.nextLong()
        def studentId = random.nextLong()

        when:
        bookService.readBook(id, studentId)

        then:
        1 * studentService.findStudentByIdIfExists(studentId) >> { throw new NotFoundException(STUDENT_NOT_FOUND.getMessage()) }
        thrown(NotFoundException)
    }

    def "Test readBook method book not found case"() {
        given:
        def id = random.nextLong()
        def studentId = random.nextLong()
        def student = random.nextObject(StudentEntity)

        when:
        bookService.readBook(id, studentId)

        then:
        1 * studentService.findStudentByIdIfExists(studentId) >> student
        1 * bookRepository.findById(id) >> Optional.empty();
        thrown(NotFoundException)
    }


}
