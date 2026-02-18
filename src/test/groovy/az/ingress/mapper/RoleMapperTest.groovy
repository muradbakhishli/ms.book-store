package az.ingress.mapper

import az.ingress.dao.entity.RoleEntity
import az.ingress.model.enums.UserRole
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.mapper.RoleMapper.ROLE_MAPPER

class RoleMapperTest extends Specification {

    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "TestRoleEntity"() {
        given:
        def userRole = random.nextObject(UserRole)

        when:
        def roleEntity = ROLE_MAPPER.toRoleEntity(userRole)

        then:
        roleEntity.authority == userRole.name()
    }

    def "TestMapEntityToResponse"() {
        given:
        def roleEntity = random.nextObject(RoleEntity)

        when:
        def roleResponse = ROLE_MAPPER.toRoleResponse(roleEntity)

        then:
        roleEntity.authority == roleEntity.authority

    }
}
