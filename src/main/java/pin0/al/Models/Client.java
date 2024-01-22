package pin0.al.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Column(name = "name")
    @NotEmpty
    @Size(min = 2, max = 255)

    private String name;

    @Column(name = "email", unique = true)
    @NotEmpty
    @Size(min = 2, max = 255)

    private String email;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @OneToMany(mappedBy = "client")
    private List<Session> sessionList;

}
