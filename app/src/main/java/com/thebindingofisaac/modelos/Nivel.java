package com.thebindingofisaac.modelos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.thebindingofisaac.GameView;
import com.thebindingofisaac.R;
import com.thebindingofisaac.gestores.CargadorGraficos;
import com.thebindingofisaac.gestores.ParserXML;
import com.thebindingofisaac.gestores.Utilidades;
import com.thebindingofisaac.global.TipoArmas;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Nivel {
    public static int scrollEjeX = 0;
    public static int scrollEjeY = 0;

    Document doc;

    public float orientacionPad = 0;

    public boolean botonDispararPulsado = false;
    public boolean botonEscudoPulsado = false;

    private Context context = null;
    private int numeroNivel;
    private Tile[][] mapaTiles;
    public Jugador jugador;
    public boolean inicializado;
    private Puerta puerta;



    public LinkedList<DisparoJugador> disparosJugador;
    public LinkedList<Enemigo> enemigos;
    public List<Cofre> cofres;
    public List<Enemigo> enemigosPorSpawnear;

    private Fondo fondo;

    public boolean nivelFinalizado;
    public Bitmap mensaje;
    public boolean nivelPausado;
    public boolean nivelPerdido;

    public Nivel(Context context, int numeroNivel) throws Exception {

        inicializado = false;

        this.context = context;
        this.numeroNivel = numeroNivel;
        inicializar();

        inicializado = true;
    }

    public void inicializar() throws Exception {

        nivelPerdido=false;
        nivelFinalizado=false;
        nivelPausado=false;
        disparosJugador = new LinkedList<DisparoJugador>();
        enemigos = new LinkedList<Enemigo>();
        cofres = new LinkedList<Cofre>();

        fondo = new Fondo(context,CargadorGraficos.cargarBitmap(context,
                R.drawable.fondo_gris), 0);


        inicializarMapaTiles();
        cargarEnemigosXML(context);
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

    public void cargarEnemigosXML(Context context) {
        ParserXML parser = new ParserXML();

        if (doc == null) {
            String textoFicheroNivel = "";
            try {
                InputStream inputStream =  context.getAssets().open("e"+numeroNivel + ".xml");
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String linea = bufferedReader.readLine();
                while (linea != null) {
                    textoFicheroNivel += linea;
                    linea = bufferedReader.readLine();
                }
                bufferedReader.close();
            } catch (Exception ex) {
            }

            doc = parser.getDom(textoFicheroNivel);
        }
        enemigosPorSpawnear = new LinkedList<Enemigo>();
        NodeList nodos = doc.getElementsByTagName("enemy");
        for (int i = 0; i < nodos.getLength(); i++) {
            Element elementoActual = (Element) nodos.item(i);
            String tipo = parser.getValor(elementoActual, "type");
            if (tipo.equals("Z"))
                enemigosPorSpawnear.add(new Enemigo(context, 0, 0));
       }
    }

    private Tile inicializarTile(char codigoTile, int x, int y) {
        int xCentroAbajoTileP;
        int yCentroAbajoTileP;
        Random random = new Random();
        int n = random.nextInt(2);
        switch (codigoTile) {

            case 'E':
                // Enemigo
                // Posición centro del tile
                int xCentroAbajoTileE = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTileE = y * Tile.altura + Tile.altura / 2;
                enemigos.add(new Enemigo(context, xCentroAbajoTileE, yCentroAbajoTileE));

                if(n==1)
                    return new Tile(CargadorGraficos.cargarDrawable(context, R.drawable.medievaltile_115), Tile.PASABLE);
                else
                    return new Tile(CargadorGraficos.cargarDrawable(context, R.drawable.medievaltile_002), Tile.PASABLE);

            case '4':
                int xCentroAbajoTile4 = x * Tile.ancho + Tile.ancho/2;
                int yCentroAbajoTile4 = y * Tile.altura + Tile.altura;
                puerta=new Puerta(context,xCentroAbajoTile4,yCentroAbajoTile4,4);

                if(n==1)
                    return new Tile(CargadorGraficos.cargarDrawable(context, R.drawable.medievaltile_115)
                            , Tile.PASABLE);
                else
                    return new Tile(CargadorGraficos.cargarDrawable(context, R.drawable.medievaltile_002)
                            , Tile.PASABLE);
            case 'C':
                // Cofre
                int xCentroAbajoTileC = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTileC = y * Tile.altura + Tile.altura;
                Random r = new Random();
                int tipo = (r.nextInt(3));
                if (tipo == 0)
                    cofres.add(new Cofre(context, xCentroAbajoTileC, yCentroAbajoTileC, Cofre.COFRE_MEJORA_DISPARO));
                else if (tipo == 1)
                    cofres.add(new Cofre(context, xCentroAbajoTileC, yCentroAbajoTileC, Cofre.COFRE_VIDA));
                else if(tipo == 2)
                    cofres.add(new Cofre(context, xCentroAbajoTileC, yCentroAbajoTileC, Cofre.COFRE_ESCUDO));

                return new Tile(null, Tile.PASABLE);

            case '1':
                // Jugador
                // Posicion centro abajo
                int xCentroAbajoTile = x * Tile.ancho + Tile.ancho / 2;
                int yCentroAbajoTile = y * Tile.altura + Tile.altura;
                jugador = new Jugador(context, xCentroAbajoTile, yCentroAbajoTile);

                return new Tile(CargadorGraficos.cargarDrawable(context, R.drawable.medievaltile_263)
                        , Tile.PASABLE);
            case '.':
                // en blanco, sin textura

                if(n==1)
                    return new Tile(CargadorGraficos.cargarDrawable(context, R.drawable.medievaltile_115)
                            , Tile.PASABLE);
                else
                    return new Tile(CargadorGraficos.cargarDrawable(context, R.drawable.medievaltile_002)
                            , Tile.PASABLE);
            case '$':
                return new Tile(CargadorGraficos.cargarDrawable(context, R.drawable.medievaltile_112)
                        , Tile.SOLIDO);
            case '#':
                // bloque de musgo, no se puede pasar
                return new Tile(
                        CargadorGraficos.cargarDrawable(context, R.drawable.medievaltile_019)
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
            jugador.procesarOrdenes(orientacionPad,botonDispararPulsado);

            for (Enemigo enemigo : enemigos) {
                enemigo.actualizar(tiempo);
            }

            for (Cofre cofre : cofres) {
                cofre.actualizar(tiempo);
            }

            if(botonEscudoPulsado){
                jugador.escudar();
                Log.i("JUGADOR", "----> Escudado");
                botonEscudoPulsado=false;
            }
            if (botonDispararPulsado) {
                DisparoJugador ataque = new DisparoJugador(context, jugador.x, jugador.y, jugador.orientacion,jugador.armaActual);
                if(jugador.orientacion == jugador.ARRIBA)
                    ataque.y -=35;
                else if(jugador.orientacion == jugador.ABAJO)
                    ataque.y +=35;
                else if(jugador.orientacion == jugador.DERECHA)
                    ataque.x+=35;
                else if(jugador.orientacion == jugador.IZQUIERDA)
                    ataque.x-=35;

                disparosJugador.add(ataque);

                botonDispararPulsado = false;
            }

            jugador.actualizar(tiempo);

            aplicarReglasMovimiento();
        }
    }


    /////// TODO COMPLETAR REGLAS DE MOVIMIENTO DEL JUGADOR
    ///                                                     //////

    private void aplicarReglasMovimiento() {
        if(jugador.colisiona(puerta) && puerta.isActiva()){
            Log.i("puerta","colisionando");
            if(!nivelFinalizado){
                mensaje=CargadorGraficos.cargarBitmap(context,R.drawable.you_win);
                nivelPausado=true;
                nivelFinalizado=true;
            }
        }

        int tileXJugadorIzquierda
                = (int) (jugador.x - (jugador.ancho / 2 - 1)) / Tile.ancho;
        int tileXJugadorDerecha
                = (int) (jugador.x + (jugador.ancho / 2 - 1)) / Tile.ancho;
        int tileXJugador = (int) (jugador.x - (jugador.ancho / 2)) / Tile.ancho;
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
            int tileXEnemigoCentro = (int) enemigo.x /Tile.ancho;
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
                    if(!jugador.escudado) {
                        if (jugador.golpeado() <= 0) {

                            nivelPausado = true;
                            nivelPerdido = true;

                            mensaje = CargadorGraficos.cargarBitmap(context, R.drawable.you_lose);
                            jugador.restablecerPosicionInicial();

                            scrollEjeX = 0;
                            return;
                        }
                    }
                }
            }

            if(enemigo.velocidadX > 0){
                //  Solo una condicion para pasar:  Tile delante libre, el de abajo solido
                if (tileXEnemigoDerecha + 1 <= anchoMapaTiles() - 1 &&
                        mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoInferior].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoCentro].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoSuperior].tipoDeColision ==
                                Tile.PASABLE
                       // && mapaTiles[tileXEnemigoDerecha + 1][tileYEnemigoInferior + 1].tipoDeColision == Tile.PASABLE
                    ) {

                    //enemigo.moverseHaciaJugador(jugador.x,jugador.y);
                    enemigo.x += enemigo.velocidadX;


                    // Sino, me acerco al borde del que estoy
                } else if (tileXEnemigoDerecha + 1 <= anchoMapaTiles() - 1 ) {

                    int TileEnemigoDerecho = tileXEnemigoDerecha*Tile.ancho + Tile.ancho ;
                    double distanciaX = TileEnemigoDerecho - (enemigo.x +  enemigo.ancho/2);

                    if( distanciaX  > 0) {
                        double velocidadNecesaria = Math.min(distanciaX, enemigo.velocidadX);
                        enemigo.x += velocidadNecesaria;
                    } else {
                        enemigo.girarX();
                    }

                    // No hay Tile, o es el final del mapa
                } else {
                    enemigo.girarX();
                }
            }

            if(enemigo.velocidadX < 0){
                // Solo una condición para pasar: Tile izquierda pasable y suelo solido.
                if (tileXEnemigoIzquierda - 1 >= 0 &&
                        mapaTiles[tileXEnemigoIzquierda-1][tileYEnemigoInferior].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoIzquierda-1][tileYEnemigoCentro].tipoDeColision ==
                                Tile.PASABLE &&
                        mapaTiles[tileXEnemigoIzquierda-1][tileYEnemigoSuperior].tipoDeColision ==
                                Tile.PASABLE
                        //&& mapaTiles[tileXEnemigoIzquierda-1][tileYEnemigoInferior +1].tipoDeColision == Tile.PASABLE
                        &&
                        mapaTiles[tileXEnemigoCentro][tileYEnemigoCentro].tipoDeColision==Tile.PASABLE) {

                    //enemigo.moverseHaciaJugador(jugador.x,jugador.y);
                    enemigo.x += enemigo.velocidadX;

                    // Solido / borde del tile acercarse.
                } else if (tileXEnemigoIzquierda -1  >= 0 ) {

                    int TileEnemigoIzquierdo= tileXEnemigoIzquierda*Tile.ancho ;
                    double distanciaX =  (enemigo.x -  enemigo.ancho/2) - TileEnemigoIzquierdo;

                    if( distanciaX  > 0) {
                        double velocidadNecesaria =
                                Utilidades.proximoACero(-distanciaX, enemigo.velocidadX);
                        enemigo.x += velocidadNecesaria;
                    } else {
                        enemigo.girarX();
                    }
                } else {
                    enemigo.girarX();
                }
            }

            if(enemigo.velocidadY < 0) {
                // Solo una condición para pasar: Tile izquierda pasable y suelo solido.
                if (tileYEnemigoSuperior - 1 >= 0 &&
                        mapaTiles[tileXEnemigoIzquierda][tileYEnemigoSuperior-1].tipoDeColision
                                == Tile.PASABLE
                        && mapaTiles[tileXEnemigoDerecha][tileYEnemigoSuperior-1].tipoDeColision
                        == Tile.PASABLE &&
                        mapaTiles[tileXEnemigoIzquierda][tileYEnemigoSuperior].tipoDeColision
                        == Tile.PASABLE
                        && mapaTiles[tileXEnemigoDerecha][tileYEnemigoSuperior].tipoDeColision
                        == Tile.PASABLE && mapaTiles[tileXEnemigoCentro][tileYEnemigoSuperior-1].tipoDeColision ==Tile.PASABLE){
                    //enemigo.moverseHaciaJugador(jugador.x,jugador.y);
                    enemigo.y += enemigo.velocidadY;

                    // Solido / borde del tile acercarse.
                } else{

                    // Si en el propio tile del jugador queda espacio para
                    // subir más, subo
                    int TileEnemigoBordeSuperior = (tileYEnemigoSuperior)*Tile.altura;
                    double distanciaY =  (enemigo.y - enemigo.altura/2) - TileEnemigoBordeSuperior;

                    if( distanciaY  > 0) {
                        enemigo.y += Utilidades.proximoACero(-distanciaY, enemigo.velocidadY);

                    } else {
                        enemigo.girarY();
                    }
                }
            }
            if(enemigo.velocidadY >= 0) {
               // Log.i("ENEMIGO", "Bajando" +mapaTiles[tileXEnemigoIzquierda][tileYEnemigoSuperior+10].tipoDeColision + mapaTiles[tileXEnemigoDerecha][tileYEnemigoSuperior].tipoDeColision);

                // Solo una condición para pasar: Tile izquierda pasable y suelo solido.
                if (tileYEnemigoInferior + 1 <= altoMapaTiles() - 1
                        && mapaTiles[tileXEnemigoIzquierda][tileYEnemigoInferior].tipoDeColision == Tile.PASABLE
                        && mapaTiles[tileXEnemigoIzquierda][tileYEnemigoInferior].tipoDeColision == Tile.PASABLE
                        && mapaTiles[tileXEnemigoDerecha][tileYEnemigoInferior].tipoDeColision == Tile.PASABLE
                        && mapaTiles[tileXEnemigoDerecha][tileYEnemigoInferior].tipoDeColision == Tile.PASABLE
                        ) {

                    //enemigo.moverseHaciaJugador(jugador.x,jugador.y);
                    enemigo.y += enemigo.velocidadY;

                    // Solido / borde del tile acercarse.
                } else if (tileYEnemigoInferior + 1 <= altoMapaTiles() - 1 &&
                        (mapaTiles[tileXEnemigoIzquierda][tileYEnemigoInferior + 1].tipoDeColision
                                == Tile.SOLIDO ||
                                mapaTiles[tileXEnemigoDerecha][tileYEnemigoInferior + 1].tipoDeColision ==
                                        Tile.SOLIDO)) {

                    // Con que uno de los dos sea solido ya no puede caer
                    // Si en el propio tile del jugador queda espacio para bajar más, bajo
                    int TileEnemigoBordeInferior =
                            tileYJugadorInferior * Tile.altura + Tile.altura;
                    double distanciaY =
                            TileEnemigoBordeInferior - (enemigo.y + enemigo.altura / 2);



                    // Esta cayendo por debajo del ULTIMO
                    // va a desaparecer y perder.
                }
            }

           enemigo.moverseHaciaJugador(jugador.x,jugador.y);

            Log.i("posicion_enemigo", "xcentro: " + tileXEnemigoCentro + " ycentro: " + tileYEnemigoCentro +
                    " xder: " + tileXEnemigoDerecha + " xizq: " + tileXEnemigoIzquierda
                    + " ysup: " + tileYEnemigoSuperior + " yinf: " + tileYEnemigoInferior);
        }

        Log.i("posicion", "xcentro: " + tileXJugador + " ycentro: " + tileYJugadorCentro +
                " xder: " + tileXJugadorDerecha + " xizq: " + tileXJugadorIzquierda
                + "ysup: " + tileYJugadorSuperior + " yinf: " + tileYJugadorInferior);


        // Hacia abajo
        if (jugador.velocidadY < 0) {

            if (tileYJugadorSuperior - 1 >= 0 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior - 1].tipoDeColision
                            == Tile.PASABLE
                    && mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior - 1].tipoDeColision
                    == Tile.PASABLE) {

                jugador.y += jugador.velocidadY;

            } else {

                // Si en el propio tile del jugador queda espacio para
                // subir más, subo
                int TileJugadorBordeSuperior = (tileYJugadorSuperior) * Tile.altura;
                double distanciaY = (jugador.y - jugador.altura / 2) - TileJugadorBordeSuperior;

                if (distanciaY > 0) {
                    jugador.y += Utilidades.proximoACero(-distanciaY, jugador.velocidadY);

                }

            }

        }

        // Hacia abajo
        if (jugador.velocidadY >= 0) {
            // Tile inferior PASABLE
            // Podemos seguir moviendo hacia abajo
            // NOTA - El ultimo tile es especial (caer al vacío )
            if (tileYJugadorInferior + 1 <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior + 1].tipoDeColision
                            == Tile.PASABLE
                    && mapaTiles[tileXJugadorDerecha][tileYJugadorInferior + 1].tipoDeColision
                    == Tile.PASABLE) {
                // si los dos están libres cae

                jugador.y += jugador.velocidadY;
                // Tile inferior SOLIDO
                // El ULTIMO, es un caso especial

            } else if (tileYJugadorInferior + 1 <= altoMapaTiles() - 1 &&
                    (mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior + 1].tipoDeColision
                            == Tile.SOLIDO ||
                            mapaTiles[tileXJugadorDerecha][tileYJugadorInferior + 1].tipoDeColision ==
                                    Tile.SOLIDO)) {

                // Con que uno de los dos sea solido ya no puede caer
                // Si en el propio tile del jugador queda espacio para bajar más, bajo
                int TileJugadorBordeInferior =
                        tileYJugadorInferior * Tile.altura + Tile.altura;
                double distanciaY =
                        TileJugadorBordeInferior - (jugador.y + jugador.altura / 2);

                if (distanciaY > 0) {
                    jugador.y += Math.min(distanciaY, jugador.velocidadY);

                }

            }
        }

        if (jugador.velocidadX > 0) {
            // Tengo un tile delante y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXJugadorDerecha + 1 <= anchoMapaTiles() - 1 &&
                    tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha + 1][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                jugador.x += jugador.velocidadX;

                // No tengo un tile PASABLE delante
                // o es el FINAL del nivel o es uno SOLIDO
            } else if (tileXJugadorDerecha <= anchoMapaTiles() - 1 &&
                    tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorDerecha][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeDerecho = tileXJugadorDerecha * Tile.ancho + Tile.ancho;
                double distanciaX = TileJugadorBordeDerecho - (jugador.x + jugador.ancho / 2);

                if (distanciaX > 0) {
                    double velocidadNecesaria = Math.min(distanciaX, jugador.velocidadX);
                    jugador.x += velocidadNecesaria;
                } else {
                    // Opcional, corregir posición
                    jugador.x = TileJugadorBordeDerecho - jugador.ancho / 2;
                }
            }
        }

