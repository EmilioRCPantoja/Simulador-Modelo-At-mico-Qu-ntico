package com.mygdx.atomo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import jakarta.persistence.*;
import org.hibernate.type.descriptor.sql.internal.Scale6IntervalSecondDdlType;

import java.util.Random;


@Entity
@Table(name = "Eletron")
public class Eletron {
    @Id
    @Column(name = "infoP", columnDefinition = "INTEGER")
    private Long id;

    @Transient
    private Vector3 pos = new Vector3();

    @OneToOne
    @MapsId
    @JoinColumn(name = "infoP", unique = true)
    private Particula infoP;

    private int nivelEnergia;

    private float tam;

    public Eletron(int tamanho){
        Random rd = new Random();
        int a = rd.nextInt(tamanho) + 1;
        this.nivelEnergia = rd.nextInt(5);
        //this.tam = tam;
        this.tam = a * 2;
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

        switch (this.nivelEnergia) {
            case 0 :
                infoP.mudarCor(Color.YELLOW);
                break;
            case 1 :
                infoP.mudarCor(Color.RED);
                break;
            case 2 :
                infoP.mudarCor(Color.BLUE);
                break;
            case 3 :
                infoP.mudarCor(Color.GREEN);
                break;
            case 4 :
                infoP.mudarCor(Color.PURPLE);
                break;
        }

        //infoP.mudarCor(Color.BLACK);

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
