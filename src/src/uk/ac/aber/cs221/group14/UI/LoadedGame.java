/*
 * @(#) LoadedGame.java 1.7 2018/04/30
 *
 * Copyright (c) 2018 Aberystwyth University.
 * All rights reserved.
 *
 */
package uk.ac.aber.cs221.group14.UI;

import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import uk.ac.aber.cs221.group14.Dictionary.WordScoreChecker;
import uk.ac.aber.cs221.group14.Game.Board;
import uk.ac.aber.cs221.group14.Game.Box;
import uk.ac.aber.cs221.group14.Game.Position;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * User interface Class using JavaFx to create the LoadedGame screen
 *
 * @author Neil Houston - neh1
 *
 * @version 1.1 Initial development
 * @version 1.2 User interface panes added
 * @version 1.3 Added buttons
 * @version 1.4 Added Background Colors
 * @version 1.5 Added score area, completed words area, and grid creation
 * @version 1.6 Added button shadows
 * @version 1.7 Code refactoring
 */
public class LoadedGame implements Runnable {


//private final boolean gameIsStarted = true;
private boolean madeFirstTurn = false;
private final Label wordLabel = new Label();
private Label label;
private Label preScore = new Label("Previous Score: ");
private Label completedWordsArea = new Label("Completed Words");
private Label scoreArea = new Label("Scoring");
private Label previousScore = new Label("Previous Score");
private Label Bottom = new Label("Bottom");
private Label Middle = new Label("Middle");
private Label TopLabel = new Label("Top");
private int sum = 0;
private int FinalScore = 0;

private final Integer startTime = 60;
private Integer endTimeSeconds = 177;
private Integer Seconds = startTime;
private Integer minutes = 2;

private ListView<String> wordsList = new ListView<>();
private ListView<Integer> scores = new ListView<>();
private ArrayList<Integer> scoreArray = new ArrayList<>();
private Timeline time = new Timeline();

//main game buttons
private final Button newGame = new Button("New game");
private final Button loadGamebutton = new Button("Load game");
private final Button beginGamebutton = new Button("Begin game");
private final Button exitButton = new Button("Exit");
private final Button saveButton = new Button("Save");
private final Button submitWordButton = new Button("Submit Word");
private final Button clearText = new Button("X");
private final Button left = new Button("Left");
private final Button right = new Button("Right");

private String getWord() {
    return wordLabel.getText();
}

public Button getNewGame() {
    return newGame;
}

public Button getLoadGamebutton() {
    return loadGamebutton;
}

public Button getExitButton() {
    return exitButton;
}

/**
 * Main method that creates the loaded game
 *
 * @return loaded game pane
 * @param gridLetters the loaded letters to be added to the grid
 */
public Pane getTheLoadedGame(String[] gridLetters) {
    Pane loadedGame = new Pane();
    HBox center = new HBox();
    Board grid = new Board();
    WordScoreChecker scoring = new WordScoreChecker();
    preScore.setText("Previous Game Score: " + gridLetters [0] + " Player: " + gridLetters [2]);
    preScore.setFont(Font.font(20));

    int numLetter = 0;

    for (int sideValue = 0; sideValue < grid.GRID_SIZE; ++sideValue) {

        GridPane pane = new GridPane(); if (sideValue < 2) {
            pane.setPadding(new Insets(20, 0, 0, 25));
        } else {
            pane.setPadding(new Insets(20, 0, 0, 25));
        } pane.setHgap(10); pane.setVgap(10); for (int col = 0; col < grid.GRID_SIZE; ++col) {
            for (int row = 0; row < grid.GRID_SIZE; ++row) {
                //new Box object
                Box cube = new Box(sideValue, col, row, gridLetters[1].charAt(numLetter)); numLetter++;

                grid.rect[sideValue][col][row] = cube;
                //keep easy reference to the stack because you are going to use it multiple times
                StackPane stack = cube.getStackPane();
                //when the stack is clicked i.e. the box
                stack.setOnMouseClicked((MouseEvent e) -> {
                    //check the side first
                    Position p = ((Position) stack.getUserData()); if (p.y == grid.currentSide) {
                        if (!madeFirstTurn) {
                            cube.getRect().setFill(Color.LIGHTBLUE); madeFirstTurn = true;
                            wordLabel.setText(wordLabel.getText() + cube.getString());
                            grid.lastClickedBox = grid.rect[p.y][p.z][p.x];//tested
                            grid.highlightPositions();
                        } else if (grid.rect[p.y][p.z][p.x].getRect().getFill() == Color.PINK) {
                            if (cube.getRect().getFill() != Color.LIGHTBLUE) {
                                //change the color
                                cube.getRect().setFill(Color.LIGHTBLUE);
                                //use word reference to add a character to it at the top
                                wordLabel.setText(wordLabel.getText() + cube.getString());
                                grid.lastClickedBox = grid.rect[p.y][p.z][p.x];//tested
                                grid.highlightPositions();
                            } else {
                                Alert a = new Alert(Alert.AlertType.ERROR);
                                a.setContentText("You cannot click the same letter twice"); a.showAndWait();
                            }
                        }
                    }
                });
                //add the stack to the grid pane
                pane.add(stack, col, row);
            }
        } center.getChildren().add(pane);
    }


    center.setLayoutX(0); center.setLayoutY(173);

    //grid words
    HBox top = new HBox(5); top.setLayoutX(14); top.setLayoutY(569); top.setPrefSize(340, 35);
    wordLabel.setFont((new Font("Arial", 24))); wordLabel.setPadding(new Insets(20, 0, 20, 0));
    top.getChildren().add(wordLabel);

    //countdown clock
    HBox countdownTimer = countdownClock(); VBox scoreboard = new VBox();

    scoreboard.getChildren().add(scores);
    ObservableList<Integer> theFinalScoreOfTheGame = FXCollections.observableArrayList();

    Label score = new Label(); score.setFont(new Font("Arial", 24)); score.setText("Score: 0");

    VBox completedWords = new VBox(); completedWords.getChildren().add(wordsList);
    ObservableList<String> items = FXCollections.observableArrayList();


    clearText.setOnMouseClicked(e -> {
        wordLabel.setText("");
        //then traverse the grid and unselect all the letters
        madeFirstTurn = false; grid.deselectAllLetters();
    });

    left.setOnMouseClicked(e -> {
        if (grid.currentSide > 0) {
            --grid.currentSide; grid.showSelectedSide();
        }
    }); right.setOnMouseClicked(e -> {
        if (grid.currentSide < 2) {
            ++grid.currentSide; grid.showSelectedSide();
        }
    }); submitWordButton.setOnMouseClicked(e -> {
        //save a reference to the word because you are using it at least 5 times
        String submittedWord = getWord();

        //check if the word is valid
        if (scoring.search(submittedWord)) {

            //if it is valid check of the word is not already inputted
            if (!items.contains(submittedWord)) {
                int wordScore = scoring.getWordValue(submittedWord); scoreArray.add(wordScore);

                for (Integer aScoreArray : scoreArray) {
                    sum += aScoreArray;
                }

                //add the word to a observable list
                items.add(submittedWord);
                //set the items in list view
                wordsList.setItems(items);
                //show score in scoreboard
                theFinalScoreOfTheGame.add(wordScore); scores.setItems(theFinalScoreOfTheGame);
                //add score
                score.setText("Score: " + String.valueOf(sum)); sum = 0;

            } else {
                Alert a = new Alert(Alert.AlertType.ERROR); a.setContentText("You have already typed that word");
                a.showAndWait();

            }
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR); a.setContentText("Not a valid Word"); a.showAndWait();

        }
        //this code is the same as clearText on mouse click event
        wordLabel.setText("");
        //then traverse the grid and unselect all the letters
        madeFirstTurn = false; grid.deselectAllLetters();
    });

