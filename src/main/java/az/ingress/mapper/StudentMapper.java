package az.ingress.mapper;

import az.ingress.dao.entity.StudentEntity;
import az.ingress.model.request.CreateUserRequest;
import az.ingress.model.request.RegistrationRequest;
import az.ingress.model.response.StudentResponse;

public enum StudentMapper {

    STUDENT_MAPPER;

    public StudentEntity toStudentEntity(CreateUserRequest userRequest) {
        return StudentEntity.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .age(userRequest.getAge())
                .build();
    }

    public StudentResponse toStudentResponse(StudentEntity studentEntity) {
        return StudentResponse.builder()
                .id(studentEntity.getId())
                .firstName(studentEntity.getFirstName())
                .lastName(studentEntity.getLastName())
                .age(studentEntity.getAge())
                .build();
    }
}
