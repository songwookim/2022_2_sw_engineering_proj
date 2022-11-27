package com.example.finalprj.db.repository;

import com.example.finalprj.db.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

}
