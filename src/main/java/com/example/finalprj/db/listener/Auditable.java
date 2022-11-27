package com.example.finalprj.db.listener;


import java.time.LocalDateTime;

// 모든 객체에서 이러한 값들이 사용된다는 것을 알리기 위한 인터페이스
// 엔터티마다 상속시켜서 MyEntityListener과 연동해 사용한다.
public interface Auditable {
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();

    void setCreatedAt(LocalDateTime createdAt);
    void setUpdatedAt(LocalDateTime updatedAt);
}
