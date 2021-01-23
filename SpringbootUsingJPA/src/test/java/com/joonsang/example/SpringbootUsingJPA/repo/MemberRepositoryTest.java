package com.joonsang.example.SpringbootUsingJPA.repo;

import com.joonsang.example.SpringbootUsingJPA.dto.MemberDto;
import com.joonsang.example.SpringbootUsingJPA.entity.Member;
import com.joonsang.example.SpringbootUsingJPA.entity.Team;
import org.assertj.core.api.Assertions;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitUtil;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional          // JPA 데이터 변경은 모두 트랜잭션 안에서 처리 됨
@Rollback(false)        // 롤백 여부... (눈으로 보고 싶다면 false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;


    @Test
    @DisplayName("테스트")
    public void testMember() {

        // 참고 : 스프링 JPA 가 Injection 하므로 Proxy 객체임.
        System.out.println("memberRepository : "  + memberRepository.getClass());


        Member member       = new Member("memberA");
        Member savedMember  = memberRepository.save(member);
        Member findMember   = memberRepository.findById(savedMember.getId()).get();

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);

        // 참고 : JPA 엔티티 동일성 보장하므로 = 같음
        System.out.println("findMember : "  + findMember.getClass());
        System.out.println("savedMember : " + savedMember.getClass());
        System.out.println("findMember : "  + member.getClass());
    }

    @Test
    @DisplayName("테스트 - CRUD")
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    @DisplayName("<< 메소드 이름으로 쿼리 생성1 >>")
    public void findByUsernameAndAgeGreaterThan() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> all = memberRepository.findByUsernameAndAgeGreaterThan("BBB",15);
        assertThat(all.get(0).getUsername()).isEqualTo("BBB");
        assertThat(all.get(0).getAge()).isEqualTo(20);
    }

    @Test
    @DisplayName("<< 메소드 이름으로 쿼리 생성2 >>")
    public void find_________________________By() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> all = memberRepository.findHelloBy();
        assertThat(all.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("<< @Query, 리포지토리 메소드에 쿼리 정의하기 >>")
    public void findUser() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> all = memberRepository.findUser("AAA",10);
        assertThat(all.get(0)).isEqualTo(member1);
    }

    @Test
    @DisplayName("<< @Query, 값, DTO 조회하기 - 단순한 값 조회 >>")
    public void findUsernameList() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> all = memberRepository.findUsernameList();
        for (String s : all) {
            System.out.println("username = " + s);
        }
    }

    @Test
    @DisplayName("<< @Query, 값, DTO 조회하기 - DTO 조회 >>")
    public void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member = new Member("AAA", 10);
        member.changeTeam(team);
        memberRepository.save(member);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto s : memberDto) {
            System.out.println("dto = " + s);
        }
    }

    @Test
    @DisplayName("<< 반환 타입 >>")
    public void returnType() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> aaa        = memberRepository.findListByUsername("AAA");       // 리스트는 절대 NULL 안 옴
        Member bbb              = memberRepository.findMemberByUsername("AAA");     // 없을 경우, NULL (noResultException)
        Optional<Member> ccc    = memberRepository.findOptionalByUsername("AAA");   // 없을 경우, Optional.empty

        System.out.println("return Type_aaa : " + aaa);
        System.out.println("return Type_bbb : " + bbb);
        System.out.println("return Type_ccc : " + ccc);
    }

    @Test
    @DisplayName("<< 스프링 데이터 JPA 페이징과 정렬 >>")
    public void findPageByAge() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        //when
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> pagingByAge = memberRepository.findPagingByAge(10, pageRequest);        // 반환 타입이 Page 객체 일 경우, 카운터 쿼리 자동으로 가져옴

        //then
        List<Member> content = pagingByAge.getContent();           // 조회된 데이터
        assertThat(content.size()).isEqualTo(3);                   // 조회된 데이터 수
        assertThat(pagingByAge.getTotalElements()).isEqualTo(5);   // 전체 데이터 수
        assertThat(pagingByAge.getNumber()).isEqualTo(0);          // 페이지 번호
        assertThat(pagingByAge.getTotalPages()).isEqualTo(2);      // 전체 페이지 번호
        assertThat(pagingByAge.isFirst()).isTrue();                // 첫번째 항목인가?
        assertThat(pagingByAge.hasNext()).isTrue();                // 다음 페이지가 있는가?
        Page<MemberDto> memberDtoPage = pagingByAge.map(member -> new MemberDto(member.getId(), member.getUsername(), null));


        /**
         * Slice
         * - Slice 는 클라이언트 요청 건 수의 +1 만큼 더 반환 한다.
         */

        //when
        Slice<Member> sliceByUsername = memberRepository.findSliceByUsername(10, pageRequest);

        //then
        List<Member> content2 = sliceByUsername.getContent();           // 조회된 데이터
        assertThat(content2.size()).isEqualTo(3);                       // 조회된 데이터 수
//        assertThat(sliceByUsername.getTotalElements()).isEqualTo(5);  // 전체 데이터 수
        assertThat(sliceByUsername.getNumber()).isEqualTo(0);           // 페이지 번호
//        assertThat(sliceByUsername.getTotalPages()).isEqualTo(2);     // 전체 페이지 번호
        assertThat(sliceByUsername.isFirst()).isTrue();                 // 첫번째 항목인가?
        assertThat(sliceByUsername.hasNext()).isTrue();                 // 다음 페이지가 있는가?
        Slice<MemberDto> memberDtoPage2 = sliceByUsername.map(member -> new MemberDto(member.getId(), member.getUsername(), null));


        /**
         * List
         * - 카운트 쿼리 없이 반환
         */
        List<Member> listByUsername = memberRepository.findListByUsername(10, pageRequest);
//        listByUsername.map(member -> new MemberDto(member.getId(), member.getUsername(), null));


        /**
         * 페이징 쿼리와 카운트 쿼리 별도로 선언
         * @Query value + countQuery
         */
        Page<Member> memberAllCountBy = memberRepository.findMemberAllCountBy(pageRequest);
        Page<MemberDto> memberDtoPage4 = memberAllCountBy.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
    }


    @Test
    @DisplayName("<< 벌크성 수정 쿼리 >>")
    public void bulkUpdate() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);
        em.flush();     // 벌크성 수정은 바로 DB 에 저장 된다. 그러면 영속성 컨텍스트를 비워줄 필요가 있다.
        em.clear();     // 또는, @Modifying(clearAutomatically = true) 으로 대체 할 수 있다.

        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    @DisplayName("<< @EntityGraph>>")
    public void findMemberLazy() throws Exception {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));
        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findAll();

        //then
        for (Member member : members) {
            member.getTeam().getName();

            // 지연 로딩 여부 - JPA 표준 방법으로 확인
            PersistenceUnitUtil util = em.getEntityManagerFactory().getPersistenceUnitUtil();
            util.isLoaded(member.getTeam());

            // 지연 로딩 여부 - Hibernate 기능으로 확인
            Hibernate.isInitialized(member.getTeam());
        }
    }

    @Test
    @DisplayName("<< QueryHint >>")
    public void queryHint() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        //when
        Member member = memberRepository.findReadOnlyByUsername("member1");
        member.setUsername("member2");
        em.flush(); //Update Query 실행X
    }



}