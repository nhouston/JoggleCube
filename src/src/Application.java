/*
 * @(#) Application.java 1.8 2018/04/30
 *
 * Copyright (c) 2018 Aberystwyth University.
 * All rights reserved.
 *
 */

import javafx.application.Platform;
import uk.ac.aber.cs221.group14.Game.Board;
import uk.ac.aber.cs221.group14.Game.Box;
import uk.ac.aber.cs221.group14.UI.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.FileNotFoundException;

import com.sun.javafx.application.PlatformImpl;



/**
 *Application class that runs the program Jogglecube
 *
 * @author Neil Houston - neh1

 * @version 1.8 Code refactoring
 */
public class Application extends javafx.application.Application {

private Stage window;

private Scene startUp, PlayMainGame, ChooseAGame, PlayLoadedGame, ShowHighscores;

//Class Objects
private StartUpScreen startingScreen = new StartUpScreen();
private MainGame playGame = new MainGame();
private ChooseALoadedGame chooseLGame = new ChooseALoadedGame();
private LoadedGame playLGame = new LoadedGame();
private Highscores getHighscores = new Highscores();
private Board loading = new Board();

@Override
public void start(Stage primaryStage) throws Exception {
    //Starting Threads
    Thread starting = new Thread(startingScreen); Thread main = new Thread(playGame);
    Thread chooseLoad = new Thread(chooseLGame); Thread playingLoad = new Thread(playLGame);
    Thread scores = new Thread(getHighscores);
    // Game starting 5 threads
    starting.start(); main.start(); chooseLoad.start(); playingLoad.start(); scores.start();

    window = primaryStage; window.setTitle("JoggleCube");


    startUp = new Scene(startingScreen.start(), 1024, 720);
    ChooseAGame = new Scene(chooseLGame.chooseGameLoad(), 1024, 720);
    ShowHighscores = new Scene(getHighscores.showHighscores(), 1024, 720);

    //actions
    startingScreen.getLoadNewGame().setOnMouseClicked(e -> {
        playGame.getMainGame().getChildren().clear();
        PlayMainGame = new Scene(playGame.getRoot(), 1024, 720);
        window.setScene(PlayMainGame);
    });

    startingScreen.getLoadPreviousGame().setOnMouseClicked(e -> {
        window.setScene(ChooseAGame);
    });

    startingScreen.getHighscore().setOnMouseClicked(e -> {
        window.setScene(ShowHighscores);
    });

    playGame.getExitButton().setOnMouseClicked(e -> {
        window.hide();
    });

    playGame.getNewGame().setOnMouseClicked(e -> {
        PlayMainGame = new Scene(playGame.getRoot(), 1024, 720);
        window.setScene(PlayMainGame);
    });

    playGame.getLoadGamebutton().setOnMouseClicked(e -> {
        window.setScene(ChooseAGame);
    });


    chooseLGame.getBeginTheLoadedGame().setOnMouseClicked(e -> {
        try {
            String [] fileData = loading.load(chooseLGame.fileName);
            PlayLoadedGame = new Scene(playLGame.getTheLoadedGame(fileData), 1024, 720);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } window.setScene(PlayLoadedGame);
    });

    chooseLGame.getBack().setOnMouseClicked(e -> {
        window.setScene(startUp);
    });


    playLGame.getNewGame().setOnMouseClicked(e -> {
        PlayMainGame = new Scene(playGame.getRoot(), 1024, 720);
        window.setScene(PlayMainGame);
    });

    playLGame.getLoadGamebutton().setOnMouseClicked(e -> {
        window.setScene(ChooseAGame);
    });

    playLGame.getExitButton().setOnMouseClicked(e -> {
        window.hide();
    });


    getHighscores.getBack().setOnMouseClicked(e -> {
        window.setScene(startUp);
    });

    window.setScene(startUp); window.show();
}

/*public Application() {
    PlatformImpl.startup(new Runnable() {
        @Override
        public void run() {
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
}*/

public static void main(String[] args) {
    launch(args);
}
}
