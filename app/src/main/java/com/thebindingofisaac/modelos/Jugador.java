package com.thebindingofisaac.modelos;

import android.content.Context;
import android.graphics.Canvas;


import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;
import com.thebindingofisaac.graficos.Sprite;

import java.util.HashMap;

/**
 * Created by jordansoy on 09/10/2017.
 */

public class Jugador extends Modelo {



    //ANIMACIONES

    public static final String PARADO_DERECHA = "Parado_derecha";
    public static final String PARADO_IZQUIERDA = "Parado_izquierda";
    public static final String CAMINANDO_DERECHA = "Caminando_derecha";
    public static final String CAMINANDO_IZQUIERDA = "Caminando_izquierda";
    public static final String CAMINANDO_ARRIBA = "Caminando_arriba";
    public static final String CAMINANDO_ABAJO = "Caminando_abajo";

    public static final String DISPARANDO_DERECHA = "disparando_derecha";
    public static final String DISPARANDO_IZQUIERDA = "disparando_izquierda";
    public static final String GOLPEADO_DERECHA = "golpeado_derecha";
    public static final String GOLPEADO_IZQUIERDA = "golpeado_izquierda";


    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();


    /// ORIENTACIONES ///
    public int orientacion;
    public static final int DERECHA = 1;
    public static final int IZQUIERDA = -1;
    public static final int ARRIBA = 2;
    public static final int ABAJO = -2;


    /// POSICIONAMIENTO ///
    double velocidadX;
    float velocidadY;

    double xInicial;
    double yInicial;


    /// ESTADO ///
    int vidas = 6;

    public boolean disparando;
    public boolean golpeado = false;
    int msInmunidad = 2;

    public static final String ARMA_DISTANCIA = "distancia";
    public static final String ARMA_MELEE = "melee";

    public String armaActual;




    public Jugador(Context context, double xInicial, double yInicial) {
        super(context, 0, 0, 40, 40);

        // guardamos la posición inicial porque más tarde vamos a reiniciarlo
        this.xInicial = xInicial;
        this.yInicial = yInicial - altura/2;

        this.x =  this.xInicial;
        this.y =  this.yInicial;

        armaActual = Jugador.ARMA_MELEE;

        inicializar();
    }

    public void inicializar (){
        Sprite disparandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_caballero_ataque_derecha),
                ancho, altura,
                4, 6, false);
        sprites.put(DISPARANDO_DERECHA, disparandoDerecha);

        Sprite disparandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_caballero_ataque_izquierda),
                ancho, altura,
                4, 6, false);
        sprites.put(DISPARANDO_IZQUIERDA, disparandoIzquierda);

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_caballero_derecha),
                ancho, altura,
                4, 2, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_caballero_izquierda),
                ancho, altura,
                4, 2, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);

        Sprite paradoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_caballero_quieto),
                ancho, altura,
                4, 2, true);
        sprites.put(PARADO_DERECHA, paradoDerecha);

        Sprite paradoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_caballero_quieto),
                ancho, altura,
                4, 2, true);
        sprites.put(PARADO_IZQUIERDA, paradoIzquierda);

        Sprite golpeadoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.playerimpactright),
                ancho, altura,
                4, 4, false);
        sprites.put(GOLPEADO_DERECHA, golpeadoDerecha);

        Sprite golpeadoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.playerimpact),
                ancho, altura,
                4, 4, false);
        sprites.put(GOLPEADO_IZQUIERDA, golpeadoIzquierda);

        Sprite caminandoArriba = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_caballero_arriba),
                ancho, altura,
                4, 2, true);
        sprites.put(CAMINANDO_ARRIBA, caminandoArriba);
        Sprite caminandoAbajo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_caballero_abajo),
                ancho, altura,
                4, 2, true);
        sprites.put(CAMINANDO_ABAJO, caminandoAbajo);


// animación actual
        sprite = paradoDerecha;
    }

    public void procesarOrdenes (float orientacionPad, boolean disparar) {

        if (disparar){
            disparando = true;
            // preparar los sprites, no son bucles hay que reiniciarlos
            sprites.get(DISPARANDO_DERECHA).setFrameActual(0);
            sprites.get(DISPARANDO_IZQUIERDA).setFrameActual(0);
        }



        if (orientacionPad ==1) {
            velocidadX = -5;
            orientacion = IZQUIERDA;
        } else if (orientacionPad == -1 ){
            velocidadX = 5;
            orientacion = DERECHA;
        }else if(orientacionPad == 2){
            velocidadY = -5;
            orientacion = ARRIBA;
        }else if ( orientacionPad  == -2 ){
            velocidadY = 5;
            orientacion = ABAJO;
         } else{
            velocidadX = 0;
            velocidadY = 0;
        }
    }


    public void actualizar (long tiempo) {
        if(msInmunidad > 0){
            msInmunidad -= tiempo;
        }

        boolean finSprite = sprite.actualizar(tiempo);

        // Deja de estar golpeado, cuando lo estaba y se acaba el sprite
        if (golpeado && finSprite){
            golpeado = false;
        }

        if(disparando && finSprite){
            disparando = false;
        }

        if (velocidadX > 0){
            sprite = sprites.get(CAMINANDO_DERECHA);
        }
        if (velocidadX < 0 ){
            sprite = sprites.get(CAMINANDO_IZQUIERDA);
        }
        if (velocidadX == 0 ){
            if (orientacion == DERECHA){
                sprite = sprites.get(PARADO_DERECHA);
            } else if (orientacion == IZQUIERDA) {
                sprite = sprites.get(PARADO_IZQUIERDA);
            }
        }
        if (orientacion==ARRIBA) {
            sprite = sprites.get(CAMINANDO_ARRIBA);
        }
        if (orientacion==ABAJO) {
            sprite = sprites.get(CAMINANDO_ABAJO);
        }
        if (disparando){
            if (orientacion == DERECHA){
                sprite = sprites.get(DISPARANDO_DERECHA);
            } else if (orientacion == IZQUIERDA) {
                sprite = sprites.get(DISPARANDO_IZQUIERDA);
            }
        }

        if (golpeado){
            if (orientacion == DERECHA){
                sprite = sprites.get(GOLPEADO_DERECHA);
            } else if (orientacion == IZQUIERDA) {
                sprite = sprites.get(GOLPEADO_IZQUIERDA);
            }
        }


    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX , (int) y - Nivel.scrollEjeY,msInmunidad > 0);
    }


    public void restablecerPosicionInicial(){
        vidas = 3;
        golpeado = false;
        msInmunidad = 0;

        this.x = xInicial;
        this.y = yInicial;
        orientacion = IZQUIERDA;
    }

    public int golpeado(){
        if (msInmunidad <= 0) {
            if (vidas > 0) {
                vidas--;
                msInmunidad = 3000;
                golpeado = true;
                // Reiniciar animaciones que no son bucle
                sprites.get(GOLPEADO_IZQUIERDA).setFrameActual(0);
                sprites.get(GOLPEADO_DERECHA).setFrameActual(0);
            }
        }
        return vidas;
    }
    public void actualizarPuntoInicial(double x, double y){
        xInicial = x;
        yInicial = y;
    }

    public int getVidas() {
        return vidas;
    }

}
