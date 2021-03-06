package com.thebindingofisaac.modelos.enemigos;

import android.content.Context;
import android.graphics.Canvas;

import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;
import com.thebindingofisaac.graficos.Sprite;
import com.thebindingofisaac.modelos.Nivel;

import java.util.HashMap;

/**
 * Created by Dani on 15/12/2017.
 */

public class EnemigoBoss extends Enemigo{
    public TipoEnemigo tipo=TipoEnemigo.BOSS;
    public int vidas;

    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();

    public EnemigoBoss(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, 95, 57);

        velocidadX = 2;
        velocidadY = 2;

        inicializar();
    }

    public void inicializar (){
        vidas = 10;
        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.cabdrch),
                ancho, altura,
                4, 4, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.cabizq),
                ancho, altura,
                4, 4, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);

        Sprite caminandoAbajo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.cababj),
                ancho, altura,
                4, 4, true);
        sprites.put(CAMINANDO_ABAJO, caminandoAbajo);

        Sprite caminandoArriba = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.cabarr),
                ancho, altura,
                4, 4, true);
        sprites.put(CAMINANDO_ARRIBA, caminandoArriba);

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
            else if (velocidadX < 0) {
                sprite = sprites.get(CAMINANDO_IZQUIERDA);
            }
            else if (velocidadX == 0 && velocidadY > 0 ) {
                sprite = sprites.get(CAMINANDO_ARRIBA);
            }
            else if (velocidadX == 0 && velocidadY < 0 ) {
                sprite = sprites.get(CAMINANDO_ABAJO);
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

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }
}
