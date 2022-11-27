package com.example.finalprj.db.domain;

import lombok.* ;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Dog extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //public String aprGbNm;
    public String dogNm;
    public String dogRegNo;
    public String kindNm;
    //public String neuterYn;
    //public String orgNm;
    //public String rfidCd;
    public String sexNm;

    @OneToOne
    User user;

}
