package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setAge(10);
            member1.setTeam(teamA);
            member1.setMemberType(MemberType.ADMIN);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(10);
            member2.setTeam(teamA);
            member2.setMemberType(MemberType.ADMIN);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setAge(10);
            member3.setTeam(teamB);
            member3.setMemberType(MemberType.ADMIN);

            Member member4 = new Member();
            member4.setUsername("회원4");
            member4.setAge(10);
            member4.setMemberType(MemberType.ADMIN);

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);
            em.persist(member4);

            em.flush();
            em.clear();

            String query = "select distinct t from Team t join fetch t.members where t.name = '팀A'";
            List<Team> resultList = em.createQuery(query, Team.class)
                    .getResultList();

            for (Team team : resultList) {
                System.out.println("team = " + team.getName() + " |members=" + team.getMembers().size());
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
