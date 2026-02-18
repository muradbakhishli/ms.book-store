package az.ingress.service.concrete;

import az.ingress.annotation.LogIgnore;
import az.ingress.dao.entity.UserEntity;
import az.ingress.dao.repository.UserRepository;
import az.ingress.exception.AlreadyExistException;
import az.ingress.exception.NotFoundException;
import az.ingress.model.request.RegistrationRequest;
import az.ingress.model.response.UserResponse;
import az.ingress.service.abstraction.UserService;
import az.ingress.service.strategy.RegistrationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static az.ingress.exception.ErrorMessage.ALREADY_EXIST_USER;
import static az.ingress.exception.ErrorMessage.USER_NOT_FOUND;
import static az.ingress.mapper.RoleMapper.ROLE_MAPPER;
import static az.ingress.mapper.UserMapper.USER_MAPPER;

@Service
@RequiredArgsConstructor
public class UserServiceHandler implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationStrategy registrationStrategy;


    @Override
    @Transactional
    @LogIgnore
    public void registrationUser(RegistrationRequest registrationRequest) {

        if (checkUsernameExists(registrationRequest.getEmail())) {
            throw new AlreadyExistException(ALREADY_EXIST_USER.getMessage());
        }

        var role = ROLE_MAPPER.toRoleEntity(registrationRequest.getRole());
        var user = USER_MAPPER.toUserEntity(registrationRequest, role);
        role.setUser(user);
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        userRepository.save(user);
        registrationStrategy.registrationStrategy(registrationRequest);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        return USER_MAPPER.toUserResponse(getUserIfExist(email));
    }

    private boolean checkUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    private UserEntity getUserIfExist(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage()));
    }
}
