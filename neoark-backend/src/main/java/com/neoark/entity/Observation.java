package com.neoark.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "observations")
public class Observation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxon_id")
    private Taxon taxon; // 어떤 생물군인지 (iNaturalist처럼 미동정일 수 있으므로 nullable 가능)

    @Column(columnDefinition = "TEXT")
    private String description; // 관찰 내용 설명

    private Double latitude;  // 위도
    private Double longitude; // 경도
    private String placeName; // 지명 (예: 설악산 국립공원)

    private LocalDateTime observedAt; // 실제 관찰 일시
    private LocalDateTime createdAt;  // 기록 등록 일시

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.observedAt == null) {
            this.observedAt = LocalDateTime.now();
        }
    }

    public Observation() {}

    // Getter 및 Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Taxon getTaxon() { return taxon; }
    public void setTaxon(Taxon taxon) { this.taxon = taxon; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getPlaceName() { return placeName; }
    public void setPlaceName(String placeName) { this.placeName = placeName; }

    public LocalDateTime getObservedAt() { return observedAt; }
    public void setObservedAt(LocalDateTime observedAt) { this.observedAt = observedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}