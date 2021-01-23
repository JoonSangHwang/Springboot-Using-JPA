package com.joonsang.example.SpringbootUsingJPA.repo;

import com.joonsang.example.SpringbootUsingJPA.dto.MemberDto;
import com.joonsang.example.SpringbootUsingJPA.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * << 메소드 이름으로 쿼리 생성1 >>
     * - 선택한 유저 그리고 age 초과 값 구하기
     * - WHERE username = :username AND age > :age
     *
     * 참고
     * - https://docs.spring.io/spring-data/jpa/docs/2.3.6.RELEASE/reference/html/#jpa.query-methods
     * - 이 기능은 엔티티의 필드명이 변경되면 인터페이스에 정의한 메서드 이름도 꼭 함께 변경해야 한다.
     */
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    /**
     * << 메소드 이름으로 쿼리 생성2 >>
     * - 유저 전체 조회
     */
    List<Member> findHelloBy();

    /**
     * << @Query, 리포지토리 메소드에 쿼리 정의하기 >>
     * - 유저 조회
     */
    @Query("select m from Member m where m.username= :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    /**
     * << @Query, 값, DTO 조회하기 - 단순한 값 조회 >>
     * - 유저 조회
     */
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    /**
     * << @Query, 값, DTO 조회하기 - DTO 조회 >>
     * - 유저 조회
     */
    @Query("select new com.joonsang.example.SpringbootUsingJPA.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    /**
     * << 파라미터 바인딩 >>
     */
    @Query("select m from Member m where m.username = :name")
    Member findMembers(@Param("name") String username);

    /**
     * << 컬렉션 파라미터 바인딩 >>
     */
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    /**
     * << 반환 타입 >>
     */
    List<Member> findListByUsername(String name);           //컬렉션
    Member findMemberByUsername(String name);               //단건
    Optional<Member> findOptionalByUsername(String name);   //단건 Optional

    /**
     * << 스프링 데이터 JPA 페이징과 정렬 >>
     */
    Page<Member>  findPagingByAge(int age, Pageable pageable);
    Page<Member>  findPagingByUsername(String name, Pageable pageable);  //count 쿼리 사용
    Slice<Member> findSliceByUsername(int age, Pageable pageable);   //count 쿼리 사용 안함
    List<Member>  findListByUsername(int age, Pageable pageable);    //count 쿼리 사용 안함
    List<Member>  findListSortByUsername(String name, Sort sort);

    @Query(value = "select m from Member m",
            countQuery = "select count(m) from Member m")
    Page<Member> findMemberAllCountBy(Pageable pageable);

    /**
     * << 벌크성 수정 쿼리 >>
     * - 참고
     *   : 벌크 연산은 영속성 컨텍스트를 무시하고 실행하기 때문에, 영속성 컨텍스트에 있는 엔티티의 상태와 DB에 엔티티 상태가 달라질 수 있다.
     * - 권장하는 방안
     *   1. 영속성 컨텍스트에 엔티티가 없는 상태에서 벌크 연산을 먼저 실행한다.
     *   2. 부득이하게 영속성 컨텍스트에 엔티티가 있으면 벌크 연산 직후 영속성 컨텍스트를 초기화 한다.
     */
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
}
