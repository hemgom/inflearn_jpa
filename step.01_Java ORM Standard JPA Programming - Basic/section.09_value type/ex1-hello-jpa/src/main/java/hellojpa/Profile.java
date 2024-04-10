package hellojpa;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Profile {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "username")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private List<LoginEntity> loginHistory = new ArrayList<>();

    public Profile() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLoginHistory(List<LoginEntity> loginHistory) {
        this.loginHistory = loginHistory;
    }

    public List<LoginEntity> getLoginHistory() {
        return loginHistory;
    }
}
