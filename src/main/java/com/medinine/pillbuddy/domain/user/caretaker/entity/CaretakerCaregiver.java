package com.medinine.pillbuddy.domain.user.caretaker.entity;

import com.medinine.pillbuddy.domain.user.caregiver.entity.Caregiver;
import com.medinine.pillbuddy.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "caretaker_caregiver")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CaretakerCaregiver extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caretaker_caregiver_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caretaker_id")
    private Caretaker caretaker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caregiver_id")
    private Caregiver caregiver;

    public void changeUser(Caretaker caretaker) {
        this.caretaker = caretaker;
    }

    public void changeCaregiver(Caregiver caregiver) {
        this.caregiver = caregiver;
    }
}
