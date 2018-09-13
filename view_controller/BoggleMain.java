package view_controller;

//This is a event driven program with a graphical user interface 
//to play the game of Boggle. This code begins as the boilerplate
//code that is needed to start any JavaFX application.  It also
//has a recommended GridPane as the backing pane to store the
//DiceTray on the left and a GridPane with three elements on the right.
//
//@author Rick Mercer and Ivan Ko

// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 
// !!! The program should work. If you pressed submit/ reshow result, and  !!!
// !!! the GUI crashed, it means the image files didn't load properly for !!!!
// !!! whatever reason. Anyway, IF THE GUI CRASHES, PLEASE COMMENT OUT the !!!
// !!! following lines: 287-289, 293-295, 298-300. Then the program !!!!!!!!!!
// !!! should work. Thanks!.                                        !!!!!!!!!!
// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.BoggleGame;

public class BoggleMain extends Application {

	private static BoggleGame game;

	public static void main(String[] args) throws FileNotFoundException {
		
		game = new BoggleGame();
		launch(args);
	}
	
	// For testing.
	@SuppressWarnings("unused")
	private char[][] charArrayForTesting(){
		char[][] tray = {
			      { 'A', 'B', 'C', 'D' },

			      { 'E', 'F', 'G', 'H' },

			      { 'I', 'J', 'K', 'L' },

			      { 'M', 'N', 'O', 'P' } };
		
		return tray;
	}

	private GridPane window;
	private TilePane diceTiles; 
	private TextArea userText = new TextArea();
	private String resultText;
	private final static String GOODIMAGEFILENAME = "a 10.gif";
	private final static String OKAYIMAGEFILENAME = "you are pretty good.jpg";
	private final static String FAILIMAGEFILENAME = "we know that fail.jpg";
	private int totalCorrectGuessesCount = -1;
	private int totalMissedGuessesCount = 0;

	@Override
	public void start(Stage stage) {
		setUpWindow();
		setUpTopLabels();
		setUpDiceOutput();
		setUpRightInputArea();
		setUpRightButtons();
		setUpLeftButton();
		stage.setTitle("A Boggle Game (non TM ver.): You can never be perfect.");
		Scene scene = new Scene(window, 600, 440);
		stage.setScene(scene);
		stage.show();
	}

	// What the basic layout will look like:
	// [padding------------------]
	// |--==Col_0==---==Col_1==--|
	// |---==Left==--==Right==---|
	// |---==Tray==--==Text==----|
	// |-==Refresh==-==Control==-|
	// [padding------------------]
	private void setUpWindow() {
		window = new GridPane();

		// Set grid line visibility here.
		// window.setGridLinesVisible(true);

		window.setHgap(15);
		window.setVgap(10);
		window.setPadding(new Insets(10, 15, 10, 15));

		// Make the two sides with dynamic columns.
		ColumnConstraints leftCol = new ColumnConstraints();
		leftCol.setPercentWidth(50);
		window.getColumnConstraints().add(leftCol);
		leftCol.setFillWidth(false);
		leftCol.setHalignment(HPos.CENTER);
		ColumnConstraints rightCol = new ColumnConstraints();
		rightCol.setPercentWidth(50);
		window.getColumnConstraints().add(rightCol);

	}

	// |--==Col_0==---==Col_1==--|
	private void setUpTopLabels() {
		Label leftLab = new Label("Current Dice Tray");
		GridPane.setHalignment(leftLab, HPos.CENTER);
		window.add(leftLab, 0, 0);

		Label rightLab = new Label("Controls and Input");
		GridPane.setHalignment(rightLab, HPos.CENTER);
		window.add(rightLab, 1, 0);
	}