    saveButton.setOnAction(e -> {
        Dialog<Pair<String, String>> dialog = new Dialog<>(); dialog.setTitle("Save Game Dialog");
        dialog.setHeaderText("Please enter a username and a filename!");


        // Set the button types.
        ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(save);

        // Create the username and password labels and fields.
        GridPane exiting = new GridPane(); exiting.setHgap(10); exiting.setVgap(10);
        exiting.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField(); username.setPromptText("Username"); TextField fileBox = new TextField();
        fileBox.setPromptText("Filename");

        exiting.add(new Label("Username"), 0, 0); exiting.add(username, 1, 0); exiting.add(new Label("Filename"), 0, 1);
        exiting.add(fileBox, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(save); loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> loginButton.setDisable(newValue.trim().isEmpty()));

        dialog.getDialogPane().setContent(exiting);

        // Request focus on the username field by default.
        Platform.runLater(username::requestFocus);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == save) {
                return new Pair<>(username.getText(), fileBox.getText());
            } return null;
        });

        int saveScore = 0; for (Integer aScoreArray : scoreArray) {
            saveScore += aScoreArray;
        }

        Optional<Pair<String, String>> result = dialog.showAndWait();

        int finalSaveScore = saveScore; result.ifPresent(usernamePassword -> {
            //pass the correct score value at
            grid.save(usernamePassword.getKey(), usernamePassword.getValue() + ".txt", finalSaveScore);

        });


    });

    completedWordsArea.setText("Completed Words");
    completedWordsArea.setLayoutX(755);
    completedWordsArea.setLayoutY(80);
    completedWordsArea.setFont(new Font("Arial", 18));

    scoreArea.setText("Scoring"); scoreArea.setLayoutX(755); scoreArea.setLayoutY(340);
    scoreArea.setFont(new Font("Arial", 18));

    Bottom.setText("Bottom"); Bottom.setLayoutX(100); Bottom.setLayoutY(150); Bottom.setFont(new Font("Arial", 18));

    Middle.setText("Middle"); Middle.setLayoutX(335); Middle.setLayoutY(150); Middle.setFont(new Font("Arial", 18));

    TopLabel.setText("Top"); TopLabel.setLayoutX(575); TopLabel.setLayoutY(150);
    TopLabel.setFont(new Font("Arial", 18));



    //buttons
    add(countdownTimer, newGame, loadGamebutton, beginGamebutton, saveButton, exitButton, submitWordButton, scoreboard, completedWords, score, center, top, left, right, clearText, completedWordsArea, scoreArea, Bottom, Middle, TopLabel,preScore, loadedGame);
    //setting layout of of everything
    newGameLayout(newGame, 558, 671, 220, 35); newGameLayout(loadGamebutton, 790, 671, 220, 35);
    newGameLayout(beginGamebutton, 14, 620, 520, 85); newGameLayout(exitButton, 558, 620, 220, 35);
    newGameLayout(saveButton, 790, 620, 220, 35); newGameLayout(submitWordButton, 350, 569, 130, 35);
    newGameLayout(clearText, 488, 569, 45, 35); newGameLayout(right, 659, 569, 90, 35);
    newGameLayout(left, 558, 569, 90, 35); score.setLayoutX(755); score.setLayoutY(569); score.setPrefSize(252, 35);
    scoreboard.setLayoutX(755); scoreboard.setLayoutY(366); scoreboard.setPrefSize(250, 180);

    completedWords.setLayoutX(755); completedWords.setLayoutY(110); completedWords.setPrefSize(252, 230);

    countdownTimer.setLayoutX(756); countdownTimer.setLayoutY(14); countdownTimer.setPrefSize(250, 75);

    label.setLayoutX(756); label.setLayoutY(14); label.setPrefSize(250, 85);

    left.setDisable(true); right.setDisable(true); clearText.setDisable(true); submitWordButton.setDisable(true);
    newGame.setDisable(true);

    beginGamebutton.setOnAction(e -> {
        //run countdown clock
        left.setDisable(false); right.setDisable(false); clearText.setDisable(false);
        submitWordButton.setDisable(false); beginGamebutton.setDisable(true); newGame.setDisable(true); grid.showSelectedSide(); doTime();
    });

    DropShadow shadow = new DropShadow();

    beginGamebutton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    beginGamebutton.setEffect(shadow);
                }
            });

    beginGamebutton.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    beginGamebutton.setEffect(null);
                }
            });

    left.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    left.setEffect(shadow);
                }
            });

    left.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    left.setEffect(null);
                }
            });

    right.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    right.setEffect(shadow);
                }
            });

    right.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    right.setEffect(null);
                }
            });

    clearText.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    clearText.setEffect(shadow);
                }
            });

    clearText.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    clearText.setEffect(null);
                }
            });

    submitWordButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    submitWordButton.setEffect(shadow);
                }
            });

    submitWordButton.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    submitWordButton.setEffect(null);
                }
            });

    exitButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    exitButton.setEffect(shadow);
                }
            });

    exitButton.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    exitButton.setEffect(null);
                }
            });


    newGame.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    newGame.setEffect(shadow);
                }
            });

    newGame.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    newGame.setEffect(null);
                }
            });

    loadGamebutton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    loadGamebutton.setEffect(shadow);
                }
            });

    loadGamebutton.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    loadGamebutton.setEffect(null);
                }
            });

    saveButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    saveButton.setEffect(shadow);
                }
            });

    saveButton.addEventHandler(MouseEvent.MOUSE_EXITED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    saveButton.setEffect(null);
                }
            });

    beginGamebutton.setStyle("-fx-background-color: whitesmoke");
    left.setStyle("-fx-background-color: whitesmoke");
    right.setStyle("-fx-background-color: whitesmoke");
    clearText.setStyle("-fx-background-color: whitesmoke");
    submitWordButton.setStyle("-fx-background-color: whitesmoke");
    exitButton.setStyle("-fx-background-color: whitesmoke");
    newGame.setStyle("-fx-background-color: whitesmoke");
    loadGamebutton.setStyle("-fx-background-color: whitesmoke");
    saveButton.setStyle("-fx-background-color: whitesmoke");

    loadedGame.setStyle("-fx-background-color: powderblue;"); return loadedGame;
}

