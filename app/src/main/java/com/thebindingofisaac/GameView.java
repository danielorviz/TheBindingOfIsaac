package com.thebindingofisaac;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.thebindingofisaac.gestores.GestorAudio;
import com.thebindingofisaac.modelos.HUD.IconoVida;
import com.thebindingofisaac.modelos.HUD.Inventario;
import com.thebindingofisaac.modelos.HUD.Texto;
import com.thebindingofisaac.modelos.Nivel;
import com.thebindingofisaac.modelos.controles.BotonDisparar;
import com.thebindingofisaac.modelos.controles.BotonEscudo;
import com.thebindingofisaac.modelos.controles.Pad;


public class GameView extends SurfaceView implements SurfaceHolder.Callback  {


    boolean iniciado = false;
    Context context;
    GameLoop gameloop;

    private Pad pad;
    private BotonDisparar botonDisparar;
    private BotonEscudo botonEscudo;

    private IconoVida[] contadorVidas;
    private Inventario inventario;
    private Texto textoInfo;

    public static int pantallaAncho;
    public static int pantallaAlto;
    public static final int NUM_NIVELES=3;

    private GestorAudio gestorAudio;

    private Nivel nivel;
    public int numeroNivel = 0;

    public GameView(Context context) {
        super(context);
        iniciado = true;

        getHolder().addCallback(this);
        setFocusable(true);


        inicializarGestorAudio(context);

        this.context = context;
        gameloop = new GameLoop(this);
        gameloop.setRunning(true);


    }


    private void inicializarGestorAudio(Context context) {
      // gestorAudio = GestorAudio.getInstancia(context,R.raw.m_fondo )
        // gestorAudio.reproducirMusicaAmbiente();/
     //   gestorAudio.registrarSonido(GestorAudio.SONIDO_ZOMBIE, R.raw.zombie);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // valor a Binario
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        // Indice del puntero
        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

        int pointerId  = event.getPointerId(pointerIndex);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                accion[pointerId] = ACTION_DOWN;
                x[pointerId] = event.getX(pointerIndex);
                y[pointerId] = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                accion[pointerId] = ACTION_UP;
                x[pointerId] = event.getX(pointerIndex);
                y[pointerId] = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                for(int i =0; i < pointerCount; i++){
                    pointerIndex = i;
                    pointerId  = event.getPointerId(pointerIndex);
                    accion[pointerId] = ACTION_MOVE;
                    x[pointerId] = event.getX(pointerIndex);
                    y[pointerId] = event.getY(pointerIndex);
                }
                break;
        }

