package com.drgproject.repository;

import com.drgproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNumberTable(String numberTable);
    void deleteByNumberTable(String numberTable);

}
