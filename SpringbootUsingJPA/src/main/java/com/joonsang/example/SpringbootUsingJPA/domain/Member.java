package com.joonsang.example.SpringbootUsingJPA.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String username;

    /**
     * JPA 표준 스펙 : 엔티티는 반드시 파라미터가 없는 생성자가 있어야 하고, 이는 public 또는 protected 여야 한다.
     */
    protected Member() {

    }

    public Member(String username) {
        this.username = username;
    }
}