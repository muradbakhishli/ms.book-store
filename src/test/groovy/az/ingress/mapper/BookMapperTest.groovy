package az.ingress.mapper

import az.ingress.dao.entity.AuthorEntity
import az.ingress.dao.entity.BookEntity
import az.ingress.dao.entity.StudentEntity
import az.ingress.model.enums.BookStatus
import az.ingress.model.request.BookRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.mapper.BookMapper.BOOK_MAPPER
import static az.ingress.model.enums.BookStatus.DELETED

class BookMapperTest extends Specification {

    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "TestMapRequestToEntity"() {
        given:
        def bookRequest = random.nextObject(BookRequest)

        when:
        def bookEntity = BOOK_MAPPER.toBookEntity(bookRequest)

        then:
        bookEntity.name == bookRequest.name
        bookEntity.isbn == bookRequest.isbn
    }

    def "TestSetDeleteStatus"() {
        given:
        def bookEntity = random.nextObject(BookEntity)

        when:
        BOOK_MAPPER.setDeleteStatus(bookEntity)

        then:
        bookEntity.status.equals(DELETED.name())
    }

    def "TestMapEntityToResponse"() {
        given:
        def bookEntity = random.nextObject(BookEntity)

        when:
        def bookResponse = BOOK_MAPPER.toBookResponse(bookEntity)

        then:
        bookResponse.id == bookEntity.id
        bookResponse.name == bookEntity.name
        bookResponse.isbn == bookEntity.isbn
    }

    def "TestSetBookAndAuthorEntityRelations"() {
        given:
        def book = random.nextObject(BookEntity)
        def author = random.nextObject(AuthorEntity)

        when:
        BOOK_MAPPER.setEntityRelations(book, author)

        then:
        book.author == author
    }

    def "TestSetBookAndStudentEntityRelations"() {
        given:
        def book = random.nextObject(BookEntity)
        def student = random.nextObject(StudentEntity)

        when:
        BOOK_MAPPER.setEntityRelations(book, student)

        then:
        book.students.contains(student)
    }

    def "TestUpdateBookEntity"() {
        given:
        def bookEntity = random.nextObject(BookEntity)
        def bookRequest = random.nextObject(BookRequest)

        when:
        BOOK_MAPPER.updateBook(bookEntity, bookRequest)

        then:
        bookEntity.name == bookRequest.name
        bookEntity.isbn == bookRequest.isbn
    }
}
