package com.example.finalprj.db.repository;

import com.example.finalprj.db.domain.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    @Override
    Optional<Alert> findById(Long aLong);

    @Transactional
    @Modifying
    @Query("UPDATE Alert a set a.status = :status where a.id = :id")
    void updateById(Long id, int status);
}
