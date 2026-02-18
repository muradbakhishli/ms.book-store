package az.ingress.exception;

import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {

    private int statusCode;

    public AuthenticationException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