	// |--==Col_0==---==Col_1==--|
	private void setUpDiceOutput() {
		
		diceTiles = new TilePane();
		
		diceTiles.setHgap(10);
		diceTiles.setVgap(7);
		diceTiles.setPrefColumns(4);
		diceTiles.setTileAlignment(Pos.CENTER);
		// To make the tilePane appears center.
		diceTiles.setPadding(new Insets(30, 50, 30, 50));
		// Set backGround color.
		diceTiles.setStyle("-fx-background-color: #006600");
		
		Scanner diceOutput = new Scanner(game.getDiceTrayAsString());
		while(diceOutput.hasNext()) {
			Text temp = new Text(diceOutput.next());
			temp.setStyle("-fx-font: 28 impact;"
					+ "-fx-stroke: black;" + 
					"-fx-stroke-width: 1;");
			temp.setFill(Color.FLORALWHITE);
			
			diceTiles.getChildren().add(temp);
		}
		diceOutput.close();
		
		window.add(diceTiles, 0, 1);
		
	}

	private void setUpRightInputArea() {
		userText.setWrapText(true);
		window.add(userText, 1, 1);
	}
	
	// pressedOnce controls many things, important to know what state it is.
	// It controls whether the textInput and submit button are disabled and 
	// whether result button is available. once the submit is pressed, 
	// the pressedOnce set to true, and disable textInput and submit, while
	// enabling result.
	private SimpleBooleanProperty pressedOnce;
	SimpleBooleanProperty notPressedOnce;
	private void setUpRightButtons() {
		HBox rPanel = new HBox();

		Button help = new Button("Help");
		Button submit = new Button("Submit");
		Button result = new Button("Reshow Result");

		// Styling to make buttons fit to size. 
		HBox.setHgrow(help, Priority.ALWAYS);
		HBox.setHgrow(submit, Priority.ALWAYS);
		HBox.setHgrow(result, Priority.ALWAYS);
		help.setMaxWidth(Double.MAX_VALUE);
		submit.setMaxWidth(Double.MAX_VALUE);
		result.setMaxWidth(Double.MAX_VALUE);

		rPanel.getChildren().addAll(help, submit, result);

		// EventHandlers - help
		EventHandler<ActionEvent> helpButtonHandler = new HelpButtonHandler();
		help.setOnAction(helpButtonHandler);
		
		// Set submit to be available only once.
		pressedOnce = new SimpleBooleanProperty();
		submit.disableProperty().bind(pressedOnce);
		// Disable the text input area too.
		userText.disableProperty().bind(pressedOnce);
		EventHandler<ActionEvent> submitButtonHandler = new SubmitButtonHandler();
		submit.setOnAction(submitButtonHandler);
		
		// result button reuses some assets from submit.
		notPressedOnce = new SimpleBooleanProperty();
		notPressedOnce.setValue(! pressedOnce.getValue());
		result.disableProperty().bind(notPressedOnce);
		EventHandler<ActionEvent> resultButtonHandler = new ResultButtonHandler();
		
		result.setOnAction(resultButtonHandler);
		

		window.add(rPanel, 1, 2);
	}

