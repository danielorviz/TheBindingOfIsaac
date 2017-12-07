package com.thebindingofisaac.modelos.HUD;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.thebindingofisaac.GameView;
import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;
import com.thebindingofisaac.modelos.Modelo;


/**
 * Created by ivan on 15/10/2017.
 */

public class ContadorVidas extends Modelo {
    private int vidas;

    Drawable imagenCorazonCompleto;
    Drawable imagenCorazonMitad;
    Drawable imagenCorazonVacio;

    Drawable imagen1;
    Drawable imagen2;
    Drawable imagen3;


    public static final String CORAZON_COMPLETO = "corazon_completo";
    public static final String CORAZON_MITAD = "corazon_mitad";
    public static final String CORAZON_VACIO = "CORAZON_VACIO";
    public ContadorVidas (Context context){
        super(context, GameView.pantallaAncho*0.15 , GameView.pantallaAlto*0.15 ,
        25, 45+45+45);

        imagenCorazonCompleto = CargadorGraficos.cargarDrawable(context, R.drawable.hudheart_full);
        imagenCorazonVacio = CargadorGraficos.cargarDrawable(context, R.drawable.hudheart_empty);
        imagenCorazonMitad = CargadorGraficos.cargarDrawable(context, R.drawable.hudheart_half);
    }

    @Override
    public void dibujar(Canvas canvas){

        int yArriba = (int)  y - altura / 2;
        int xIzquierda = (int) x - ancho / 2;

        /// TODO COMPLETAR EL RESTO DE ESTADOS
        if(vidas/2 == 3){
            imagen1 = imagen2 = imagen3 = imagenCorazonCompleto;
        }


        imagen1.setBounds(xIzquierda, yArriba, xIzquierda
                + ancho, yArriba + altura);
        imagen2.setBounds(xIzquierda+45, yArriba, xIzquierda+45
                + ancho, yArriba + altura);
        imagen2.setBounds(xIzquierda+90, yArriba, xIzquierda+90
                + ancho, yArriba + altura);

        imagen1.draw(canvas);
        imagen2.draw(canvas);
        imagen3.draw(canvas);
    }


   public void actualizarVida(int vida){
       this.vidas=vida;
   }
   public int getVidas(){
       return vidas;
   }


}
