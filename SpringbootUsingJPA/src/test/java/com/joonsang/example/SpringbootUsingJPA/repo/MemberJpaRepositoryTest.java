package com.joonsang.example.SpringbootUsingJPA.repo;

import com.joonsang.example.SpringbootUsingJPA.domain.Member;
import javafx.beans.binding.BooleanExpression;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional          // JPA 데이터 변경은 모두 트랜잭션 안에서 처리 됨
@Rollback(false)        // 롤백 여부... (눈으로 보고 싶다면 false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    @DisplayName("테스트")
    public void testMember() {
        Member member       = new Member("memberA");
        Member savedMember  = memberJpaRepository.save(member);
        Member findMember   = memberJpaRepository.find(savedMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);

        // //JPA 엔티티 동일성 보장하므로 = 같음
        System.out.println("findMember : "  + findMember.getClass());
        System.out.println("savedMember : " + savedMember.getClass());
        System.out.println("findMember : "  + member.getClass());
    }

}