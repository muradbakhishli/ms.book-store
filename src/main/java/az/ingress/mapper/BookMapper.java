package az.ingress.mapper;

import az.ingress.dao.entity.AuthorEntity;
import az.ingress.dao.entity.BookEntity;
import az.ingress.dao.entity.StudentEntity;
import az.ingress.model.enums.BookStatus;
import az.ingress.model.request.BookRequest;
import az.ingress.model.response.BookResponse;

import java.util.List;
import java.util.Set;

import static az.ingress.model.enums.BookStatus.CREATED;
import static az.ingress.model.enums.BookStatus.DELETED;

public enum BookMapper {

    BOOK_MAPPER;

    public BookEntity toBookEntity(BookRequest bookRequest) {
        return BookEntity.builder()
                .name(bookRequest.getName())
                .isbn(bookRequest.getIsbn())
                .status(CREATED.name())
                .build();
    }

    public void setDeleteStatus(BookEntity bookEntity) {
        bookEntity.setStatus(DELETED.name());
    }

    public BookResponse toBookResponse(BookEntity bookEntity) {
        return BookResponse.builder()
                .id(bookEntity.getId())
                .name(bookEntity.getName())
                .isbn(bookEntity.getIsbn())
                .build();
    }

    public void setEntityRelations(BookEntity bookEntity, AuthorEntity authorEntity) {
        bookEntity.setAuthor(authorEntity);
        authorEntity.setBooks(Set.of(bookEntity));
    }

    public void setEntityRelations(BookEntity bookEntity, StudentEntity studentEntity) {
        bookEntity.setStudents(List.of(studentEntity));
        studentEntity.setBooks(Set.of(bookEntity));
    }

    public void updateBook(BookEntity bookEntity, BookRequest bookRequest) {
        bookEntity.setName(bookRequest.getName());
        bookEntity.setIsbn(bookRequest.getIsbn());
    }
}
