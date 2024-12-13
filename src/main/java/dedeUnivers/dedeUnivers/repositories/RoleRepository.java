package dedeUnivers.dedeUnivers.repositories;

import dedeUnivers.dedeUnivers.entities.Role;
import dedeUnivers.dedeUnivers.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findById(int id);
    Optional<Role> findByRoleType(RoleType roleType);
}