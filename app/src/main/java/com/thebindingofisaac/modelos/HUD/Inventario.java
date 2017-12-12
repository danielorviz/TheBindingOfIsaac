package com.thebindingofisaac.modelos.HUD;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;
import com.thebindingofisaac.modelos.Modelo;


public class Inventario extends Modelo {
    private int numeroEscudos;
    private int numeroMunicion;

    Drawable imagenEscudo;


    public Inventario(Context context, double x, double y) {
        super(context, x, y, 20,20);

        imagenEscudo = CargadorGraficos.cargarDrawable(context, R.drawable.shield32);

    }

    @Override
    public void dibujar(Canvas canvas){

        int yArriba = (int)  y - altura / 2;
        int xIzquierda = (int) x - ancho / 2;
        imagenEscudo.setBounds(xIzquierda, yArriba, xIzquierda
                + ancho, yArriba + altura);
        imagenEscudo.draw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setTextSize(15);
        canvas.drawText(String.valueOf(numeroEscudos), (int)x+(ancho), (int)y+5, paint);
    }

    public void setNumeroEscudos(int n){
        numeroEscudos=n;
    }

}
