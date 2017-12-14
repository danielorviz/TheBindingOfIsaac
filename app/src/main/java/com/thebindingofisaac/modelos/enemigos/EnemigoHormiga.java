package com.thebindingofisaac.modelos.enemigos;

import android.content.Context;
import android.graphics.Canvas;

import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;
import com.thebindingofisaac.graficos.Sprite;
import com.thebindingofisaac.modelos.Nivel;

import java.util.HashMap;


public class EnemigoHormiga extends Enemigo {

    public TipoEnemigo tipo=TipoEnemigo.HORMIGA;


    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();

    public int divisiones = 1;

    public EnemigoHormiga(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, 30, 45);

        velocidadX = 0.8;
        velocidadY = 0.8;

        if(divisiones==0){
            ancho=35;
            altura=20;
            velocidadX=1;
            velocidadY=1;
        }

        inicializar();
    }

    public void inicializar (){

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_hormiga_derecha),
                ancho, altura,
                8, 4, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_hormiga_izquierda),
                ancho, altura,
                8, 4, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);

        Sprite muerteDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_hormiga_destruida),
                ancho, altura,
                12, 12, false);
        sprites.put(MUERTE_DERECHA, muerteDerecha);

        Sprite muerteIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.animacion_hormiga_destruida),
                ancho, altura,
                12, 12, false);
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

    public void moverseHaciaJugador(double jugadorX,double jugadorY){
        //Log.i("posicionesC","enemigo y "+ y + " x "+x+ "juga x "+jugadorX + " jugay "+jugadorY);

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
