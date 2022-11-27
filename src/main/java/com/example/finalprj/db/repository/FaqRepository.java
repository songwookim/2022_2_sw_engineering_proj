package com.example.finalprj.db.repository;

import com.example.finalprj.db.domain.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface FaqRepository extends JpaRepository<Faq, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Faq f set f.subject = :subject, f.content = :content where f.id = :id")
    void updateById(Long id, String subject, String content);

    @Transactional
    @Modifying
    @Query(value = "update faq f, (select @COUNT\\:=0) x \n" +
            "set f.id = (@COUNT\\:=@COUNT+1) \n" +
            "where f.id like '%';", nativeQuery = true)
    void indexing();
}
