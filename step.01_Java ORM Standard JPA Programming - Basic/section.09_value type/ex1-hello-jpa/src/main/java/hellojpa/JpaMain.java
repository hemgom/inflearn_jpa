package hellojpa;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();                                     // 트랜잭션 시작

        try {

            Profile profile = new Profile();
            profile.setName("profile1");
            profile.getLoginHistory().add(new Login("first", LocalDateTime.now()));
            profile.getLoginHistory().add(new Login("second", LocalDateTime.of(2024, 10, 4, 12, 10, 10)));

            em.persist(profile);

            em.flush();
            em.clear();

            Profile findProfile = em.find(Profile.class, profile.getId());

            findProfile.getLoginHistory().remove(new Login("second", LocalDateTime.of(2024, 10, 4, 12, 10, 10)));
            findProfile.getLoginHistory().add(new Login("third", LocalDateTime.of(1, 1, 1, 1, 1, 1)));

            tx.commit();                                // 트랜잭션 커밋(반영)
        } catch (Exception e) {
            tx.rollback();                              // 트랜잭션 롤백(미반영)
        } finally {
            em.close();
        }

        emf.close();
    }

}
