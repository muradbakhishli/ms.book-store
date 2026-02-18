package az.ingress.controller;

import az.ingress.model.request.BookRequest;
import az.ingress.model.response.BookResponse;
import az.ingress.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyAuthority('AUTHOR')")
    public void createBook(@RequestParam Long authorId, @RequestBody @Valid BookRequest bookRequest) {
        bookService.createBook(authorId, bookRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('AUTHOR')")
    public void deleteBook(@PathVariable Long id,
                           @RequestParam Long authorId) {
        bookService.deleteBook(authorId, id);
    }

    @GetMapping("/{id}")
    public BookResponse getBook(@PathVariable Long id) {
        return bookService.getBook(id);
    }

    @PutMapping("/{id}/read")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public void readBook(@PathVariable Long id,
                         @RequestParam Long studentId) {
        bookService.readBook(id, studentId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('AUTHOR')")
    public void updateBook(@PathVariable Long id,
                           @RequestBody @Valid BookRequest bookRequest) {
        bookService.updateBook(id, bookRequest);
    }
}
