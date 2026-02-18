package az.ingress.service.concrete;

import az.ingress.annotation.Log;
import az.ingress.dao.entity.BookEntity;
import az.ingress.dao.repository.BookRepository;
import az.ingress.exception.ErrorMessage;
import az.ingress.exception.NotFoundException;
import az.ingress.model.request.BookRequest;
import az.ingress.model.response.BookResponse;
import az.ingress.service.BookService;
import az.ingress.service.abstraction.AuthorService;
import az.ingress.service.abstraction.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import static az.ingress.exception.ErrorMessage.BOOKS_NOT_FOUND;
import static az.ingress.mapper.BookMapper.BOOK_MAPPER;

@Log
@Service
public class BookServiceHandler implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final StudentService studentService;

    public BookServiceHandler(BookRepository bookRepository,
                              AuthorService authorService,
                              @Lazy StudentService studentService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.studentService = studentService;
    }

    @Override
    public void createBook(Long authorId, BookRequest bookRequest) {
        var author = authorService.findAuthorByIdIfExists(authorId);
        var book = BOOK_MAPPER.toBookEntity(bookRequest);
        BOOK_MAPPER.setEntityRelations(book, author);
        bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id, Long authorId) {
        var author = authorService.findAuthorByIdIfExists(authorId);
        var book = bookRepository.findBookByIdAndAuthor(id, author)
                .orElseThrow(() -> new NotFoundException(BOOKS_NOT_FOUND.getMessage()));
        BOOK_MAPPER.setDeleteStatus(book);
        bookRepository.save(book);
    }

    @Override
    public BookResponse getBook(Long id) {
        var book = findBookIfExist(id);
        return BOOK_MAPPER.toBookResponse(book);
    }

    @Override
    public void readBook(Long id, Long studentId) {
        var student = studentService.findStudentByIdIfExists(studentId);
        var book = findBookIfExist(id);
        BOOK_MAPPER.setEntityRelations(book, student);
        bookRepository.save(book);
    }

    @Override
    public void updateBook(Long id, BookRequest bookRequest) {
        var bookEntity = findBookIfExist(id);
        BOOK_MAPPER.updateBook(bookEntity, bookRequest);
        bookRepository.save(bookEntity);
    }

    @Override
    public BookEntity findBookIfExist(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BOOKS_NOT_FOUND.getMessage()));
    }
}
