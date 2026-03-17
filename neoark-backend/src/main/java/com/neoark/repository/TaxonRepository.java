package com.neoark.repository;

import com.neoark.entity.Taxon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaxonRepository extends JpaRepository<Taxon, Long> {
    // 특정 분류 단계의 데이터 검색 (예: 모든 '목(Order)' 검색)
    List<Taxon> findByRank(String rank);

    // 이름으로 검색
    List<Taxon> findByNameContaining(String name);
}
