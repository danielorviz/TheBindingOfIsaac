package com.thebindingofisaac.modelos.controles;

import android.content.Context;

import com.thebindingofisaac.GameView;
import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;
import com.thebindingofisaac.modelos.Modelo;


public class BotonEscudo extends Modelo {

    public BotonEscudo(Context context) {
        super(context, GameView.pantallaAncho*0.85 , GameView.pantallaAlto*0.60,
                40 ,40);

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.shield48);
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

