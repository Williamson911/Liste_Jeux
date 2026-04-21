package be.technifutur.introjakartaee.models;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Jeux {
    @Getter @Setter
    private int id;
    @Getter @Setter
    private String titre;
    @Getter @Setter
    private int annee;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private String imageUrl;
    @Getter @Setter
    private double prix;
}
