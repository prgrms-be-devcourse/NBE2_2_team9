package com.mednine.pillbuddy.domain.user.caretaker.entity;

import com.mednine.pillbuddy.domain.notification.entity.Notification;
import com.mednine.pillbuddy.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Caretaker extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caretaker_id")
    private Long id;

    @OneToMany(mappedBy = "caretaker", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();
}
