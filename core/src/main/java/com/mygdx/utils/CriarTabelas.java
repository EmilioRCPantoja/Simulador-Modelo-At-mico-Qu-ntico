package com.mygdx.utils;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class CriarTabelas {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MeuPU");
    }
}
