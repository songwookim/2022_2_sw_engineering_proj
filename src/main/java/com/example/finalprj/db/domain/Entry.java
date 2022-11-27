package com.example.finalprj.db.domain;

import lombok.* ;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "entry_list")
@IdClass(Entry.class) // @Id를 여러 개 가질 경우
public class Entry extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    private Long playgroundId ;

    @Id
    private Long userId ;

    private int status; // -1 : 관리자
}
