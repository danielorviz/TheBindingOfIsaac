package com.thebindingofisaac.modelos;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;
import com.thebindingofisaac.graficos.Sprite;

import java.util.HashMap;
import java.util.List;


public class Enemigo extends Modelo {
    public int estado = ACTIVO;
    public static final int ACTIVO = 1;
    public static final int INACTIVO = 0;
    public static final int ELIMINAR = -1;


    public enum TipoEnemigo{
        ZOMBIE
    }
    public TipoEnemigo tipo=TipoEnemigo.ZOMBIE;




    public static final String CAMINANDO_DERECHA = "caminando_derecha";
    public static final String CAMINANDO_IZQUIERDA = "caminando_izquierda";
    public static final String MUERTE_DERECHA = "muerte_derecha";
    public static final String MUERTE_IZQUIERDA = "muerte_izquierda";


    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();

    public double velocidadX = 1.2;
    public double velocidadY= 1.2;


    public Enemigo(Context context, double xInicial, double yInicial) {
        super(context, 0, 0, 49, 30);

        this.x = xInicial;
        this.y = yInicial - altura/2;

        cDerecha = 15;
        cIzquierda = 15;
        cArriba = 20;
        cAbajo = 10;

        inicializar();
    }

    public void destruir (){
        velocidadX = 0;
        estado = INACTIVO;
    }

    public void inicializar (){

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_derecha),
                ancho, altura,
                4, 9, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_izquierda),
                ancho, altura,
                4, 9, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);

        Sprite muerteDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydieright),
                ancho, altura,
                4, 8, false);
        sprites.put(MUERTE_DERECHA, muerteDerecha);

        Sprite muerteIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemydie),
                ancho, altura,
                4, 8, false);
        sprites.put(MUERTE_IZQUIERDA, muerteIzquierda);


        sprite = caminandoDerecha;
    }

    public void actualizar (long tiempo) {
        boolean finSprite = sprite.actualizar(tiempo);

        if ( estado == INACTIVO && finSprite == true){
            estado = ELIMINAR;
        }

        if (estado == INACTIVO){
            if (velocidadX > 0)
                sprite = sprites.get(MUERTE_DERECHA);
            else
                sprite = sprites.get(MUERTE_IZQUIERDA);

        } else {
            if (velocidadX > 0) {
                sprite = sprites.get(CAMINANDO_DERECHA);
            }
            if (velocidadX < 0) {
                sprite = sprites.get(CAMINANDO_IZQUIERDA);
            }
        }
    }

    public void girarX(){
        velocidadX = velocidadX*-1;
    }

    public void girarY(){
        velocidadY = velocidadY*-1;
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY);
    }

    public void moverseHaciaJugador(double jugadorX,double jugadorY){
        Log.i("posicionesC","enemigo y "+ y + " x "+x+ "juga x "+jugadorX + " jugay "+jugadorY);

        if(x==jugadorX){
            if(y< jugadorY && velocidadY<0) {
                girarY();
            }else if(y> jugadorY && velocidadY>0){
                girarY();
            }
            return;
        }
        if(y==jugadorY){
            if(x< jugadorX && velocidadX<0) {
                girarX();
            }else if(x> jugadorX && velocidadY>0){
                girarY();
            }
            return;
        }
        if(x < jugadorX && y<jugadorY){
            if(velocidadX<0){
                girarX();
            }
            if(velocidadY<0){
                girarY();
            }
            return;
        }
        if(x < jugadorX && y>jugadorY){
            if(velocidadX<0){
                girarX();
            }
            if(velocidadY>0){
                girarY();
            }
            return;
        }
        if(x > jugadorX && y<jugadorY){
            if(velocidadX>0){
                girarX();
            }
            if(velocidadY<0){
                girarY();
            }
            return;
        }
        if(x > jugadorX && y>jugadorY){
            if(velocidadX>0){
                girarX();
            }
            if(velocidadY>0){
                girarY();
            }
            return;
        }

    }
}
