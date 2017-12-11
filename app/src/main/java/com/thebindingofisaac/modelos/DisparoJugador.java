package com.thebindingofisaac.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;
import com.thebindingofisaac.global.TipoArmas;
import com.thebindingofisaac.graficos.Sprite;


public class DisparoJugador extends Modelo {

    private Sprite sprite;
    public double velocidadX = 5;
    public double velocidadY = 5;

    public float tVidaMaximo;
    public float tVida;
    private float tiempoInicial=0;

    public String tipoArma;

    public int rebotes = 0;
    public int maxRebotes = 2;
    public boolean rebotando;

    public DisparoJugador(Context context, double xInicial, double yInicial, int orientacion, String tipoArma) {
        super(context, xInicial, yInicial, 35, 35);

        if (orientacion == Jugador.IZQUIERDA)
            velocidadX = velocidadX*-1;

        cDerecha = 6;
        cIzquierda = 6;
        cArriba = 6;
        cAbajo = 6;

        this.tipoArma=tipoArma;


        inicializar();
    }

    public void inicializar (){
        sprite= new Sprite(
                CargadorGraficos.cargarDrawable(context,
                        R.drawable.animacion_disparo1),
                ancho, altura,
                25, 5, true);

        if(tipoArma== TipoArmas.ARMA_MELEE){
            tVidaMaximo = 2f;
        }else
            tVidaMaximo= 10f;
    }

    public void rebotar(){
        if(velocidadY>0) y = y+0.01;
        velocidadY = -velocidadY;
        rebotes++;
        rebotando = true;
    }
    public void actualizar (long tiempo) {

        sprite.actualizar(tiempo);
        if(tiempoInicial>0) tiempoInicial= tiempo;
        tVida = tiempo-tiempoInicial;
    }

    public void dibujar(Canvas canvas){
        if(tipoArma != TipoArmas.ARMA_MELEE)
            sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y - Nivel.scrollEjeY);
    }
}

