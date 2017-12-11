package com.thebindingofisaac.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;
import com.thebindingofisaac.graficos.Sprite;

import java.util.HashMap;

/**
 * Created by Darío on 11/12/2017.
 */

public class Cofre extends Modelo {

    public int estado = ACTIVO;
    public static final int ACTIVO = 1;
    public static final int INACTIVO = 0;
    public static final int ELIMINAR = -1;

    public static final int COFRE_MEJORA_DISPARO = 1;
    public static final int COFRE_VIDA = 2;
    public int tipoCofre;

    private Sprite sprite;
    public final String COFRE_PARADO = "Parado";
    public final String COFRE_COGIDO = "Cogido";
    private HashMap<String,Sprite> sprites = new HashMap<String,Sprite> ();

    public Cofre(Context context, double x, double y, int tipoCofre){
        super(context, x, y, 50, 50);
        this.y = y - altura/2;
        this.tipoCofre = tipoCofre;

        inicializar();
    }

    private void inicializar(){

        Sprite spriteParado = new Sprite(CargadorGraficos.cargarDrawable(context, R.drawable.animacion_cofre), ancho, altura, 4, 10, true);
        Sprite spriteCogido = new Sprite(CargadorGraficos.cargarDrawable(context, R.drawable.boxcrate_double), ancho, altura, 4, 1, true);
        sprites.put(COFRE_COGIDO, spriteCogido);
        sprites.put(COFRE_PARADO, spriteParado);

        //Animacion actual
        sprite = spriteParado;
    }

    public void actualizar(long tiempo){

        boolean finSprite = sprite.actualizar(tiempo);

        if (estado == INACTIVO && finSprite == true){
            estado = ELIMINAR;
        }
        if (estado == INACTIVO){
            sprite = sprites.get(COFRE_COGIDO);
        }
    }

    public void dibujar(Canvas canvas){
        sprite.dibujarSprite(canvas, (int) x - Nivel.scrollEjeX, (int) y- Nivel.scrollEjeY, false);
    }

    public void destruir (){
        estado = INACTIVO;
    }

}