	// Show info box with helper message to how to play the game, kinda.
	private class HelpButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent ae) {
			
			Alert hText = new Alert(AlertType.INFORMATION);
			hText.setTitle("A really bad manual ^_^;");
			hText.setHeaderText("Sort of how to use this junk: ");
			hText.setContentText("Without repeating the same grid position for each word, "
					+ "guess as many words as you can from the 4 x 4 grid on the left.\n\n"
					+ "Type your guesses in the right textbox.\n" + "Word length less than 3 will be ignored :P\n\n"
					+ "Click Submit when done.\n\n");

			hText.showAndWait();
		}
	}
	

	private class SubmitButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent ae) {
			// Set submit to be disabled if it's been pressed once.
			pressedOnce.setValue(true);
			notPressedOnce.setValue(false);
			String input = userText.getText();

			// Make resultText here.
			StringBuilder protoResultText = new StringBuilder();
			game.computeAgainstUserString(input);
			protoResultText.append("Your Score: " + game.getTotalScore() + "\n\n");
			protoResultText.append("Words you found:\n" + 
					game.getAllCorrectlyGuessedWordsAsString() + "\n\n");
			protoResultText.append("Incorrect words:\n" + 
					game.getAllIncorrectlyGuessedWordsAsString() + "\n\n");
			totalMissedGuessesCount = game.getTotalMissedWordsCount();
			protoResultText.append("You could have found " + totalMissedGuessesCount + " words.\n");
			protoResultText.append(game.getAllWordsMissedAsString() + "\n");
			
			resultText = protoResultText.toString();
			System.out.println(resultText);
			
			totalCorrectGuessesCount = game.getTotalCorrectGuessesCount();
			
			Alert sText = new Alert(AlertType.INFORMATION);
			try {
				sText = makeAlertFromResult();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sText.showAndWait();
		}

	}
	
	private Alert makeAlertFromResult() throws IOException {
		Alert newAlert = new Alert(AlertType.INFORMATION);
		newAlert.setTitle("Result");
		if(totalCorrectGuessesCount < totalMissedGuessesCount) {
			newAlert.setHeaderText("lmao");
			// Control for image height, preserve ratio.
			Image pic = new Image(FAILIMAGEFILENAME, 0, 320, true, true);
			ImageView picView = new ImageView(pic);
			newAlert.setGraphic(picView);
			
		} else if(totalMissedGuessesCount == 0) {
			newAlert.setHeaderText("Woooow!\nNo Way!\nGood job my dude!!!");
			Image pic = new Image(GOODIMAGEFILENAME, 0, 320, true, true);
			ImageView picView = new ImageView(pic);
			newAlert.setGraphic(picView);
		} else {
			newAlert.setHeaderText("You're pretty good!");
			Image pic = new Image(OKAYIMAGEFILENAME, 0, 320, true, true);
			ImageView picView = new ImageView(pic);
			newAlert.setGraphic(picView);
		}
		
		ScrollPane scroll = new ScrollPane();
		TextArea innerText = new TextArea(resultText);
		scroll.setContent(innerText);
		scroll.setMaxHeight(300);
		newAlert.getDialogPane().setContent(scroll);
		
		newAlert.setContentText(resultText);
		
		return newAlert;
	}
	
	private class ResultButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			// Basically the first condition will not happen, due
			// to refactoring code to disabling result from the start.
			if(! pressedOnce.getValue()) {
				Alert warning = new Alert(AlertType.ERROR);
				warning.setTitle("Submit has not been pressed yet.");
				warning.setHeaderText("Thou shalt SUBMIT first, baby.");
				warning.showAndWait();
			} else {
				Alert rText = new Alert(AlertType.INFORMATION);
				try {
					rText = makeAlertFromResult();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				rText.showAndWait();
			}
		}
	}
	
	private void setUpLeftButton() {
		HBox lPanel = new HBox();

		Button refresh = new Button("Refresh (New Game)");

		// Styling to make buttons fit to size. 
		refresh.setMaxWidth(Double.MAX_VALUE);
		refresh.setPrefWidth(lPanel.getMaxWidth());

		lPanel.getChildren().add(refresh);
		lPanel.setStyle("-fx-fill: #006600");

		// EventHandlers - refresh
		EventHandler<ActionEvent> refreshButtonHandler = new RefreshButtonHandler();
		refresh.setOnAction(refreshButtonHandler);
		

		window.add(lPanel, 0, 2);
	}
	
	private class RefreshButtonHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event) {
			Alert update = new Alert(AlertType.CONFIRMATION);
			update.setTitle("Create New Game?");
			update.setHeaderText("Are you sure you want to reset everything?");
			// Setup conditional check for pressing ok.
			Optional<ButtonType> result = update.showAndWait();
			if (result.isPresent()) {
				if(result.get() == ButtonType.OK) {
					game.resetGameData();
					
					// Also need to reset the diceTiles here.
					diceTiles.getChildren().clear();
					setUpDiceOutput();

					totalCorrectGuessesCount = -1;
					totalMissedGuessesCount = 0;
					// Important to reset pressedOnce here also.
					pressedOnce.setValue(false);
					notPressedOnce.setValue(true);
				}
			}
			 
		}
		
	}
	
}