package hellojpa;

import hellojpa.jpql.Member;
import hellojpa.jpql.MemberDTO;
import jakarta.persistence.*;

import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();                                     // 트랜잭션 시작

        try {

            for (int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }

            em.flush();
            em.clear();

            List<Member> result = em.createQuery("select m from Member m order by m.age desc ", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("result size = " + result.size());
            for (Member m : result) {
                System.out.println("name : " + m.getUsername());
                System.out.println("age : " + m.getAge());
            }

            tx.commit();                                // 트랜잭션 커밋(반영)
        } catch (Exception e) {
            tx.rollback();                              // 트랜잭션 롤백(미반영)
        } finally {
            em.close();
        }

        emf.close();
    }
}