        procesarEventosTouch();
        return true;
    }

    int NO_ACTION = 0;
    int ACTION_MOVE = 1;
    int ACTION_UP = 2;
    int ACTION_DOWN = 3;
    int accion[] = new int[6];
    float x[] = new float[6];
    float y[] = new float[6];

    public void procesarEventosTouch(){
        boolean pulsacionPadMover = false;

        for(int i=0; i < 6; i++){
            if(accion[i] != NO_ACTION ) {

                if(accion[i] == ACTION_DOWN){
                    if(nivel.nivelPausado)
                        nivel.nivelPausado = false;
                }

                if(botonEscudo.estaPulsado(x[i],y[i])){
                    if(accion[i]== ACTION_DOWN ){
                        nivel.botonEscudoPulsado=true;
                    }
                }

                if (botonDisparar.estaPulsado(x[i], y[i])) {
                    if (accion[i] == ACTION_DOWN) {
                        nivel.botonDispararPulsado = true;
                    }
                }

                if (pad.estaPulsado(x[i], y[i])) {
                    float orientacion = pad.getOrientacion(x[i],y[i]);
                    Log.i("orient", ""+orientacion);
                    if (accion[i] != ACTION_UP) {
                        pulsacionPadMover = true;
                        nivel.orientacionPad = orientacion;
                    }
                }
                Log.println(Log.INFO, "EVENTOS_TOUCH", "pulsacion PAD: Orientacion = " +  nivel.orientacionPad );
            }
        }
        if(!pulsacionPadMover) {
            nivel.orientacionPad = 0;
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v("Tecla","Tecla pulsada: "+keyCode);

        if( keyCode == 62) {
            nivel.botonDispararPulsado = true;
        }

        if( keyCode == 32) {
            nivel.orientacionPad = -0.5f;
        }
        if( keyCode == 29) {
            nivel.orientacionPad = 0.5f;
        }
        if( keyCode == 47) {
            nivel.orientacionPad = 0;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp (int keyCode, KeyEvent event) {
        if( keyCode == 32 || keyCode == 29) {
            nivel.orientacionPad = 0;
        }
        return super.onKeyDown(keyCode, event);
    }



    protected void inicializar() throws Exception {
        nivel = new Nivel(context,numeroNivel);
        pad = new Pad(context);
        botonDisparar = new BotonDisparar(context);
        botonEscudo = new BotonEscudo(context);
        contadorVidas = new IconoVida[3];
        contadorVidas[0] = new IconoVida(context, GameView.pantallaAncho*0.05,
                GameView.pantallaAlto*0.1);
        contadorVidas[1] = new IconoVida(context, GameView.pantallaAncho*0.15,
                GameView.pantallaAlto*0.1);
        contadorVidas[2] = new IconoVida(context, GameView.pantallaAncho*0.25,
                GameView.pantallaAlto*0.1);

        for(int i = 0; contadorVidas.length>i; i++){
            contadorVidas[i].inicializar();
        }
        inventario = new Inventario(context, GameView.pantallaAncho*0.85,
                GameView.pantallaAlto*0.05);

        textoInfo = new Texto(context, GameView.pantallaAncho*0.03,
                GameView.pantallaAlto*0.23);
    }
    public void actualizar(long tiempo) throws Exception {
        if (nivel.nivelPerdido && !nivel.nivelPausado) {
            nivel.inicializar();
        }
        if (nivel.nivelFinalizado && !nivel.nivelPausado) {
            if (numeroNivel < NUM_NIVELES) {
                numeroNivel++;
            }
            inicializar();
        }
        if (!nivel.nivelPausado) {
            nivel.actualizar(tiempo);
            inventario.setNumeroEscudos(nivel.jugador.getEscudos());
            int vidas = nivel.jugador.getVidas();
            if (vidas == 5) {
                contadorVidas[2].estado = IconoVida.MITAD;
            } else if (vidas == 4) {
                contadorVidas[2].estado = IconoVida.VACIA;
            } else if (vidas == 3) {
                contadorVidas[2].estado = IconoVida.VACIA;
                contadorVidas[1].estado = IconoVida.MITAD;
            } else if (vidas == 2) {
                contadorVidas[2].estado = IconoVida.VACIA;
                contadorVidas[1].estado = IconoVida.VACIA;
            } else if (vidas == 1) {
                contadorVidas[2].estado = IconoVida.VACIA;
                contadorVidas[1].estado = IconoVida.VACIA;
                contadorVidas[0].estado = IconoVida.MITAD;
            } else if (vidas == 0) {
                contadorVidas[2].estado = IconoVida.VACIA;
                contadorVidas[1].estado = IconoVida.VACIA;
                contadorVidas[0].estado = IconoVida.VACIA;
            }
            for (int i = 0; i < contadorVidas.length; i++) {
                contadorVidas[i].actualizar(tiempo);
                if (vidas == 6) contadorVidas[i].estado = IconoVida.COMPLETA;
            }

            if(nivel.textoPorMostrar){
                textoInfo.mostrar(nivel.texto);
                nivel.textoPorMostrar=false;
            }
            textoInfo.actualizar(tiempo);
        }

    }

    protected void dibujar(Canvas canvas) {
        nivel.dibujar(canvas);
        pad.dibujar(canvas);
        botonDisparar.dibujar(canvas);
        botonEscudo.dibujar(canvas);
        inventario.dibujar(canvas);
        textoInfo.dibujar(canvas);
        for (int i = 0; i < contadorVidas.length; i++) {
            contadorVidas[i].dibujar(canvas);
        }

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        pantallaAncho = width;
        pantallaAlto = height;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (iniciado) {
            iniciado = false;
            if (gameloop.isAlive()) {
                iniciado = true;
                gameloop = new GameLoop(this);
            }

            gameloop.setRunning(true);
            gameloop.start();
        } else {
            iniciado = true;
            gameloop = new GameLoop(this);
            gameloop.setRunning(true);
            gameloop.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        iniciado = false;

        boolean intentarDeNuevo = true;
        gameloop.setRunning(false);
        while (intentarDeNuevo) {
            try {
                gameloop.join();
                intentarDeNuevo = false;
            }
            catch (InterruptedException e) {
            }
        }
    }

}

