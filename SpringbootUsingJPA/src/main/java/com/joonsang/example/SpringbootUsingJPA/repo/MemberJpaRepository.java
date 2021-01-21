package com.joonsang.example.SpringbootUsingJPA.repo;

import com.joonsang.example.SpringbootUsingJPA.entity.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    // 회원 조회
    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    // 회원 모두 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    // 회원 조회 (Optional)
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);     // NULL 일 수도 있고 아닐 수도 있음
    }

    // 모든 회원 카운트 조회
    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }

    // 회원 저장
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    // 회원 삭제
    public void delete(Member member) {
        em.remove(member);
    }

    // 선택한 유저 중, age 보다 이상인 값 구하기
    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    /**
     * << 순수 JPA 페이징과 정렬 >>
     */
    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc")
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * << 순수 JPA 페이징과 정렬 - 카운트 쿼리 >>
     */
    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }
}
