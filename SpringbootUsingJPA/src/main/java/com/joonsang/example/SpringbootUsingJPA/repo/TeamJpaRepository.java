package com.joonsang.example.SpringbootUsingJPA.repo;

import com.joonsang.example.SpringbootUsingJPA.entity.Team;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamJpaRepository {

    @PersistenceContext
    private EntityManager em;

    // 팀 조회
    public Optional<Team> findById(Long id) {
        Team team = em.find(Team.class, id);
        return Optional.ofNullable(team);
    }

    // 모든 팀 조회
    public List<Team> findAll() {
        return em.createQuery("select m from Team t", Team.class).getResultList();
    }

    // 모든 팀 카운트 조회
    public long count() {
        return em.createQuery("select count(t) from Team t", Long.class).getSingleResult();
    }
    
    // 팀 저장
    public Team save(Team team) {
        em.persist(team);
        return team;
    }

    // 팀 삭제
    public void delete(Team team) {
        em.remove(team);
    }

}
