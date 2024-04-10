package hellojpa;

import jakarta.persistence.*;

@Entity
@Table(name = "login")
public class LoginEntity {

    @Id @GeneratedValue
    private Long id;

    @Embedded
    private Login login;

}
