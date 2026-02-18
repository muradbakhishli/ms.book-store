package az.ingress.controller;

import az.ingress.service.abstraction.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PutMapping("/{id}/subscribe")
    @ResponseStatus(NO_CONTENT)
    public void subscribe(@PathVariable Long id, @RequestParam Long studentId) {
        authorService.subscribe(id, studentId);
    }
}
