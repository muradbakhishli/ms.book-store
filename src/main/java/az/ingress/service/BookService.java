package az.ingress.service;

import az.ingress.dao.entity.BookEntity;
import az.ingress.model.request.BookRequest;
import az.ingress.model.response.BookResponse;
import jakarta.validation.Valid;

public interface BookService {

    void createBook(Long authorId, BookRequest bookRequest);

    void deleteBook(Long id, Long authorId);

    BookResponse getBook(Long id);

    void readBook(Long id, Long studentId);

    void updateBook(Long id, BookRequest bookRequest);

    BookEntity findBookIfExist(Long id);
}
