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
        public PerspectiveCamera cam;
        public Environment environment;
        public CameraInputController camCont;
        //conexão banco
        private EntityManagerFactory emf;
        private EntityManager em;

        //partes do atomo
        private Vector3 v = new Vector3(0,0,-550);
        private Vector3 vb = new Vector3(0,0,550);
        private Vector3 vc = new Vector3(0,0,0);

        private ArrayList<Vector3> pos = new ArrayList<Vector3>();
        private ArrayList<Particula> ps = new ArrayList<Particula>();
        private ArrayList<Nucleo> ns = new ArrayList<Nucleo>();
        private ArrayList<ArrayList<Eletron>> els = new ArrayList<ArrayList<Eletron>>();
        private ArrayList<NuvemEletronica> nus = new ArrayList<NuvemEletronica>();
        private ArrayList<Atomo> as = new ArrayList<Atomo>();



        @Override
        public void create() {

            //partes do atomo
            pos.add(v);
            pos.add(vb);
            pos.add(vc);

            for(Vector3 p : pos)
                ps.add( new Particula(p, 500, 0.0, 40f));


            for(Particula p : ps)
                ns.add(new Nucleo(p));

            for(Nucleo n : ns)
                els.add(new ArrayList<Eletron>());

            for(ArrayList<Eletron> es : els){
                for(int j =0; j<3000; j++) {
                    es.add(new Eletron(0, 8f));
                }
            }

            for(int i = 0; i < ns.size(); i++)
                nus.add(new NuvemEletronica(ns.get(i), els.get(i)));



            for(int i = 0; i < ns.size(); i++)
                as.add(new Atomo(nus.get(i), ns.get(i)));

            nus.get(1).setOrbital(NuvemEletronica.Orbital.p3x);
            nus.get(2).setOrbital(NuvemEletronica.Orbital.p3y);

            for(Atomo a: as)
                a.create();

            as.get(0).getNuvem().alternarOrb(true);


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


            try{
                emf = Persistence.createEntityManagerFactory("MeuPU");
                salvarAtomoBD();

            }catch (Exception e){
                Gdx.app.error("DATABASE", "Erro ao estabelecer conexão com o banco de dados!");
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
            for(Atomo a: as)
                a.render(batch, cam, environment);
            batch.end();


        }

        @Override
        public void dispose() {

            for(Atomo a: as)
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
                        for(Atomo a : as )
                            localEm.persist(a);
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
