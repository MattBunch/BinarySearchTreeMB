import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

	// main tree
	public static BinarySearchTree binarySearchTree = new BinarySearchTree();

	// Window
	Stage window;
	Scene scene;
	private final int WINDOW_WIDTH = 420;
	private final int WINDOW_HEIGHT = 430;

	// Files
	public static String filePath = "";
	FileChooser fileChooser = new FileChooser();
	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");

	// Labels and TextArea
	Label label = new Label();
	Label label2 = new Label("Remember to save tree after inserting or deleting nodes");
	Label titleLabel = new Label("Binary Search Tree");
	TextArea textArea = new TextArea();
	TextField textField = new TextField();

	// buttons
	Button loadFileButton = new Button("Load file");
	Button saveTreeButton = new Button("Save file");
	Button printNamesButton = new Button("Print names");
	Button insertButton = new Button("Insert");
	Button searchFirstNameButton = new Button("Search by first name");
	Button searchSurnameButton = new Button("Search by surname");
	Button searchFullNameButton = new Button("Search by full name");
	Button deleteButton = new Button("Delete");
	Button graphButton = new Button("Graph");
	Button changeNameButton = new Button("Change name");
	Button monitorSkewButton = new Button("Monitor skew");
	Button balanceTreeButton = new Button("Balance tree");

	// arraylists holding choicebox options
	List<String> printOptions = List.of("in-order depth-first", "breadth-first", "pre-order depth-first",
			"post-order depth-first", "name longer than user input", "name shorter than user input",
			"1st name longer than 2nd name", "2nd name longer than 1st name", "indented");
	public static List<String> names = List.of("First Name", "Surname");

	// text area output string
	public static String textAreaOutput;

	// operation cancelled
	String oc = "Operation cancelled";

	public static void loadFile(BinarySearchTree binarySearchTree, String filename, boolean surname) {

		try {
			Scanner scan = new Scanner(new File(filename));
			while (scan.hasNext()) {
				String firstName = scan.next().trim();
				String lastName = scan.nextLine().replace('\u00A0', ' ').trim();

				filePath = filename.toString();

				if (surname)
					binarySearchTree.insert(new Node(lastName, firstName));
				else
					binarySearchTree.insert(new Node(firstName, lastName));
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void saveFile(BinarySearchTree binarySearchTree, String fileLocation) {
		try {
			FileWriter csvWriter = new FileWriter(fileLocation);

			writeNode(binarySearchTree.root, csvWriter);

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeNode(Node node, FileWriter fw) {
		try {
			fw.append(node.fullName + "\n");
			if (node.before != null)
				writeNode(node.before, fw);
			if (node.after != null)
				writeNode(node.after, fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// load file to arraylist

	public ArrayList<String> filePathToArrayList(String filename) {
		ArrayList<String> output = new ArrayList<>();

		// load filepath to arraylist

		try {
			Scanner scan = new Scanner(new File(filename));
			while (scan.hasNext()) {
				String firstName = scan.next().trim();
				String lastName = scan.nextLine().replace('\u00A0', ' ').trim();
				output.add(firstName + " " + lastName);
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Collections.sort(output);

		return output;
	}

	// load ArrayList to BinarySearchTree
	public void listToTree(BinarySearchTree binarySearchTree, ArrayList<String> sortedList) {

		// divide list into two
		int middle = sortedList.size() / 2;
		List<String> firstHalf = sortedList.subList(0, middle);
		List<String> secondHalf = sortedList.subList(middle, sortedList.size());
		int size1 = firstHalf.size();
		int size2 = secondHalf.size();
		Collections.reverse(firstHalf);

		if (size1 > size2) {
			firstHalf.add(null);
		} else if (size2 > size1) {
			firstHalf.add(null);
		}

		halfListToTree(binarySearchTree, firstHalf, secondHalf, size1);

	}

	public void halfListToTree(BinarySearchTree binarySearchTree, List<String> firstHalf, List<String> secondHalf,
			int size) {
		for (int i = 0; i < size; i++) {
			String name1 = firstHalf.get(i);
			String name2 = secondHalf.get(i);

			if (name1 != null || name2 != null) {
				Node node1 = nameToNode(name1);
				Node node2 = nameToNode(name2);

				binarySearchTree.insert(node1);
				binarySearchTree.insert(node2);
			}
		}
	}

	// name to node

	public Node nameToNode(String fullName) {
		Node n = null;

		Scanner scan = new Scanner(fullName);
		while (scan.hasNext()) {
			String firstName = scan.next().trim();
			String lastName = scan.nextLine().replace('\u00A0', ' ').trim();
			n = new Node(firstName, lastName);
		}
		scan.close();

		return n;
	}

	// edit text area

	public void clearText() {
		textArea.clear();
		textAreaOutput = "";
		label.setText("");
	}

	public void enterText(String labelMessage) {
		textArea.setText(textAreaOutput);
		label.setText(labelMessage);
	}

	// parse string from textbox to int
	public int parseTextBoxString() {
		int userNum = 0;
		String userNumString = "";
		while (!userNumString.matches("[0-9]+")) {
			userNumString = EnterTextBox.display("Enter number", "Enter number (must contain interger)");
		}
		userNum = Integer.parseInt(userNumString);
		return userNum;
	}

	public void nodeToText(Node node, String input) {

		if (node != null)
			enterText(input + "[search item] found as " + node.toString() + "[node]");
		else
			enterText(input + " not found in tree");
	}

	@Override
	public void start(Stage arg0) throws Exception {

		loadFileButton.setOnAction(e -> {

			clearText();

			// select csv file with file chooser
			fileChooser.setTitle("Open CSV File");
			fileChooser.getExtensionFilters().add(extFilter);
			File file = fileChooser.showOpenDialog(window);

			// call loadFile() with csv file location and binarySearchTree
			if (file != null) {
				// clear tree
				binarySearchTree.clearTree();

				filePath = file.toString();
				loadFile(binarySearchTree, filePath, false);
				enterText(file.toString() + " loaded to Binary Search Tree");
			} else
				enterText("Load file cancelled");

		});

		saveTreeButton.setOnAction(e -> {

			clearText();

			// select file location
			fileChooser.setTitle("Save to csv file");
			fileChooser.getExtensionFilters().add(extFilter);
			File file = fileChooser.showSaveDialog(window);

			// save file to location through saveFile()
			if (!filePath.isBlank() && file != null) {
				filePath = file.toString();
				saveFile(binarySearchTree, filePath);
				enterText("Binary Search Tree saved to " + file.toString());
			} else
				enterText("Save file cancelled");
		});

		printNamesButton.setOnAction(e -> {

			// clear textArea
			clearText();

			// get user selection
			String firstNameOrSurname = ChoiceBoxWindow.display(names);

			System.out.println(firstNameOrSurname);

			if (firstNameOrSurname != null) {
				String selectedPrintOption = ChoiceBoxWindow.display(printOptions);

				// instantiateSurnameList
				if (firstNameOrSurname.equals("Surname"))
					binarySearchTree.instantiateSurnameList();

				// switch case depending
				if (!selectedPrintOption.trim().isBlank()) {

					switch (selectedPrintOption) {
					case "in-order depth-first":
						binarySearchTree.printInOrder();
						enterText("in-order depth-first");
						break;
					case "breadth-first":
						binarySearchTree.printBreadthFirst();
						enterText("breadth-first");
						break;
					case "pre-order depth-first":
						binarySearchTree.printPreOrder();
						enterText("pre-order depth-first");
						break;
					case "post-order depth-first":
						binarySearchTree.printPostOrder();
						enterText("post-order depth-first");
						break;
					case "name longer than user input":
						int userNum = 0;
						userNum = parseTextBoxString();
						binarySearchTree.printNameLongerThanUserInput(userNum);
						enterText("name longer than user input: " + userNum);
						break;
					case "name shorter than user input":
						int userShortNum = 0;
						userShortNum = parseTextBoxString();
						binarySearchTree.printNameShorterThanUserInput(userShortNum);
						enterText("name shorter than user input: " + userShortNum);
						break;
					case "1st name longer than 2nd name":
						binarySearchTree.printNameLongerThanSurname();
						enterText("1st name longer than 2nd name");
						break;
					case "2nd name longer than 1st name":
						binarySearchTree.printSurnameLongerThanName();
						enterText("2nd name longer than 1st name");
						break;
					case "indented":
						binarySearchTree.printIndent();
						enterText("indented");
						break;
					}

					// instantiate first names if surname
					if (firstNameOrSurname.equals("Surname"))
						binarySearchTree.reinstantiateFirstNameList();

				} else
					label.setText(oc);
			}
		});

		insertButton.setOnAction(e -> {

			clearText();

			// ask user for name
			String firstName = EnterTextBox.display("Enter first name", "Enter first name");
			if (!firstName.trim().isBlank()) {
				String surname = EnterTextBox.display("Enter surname", "Enter surname");
				// insert name in binarySearchTree if input is not blank
				if (!surname.trim().isBlank()) {
					binarySearchTree.insert(new Node(firstName, surname));

					// user feedback
					binarySearchTree.printInOrder();
					String labelMessage = firstName + " " + surname + " added to Binary Search Tree";
					enterText(labelMessage);
				} else
					label.setText(oc);
			} else
				label.setText(oc);
		});

		searchFirstNameButton.setOnAction(e -> {

			String firstName = EnterTextBox.display("Enter first name", "Enter first name");
			if (!firstName.isBlank()) {
				Node node = binarySearchTree.searchFirstName(firstName);

				nodeToText(node, firstName);
			} else
				enterText(oc);

		});

		searchSurnameButton.setOnAction(e -> {

			String surname = EnterTextBox.display("Enter surname", "Enter surname");

			if (!surname.isBlank()) {
				Node node = binarySearchTree.searchLastName(surname);

				nodeToText(node, surname);
			} else
				enterText(oc);

		});

		searchFullNameButton.setOnAction(e -> {
			String fullName = EnterTextBox.display("Enter name", "Enter name");
			if (!fullName.isBlank()) {
				Node node = binarySearchTree.searchFullName(fullName);

				nodeToText(node, fullName);
			} else
				enterText(oc);
		});

		deleteButton.setOnAction(e -> {

			clearText();

			String fullName = EnterTextBox.display("Enter full name", "Enter full name"); // get name from user
			
			// search for node
			Node node = binarySearchTree.searchFullName(fullName);

			if (!fullName.isBlank() && node != null) {
				binarySearchTree.delete(fullName);

				enterText(fullName + " deleted from Binary Search Tree"); // method to give user feedback through a
																			// TextArea
			}
			else if (node == null)
				enterText(fullName + " not found in Binary Search Tree");
			else
				enterText(oc);
		});

		changeNameButton.setOnAction(e -> {

			clearText();

			// ask user for full name
			String fullName = EnterTextBox.display("Enter full name", "Enter name to replace");

			try {
				if (!fullName.trim().isBlank()) {
					// ask user for replacement name
					String newName = EnterTextBox.display("Enter new full name", "Enter new name");
					if (!newName.trim().isBlank()) {
						Scanner scan = new Scanner(newName);
						String newFirstName = scan.next();
						String newLastName = scan.nextLine().trim();
						scan.close();
						
						binarySearchTree.delete(fullName);
						binarySearchTree.insert(new Node(newFirstName, newLastName));
						
						clearText();
						enterText(fullName + " changed to " + newName);
					}
				}
			} catch (Exception e1) {
				enterText(fullName + " not detected in Binary Search Tree");
			}
		});

		graphButton.setOnAction(e -> GraphicWindow.display(binarySearchTree));

		monitorSkewButton.setOnAction(e -> {
			clearText();

			binarySearchTree.monitorSkew();
			enterText("monitor skew");
		});

		balanceTreeButton.setOnAction(e -> {
			clearText();
			
			binarySearchTree.balanceTree(binarySearchTree.root);
			
			enterText("Tree balanced");
			// add filePath to arraylist and sorted
//			if (!filePath.isBlank()) {
//				binarySearchTree.clearTree();
//
//				ArrayList<String> sortedList = filePathToArrayList(filePath);
//
//				listToTree(binarySearchTree, sortedList);
//
//				enterText("Tree balanced");
//			} else
//				enterText("Please enter filepath");
		});

		// text area setup
		textArea.setEditable(false);
		textArea.setPrefSize(200, 200);
		textArea.setId("textarea-text");
		
		// title styling
		titleLabel.setId("title-text");
		label.setId("feedback-text");
		
		// gridpane
		
		GridPane layout = new GridPane();
		layout.setPadding(new Insets(10, 10, 10, 10));
		layout.setStyle("-fx-background-color: #FFFFFF;");
		layout.setVgap(5);
		layout.setHgap(5);
		layout.setAlignment(Pos.CENTER_LEFT);

		// arranging nodes for gridpane
		layout.add(titleLabel, 0, 0, 3, 1);
		
		layout.add(loadFileButton, 0, 1);
		layout.add(saveTreeButton, 1, 1);
		layout.add(printNamesButton, 2, 1);

		layout.add(insertButton, 0, 2);
		layout.add(deleteButton, 1, 2);
		layout.add(changeNameButton, 2, 2);

		layout.add(searchFirstNameButton, 0, 3);
		layout.add(searchSurnameButton, 1, 3);
		layout.add(searchFullNameButton, 2, 3);

		layout.add(monitorSkewButton, 0, 4);
		layout.add(balanceTreeButton, 1, 4);
		layout.add(graphButton, 2, 4);

		layout.add(label, 0, 5, 3, 2);

		layout.add(textArea, 0, 7, 3, 3);
		
		label2.setId("label2-text");
		layout.add(label2, 0, 11, 3, 2);

		Scene scene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
		scene.getStylesheets().add("style.css");
		window = arg0;
		window.setTitle("Binary Search Tree");
		window.setResizable(false);
		window.getIcons().add(new Image("file: icon.png"));
		window.setScene(scene);
		window.show();
	}

	public static void main(String[] args) {
		Application.launch();
	}

}
