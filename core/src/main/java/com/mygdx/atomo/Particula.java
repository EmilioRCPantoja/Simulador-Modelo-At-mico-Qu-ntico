package com.mygdx.atomo;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import jakarta.persistence.*;

@Entity
@Table(name = "Particula")
public class Particula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INTEGER")
    private Long id;

    @Transient
    private Vector3 pos;

    private int carga;

    private Double massa;

    @Transient
    private static Model modelN;

    @Transient
    private static Model modelE;

    @Transient
    private ModelBuilder builder;

    private float tam;

    @Transient
    private ColorAttribute colorAttribute;

    @Transient
    private ModelInstance instance;

    @Transient
    private static int refN = 0;

    @Transient
    private static int refE = 0;

    @Transient
    private int divs = 12;

    public Particula() {

    }

    //Getters e Setters
    public ModelInstance getInstance() {
        return instance;
    }

    public void setInstance(ModelInstance instance) {
        this.instance = instance;
    }

    public Vector3 getPos() {
        return pos;
    }

    public void setPos(Vector3 pos) {
        this.pos = pos;
    }

    public int getCarga() {
        return carga;
    }

    public void setCarga(int carga) {
        this.carga = carga;
    }

    public Double getMassa() {
        return massa;
    }

    public void setMassa(Double massa) {
        this.massa = massa;
    }

    public ModelBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(ModelBuilder builder) {
        this.builder = builder;
    }

    public void setDivs(int div){ this.divs = div;}

    public int getDivs(){return this.divs;}

    public Particula(Vector3 pos, int carga, Double massa, ModelBatch batch, Model model, ModelBuilder builder, ModelInstance instance) {
        this.pos = pos;
        this.carga = carga;
        this.massa = massa;
        this.builder = builder;
        this.instance = instance;
    }

    public Particula(Vector3 pos, int carga, Double massa, float tam){
        this.pos = pos;
        this.carga = carga;
        this.massa = massa;
        this.tam = tam;
    }

    public void create(){
            if (tam > 10f) { // nucleo
                if (modelN == null)
                    modelN = new ModelBuilder().createSphere(tam, tam, tam, divs, divs,
                        new Material(ColorAttribute.createDiffuse(Color.RED)),
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                refN++;
                this.instance = new ModelInstance(modelN);
            } else { // eletron
                if (modelE == null)
                    modelE = new ModelBuilder().createSphere(tam, tam, tam, divs, divs,
                        new Material(ColorAttribute.createDiffuse(Color.RED)),
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                refE++;
                this.instance = new ModelInstance(modelE);
            }
            colorAttribute = (ColorAttribute) instance.materials.get(0).get(ColorAttribute.Diffuse);
            this.instance.transform.setToTranslation(pos);

    }

    public float getTam() {
        return tam;
    }

    public void render(ModelBatch batch, PerspectiveCamera cam, Environment env){
        this.instance.transform.setToTranslation(this.pos);
        batch.render(this.instance, env);

    }

    public void dispose(){
        if (tam > 10f) {
            if (--refN <= 0 && modelN != null) {
                modelN.dispose();
                modelN = null;
            }
        } else {
            if (--refE <= 0 && modelE != null) {
                modelE.dispose();
                modelE = null;
            }
        }
    }

    public void translation(Vector3 pos){
        this.instance.transform.setToTranslation(pos);
    }

    public void mudarCor(Color novaCor) {
        colorAttribute.color.set(novaCor);
    }

    @Override
    public String toString() {
        return "Particula{" +
            "id=" + id +
            ", pos=" + pos +
            ", carga=" + carga +
            ", massa=" + massa +
            ", builder=" + builder +
            ", tam=" + tam +
            ", colorAttribute=" + colorAttribute +
            ", instance=" + instance +
            '}';
    }

    public void setTam(float tam) {
        this.tam = tam;

        if (this.instance != null) {

            this.instance.transform.setToTranslation(this.pos).scl(tam);
        }
    }
}
