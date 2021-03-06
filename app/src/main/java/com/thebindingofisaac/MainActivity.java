package com.thebindingofisaac;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.thebindingofisaac.gestores.GestorAudio;
import com.thebindingofisaac.modelos.Nivel;

public class MainActivity extends Activity {
    GameView gameView = null;



    public MainActivity(){}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);


        gameView = new GameView(this);
        setContentView(gameView);
        gameView.requestFocus();

        gameView.numeroNivel=0;

    }


    @Override
    protected void onPause() {
        if (GestorAudio.getInstancia() != null){
            GestorAudio.getInstancia().pararMusicaAmbiente();
        }
        super.onPause();
    }
    @Override
    public void onBackPressed() {
        finish();
        System.gc();

        synchronized(gameView.gameloop)
        {
            gameView.context = null;
            gameView.gameloop.setRunning(false);
            gameView = null;
        }
    }
    @Override
    protected void onResume() {
        if (GestorAudio.getInstancia() != null){
            GestorAudio.getInstancia().reproducirMusicaAmbiente();
        }
        super.onResume();
    }
}