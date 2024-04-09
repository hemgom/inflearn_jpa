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
            // 팀, 멤버 객체 생성
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setName("member1");
            member.setAge(10);
            member.setTeam(team);
            em.persist(member);

            em.flush();     // 영속성 컨텍스트의 내용을 DB 에 반영하기 위해 사용
            em.clear();     // 영속성 컨텍스트 초기화
            // 영속성 컨텍스트가 초기화 되었기 때문에 이 후 저장한 정보는 DB 에서 가져오게 된다.
            // 정확히는 DB 에서 조회한 후 영속성 컨텍스트에 올린 후 정보를 반환한다.

            // 조회
            Member findMember = em.find(Member.class, member.getId());

            // 참조를 통해 연관관계 조회
            Team findTeam = findMember.getTeam();

            // 수정
            Team team2 = new Team();
            team2.setName("teamB");
            em.persist(team2);

            member.setTeam(team2);  // member1 에 새로운 팀 설정

            tx.commit();                                // 트랜잭션 커밋(반영)
        } catch (Exception e) {
            tx.rollback();                              // 트랜잭션 롤백(미반영)
        } finally {
            em.close();
        }

        emf.close();
    }

}
