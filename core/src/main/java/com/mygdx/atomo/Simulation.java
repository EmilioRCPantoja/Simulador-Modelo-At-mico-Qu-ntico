    package com.mygdx.atomo;

    import com.badlogic.gdx.ApplicationAdapter;
    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.graphics.GL20;
    import com.badlogic.gdx.graphics.PerspectiveCamera;
    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.graphics.g3d.Environment;
    import com.badlogic.gdx.graphics.g3d.ModelBatch;
    import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
    import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
    import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
    import com.badlogic.gdx.math.Vector3;
    import com.badlogic.gdx.utils.ScreenUtils;
    import com.badlogic.gdx.utils.Timer;
    import jakarta.persistence.EntityManager;
    import jakarta.persistence.EntityManagerFactory;
    import jakarta.persistence.Persistence;

    import java.util.ArrayList;

    /** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
    public class Simulation extends ApplicationAdapter {
        private ModelBatch batch    ;
        private Texture image;
        private Vector3 v = new Vector3(0,0,-550);
        private Vector3 vb = new Vector3(0,0,550);
        private Vector3 vc = new Vector3(0,0,0);
        public PerspectiveCamera cam;
        public Environment environment;
        public CameraInputController camCont;
        //conexão banco
        private EntityManagerFactory emf;
        private EntityManager em;

        //partes do atomo
        private Particula p = new Particula(v,0,0.0, 40f);
        private  Nucleo n = new Nucleo(p);
        private Particula pb = new Particula(vb,500,0.0, 40f);
        private  Nucleo nb = new Nucleo(pb);
        private Particula pc = new Particula(vc,500,0.0, 40f);
        private  Nucleo nc = new Nucleo(pc);
        private ArrayList<Eletron> el = new ArrayList<Eletron>();
        private ArrayList<Eletron> elb = new ArrayList<Eletron>();
        private ArrayList<Eletron> elc = new ArrayList<Eletron>();
        private NuvemEletronica nu = new NuvemEletronica(300, n,20, el);
        private NuvemEletronica nub = new NuvemEletronica(300, nb,20, elb);
        private NuvemEletronica nuc = new NuvemEletronica(300, nc,20, elc);
        private Atomo a = new Atomo( nu, n);
        private Atomo ab = new Atomo( nub, nb);
        private Atomo ac = new Atomo( nuc, nc);

        @Override
        public void create() {


            //config janela
            environment = new Environment();
            environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.2f, 0.2f, 0.2f, 0.5f));
            environment.add(new DirectionalLight().set(0.4f, 0.4f, 0.4f, -1f, -0.5f, 1f));
            cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            cam.position.set(800f,350f,500f);
            cam.lookAt(vc);
            cam.near = 1f;
            cam.far = 10000f;
            cam.update();
            camCont = new CameraInputController(cam);
            Gdx.input.setInputProcessor(camCont);

            batch = new ModelBatch();

            //partes do atomo

            nub.setOrbital(NuvemEletronica.Orbital.p2x);
            nuc.setOrbital(NuvemEletronica.Orbital.p2y);

            for(int i =0; i<5000; i++) {
                el.add(new Eletron(0, 5f));
                elb.add(new Eletron(0, 5f));
                elc.add(new Eletron(0, 5f));
            }
            a.create();
            ab.create();
            ac.create();

            nub.alternarOrb(false);

            try{
                emf = Persistence.createEntityManagerFactory("MeuPU");
                salvarAtomoBD();

            }catch (Exception e){
                Gdx.app.error("Database", "Erro ao estabelecer conexão com o banco de dados!");
            }
        }

        @Override
        public void render() {
            //config janela
            camCont.update();

            ScreenUtils.clear(1, 1, 1, 1, true);

            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            //partes do atomo
            batch.begin(cam);
            ac.render(batch, cam, environment);
            ab.render(batch, cam, environment);
            a.render(batch, cam, environment);
            batch.end();


        }

        @Override
        public void dispose() {
            ac.dispose();
            ab.dispose();
            a.dispose();
            batch.dispose();
            if (em != null) em.close();
            if (emf != null) emf.close();
        }

        public void salvarAtomoBD(){
            if(emf == null) return;

            new Thread(new Runnable() {
                @Override
                public void run() {

                    EntityManager localEm = emf.createEntityManager();
                    try{
                        localEm.getTransaction().begin();
                        localEm.persist(a);
                        localEm.persist(ab);
                        localEm.persist(ac);

                        localEm.flush();

                        localEm.getTransaction().commit();
                    }catch (Exception e) {
                        if (localEm.getTransaction().isActive()) {
                            try {
                                localEm.getTransaction().rollback();
                            } catch (Exception rollbackEx) {
                                Gdx.app.error("DATABASE", "Erro no rollback: " + rollbackEx.getMessage());
                            }
                        }
                        e.printStackTrace();
                        Gdx.app.error("DATABASE", "FALHA CRÍTICA NO BANCO: " + e.getMessage());
                    }
                }
            }).start();
        }


    }
