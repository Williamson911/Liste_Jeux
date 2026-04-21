package be.technifutur.introjakartaee.models;

import be.technifutur.introjakartaee.enums.UserRole;
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
}
