package com.mygdx.atomo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import jakarta.persistence.*;


@Entity
@Table(name = "Eletron")
public class Eletron {
    @Id
    @Column(name = "infoP")
    private Long id;

    @Transient
    private Vector3 pos = new Vector3();

    @OneToOne
    @MapsId
    @JoinColumn(name = "infoP", unique = true)
    private Particula infoP;

    private int nivelEnergia;

    private float tam;

    public Eletron(int nivelEnergia, float tamanho){
        this.nivelEnergia = nivelEnergia;
        this.tam = tamanho;
        infoP = new Particula(pos,1,0d,tam);
    }

    public Eletron() {

    }

    //Getters e Setters
    public Vector3 getPos() {
        return pos;
    }

    public void setPos(Vector3 pos) {
        this.pos = pos;
    }

    public Particula getInfoP() {
        return infoP;
    }

    public void setInfoP(Particula infoP) {
        this.infoP = infoP;
    }

    public int getNivelEnergia() {
        return nivelEnergia;
    }

    public void setNivelEnergia(int nivelEnergia) {
        this.nivelEnergia = nivelEnergia;
    }

    public float getTam() {
        return tam;
    }

    public void setTam(float tam) {
        this.tam = tam;
    }

    public void create(){
        infoP.create();
    }

    public void dispose(){
        infoP.dispose();
    }

    public void render(ModelBatch batch,PerspectiveCamera cam, Environment env){
        infoP.render(batch, cam,env);
        infoP.mudarCor(Color.BLACK);
        infoP.setDivs(8);
    }

    @Override
    public String toString() {
        return "Eletron{" +
            "id=" + id +
            ", pos=" + pos +
            ", nivelEnergia=" + nivelEnergia +
            ", tam=" + tam +
            '}' + infoP.toString();
    }

    public void translation(Vector3 pos){
        this.infoP.translation(pos);
    }
}
