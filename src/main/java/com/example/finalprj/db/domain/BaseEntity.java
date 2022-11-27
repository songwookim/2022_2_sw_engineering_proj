package com.example.finalprj.db.domain;

import com.example.finalprj.db.listener.Auditable;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass // 해당클래스의 필드를 상속받는 Entity의 컬럼으로 포함시키겠음
@EntityListeners(value = AuditingEntityListener.class)
public class BaseEntity implements Auditable {
    @CreatedDate // 다음의 컬럼정의로 시간의 기본값 설정함
    @Column(columnDefinition = "datetime(6) default now(6) ", nullable = false, updatable = false)  // data.sql처럼 시간값 안 들어가는 경우 실행오류 나도록 하기
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(columnDefinition = "datetime(6) default now(6) ", nullable = false)
    private LocalDateTime updatedAt;
}
