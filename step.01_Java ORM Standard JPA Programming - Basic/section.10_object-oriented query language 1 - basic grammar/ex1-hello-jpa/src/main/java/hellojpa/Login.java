package hellojpa;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class Login {

    private String loginName;

    private LocalDateTime loginAt;

    public Login() {}

    public Login(String loginName, LocalDateTime loginAt) {
        this.loginName = loginName;
        this.loginAt = loginAt;
    }

    public String getLoginName() {
        return loginName;
    }

    public LocalDateTime getLoginAt() {
        return loginAt;
    }
}
