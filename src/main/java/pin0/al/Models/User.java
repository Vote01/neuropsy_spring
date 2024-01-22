package pin0.al.Models;

import jakarta.persistence.*;
import jakarta.persistence.Id;

@Entity
@Table(name = "userr")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Long getIduser() {
        return iduser;
    }

    public void setIduser(Long iduser) {
        this.iduser = iduser;
    }

    public Role getRoleEnum() {
        switch (role){
            case "ADMIN": return Role.ADMIN;
            case "CLIENT": return Role.CLIENT;
            case "PSYCHOLOGIST": return Role.PSYCHOLOGIST;
        }
        return Role.CLIENT;
    }
    public String getRole() {
        return role;
    }
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;
    @Column(name = "fk_id")
    private Long iduser;
    @Column(name = "role")
    private String role;


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
