package com.joonsang.example.SpringbootUsingJPA.repo;

import com.joonsang.example.SpringbootUsingJPA.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor        // 생성자 주입 생략
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    /**
     * << 사용자 정의 리포지토리 구현 >>
     * - 사용자 정의 구현 클래스
     * - 규칙: 리포지토리 인터페이스 이름 + Impl
     * - 스프링 데이터 JPA가 인식해서 스프링 빈으로 등록
     */
    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m").getResultList();
    }
}
