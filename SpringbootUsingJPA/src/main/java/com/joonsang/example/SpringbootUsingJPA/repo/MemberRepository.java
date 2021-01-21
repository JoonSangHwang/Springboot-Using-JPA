package com.joonsang.example.SpringbootUsingJPA.repo;

import com.joonsang.example.SpringbootUsingJPA.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
