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
            // DB 에 객체 데이터 저장 로직
//            Member member = new Member();
//            member.setId(1L);
//            member.setName("helloJPA");
//            em.persist(member);


            // DB 에 저장된 객체 조회 및 조회한 객체 정보 확인
//            Member findMember = em.find(Member.class, 1L);
//            System.out.println("findMember Id = " + findMember.getId());
//            System.out.println("findMember Name = " + findMember.getName());


            // DB 에 저장된 객체 데이터 삭제
//            Member findMember = em.find(Member.class, 1L);
//            em.remove(findMember);


            // Db 에 저장된 객체 데이터 수정 (식별 값은 제외)
            // 따로 persistence 를 하지 않더라도 변경사항이 반영됨
            // commit 시점에서 변경사항을 확인하고 있다면 update query 를 만들어 날리기 때문
//            Member findMember = em.find(Member.class, 1L);
//            findMember.setName("helloNewName");


            // JPQL 작성
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(0)      // DB 에서 찾은 첫 번째 레코드 부터 (0 으로 시작함)
                    .setMaxResults(10)      // 11 번째 레코드 까지
                    .getResultList();       // 리스트에 담아서 반환한다.

            // 조회 결과 출력
            for (Member m : result) {
                System.out.println("member Name = " + m.getName());
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
