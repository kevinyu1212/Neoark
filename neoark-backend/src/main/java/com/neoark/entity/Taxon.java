package com.neoark.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "taxa")
public class Taxon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 학명 (Scientific Name, 예: Araneae)

    private String commonName; // 일반명 (예: 거미목)

    @Column(nullable = false)
    private String rank; // 분류 단계 (예: kingdom, phylum, class, order, family, genus, species)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Taxon parent; // 상위 분류군

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Taxon> children = new ArrayList<>(); // 하위 분류군들

    // 기본 생성자
    public Taxon() {}

    // Getter 및 Setter (Lombok 미사용)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCommonName() { return commonName; }
    public void setCommonName(String commonName) { this.commonName = commonName; }

    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }

    public Taxon getParent() { return parent; }
    public void setParent(Taxon parent) { this.parent = parent; }

    public List<Taxon> getChildren() { return children; }
    public void setChildren(List<Taxon> children) { this.children = children; }
}