/**
 * @param button
 * @param value
 * @param value2
 * @param prefWidth
 * @param prefHeight
 */
private void newGameLayout(Button button, int value, int value2, int prefWidth, int prefHeight) {
    button.setLayoutX(value); button.setLayoutY(value2); button.setPrefSize(prefWidth, prefHeight);
}

/**
 * @param countdownTimer
 * @param exitButton
 * @param newGame
 * @param loadGamebutton
 * @param beginGamebutton
 * @param saveButton
 * @param submitWordButton
 * @param scoreboard
 * @param completedWords
 * @param score
 * @param top
 * @param center
 * @param left
 * @param right
 * @param clearText
 * @param completedWordsArea
 * @param scoreArea
 * @param bottom
 * @param middle
 * @param TopLabel
 * @param preScore
 * @param root
 */
private void add(HBox countdownTimer, Button exitButton, Button newGame, Button loadGamebutton, Button beginGamebutton, Button saveButton, Button submitWordButton, VBox scoreboard, VBox completedWords, Label score, HBox top, HBox center, Button left, Button right, Button clearText, Label completedWordsArea, Label scoreArea, Label bottom, Label middle, Label TopLabel, Label preScore, Pane root) {

    root.getChildren().addAll(this.label, countdownTimer); root.getChildren().add(completedWords);
    root.getChildren().add(scoreboard); root.getChildren().add(submitWordButton); root.getChildren().add(score);
    root.getChildren().add(newGame); root.getChildren().add(loadGamebutton); root.getChildren().add(beginGamebutton);
    root.getChildren().add(saveButton); root.getChildren().add(exitButton); root.getChildren().add(center);
    root.getChildren().add(top); root.getChildren().add(left); root.getChildren().add(right);
    root.getChildren().add(clearText); root.getChildren().add(completedWordsArea); root.getChildren().add(scoreArea);
    root.getChildren().add(bottom); root.getChildren().add(middle); root.getChildren().add(TopLabel);
    root.getChildren().add(preScore);

}

