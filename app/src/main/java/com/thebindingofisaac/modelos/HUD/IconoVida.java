package com.thebindingofisaac.modelos.HUD;

import android.content.Context;
import android.graphics.Canvas;

import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;
import com.thebindingofisaac.graficos.Sprite;
import com.thebindingofisaac.modelos.Modelo;
import com.thebindingofisaac.modelos.Nivel;

import java.util.HashMap;


/**
 * Created by jordansoy on 10/10/2017.
 */

public class IconoVida extends Modelo {

    public static final int VACIA = 0;
    public static final int MITAD = 1;
    public static final int COMPLETA = 2;
    private HashMap<Integer,Sprite> sprites = new HashMap<Integer,Sprite> ();
    private Sprite sprite;

    public int estado;

    public IconoVida(Context context, double x, double y) {
        super(context, x, y, 40,40);
        estado=IconoVida.COMPLETA;

    }


    public void inicializar () {
        Sprite lleno = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.hudheart_full),
                ancho, altura,
                1, 1, true);
        sprites.put(COMPLETA, lleno);
        Sprite mitad = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.hudheart_half),
                ancho, altura,
                1, 1, true);
        sprites.put(MITAD, mitad);
        Sprite vacio = new Sprite(
                CargadorGraficos.cargarDrawable(context, R.drawable.hudheart_empty),
                ancho, altura,
                1, 1, true);
        sprites.put(VACIA, vacio);

        sprite = lleno;
    }
    public void actualizar (long tiempo) {

        if(estado == IconoVida.COMPLETA)
           sprite = sprites.get(COMPLETA);
        if(estado == IconoVida.MITAD)
            sprite=sprites.get(MITAD);
        if(estado == IconoVida.VACIA)
            sprite = sprites.get(VACIA);
    }
    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas,(int)x, (int)y);
    }

}

