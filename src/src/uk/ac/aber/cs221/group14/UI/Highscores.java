/*
 * @(#) Highscores.java 1.5 2018/04/30
 *
 * Copyright (c) 2018 Aberystwyth University.
 * All rights reserved.
 *
 */
package uk.ac.aber.cs221.group14.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 *
 * User interface Class using JavaFx to create the Highscores screen
 *
 * @author Neil Houston - neh1
 *
 * @version 1.1 Initial development
 * @version 1.2 Pane to add scores to created
 * @version 1.3 Observable list added to display the scores
 * @version 1.4 back button added
 * @version 1.5 Code refactoring
 */


public class Highscores implements Runnable {

    private ListView<String> scoresView = new ListView<>();
    private Button back = new Button("Back");
    private ObservableList<String> scoresToLoad = FXCollections.observableArrayList();

  private PriorityQueue AllScores = new PriorityQueue();


    public Button getBack() {
        return back;
    }

    /**
     * Main method that creates the highscores pane
     *
     * Displays the highscores in an observable list
     *
     * @return highscores pane to be used in Javafx scene
     */
    public Pane showHighscores() {
        Pane scores = new Pane();

        VBox scoresToLoadScreen = new VBox(); String fileName = "src/uk/ac/aber/cs221/group14/HighscoreFile/SCORES.txt";

        String line; try {
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                AllScores.add(line);
            }

            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        scoresToLoad.addAll(AllScores);

        scoresView.setItems(scoresToLoad);

        scoresToLoadScreen.getChildren().add(scoresView);


        back.setLayoutX(290); back.setLayoutY(450); back.setPrefSize(400, 35);

        scoresToLoadScreen.setLayoutX(290); scoresToLoadScreen.setLayoutY(40); scoresToLoadScreen.setPrefSize(400, 450);

        DropShadow shadow = new DropShadow();

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

        back.setStyle("-fx-background-color: whitesmoke");

        scores.getChildren().addAll(scoresToLoadScreen, back); scores.setStyle("-fx-background-color: powderblue;");

        return scores;
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
