package be.technifutur.introjakartaee.servlets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Jeux {
    private int id;
    private String titre;
    private int annee;
    private String description;
    private String imageUrl;
}
