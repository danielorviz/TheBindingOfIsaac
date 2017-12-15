package com.thebindingofisaac.modelos.enemigos;

import android.content.Context;
import android.graphics.Canvas;

import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;
import com.thebindingofisaac.modelos.Modelo;
import com.thebindingofisaac.modelos.Nivel;

/**
 * Created by Dario on 13/12/2017.
 */

public class Bomba extends Modelo {

    public int estado = ACTIVO;
    public static final int ACTIVO = 1;
    public static final int EXPLOTADA = -1;

    public int radioExplosion = 50;

    public Bomba(Context context, double x, double y) {
        super(context, x, y, 30,30);
        this.y =  y - altura/2;

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.bomb);
    }

    public void dibujar(Canvas canvas){
        int yArriva = (int)  y - altura / 2 - Nivel.scrollEjeY;
        int xIzquierda = (int) x - ancho / 2 - Nivel.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriva, xIzquierda + ancho, yArriva + altura);
        imagen.draw(canvas);
    }


    public void explotar() {
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.hole_dirt);
        estado = EXPLOTADA;
    }
}
