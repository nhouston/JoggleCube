/*
 * @(#) ChooseALoadedGame.java 1.8 2018/04/30
 *
 * Copyright (c) 2018 Aberystwyth University.
 * All rights reserved.
 *
 */
package uk.ac.aber.cs221.group14.UI;

import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import uk.ac.aber.cs221.group14.Game.Board;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.File;

/**
 *
 * Class that creates a choose a loaded game scene
 * @author Neil Houston - neh1
 * @author Alexander Atkinson-sims - ala28
 *
 * @version 1.8 Code refactoring
 */

public class ChooseALoadedGame implements Runnable {

private ListView<File> files = new ListView<>();
private Button beginTheLoadedGame = new Button("Begin game");
private Button back = new Button("Back");
private ObservableList<File> filesToLoad = FXCollections.observableArrayList();


public String fileName;

public Button getBeginTheLoadedGame() {
    return beginTheLoadedGame;
}

    public Button getBack() {
        return back;
    }

    /**
     * Method that list all the files in the saves directory
     */
    private void listFiles() {
    File directory = new File("src/uk/ac/aber/cs221/group14/saves/");
    //get all the files from a directory
    File[] fList = directory.listFiles();
    assert fList != null;
    for (File file : fList) {
        if (file.isFile()) {

            filesToLoad.add(new File(file.getName()));
        }
    }
}

/**
 * Method that creates the choose a loaded game
 *
 * @return choose a loaded game
 */
public Pane chooseGameLoad() {
    Board test = new Board(); Pane chooseGameLoad = new Pane();

    VBox gameToLoad = new VBox(); gameToLoad.getChildren().add(files);

    listFiles();

    filesToLoad.addAll();

    files.setItems(filesToLoad);

    chooseGameLoad.getChildren().addAll(beginTheLoadedGame, gameToLoad,back);
    //layouts
    beginTheLoadedGame.setLayoutX(668); beginTheLoadedGame.setLayoutY(230); beginTheLoadedGame.setPrefSize(310, 152);
    back.setLayoutX(668);back.setLayoutY(400);back.setPrefSize(310,35);

    files.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<File>() {

        @Override
        public void changed(ObservableValue<? extends File> observable, File oldValue, File newValue) {
            fileName = String.valueOf(newValue);
        }
    });


    gameToLoad.setLayoutX(32); gameToLoad.setLayoutY(37); gameToLoad.setPrefSize(608, 650);
    DropShadow shadow = new DropShadow();

    beginTheLoadedGame.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    beginTheLoadedGame.setEffect(shadow);
                }
            });

    beginTheLoadedGame.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    beginTheLoadedGame.setEffect(null);
                }
            });

    back.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    back.setEffect(shadow);
                }
            });

    back.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    back.setEffect(null);
                }
            });



    beginTheLoadedGame.setStyle("-fx-background-color: whitesmoke");
    back.setStyle("-fx-background-color: whitesmoke");

    chooseGameLoad.setStyle("-fx-background-color: powderblue;"); return chooseGameLoad;
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
