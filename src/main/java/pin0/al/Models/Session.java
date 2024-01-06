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
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long id;

    @Column(name = "format")
    @NotEmpty
    @Size(min = 2, max = 255)

    private boolean format;

    @NotNull
    @Column(name = "day_session")
    @Temporal(TemporalType.DATE)
    private Date dateDay;

    @NotNull
    @Column(name = "time_session")
    @Temporal(TemporalType.DATE)
    private Date dateTime;


    @NotNull
    @Column(name = "status")
    private Status status;

    @ManyToMany
    @JoinTable(name = "cp_session",
            joinColumns = @JoinColumn(name = "session_id"),
            inverseJoinColumns = @JoinColumn(name = "psychologist_id"))
    private List<Psychologist> psychologist;
    @ManyToMany
    @JoinTable(name = "cp_session",
            joinColumns = @JoinColumn(name = "session_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
   // @JoinColumn(name = "client_id")
    private List<Client> client;


    //insert into cp_session(session_id, psychologist_id, client_id) values(1,1,2)
   // insert into cp_session(session_id, psychologist_id, client_id) values(2,2,2)
   // insert into cp_session(session_id, psychologist_id, client_id) values(3,2,2)

    public enum Status {

        wait("Ожидается"),
        cancelled("Отменена"),
        completed("Завершена");

        private String title;

        Status(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

    }

}
