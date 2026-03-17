package com.neoark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neoark.entity.Observation;
import com.neoark.entity.Taxon;
import com.neoark.entity.User;

public interface ObservationRepository extends JpaRepository<Observation, Long> {
    
    // 특정 유저가 작성한 모든 관찰 기록 조회
    List<Observation> findByUser(User user);

    // 특정 생물 분류군(Taxon)에 해당하는 모든 관찰 기록 조회
    List<Observation> findByTaxon(Taxon taxon);

    // 최근 등록순으로 조회
    List<Observation> findAllByOrderByCreatedAtDesc();
}