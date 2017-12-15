package com.thebindingofisaac.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;

/**
 * Created by Darío on 11/12/2017.
 */

public class BloqueDestructible extends Modelo {


    boolean destruido;
    int xTile;
    int yTile;

    public BloqueDestructible(Context context, double x, double y){
        super(context, x, y, 35, 35);


        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.medievaltile_208);
    }

    @Override
    public void dibujar(Canvas canvas) {
        if(!destruido) {
            int yArriva = (int) y - altura / 2 - Nivel.scrollEjeY;
            int xIzquierda = (int) x - ancho / 2 - Nivel.scrollEjeX;

            imagen.setBounds(xIzquierda, yArriva, xIzquierda
                    + ancho, yArriva + altura);
            imagen.draw(canvas);
        }
    }

    public void destruir(){
        destruido = true;
    }


}
