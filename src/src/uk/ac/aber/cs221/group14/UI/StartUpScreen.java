/*
 * @(#) StartUpScreen.java 1.7 2018/04/30
 *
 * Copyright (c) 2018 Aberystwyth University.
 * All rights reserved.
 *
 */
package uk.ac.aber.cs221.group14.UI;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 *
 * User interface Class using JavaFx to create the start up screen
 *
 * @author Neil Houston - neh1
 *
 * @version 1.1 Initial development
 * @version 1.2 User interface panes added
 * @version 1.3 Added buttons
 * @version 1.4 Added Background Colors
 * @version 1.5 Added JogglueCube image
 * @version 1.6 Added button shadows
 * @version 1.7 Code refactoring
 */

public class StartUpScreen implements Runnable {

private Button loadNewGame = new Button("New game");
private Button loadPreviousGame = new Button("Load game");
private Button highscore = new Button("Highscore");

public Button getHighscore() {
    return highscore;
}

public Button getLoadNewGame() {
    return loadNewGame;
}

public Button getLoadPreviousGame() {
    return loadPreviousGame;
}

    /**
     *
     * Main method that creates the start up screen
     *
     * @return load pane, called by Application class to create a new scene
     */

public Pane start() {
    Pane load = new Pane();

    load.getChildren().add(loadNewGame); load.getChildren().add(loadPreviousGame);
    //layouts
    loadNewGame.setLayoutX(373); loadNewGame.setLayoutY(361); loadNewGame.setPrefSize(135, 35);

    loadPreviousGame.setLayoutX(520); loadPreviousGame.setLayoutY(361); loadPreviousGame.setPrefSize(135, 35);

    load.setStyle("-fx-background-color: powderblue;");

    FileInputStream inputstream = null; try {
        inputstream = new FileInputStream("jogglecube.png");
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } assert inputstream != null; Image image = new Image(inputstream); ImageView imageView = new ImageView(image);

    //Setting the position of the image
    imageView.setX(135); imageView.setY(80);

    //setting the fit height and width of the image view
    imageView.setFitHeight(250); imageView.setFitWidth(750);

    load.getChildren().add(imageView);

    highscore.setLayoutX(402); highscore.setLayoutY(421); highscore.setPrefSize(220, 35);

    DropShadow shadow = new DropShadow();


    loadNewGame.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    loadNewGame.setEffect(shadow);
                }
            });

    loadNewGame.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    loadNewGame.setEffect(null);
                }
            });

    loadPreviousGame.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    loadPreviousGame.setEffect(shadow);
                }
            });

    loadPreviousGame.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    loadPreviousGame.setEffect(null);
                }
            });


    highscore.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    highscore.setEffect(shadow);
                }
            });

    highscore.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    highscore.setEffect(null);
                }
            });

    loadNewGame.setStyle("-fx-background-color: whitesmoke");
    loadPreviousGame.setStyle("-fx-background-color: whitesmoke");
    highscore.setStyle("-fx-background-color: whitesmoke");

    load.getChildren().add(highscore);

    return load;

}

@Override
public void run() {
    try {
        Thread.sleep(500L);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
}
