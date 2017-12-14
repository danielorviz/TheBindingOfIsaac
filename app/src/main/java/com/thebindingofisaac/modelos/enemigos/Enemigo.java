package com.thebindingofisaac.modelos.enemigos;

import android.content.Context;

import com.thebindingofisaac.modelos.Modelo;

/**
 * Created by user on 14/12/2017.
 */

public abstract class Enemigo extends Modelo {

    public int estado = ACTIVO;
    public static final int ACTIVO = 1;
    public static final int INACTIVO = 0;
    public static final int ELIMINAR = -1;

    public enum TipoEnemigo{
        ZOMBIE
    }


    public static final String CAMINANDO_DERECHA = "caminando_derecha";
    public static final String CAMINANDO_IZQUIERDA = "caminando_izquierda";
    public static final String CAMINANDO_ABAJO = "caminando_abajo";
    public static final String CAMINANDO_ARRIBA = "caminando_arriba";
    public static final String MUERTE_DERECHA = "muerte_derecha";
    public static final String MUERTE_IZQUIERDA = "muerte_izquierda";


    public double velocidadX = 1.2;
    public double velocidadY= 1.2;


    public Enemigo(Context context, double xInicial, double yInicial, int altura, int ancho) {
        super(context, 0, 0, altura, ancho);


        this.x = xInicial;
        this.y = yInicial - altura/2;

        cDerecha = 15;
        cIzquierda = 15;
        cArriba = 20;
        cAbajo = 10;
    }


    public void girarX(){
        velocidadX = velocidadX*-1;
    }

    public void girarY(){
        velocidadY = velocidadY*-1;
    }


    public void destruir (){
        velocidadX = 0;
        estado = INACTIVO;
    }
}
