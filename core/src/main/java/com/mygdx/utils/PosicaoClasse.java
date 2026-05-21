package com.mygdx.utils;

import com.badlogic.gdx.math.Vector3;

import java.lang.reflect.Method;
import java.util.Map;

public class PosicaoClasse {
    public static Object obterPos(Object o){
        Class<?> classe = o.getClass();
        Object valor = null;
        for(Method m: classe.getMethods()){
            try{
                if(isPos(m)){
                    valor = m.invoke(o);
                }
            }
            catch(Exception e){
                throw new RuntimeException("Não foi possível obter Posição", e);
            }
        }
        return valor;

    }

    public static boolean isPos(Method m){
        return m.getName().equals("getPosNucleo") && m.getReturnType() == Vector3.class &&
                m.getParameterTypes().length == 0;

    }
}
