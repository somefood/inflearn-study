package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            // 비영속
            Member member = new Member();
//            member.setId("ID_A");
            member.setUsername("HelloJPA");
//
//            // 영속
//            System.out.println("===BEFORE===");
//            em.persist(member); // 이때 디비 저장되는건 아님, 1차 캐시에 저장
//            System.out.println("===AFTER===");

//            Member member = em.find(Member.class, 150L);
//            member.setName("BBBBB");
//
////            em.detach(member);
//            em.clear();
//
//            Member member2 = em.find(Member.class, 150L);


//            Member member = new Member(201L, "member200");
            em.persist(member);
//
//            em.flush();

            System.out.println("=================");
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}