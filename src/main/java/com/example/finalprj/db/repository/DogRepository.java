package com.example.finalprj.db.repository;

import com.example.finalprj.db.domain.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long> {
}
