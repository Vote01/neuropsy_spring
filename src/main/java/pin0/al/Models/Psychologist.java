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
    @NotEmpty(message = "Имя должно быть заполнено")
    @Size(min = 2, max = 255)

    private String name;
    @Column(name = "lname")
    @NotEmpty(message = "Фамилия должна быть заполнена")
    @Size(min = 2, max = 255)

    private String lname;

    @Column(name = "town")
    @NotEmpty(message = "Город должен быть заполнен")
    @Size(min = 2, max = 255)

    private String town;

    @Column(name = "videocon")
    @NotNull
    private boolean videocon;

    @Column(name = "confirmed")
    @NotNull
    private boolean confirmed;


    @Column(name = "description")
    @NotEmpty(message = "Описание должно быть заполнено")
    @Size(min = 30, max = 255)
    private String description;

    @NotNull(message = "Введите дату рождения в формате yyyy-mm-dd")
    @Column(name = "age")
    @Temporal(TemporalType.DATE)
    private  Date age;

    @Column(name = "education")
    @NotEmpty(message = "Образование должно быть заполнено")
    @Size(min = 3, max = 255)
    private String education;

    @Column(name = "email")
    @NotEmpty(message = "Почта должна быть заполнена")
    @Size(min = 3, max = 100)
    private String email;


    @NotNull(message = "Введите дату в формате yyyy-mm-dd")
    @Column(name = "practice")
    @Temporal(TemporalType.DATE)
    private Date practice;

    public Date getRegistration() {
        return registration;
    }

    public Date getAge() {
        return age;
    }

    public List<Session> getSessionList() {
        return sessionList;
    }

    public Date getPractice() {
        return practice;
    }

    @NotNull(message = "Введите дату в формате yyyy-mm-dd")
    @Column(name = "registration")
    @Temporal(TemporalType.DATE)
    private Date registration;


    @Column(name = "image")
    private String image;

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @NotNull(message = "Введите цену")
    @Column(name = "price")
    private int price;

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

    public String isVideocon() {
        if(videocon) return "Есть";
        else return "Отсутствует";
    }
    public boolean getVideocon() {
        return videocon;
    }
    public String getAgeMath() {
        Date currentDate = new Date();
        long differenceInMilliseconds = currentDate.getTime() - age.getTime();
        long difference = differenceInMilliseconds / (30L *24*60*60*1000*12);
        return String.valueOf(difference);
    }

    public String getPracticeMath() {
        Date currentDate = new Date();
        long differenceInMilliseconds = currentDate.getTime() - practice.getTime();
        long differenceInMonths = differenceInMilliseconds / (30L *24*60*60*1000);

        if (differenceInMonths < 6) {
            return "меньше 6 месяцев";
        } else if (differenceInMonths >= 6 && differenceInMonths < 12) {
            return "больше 6 месяцев";
        } else if (differenceInMonths >= 12 && differenceInMonths < 24) {
            return "больше года";
        } else {
            return "больше 2 лет";
        }
    }


    public List<Method> getMethodList() {
        return methodList;
    }

    public List<Specialization> getSpecializationList() {
        return specializationList;
    }

    @OneToMany(mappedBy = "psychologist")

    private List<Session> sessionList;

   // insert into client_session (client_id, session_id) values (1,1)
}
