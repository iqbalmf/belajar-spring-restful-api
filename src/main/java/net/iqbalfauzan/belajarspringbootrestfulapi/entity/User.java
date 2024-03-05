package net.iqbalfauzan.belajarspringbootrestfulapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    private String username;
    private String password;
    private String name;
    private String token;
    @Column(name = "token_expired_at")
    private Long tokenExpiredAt;
    @OneToMany(mappedBy = "user")
    private List<Contact> contacts;
}
