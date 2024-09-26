package com.mednine.pillbuddy.domain.user.caregiver.entity;

import com.mednine.pillbuddy.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "caregiver")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Caregiver extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caregiver_id")
    private Long id;

    @Column(name = "username", length = 30, nullable = false)
    private String username;

    @Column(name = "login_id", length = 20, unique = true, nullable = false)
    private String loginId;

    @Column(name = "password", length = 30, nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private CaregiverImage image;

    @Column(name = "email", length = 50, unique = true, nullable = false)
    private String email;

}