import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ChoiceBoxWindow {

	static String output;

	public static String display(List<String> inputItems) {
		Stage window = new Stage();
//		window.setOnCloseRequest(e->e.consume());

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Select search option");
		window.setWidth(250);
		window.setHeight(275);
		window.setResizable(false);

		Label label = new Label();
		label.setText("Select search option");

		// Create ComboBox
		ChoiceBox<String> choiceBox = new ChoiceBox<>();

		// choicebox full of options
		choiceBox.getItems().addAll(inputItems);
		choiceBox.setValue(inputItems.get(0));
		choiceBox.setId("choice-box");
		
		// exit
		window.setOnCloseRequest(e -> choiceBox.setValue(""));

		// button
		Button button = new Button("Select search");
		button.setOnAction(e -> {
			output = choiceBox.getValue();
			window.close();
		});

		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, choiceBox, button);
		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout);
		scene.getStylesheets().add("style.css");
		window.setScene(scene);
		window.showAndWait();
		return output;
	}

}
