package com.mygdx.utils;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.atomo.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class testeBD {
    public static void main(String[] args) {
        Vector3 pos = new Vector3(0,0,0);
        Particula p = new Particula(pos,1,3.3,5f);
        Eletron e = new Eletron(1,2);
        Nucleo n = new Nucleo();
        n.addParticula(p);
        NuvemEletronica nu = new NuvemEletronica(10, n);
        Atomo a = new Atomo(nu, n);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MeuPU");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();
        em.persist(a);
        tx.commit();

        Atomo pp  = em.createQuery("from Atomo ", Atomo.class).getSingleResult();
        System.out.println(pp.toString());
    }

}
