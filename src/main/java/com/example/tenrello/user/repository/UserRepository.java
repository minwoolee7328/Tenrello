package com.example.tenrello.user.repository;

import com.example.tenrello.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;

@RepositoryDefinition(domainClass = User.class, idClass = Long.class)
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryQuery {
    Optional<User> findByUsername(String username);
    Optional<User> findByNickname(String nickname);

}
