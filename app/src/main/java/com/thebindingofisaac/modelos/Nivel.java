package com.thebindingofisaac.modelos;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.thebindingofisaac.GameView;
import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Nivel {
    public static int scrollEjeX = 0;
    public static int scrollEjeY = 0;

    public float orientacionPad = 0;

    public boolean botonDispararPulsado = false;

    private Context context = null;
    private int numeroNivel;
    private Tile[][] mapaTiles;
    public Jugador jugador;
    public boolean inicializado;

    public LinkedList<DisparoJugador> disparosJugador;
    LinkedList<Enemigo> enemigos;


    public Nivel(Context context, int numeroNivel) throws Exception {
        inicializado = false;

        this.context = context;
        this.numeroNivel = numeroNivel;
        inicializar();

        inicializado = true;
    }

    public void inicializar() throws Exception {
        disparosJugador = new LinkedList<DisparoJugador>();
        enemigos = new LinkedList<Enemigo>();


        inicializarMapaTiles();
    }

    private void inicializarMapaTiles() throws Exception {
        InputStream is = context.getAssets().open(numeroNivel + ".txt");
        int anchoLinea;

        List<String> lineas = new LinkedList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        {
            String linea = reader.readLine();
            anchoLinea = linea.length();
            while (linea != null) {
                lineas.add(linea);
                if (linea.length() != anchoLinea) {
                    Log.e("ERROR", "Dimensiones incorrectas en la línea");
                    throw new Exception("Dimensiones incorrectas en la línea.");
                }
                linea = reader.readLine();
            }
        }

        // Inicializar la matriz
        mapaTiles = new Tile[anchoLinea][lineas.size()];
        // Iterar y completar todas las posiciones
        for (int y = 0; y < altoMapaTiles(); ++y) {
            for (int x = 0; x < anchoMapaTiles(); ++x) {
                char tipoDeTile = lineas.get(y).charAt(x);//lines[y][x];
                mapaTiles[x][y] = inicializarTile(tipoDeTile, x, y);
            }
        }
    }

    private Tile inicializarTile(char codigoTile, int x, int y) {
        int xCentroAbajoTileP;
        int yCentroAbajoTileP;
        switch (codigoTile) {
            case 'E':
                // Enemigo
                // Posición centro del tile
                int xCentroAbajoTileE = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTileE = y * Tile.altura + Tile.altura / 2;
                enemigos.add(new Enemigo(context, xCentroAbajoTileE, yCentroAbajoTileE));

                return new Tile(null, Tile.PASABLE);

            case '1':
                // Jugador
                // Posicion centro abajo
                int xCentroAbajoTile = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTile = y * Tile.altura + Tile.altura;
                jugador = new Jugador(context, xCentroAbajoTile, yCentroAbajoTile);

                return new Tile(CargadorGraficos.cargarDrawable(context, R.drawable.platform_dos)
                        , Tile.PASABLE);
            case '.':
                // en blanco, sin textura
                return new Tile(CargadorGraficos.cargarDrawable(context, R.drawable.platform_dos)
                        , Tile.PASABLE);
            case '#':
                // bloque de musgo, no se puede pasar
                return new Tile(
                        CargadorGraficos.cargarDrawable(context, R.drawable.musgo)
                        , Tile.SOLIDO);
            default:
                //cualquier otro caso
                return new Tile(null, Tile.PASABLE);
        }
    }


    public void actualizar(long tiempo) {
        if (inicializado) {
            for (DisparoJugador disparoJugador : disparosJugador) {
                disparoJugador.actualizar(tiempo);
            }

            for (Enemigo enemigo : enemigos) {
                enemigo.actualizar(tiempo);
            }

            if (botonDispararPulsado) {
                disparosJugador.add(new DisparoJugador(context, jugador.x, jugador.y, jugador.orientacion));
                botonDispararPulsado = false;
            }

            jugador.actualizar(tiempo);
            aplicarReglasMovimiento();
        }
    }


    /////// TODO COMPLETAR REGLAS DE MOVIMIENTO DEL JUGADOR
    ///                                                     //////

    private void aplicarReglasMovimiento() {

        int tileXJugadorIzquierda
                = (int) (jugador.x - (jugador.ancho / 2 - 1)) / Tile.ancho;
        int tileXJugadorDerecha
                = (int) (jugador.x + (jugador.ancho / 2 - 1)) / Tile.ancho;

        int tileYJugadorInferior
                = (int) (jugador.y + (jugador.altura / 2 - 1)) / Tile.altura;
        int tileYJugadorCentro
                = (int) jugador.y / Tile.altura;
        int tileYJugadorSuperior
                = (int) (jugador.y - (jugador.altura / 2 - 1)) / Tile.altura;

        for (Iterator<Enemigo> iterator = enemigos.iterator(); iterator.hasNext(); ) {
            Enemigo enemigo = iterator.next();

            if (enemigo.estado == Enemigo.ELIMINAR) {
                iterator.remove();
                continue;
            }

            if (enemigo.estado != Enemigo.ACTIVO)
                continue;

            int tileXEnemigoIzquierda =
                    (int) (enemigo.x - (enemigo.ancho / 2 - 1)) / Tile.ancho;
            int tileXEnemigoDerecha =
                    (int) (enemigo.x + (enemigo.ancho / 2 - 1)) / Tile.ancho;

            int tileYEnemigoInferior =
                    (int) (enemigo.y + (enemigo.altura / 2 - 1)) / Tile.altura;
            int tileYEnemigoCentro =
                    (int) enemigo.y / Tile.altura;
            int tileYEnemigoSuperior =
                    (int) (enemigo.y - (enemigo.altura / 2 - 1)) / Tile.altura;

            int rango = 4;
            if (tileXJugadorIzquierda - rango < tileXEnemigoIzquierda &&
                    tileXJugadorIzquierda + rango > tileXEnemigoIzquierda) {

                if (jugador.colisiona(enemigo)) {
                    if (jugador.golpeado() <= 0) {
                        jugador.restablecerPosicionInicial();
                        scrollEjeX = 0;
                        return;
                    }
                }
            }



        }


    }

    public void dibujar (Canvas canvas) {
        if(inicializado) {

            dibujarTiles(canvas);
            for(DisparoJugador disparoJugador: disparosJugador){
                disparoJugador.dibujar(canvas);
            }

            jugador.dibujar(canvas);

            for(Enemigo enemigo: enemigos){
                enemigo.dibujar(canvas);
            }

        }
    }



     //////  TODO SCROLL Y ////

    private void dibujarTiles(Canvas canvas) {

        Log.println(Log.INFO, "POS_JUGADOR", "X: " + jugador.x + "Y: " + jugador.y);
        //calculamos los tiles en los que se encuentra el jugador
        int tileXJugador = (int) jugador.x / Tile.ancho;
        int tileYJugador = (int) jugador.y / Tile.altura;



        int izquierda = (int) (tileXJugador - tilesEnDistanciaX(jugador.x - scrollEjeX));
        izquierda = Math.max(0, izquierda); // Que nunca sea < 0, ej -1

        if (jugador.x < anchoMapaTiles() * Tile.ancho - GameView.pantallaAncho * 0.3)
            if (jugador.x - scrollEjeX > GameView.pantallaAncho * 0.7) {
                scrollEjeX = (int) ((jugador.x) - GameView.pantallaAncho * 0.7);
            }
        if (jugador.x > GameView.pantallaAncho * 0.3)
            if (jugador.x - scrollEjeX < GameView.pantallaAncho * 0.3) {
                scrollEjeX = (int) (jugador.x - GameView.pantallaAncho * 0.3);
            }


        if((jugador.y < GameView.pantallaAlto * 0.45)){
            scrollEjeY = (int) ((GameView.pantallaAlto-285) - ( GameView.pantallaAlto * 0.45 - jugador.y));
        }else
            scrollEjeY = (int) (GameView.pantallaAlto-285);


        int derecha = izquierda +
                GameView.pantallaAncho / Tile.ancho + 1;

        // el ultimo tile visible, no puede superar el tamaño del mapa
        derecha = Math.min(derecha, anchoMapaTiles() - 1);

        for (int y = 0; y < altoMapaTiles() ; ++y) {
            for (int x = izquierda; x <= derecha; ++x) {
                if (mapaTiles[x][y].imagen != null) {
                    // Calcular la posición en pantalla correspondiente
                    // izquierda, arriba, derecha , abajo
                    mapaTiles[x][y].imagen.setBounds(
                            (x  * Tile.ancho) - scrollEjeX,
                            (y * Tile.altura) - scrollEjeY,
                            (x * Tile.ancho) + Tile.ancho - scrollEjeX,
                            (y * Tile.altura )+ Tile.altura - scrollEjeY);
                    mapaTiles[x][y].imagen.draw(canvas);
                }
            }
        }
    }

    private float tilesEnDistanciaX(double distanciaX){
        return (float) distanciaX/Tile.ancho;
    }

    private float tilesEnDistanciaY(double distanciaY){
        return (float) distanciaY/Tile.altura;
    }


    public int anchoMapaTiles(){
        return mapaTiles.length;
    }

    public int altoMapaTiles(){
        return mapaTiles[0].length;
    }


}

