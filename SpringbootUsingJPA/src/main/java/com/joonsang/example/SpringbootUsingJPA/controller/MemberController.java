package com.joonsang.example.SpringbootUsingJPA.controller;

import com.joonsang.example.SpringbootUsingJPA.dto.MemberDto;
import com.joonsang.example.SpringbootUsingJPA.entity.Member;
import com.joonsang.example.SpringbootUsingJPA.repo.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @Autowired
    MemberRepository memberRepository;

    /**
     * 페이징과 정렬
     */

    // 1. 기본 ex) /members?page=0&size=3&sort=id,desc&sort=username,desc
//    @GetMapping("/members")
//    public Page<Member> list1(Pageable pageable) {
//        return memberRepository.findAll(pageable);
//    }

    // 2. 사이즈 등 개별 설정
    @GetMapping("/members")
    public Page<Member> list2(@PageableDefault(size = 5, sort = "username", direction = Sort.Direction.DESC) Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    // 3. 페이징 정보가 둘 이상이면 접두사로 구분    ex) /members?member_page=0&order_page=1
//    @GetMapping("/members")
//    public String list(
//            @Qualifier("member") Pageable memberPageable,
//            @Qualifier("order") Pageable orderPageable) {
//        return "";
//    }

    // 4. Page 내용을 DTO로 변환하기 - Page.map() 사용
//    @GetMapping("/members")
//    public Page<MemberDto> list3(Pageable pageable) {
//        Page<Member> page = memberRepository.findAll(pageable);
//        Page<MemberDto> pageDto = page.map(MemberDto::new);
//        return pageDto;
//    }

    // 5. Page 내용을 DTO로 변환하기 - Page.map() 코드 최적화
//    @GetMapping("/members")
//    public Page<MemberDto> list4(Pageable pageable) {
//        return memberRepository.findAll(pageable).map(MemberDto::new);
//    }
}
