package com.thebindingofisaac.modelos.HUD;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;
import com.thebindingofisaac.modelos.Modelo;


public class Texto extends Modelo {

    String texto="";
    float msTiempo=0;
    boolean activo=false;

    public Texto(Context context, double x, double y) {
        super(context, x, y, 20,20);
    }

    public void mostrar(String texto){
        this.texto=texto;
        msTiempo = 5000;
        activo =true;
    }

    public void actualizar (long tiempo) {
        if(msTiempo >0){
            msTiempo-=tiempo;
            if(msTiempo<=0) activo=false;
        }
    }

    @Override
    public void dibujar(Canvas canvas){
        if(activo) {
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setAntiAlias(true);
            if(msTiempo<1000) paint.setAlpha((int) ((msTiempo)*0.1));
            paint.setFakeBoldText(true);
            paint.setTextSize(13);
            canvas.drawText(texto, (int) x, (int) y, paint);
        }
    }


}
