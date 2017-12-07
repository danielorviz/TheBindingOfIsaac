package com.thebindingofisaac.modelos.controles;

import android.content.Context;
import android.util.Log;

import com.thebindingofisaac.GameView;
import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;
import com.thebindingofisaac.modelos.Modelo;

/**
 * Created by jordansoy on 09/10/2017.
 */

public class Pad extends Modelo {

    public Pad(Context context) {
        super(context, GameView.pantallaAncho*0.15 , GameView.pantallaAlto*0.8 ,
                GameView.pantallaAlto, GameView.pantallaAncho);

        altura = 100;
        ancho = 100;
        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.pad);
    }

    public boolean estaPulsado(float clickX, float clickY) {
        boolean estaPulsado = false;

        if (clickX <= (x + ancho / 2) && clickX >= (x - ancho / 2)
                && clickY <= (y + altura / 2) && clickY >= (y - altura / 2)
                ) {
            estaPulsado = true;
        }
        return estaPulsado;
    }


    /// TODO La orientacion aun no funciona bien.  ///

    public int getOrientacion( float clickX, float clickY) {

        Log.println(Log.INFO, "EVENTOS_TOUCH", "getOrientacon ClickX = " + clickX + " ClickY = " + clickY);

        double valorCLickY = Math.abs((clickY - y ));
        double valorClickX = Math.abs(clickX - x);

        if(valorClickX >  valorCLickY) {
            /// SI x -click > 0 -> Izquierda  ///
            if(x - clickX >0 ) return 1;
            else return -1;
        }else{
            /// SI y -click > 0 -> ARRIBA  ///
            if((y - clickY) > 0)
                return 2;
            else return -2;
        }
    }

    public int getOrientacionX(float clickX){
        return (int) (clickX - x);
    }
     public int getOrientacionY(float clickY){
            return (int) (clickY - y);
}       }

