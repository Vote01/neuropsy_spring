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

    @Column(name = "name")
    @NotEmpty
    @Size(min = 2, max = 255)

    private String name;

    @Column(name = "email", unique = true)
    @NotEmpty
    @Size(min = 2, max = 255)

    private String email;

    @ManyToMany
    @JoinTable(name = "cp_session",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "session_id"))

    private List<Session> sessionList;

}