// izquierda
        if (jugador.velocidadX <= 0 && jugador.orientacion == Jugador.IZQUIERDA) {
            // Tengo un tile detrás y es PASABLE
            // El tile de delante está dentro del Nivel
            if (tileXJugadorIzquierda - 1 >= 0 &&
                    tileYJugadorInferior < altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda - 1][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro].tipoDeColision ==
                            Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior].tipoDeColision ==
                            Tile.PASABLE) {

                jugador.x += jugador.velocidadX;
                // No tengo un tile PASABLE detrás
                // o es el INICIO del nivel o es uno SOLIDO
            } else if (tileXJugadorIzquierda >= 0 && tileYJugadorInferior <= altoMapaTiles() - 1 &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorInferior].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorCentro].tipoDeColision
                            == Tile.PASABLE &&
                    mapaTiles[tileXJugadorIzquierda][tileYJugadorSuperior].tipoDeColision
                            == Tile.PASABLE) {

                // Si en el propio tile del jugador queda espacio para
                // avanzar más, avanzo
                int TileJugadorBordeIzquierdo = tileXJugadorIzquierda * Tile.ancho;
                double distanciaX = (jugador.x - jugador.ancho / 2) - TileJugadorBordeIzquierdo;

                if (distanciaX > 0) {
                    double velocidadNecesaria = Utilidades.proximoACero(-distanciaX, jugador.velocidadX);
                    jugador.x += velocidadNecesaria;
                } else {
                    // Opcional, corregir posición
                    jugador.x = TileJugadorBordeIzquierdo + jugador.ancho / 2;
                }
            }
        }

        for (Iterator<DisparoJugador> iterator = disparosJugador.iterator(); iterator.hasNext(); ) {

            DisparoJugador disparoJugador = iterator.next();
            if (disparoJugador.tVidaMaximo <= disparoJugador.tVida) {
                iterator.remove();
                break;
            }

            disparoJugador.x += disparoJugador.velocidadX;
            disparoJugador.y += disparoJugador.velocidadY;

            for (Enemigo enemigo : enemigos) {
                if (disparoJugador.colisiona(enemigo)) {
                    if (enemigo.estado != enemigo.INACTIVO)
                        enemigo.destruir();

                    iterator.remove();
                    break;
                }
            }
        }
        for (Iterator<Cofre> iterator = cofres.iterator(); iterator.hasNext();) {
            Cofre cofre = iterator.next();
            if (cofre.estado == Cofre.ELIMINAR){
                iterator.remove();
                continue;
            }
            if (jugador.colisiona(cofre) && cofre.estado == Cofre.ACTIVO ) {
                cofre.destruir();
                if (cofre.tipoCofre == Cofre.COFRE_MEJORA_DISPARO) {
                    jugador.armaActual = TipoArmas.ARMA_DISTANCIA;
                }
                else if (cofre.tipoCofre == Cofre.COFRE_VIDA) {
                    jugador.vidas++;
                }else if(cofre.tipoCofre == Cofre.COFRE_ESCUDO){
                    jugador.numeroEscudos++;
                }
            }
        }

    }




    public void dibujar (Canvas canvas) {
        // EL orden importa
        if(inicializado) {
            fondo.dibujar(canvas);

            dibujarTiles(canvas);
            if(enemigos.size()==0){

                puerta.dibujar(canvas);
                if(!puerta.isActiva()){
                    puerta.setActiva(true);
                }

            }else{
                puerta.setActiva(false);
            }
            for(DisparoJugador disparoJugador: disparosJugador){
                disparoJugador.dibujar(canvas);
            }

            for (Cofre cofre : cofres) {
                cofre.dibujar(canvas);
            }

            jugador.dibujar(canvas);

            for(Enemigo enemigo: enemigos){
                enemigo.dibujar(canvas);
            }
            if (nivelPausado){
                // la foto mide 480x320
                Rect orgigen = new Rect(0,0 ,
                        480,320);

                Paint efectoTransparente = new Paint();
                efectoTransparente.setAntiAlias(true);

                Rect destino = new Rect((int)(GameView.pantallaAncho/2 - 480/2),
                        (int)(GameView.pantallaAlto/2 - 320/2),
                        (int)(GameView.pantallaAncho/2 + 480/2),
                        (int)(GameView.pantallaAlto/2 + 320/2));
                canvas.drawBitmap(mensaje,orgigen,destino, null);
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


    /*    if((jugador.y < GameView.pantallaAlto * 0.45)){
            scrollEjeY = (int) ((GameView.pantallaAlto-285) - ( GameView.pantallaAlto * 0.45 - jugador.y));
        }else
            scrollEjeY = (int) (GameView.pantallaAlto-285);*/
        if(jugador.y -scrollEjeY > GameView.pantallaAlto*0.75) {
            scrollEjeY = (int) (jugador.y - GameView.pantallaAlto * 0.75);

        }
        if(jugador.y -scrollEjeY < GameView.pantallaAlto*0.2) {
            if(jugador.y  > altoMapaTiles()*3 ) {
                scrollEjeY = (int) (jugador.y - GameView.pantallaAlto * 0.2);
            }
        }

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

