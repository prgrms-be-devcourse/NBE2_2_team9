package com.mednine.pillbuddy.domain.user.entity;

import com.mednine.pillbuddy.domain.user.profile.entity.Image;
import com.mednine.pillbuddy.global.entity.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class User extends BaseTimeEntity {

    @Column(name = "username", length = 30, nullable = false)
    private String username;

    @Column(name = "login_id", length = 20, unique = true, nullable = false)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", length = 50, unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number", length = 20, unique = true, nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateLoginId(String loginId) {
        this.loginId = loginId;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateImage(Image image) {
        this.image = image;
    }
}
