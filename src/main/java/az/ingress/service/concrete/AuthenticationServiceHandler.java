package az.ingress.service.concrete;

import az.ingress.model.jwt.AuthPayloadDto;
import az.ingress.model.jwt.RefreshTokenRequest;
import az.ingress.model.request.LoginRequest;
import az.ingress.model.response.LoginResponse;
import az.ingress.service.abstraction.AuthenticationService;
import az.ingress.service.abstraction.TokenService;
import az.ingress.service.abstraction.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceHandler implements AuthenticationService {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        authenticate(loginRequest);
        var user = userService.getUserByEmail(loginRequest.getEmail());
        return tokenService.generateToken(
                AuthPayloadDto.of(
                        user.getUserId().toString(),
                        user.getEmail())
        );
    }

    @Override
    public LoginResponse refresh(RefreshTokenRequest refreshTokenRequest) {
        return tokenService.refreshToken(refreshTokenRequest.getRefreshToken());
    }


    private void authenticate(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()
                )
        );
    }
}