/**
 * @return
 */
//setting label, setting font and spacing
private HBox countdownClock() {
    label = new Label(); label.setFont(new Font("Arial", 18)); return new HBox(5);
}

/**
 *
 */
private void doTime() {

    time.setCycleCount(Timeline.INDEFINITE);

    KeyFrame frame = new KeyFrame(Duration.seconds(1), event -> {

        Seconds--; endTimeSeconds--;

        if (Seconds == 0) {
            minutes--; Seconds = 59;
        } else if (endTimeSeconds == 0) {
            time.stop();


            for (Integer aScoreArray : scoreArray) {
                FinalScore += aScoreArray;
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Game Over"); alert.setHeaderText("Time Up!");
            alert.setContentText("Your score: " + FinalScore);

            alert.getButtonTypes().clear(); ButtonType ok = new ButtonType("Ok"); alert.getButtonTypes().addAll(ok);


            left.setDisable(true); right.setDisable(true); beginGamebutton.setDisable(true);
            submitWordButton.setDisable(true); clearText.setDisable(true);newGame.setDisable(false);

            alert.show();
        } String secs;

        if (Seconds.toString().length() <= 0) {
            secs = "0" + Seconds.toString();
        } else {
            secs = Seconds.toString();
        } label.setText("Time remaining: 0" + minutes.toString() + ":" + secs);
    });

    time.getKeyFrames().add(frame); time.playFromStart();
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
