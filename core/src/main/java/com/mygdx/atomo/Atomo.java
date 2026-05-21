package com.mygdx.atomo;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import jakarta.persistence.*;

@Entity
@Table(name = "Atomo")
public class Atomo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private Vector3 pos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nucleo_id", unique = true)
    private Nucleo nucleo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nuvem_id", unique = true)
    private NuvemEletronica nuvem;

    public Atomo(NuvemEletronica nuvem, Nucleo nucleo){
        this.pos = nucleo.getPosNucleo();
        this.nuvem = nuvem;
        this.nucleo = nucleo;
    }

    public Atomo() {

    }

    public void create(){
        nuvem.create();
        nucleo.create();
    }

    public void dispose(){
        nuvem.dispose();
        nucleo.dispose();
    }

    public Vector3 getPos() {
        return pos;
    }

    public void setPos(Vector3 pos) {
        this.pos = pos;
    }

    public void render(ModelBatch batch, PerspectiveCamera cam, Environment env){
        nuvem.render(batch,cam,env);
        nucleo.render(batch,cam, env);
    }

    @Override
    public String toString() {
        String s = "Atomo{" +
            "id=" + id +
            ", pos=" + pos +
            ", nucleo=" + nucleo.toString() +
            ", nuvem=" + nuvem.toString() +
            '}';
        return s;
    }
}
