package hellojpa.jpql;

public class MemberDTO {
    // 필드
    private String username;
    private int age;


    // 생성자
    public MemberDTO() {
    }

    public MemberDTO(String username, int age) {
        this.username = username;
        this.age = age;
    }


    // getter, setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
