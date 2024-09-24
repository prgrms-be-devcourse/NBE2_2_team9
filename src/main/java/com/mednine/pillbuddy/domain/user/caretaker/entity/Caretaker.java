package com.mednine.pillbuddy.domain.user.caretaker.entity;

import com.mednine.pillbuddy.domain.notification.entity.Notification;
import com.mednine.pillbuddy.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "caretaker")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Caretaker extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caretaker_id")
    private Long id;

    @Column(name = "username", length = 30, nullable = false)
    private String username;

    @Column(name = "login_id", length = 20, unique = true, nullable = false)
    private String loginId;

    @Column(name = "password", length = 30, nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private CaretakerImage image;

    @Column(name = "email", length = 50, unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "caretaker", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();
}
