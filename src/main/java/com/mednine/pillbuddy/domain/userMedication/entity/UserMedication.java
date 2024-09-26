package com.mednine.pillbuddy.domain.userMedication.entity;

import com.mednine.pillbuddy.domain.notification.entity.Notification;
import com.mednine.pillbuddy.domain.record.entity.Record;
import com.mednine.pillbuddy.domain.user.caretaker.entity.Caretaker;
import com.mednine.pillbuddy.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_medication")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMedication extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_medication_id")
    private Long id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "dosage")
    private Integer dosage;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", nullable = false)
    private Frequency frequency;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MedicationType type;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caretaker_id")
    private Caretaker caretaker;

    @OneToMany(mappedBy = "userMedication", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Notification> notificationList = new ArrayList<>();

    @OneToMany(mappedBy = "userMedication", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Record> records = new ArrayList<>();

    public void changeName(String name) {
        this.name = name;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeDosage(Integer dosage) {
        this.dosage = dosage;
    }

    public void changeCaretaker(Caretaker caretaker) {
        this.caretaker = caretaker;
    }
}
