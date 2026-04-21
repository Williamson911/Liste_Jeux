package be.technifutur.introjakartaee.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favoris")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomElement;

    @Column(nullable = false)
    private String typeElement;

    @Column(nullable = false)
    private String userId;
}
