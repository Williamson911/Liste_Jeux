package be.technifutur.introjakartaee.models;

import be.technifutur.introjakartaee.enums.UserRole;
import lombok.*;

@Data
@EqualsAndHashCode @ToString
@NoArgsConstructor @AllArgsConstructor
public class User {
    @Getter
    private Integer id;
    @Getter @Setter
    private String email;
    @Getter @Setter
    private String password;
    @Getter @Setter
    private UserRole role;

    public User(String email, String password, UserRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}