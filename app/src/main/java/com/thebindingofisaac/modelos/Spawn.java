package com.thebindingofisaac.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;


/**
 * Created by Dani on 11/11/2017.
 */

public class Spawn extends Modelo {

    private int id;
    private boolean activa;

    public Spawn(Context context, double x, double y, int id) {
        super(context, x, y, 40, 25);
        this.y =  y - altura/2;
        this.id=id;
        this.activa=false;
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.puerta);
    }

    @Override
    public void dibujar(Canvas canvas) {
        int yArriva = (int)  y - altura / 2 - Nivel.scrollEjeY;
        int xIzquierda = (int) x - ancho / 2 - Nivel.scrollEjeX;

        imagen.setBounds(xIzquierda, yArriva, xIzquierda
                + ancho, yArriva + altura);
        imagen.draw(canvas);
    }

    public int getId() {
        return id;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
