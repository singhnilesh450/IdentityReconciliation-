package com.singhfusion.IdentityReconciliation.entity;

import com.singhfusion.IdentityReconciliation.enums.LinkPrecedenceType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Table(name = "Contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="phone_number")
    private String phoneNumber;


    private String email;

    @Column(name="linked_id")
    private Long linkedId;

    @Column(name="link_precedence",nullable = false)
    @Enumerated(EnumType.STRING)
    private LinkPrecedenceType linkPrecedence;

    @Column(name="created_at")

    private String createdAt;

    @Column(name="updated_at")

    private String updatedAt;

    @Column(name="deleted_at")
    private String deletedAt;
}