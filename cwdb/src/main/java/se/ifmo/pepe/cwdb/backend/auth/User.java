package se.ifmo.pepe.cwdb.backend.auth;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "role")
    @Enumerated
    private Role role;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "pharmacyID")
    private Long pharmacyID;

    @Column(name = "companyID")
    private Long companyID;

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRole(String label) {
        switch (label) {
            case "Pharmacy": {
                this.role = Role.PHARMACY;
                break;
            }
            case "Company": {
                this.role = Role.COMPANY;
                break;
            }
        }
    }
}
