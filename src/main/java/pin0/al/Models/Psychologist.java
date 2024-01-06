package pin0.al.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Psychologist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "psychologist_id")
    private Long id;

    @Column(name = "name")
    @NotEmpty
    @Size(min = 2, max = 255)

    private String name;
    @Column(name = "lname")
    @NotEmpty
    @Size(min = 2, max = 255)

    private String lname;

    @Column(name = "town")
    @NotEmpty
    @Size(min = 2, max = 255)

    private String town;

    @Column(name = "videocon")
    @NotEmpty
    private boolean videocon;

    @Column(name = "description")
    @NotEmpty
    @Size(min = 30, max = 255)
    private String description;

    @Column(name = "age")
    @NotEmpty
    @Size(min = 30, max = 255)
    private String age;

    @Column(name = "education")
    @NotEmpty
    @Size(min = 30, max = 255)
    private String education;


    @NotNull
    @Column(name = "practice_start")
    @Temporal(TemporalType.DATE)
    private Date practice;

    @NotNull
    @Column(name = "registration")
    @Temporal(TemporalType.DATE)
    private Date registration;



    @ManyToMany
    @JoinTable(name = "psychologist_method",
            joinColumns = @JoinColumn(name = "psychologist_id"),
            inverseJoinColumns = @JoinColumn(name = "method_id"))
    private List<Method> methodList;

    @ManyToMany
    @JoinTable(name = "psychologist_specialization",
            joinColumns = @JoinColumn(name = "psychologist_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id"))
    private List<Specialization> specializationList;

    @ManyToMany
    @JoinTable(name = "cp_session",
            joinColumns = @JoinColumn(name = "psychologist_id"),
            inverseJoinColumns = @JoinColumn(name = "session_id"))

    private List<Session> sessionList;

   // insert into psychologist(name, lname, town, videocon, age , education, practice_start, description)

   // insert into client_session (client_id, session_id) values (1,1)
}
