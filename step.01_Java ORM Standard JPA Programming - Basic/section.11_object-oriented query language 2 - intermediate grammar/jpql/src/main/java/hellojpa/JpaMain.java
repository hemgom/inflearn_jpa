package hellojpa;

import hellojpa.jpql.Member;
import hellojpa.jpql.MemberDTO;
import hellojpa.jpql.Team;
import jakarta.persistence.*;

import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();                                     // 트랜잭션 시작

        try {

            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");
            Team teamC = new Team("teamC");
            em.persist(teamA);
            em.persist(teamB);
            em.persist(teamC);

            Member member1 = new Member("member1", 10, teamA);
            Member member2 = new Member("member2", 15, teamA);
            Member member3 = new Member("member3", 13, teamB);
            Member member4 = new Member("member4", 17, null);
            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
            em.persist(member4);

            // 해당 부분 생략해도 결과는 나옴 하지만 실전에서는 써야 함 (영속성 컨텍스트와 DB 정합성)
            em.flush();
            em.clear();

            // 페치 조인 - 엔티티 예제
//            String jpql = "select m from Member m join fetch m.team";
//            List<Member> result = em.createQuery(jpql, Member.class)
//                    .getResultList();
//
//            for (Member m : result) {
//                System.out.println("============================== ");
//                System.out.println("username = " + m.getUsername());
//                System.out.println("team = " + m.getTeam().getName());
//            }

            // 페치 조인 - 컬렉션 에제
            String jpql = "select t from Team t join fetch t.members";
            List<Team> result = em.createQuery(jpql, Team.class)
                    .getResultList();

            for (Team t : result) {
                System.out.println("============================== ");;
                System.out.println("teamName = " + t.getName());
                System.out.println("team = " + t);
                for (Member m : t.getMembers()) {
                    System.out.println("username = " + m.getUsername());
                    System.out.println("member = " + m);
                }
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