package com.drgproject.repository;

import com.drgproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByFio(String username);
}
