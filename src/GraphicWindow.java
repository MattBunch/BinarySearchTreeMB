import java.awt.Dimension;
import java.awt.Toolkit;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GraphicWindow {

	public static void display(BinarySearchTree bst) {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		final double GRAPHIC_WINDOW_WIDTH = screenSize.getWidth();;
		final double GRAPHIC_WINDOW_HEIGHT = 500;

		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Select search option");
		window.setWidth(GRAPHIC_WINDOW_WIDTH);
		window.setHeight(GRAPHIC_WINDOW_HEIGHT);
		window.setResizable(true);

		// canvas
		Canvas canvas = new Canvas();
		canvas.setWidth(GRAPHIC_WINDOW_WIDTH);
		canvas.setHeight(GRAPHIC_WINDOW_HEIGHT);
		
        // Get the graphics context of the canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        // pass graphics context to bst
        bst.drawGraph(gc);

		// Create the Pane
		Pane root = new Pane();
//		root.setMaxHeight(GRAPHIC_WINDOW_HEIGHT);
//		root.setMaxWidth(GRAPHIC_WINDOW_WIDTH);
//		root.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
//				+ "-fx-border-insets: 5;" + "-fx-border-radius: 5;" + "-fx-border-color: blue;");

		root.getChildren().add(canvas);
		Scene scene = new Scene(root);
		window.setScene(scene);
		window.showAndWait();
	}

}
