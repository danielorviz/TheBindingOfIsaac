package com.thebindingofisaac.modelos.enemigos;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;
import com.thebindingofisaac.graficos.Sprite;
import com.thebindingofisaac.modelos.Modelo;
import com.thebindingofisaac.modelos.Nivel;

import java.util.HashMap;
import java.util.List;


public class EnemigoZombie extends Enemigo {

    public TipoEnemigo tipo=TipoEnemigo.ZOMBIE;


    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();


    public EnemigoZombie(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, 49, 30);

        velocidadX = 1.2;
        velocidadY = 1.2;

        inicializar();
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
            if (velocidadX >= 0) {
                sprite = sprites.get(CAMINANDO_DERECHA);
            }
            else if (velocidadX < 0) {
                sprite = sprites.get(CAMINANDO_IZQUIERDA);
            }
        }
    }

    public void moverseHaciaJugador(double jugadorX,double jugadorY){

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

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY);
    }
}
