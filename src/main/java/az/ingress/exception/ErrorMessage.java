package az.ingress.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    UNEXPECTED_ERROR("Unexpected error occurred"),
    ALREADY_EXIST_USER("Already user exists with this username"),
    USER_UNAUTHORIZED("User is not authorized"),
    REFRESH_TOKEN_EXPIRED("Refresh token expired"),
    REFRESH_TOKEN_COUNT_EXPIRED("Refresh token count expired"),
    TOKEN_EXPIRED("Token expired"),
    USER_NOT_FOUND("User not found"),
    AUTHOR_NOT_FOUND("Author not found"),
    STUDENT_NOT_FOUND("Student not found"),
    BOOKS_NOT_FOUND("Books not found");

    private final String message;
}