/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.vn41.jpademo.entities;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author vnord
 */
public class Tester {
    
    public static void main(String[] args) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        
        Person p1 = new Person("Lou", 1998);
        Person p2 = new Person("Adelaide", 1994);
        
        Address a1 = new Address("Store Torv 1", 2323, "København");
        Address a2 = new Address("Kagså 2", 2345, "Gladsaxe");
        
        p1.setAddress(a1);
        p2.setAddress(a2);
        
        Fee f1 = new Fee(100);
        Fee f2 = new Fee(200);
        Fee f3 = new Fee(300);
        
        p1.addFee(f1);
        p1.addFee(f3);
        p2.addFee(f2);
        
        SwimStyle s1 = new SwimStyle("Crawl");
        SwimStyle s2 = new SwimStyle("Butterfly");
        SwimStyle s3 = new SwimStyle("Breast stroke");
        
        p1.addSwimStyle(s1);
        p1.addSwimStyle(s2);
        p2.addSwimStyle(s3);
        
        em.getTransaction().begin();
        //em.persist(a1);
        //em.persist(a2);
        em.persist(p1);
        em.persist(p2);
        em.getTransaction().commit();
        
        
        em.getTransaction().begin();
        p1.removeSwimStyle(s2);
        em.getTransaction().commit();
        
        
        System.out.println("p1: " + p1.getP_id() + ", " + p1.getName());
        System.out.println("p2: " + p2.getP_id());
        
        System.out.println("Lous gade: " + p1.getAddress().getStreet());
        
        System.out.println("To vejs virker: " + a1.getPerson().getName());
        
        System.out.println("Hvem har betalt f2: " + f2.getPerson().getName());
        
        System.out.println("Hvad er der blevet betalt i alt?");
        
        TypedQuery<Fee> q1 = em.createQuery("SELECT f FROM Fee f", Fee.class);
        List<Fee> fees = q1.getResultList();
        
        for (Fee f : fees) {
            System.out.println(f.getPerson().getName() + ": " + f.getAmount() + ", " + f.getPayDate() + ", Adr: " + f.getPerson().getAddress().getCity());
        }
        
    }
    
}
