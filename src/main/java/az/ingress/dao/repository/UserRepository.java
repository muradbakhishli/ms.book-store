package az.ingress.dao.repository;

import az.ingress.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    boolean existsByUsername(String username);

    @EntityGraph(attributePaths = "roles")
    Optional<UserEntity> findByUsername(String username);
}
