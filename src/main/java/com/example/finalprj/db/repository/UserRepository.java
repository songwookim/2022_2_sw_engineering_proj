package com.example.finalprj.db.repository;

import com.example.finalprj.db.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findAllByPlaygroundIsNotNull();

    @Query("select e.id from User e where e.email = ?1")
    Long findIdByEmail(String email);
}
