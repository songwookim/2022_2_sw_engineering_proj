package com.example.finalprj.db.repository;

import com.example.finalprj.db.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Notice n set n.subject = :subject, n.content = :content where n.id = :id")
    void updateById(Long id, String subject, String content);

    @Transactional
    @Modifying
    @Query(value = "update notice n, (select @COUNT\\:=0) x \n" +
            "set n.id = (@COUNT\\:=@COUNT+1) \n" +
            "where n.id like '%';", nativeQuery = true)
    void indexing();
}
