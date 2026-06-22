package com.mygdx.atomo;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Nucleo")
public class Nucleo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double qtdParticula = 0;

    @Override
    public String toString() {
        String s = "Nucleo{" +
            "id=" + id +
            ", posNucleo=" + posNucleo +
            '}';

        for(Particula p : nucleo){
            s += p.toString();
        }

        return s;
    }

    @Transient
    private Vector3 posNucleo;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "particulas")
    private List<Particula> nucleo = new ArrayList<>();

    //Contructors
    public Nucleo(Particula p ){
        nucleo.add(p);
        Vector3 po = pontoMedio((ArrayList<Particula>) nucleo);
        this.posNucleo = po;
        this.qtdParticula ++;
    }

    public Nucleo (List<Particula> p){
        this.nucleo = p;
        Vector3 po = pontoMedio((ArrayList<Particula>) nucleo);

        this.posNucleo = po;
        this.qtdParticula = p.size();
    }

    public Nucleo() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //Getters e Setters
    public Vector3 getPosNucleo() {
        return posNucleo;

    }
    public void setNucleo(List<Particula> nucleo) {
        this.nucleo = nucleo;
        this.posNucleo = pontoMedio(this.nucleo);
        this.qtdParticula = nucleo.size();
    }

    public void create(){
        for(Particula p : nucleo){
            p.create();
        }
    }

    public void render(ModelBatch batch, PerspectiveCamera cam, Environment env){
        for(Particula p : nucleo){
            p.render(batch, cam, env);
        }
    }

    public void dispose(){
        for(Particula p : nucleo){
            p.dispose();
        }
    }

    private Vector3 pontoMedio(List<Particula> p){
        Vector3 po = new Vector3();
        if(nucleo.size() > 1){
            for(Particula pa : nucleo){
                po.x += pa.getPos().x;
                po.y += pa.getPos().y;
                po.z += pa.getPos().z;
            }
            po.x/=nucleo.size();
            po.z/=nucleo.size();
            po.y/=nucleo.size();
        }
        else
            po = p.get(0).getPos();

        return po;
    }

    public void addParticula(Particula p){
        this.nucleo.add(p);
        this.posNucleo = pontoMedio((ArrayList<Particula>) nucleo);
        this.qtdParticula ++;
    }

    public double getTam(){
        if(this.nucleo.size() == 0 )
            return 0;
        else if(this.nucleo.size() == 1)
            return this.nucleo.get(0).getTam();
        else if(this.nucleo.size() == 2)
            return 2 * this.nucleo.get(0).getTam();

        return this.nucleo.get(0).getTam() * Math.cbrt(this.nucleo.size()/0.74);
    }

}
