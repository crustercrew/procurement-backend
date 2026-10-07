package com.crustercrew.userservice.repositories;

import com.crustercrew.enums.UserRole;
import com.crustercrew.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByRole(UserRole role);
}