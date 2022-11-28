package com.example.finalprj.db.repository;

import com.example.finalprj.db.domain.Trash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface TrashRepository extends JpaRepository<Trash, Long> {
    @Override
    Optional<Trash> findById(Long aLong);

    @Transactional
    @Modifying
    @Query("UPDATE Trash t set t.status = :status where t.id = :id")
    void updateById(Long id, int status);
}
