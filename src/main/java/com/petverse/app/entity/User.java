package com.petverse.app.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "AS_Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;  // เข้ารหัสแล้ว

    private String fullName;

    private String phone;

    private String address;

    private String profilePictureUrl;

    /**
     * ระบุ social provider เช่น "GOOGLE", "FACEBOOK" หรือ null ถ้าใช้ email/password ปกติ
     */
    private String socialProvider;

    /**
     * รหัสผู้ใช้จาก social provider
     */
    private String socialId;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Pet> pets = new LinkedHashSet<>();

    /**
     * ผู้ติดตาม (followers) - คนที่ติดตาม user นี้
     */
    @ManyToMany
    @JoinTable(
            name = "AS_Followers",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "followerId")
    )
    @Builder.Default
    private Set<User> followers = new LinkedHashSet<>();

    /**
     * ผู้ที่ user นี้ติดตาม (following)
     */
    @ManyToMany(mappedBy = "followers")
    @Builder.Default
    private Set<User> following = new LinkedHashSet<>();
}
