package hellojpa;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();                                     // 트랜잭션 시작

        try {

//            Member member = new Member();
//            member.setName("hello");
//
//            em.persist(member);
//
//            em.flush();
//            em.clear();

//            Member findMember = em.find(Member.class, member.getId());
//            System.out.println("findMemberId = " + findMember.getId());
//            System.out.println("findMemberName = " + findMember.getName());

//            Member findMember = em.getReference(Member.class, member.getId());  // 호출 시에는 DB 에 쿼리가 나가지 않음
//            System.out.println("findMember is who? = " + findMember.getClass());
//            System.out.println("findMemberId = " + findMember.getId());         // 실제 데이터 요청이 들어오면 DB 에서 쿼리가 나감
//            System.out.println("findMemberName = " + findMember.getName());

            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);

            em.flush();
            em.clear();

            Parent findParent = em.find(Parent.class, parent.getId());
            findParent.getChildren().remove(0);
//            em.persist(child1);
//            em.persist(child2);

            tx.commit();                                // 트랜잭션 커밋(반영)
        } catch (Exception e) {
            tx.rollback();                              // 트랜잭션 롤백(미반영)
        } finally {
            em.close();
        }

        emf.close();
    }

}
