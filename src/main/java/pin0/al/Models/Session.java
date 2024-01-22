package pin0.al.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "session_id")
    private Long id;

    @Column(name = "format")
    @NotNull
    private boolean format;

    @NotNull
    @Column(name = "day_session")
    private LocalDate dateDay;

    @NotNull
    @Column(name = "time_session")
    private LocalTime dateTime;

    @NotEmpty
    @Column(name = "status")
    private String status;

    public Long getId() {
        return id;
    }

    public boolean getFormat() {
        return format;
    }

    public String getFormatStr() {
        if(format) return "Онлайн";
        else return "Оффлайн";
    }

    public LocalDate getDateDay() {
        return dateDay;
    }

    public LocalTime getDateTime() {
        return dateTime;
    }

    public String getFormattedDay() {
        return dateDay.format(DateTimeFormatter.ofPattern("dd MMMM"));
    }

    // Метод для получения времени в нужном формате
    public String getFormattedTime() {
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }



    public String getStatus() {
        switch (status){
            case "W": return "Ожидается";
            case "F": return "Завершена";
            case "M": return "Отменена";
        }
        return status;
    }

    public Psychologist getPsychologist() {
        return psychologist;
    }

    public Client getClient() {
        return client;
    }

    @ManyToOne
    @JoinColumn(name = "psychologist_id")
    private Psychologist psychologist;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;


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
