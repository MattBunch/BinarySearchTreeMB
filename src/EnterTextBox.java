import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EnterTextBox {

	// choice box used for selecting

	public static String display(String title, String message) {
		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);

		window.setWidth(250);
		window.setHeight(160);
		window.setResizable(false);

		Label label = new Label();
		label.setText(message);

		TextField textField = new TextField();

		Label label2 = new Label("");

		Button closeButton = new Button("Enter");
		closeButton.setOnAction(e -> {
			if (textField.getText().trim().isEmpty()) {
				label2.setText("blank input detected");
				textField.clear();
			} else if (textField.getText().contains(";")) {
				label2.setText("character \";\" not allowed");
				textField.clear();
			} else
				window.close();
		});

		window.setOnCloseRequest(e -> textField.clear());

		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, textField, closeButton, label2);
		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout);
		scene.getStylesheets().add("style.css");
		window.setScene(scene);
		window.showAndWait();

		return textField.getText();
	}

}
