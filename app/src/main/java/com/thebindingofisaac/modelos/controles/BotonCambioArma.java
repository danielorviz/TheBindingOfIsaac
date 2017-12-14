package com.thebindingofisaac.modelos.controles;

import android.content.Context;

import com.thebindingofisaac.GameView;
import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;
import com.thebindingofisaac.modelos.Modelo;


public class BotonCambioArma extends Modelo {

    public BotonCambioArma(Context context) {
        super(context, GameView.pantallaAncho*0.73 , GameView.pantallaAlto*0.8,
                40 ,40);

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.cambiararma_psv2);
    }

    public boolean estaPulsado(float clickX, float clickY) {
        boolean estaPulsado = false;

        if (clickX <= (x + ancho / 2) && clickX >= (x - ancho / 2)
                && clickY <= (y + altura / 2) && clickY >= (y - altura / 2)) {
            estaPulsado = true;
        }
        return estaPulsado;
    }
}

