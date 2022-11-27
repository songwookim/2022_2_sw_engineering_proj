package com.example.finalprj.db.repository;

import com.example.finalprj.db.domain.Playground;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaygroundRepository extends JpaRepository<Playground, Long> {
    Optional<Playground> findPlaygroundByPgName(String pgName);

}
