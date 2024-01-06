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
public class Specialization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialization_id")
    private Long id;

    @Column(name = "specialization_name", unique = true)
    @NotEmpty
    @Size(min = 2, max = 255)

    private String name;

    @ManyToMany
    @JoinColumn(name = "psychologist_id") // имя столбца, содержащего внешний ключ к таблице Psychologist
    private List<Psychologist>  psychologistList;

}
