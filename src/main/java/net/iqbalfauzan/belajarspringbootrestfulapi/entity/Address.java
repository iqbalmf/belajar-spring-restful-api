package net.iqbalfauzan.belajarspringbootrestfulapi.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    private String username;
    private String street;
    private String city;
    private String province;
    private String country;
    @Column(name = "postal_code")
    private String postalCode;
    @ManyToOne
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    private Contact contact;
}
