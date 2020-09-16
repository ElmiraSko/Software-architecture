package ru.erasko.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.erasko.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
