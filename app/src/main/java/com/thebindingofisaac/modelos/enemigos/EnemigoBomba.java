package com.thebindingofisaac.modelos.enemigos;

import android.content.Context;
import android.graphics.Canvas;

import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;
import com.thebindingofisaac.graficos.Sprite;
import com.thebindingofisaac.modelos.Nivel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Darío on 13/12/2017.
 */

public class EnemigoBomba extends Enemigo {


    private Sprite sprite;
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite>();

    private List<Bomba> bombas;

    public long tiempoBomba = 0;
    public int radioExplosion = 100;

    public TipoEnemigo tipo=TipoEnemigo.BOMBA;

    public EnemigoBomba(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, 44, 33);

        velocidadX = 1.6;
        velocidadY = 1.6;

        bombas = new ArrayList<Bomba>();

        inicializar();
    }

    public void inicializar (){

        Sprite caminandoDerecha = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_bomba_derecha),
                ancho, altura,
                4, 3, true);
        sprites.put(CAMINANDO_DERECHA, caminandoDerecha);

        Sprite caminandoIzquierda = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_bomba_izquierda),
                ancho, altura,
                4, 3, true);
        sprites.put(CAMINANDO_IZQUIERDA, caminandoIzquierda);

        Sprite caminandoAbajo = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_bomba_abajo),
                ancho, altura,
                4, 3, true);
        sprites.put(CAMINANDO_ABAJO, caminandoAbajo);

        Sprite caminandoArriba = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.enemigo_bomba_arriba),
                ancho, altura,
                4, 3, true);
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
            if (velocidadX < 0) {
                sprite = sprites.get(CAMINANDO_IZQUIERDA);
            }
            if (velocidadX == 0 && velocidadY > 0 ) {
                sprite = sprites.get(CAMINANDO_ARRIBA);
            }
            if (velocidadX == 0 && velocidadY < 0 ) {
                sprite = sprites.get(CAMINANDO_ABAJO);
            }
        }

        if (this.tiempoBomba == 0) {
            tiempoBomba = System.currentTimeMillis();
        }

        for (Bomba bomba : bombas) {
            if (bomba.tiempoExplosion == 0)
                bomba.tiempoExplosion = System.currentTimeMillis();
        }
    }

    public void movimientoAleatorio() {

        Random r = new Random();
        int mov = r.nextInt(2);

        if (mov == 0) {
            girarX();
        }
        else if (mov == 1) {
            girarY();
        }

        long tiempoActual = System.currentTimeMillis();

        if (this.tiempoBomba != 0 && (tiempoActual - tiempoBomba) > 5000) {
            this.tiempoBomba = 0;
            colocarBomba();
        }
        for (Bomba bomba : bombas) {
            if (bomba.tiempoExplosion != 0 && (tiempoActual - bomba.tiempoExplosion) > 4000) {
                bomba.explotar();
                bombas.remove(bomba);
            }
        }
    }

    public void girarX(){
        velocidadX = velocidadX*-1;
    }

    public void girarY(){
        velocidadY = velocidadY*-1;
    }


    public void colocarBomba() {

        Bomba bomba = new Bomba(context, x, y);
        bombas.add(bomba);
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY);

//        for (Bomba bomba : bombas) {
//            bomba.dibujar(canvas);
//        }
    }

}
