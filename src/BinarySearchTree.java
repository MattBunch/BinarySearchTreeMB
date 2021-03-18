import java.util.Vector;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class BinarySearchTree {

	Node root;
	static int counter = 1;

	public BinarySearchTree() {
		root = null;
	}

	// instantiate surname list
	public boolean instantiateSurnameList() {
		if (!Main.filePath.isBlank()) {
			this.clearTree();
			Main.loadFile(this, Main.filePath, true);
			return true;
		} else
			return false;
	}

	// reinstantiate first name list
	public boolean reinstantiateFirstNameList() {
		if (!Main.filePath.isBlank()) {
			this.clearTree();
			Main.loadFile(this, Main.filePath, false);
			return true;
		} else
			return false;
	}
	
	public void instantiateSurnameListVector() {
		Vector<Node> nodes = new Vector<>();
		this.storeNodes(root, nodes);
		
		this.clearTree();
		
		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			this.insert(new Node(node.name2, node.name));
		}
	}


	// insert

	public void insert(Node newNode) {
		insert(root, newNode);
	}

	private Node insert(Node current, Node newNode) {
		// instantiate root node
		if (current == null) {
			root = newNode;
			return root;
		}

		// higher
		if (current.name.compareTo(newNode.name) > 0) {
			if (current.before == null)
				current.before = newNode;
			else
				insert(current.before, newNode);
		}
		// lower
		else {
			if (current.after == null)
				current.after = newNode;
			else
				insert(current.after, newNode);
		}

		return current;

	}

	// delete attempt #3

	public void delete(String toDelete) {
		root = delete(root, toDelete);
	}

	public Node delete(Node current, String toDelete) {
		if (current == null)
			return null;

		else if (current.fullName.compareTo(toDelete) > 0) {
			System.out.println(current.fullName);
			current.before = delete(current.before, toDelete);
		}

		else if (current.fullName.compareTo(toDelete) < 0) {
			System.out.println(current.fullName);
			current.after = delete(current.after, toDelete);
		}

		else {
			if (current.before == null)
				return current.after;
			else if (current.after == null)
				return current.before;

			{
				// get data from rightmost node in left subtree
				current.fullName = retrieveName(current.before);

				// delete the rightmost node in tree
				current.before = delete(current.before, current.fullName);
			}
		}
		return current;
	}

	public String retrieveName(Node current) {
		while (current.after != null)
			current = current.after;

		return current.fullName;
	}

	// search

	public Node searchFullName(String key) {
		return searchFullName(this.root, key);
	}

	public Node searchFullName(Node root, String key) {
		if (root == null || root.fullName.equals(key))
			return root;

		if (root.fullName.compareTo(key) > 0)
			return searchFullName(root.before, key);

		return searchFullName(root.after, key);
	}

	public Node searchFirstName(String key) {
		return searchFirstName(this.root, key);
	}

	public Node searchFirstName(Node root, String key) {
		if (root == null || root.name.equals(key)) {
			System.out.println(root.name);
			return root;
		}

		if (root.fullName.compareTo(key) > 0)
			return searchFirstName(root.before, key);

		return searchFirstName(root.after, key);
	}

	public Node searchLastName(String key) {
		return searchLastName(this.root, key);
	}

	public Node searchLastName(Node root, String key) {
		if (root == null || root.name2.equals(key))
			return root;

		if (root.fullName.compareTo(key) > 0)
			return searchLastName(root.before, key);

		return searchLastName(root.after, key);
	}

	// print all
	// (In-order depth-first)
	public void printInOrder() {
		printInOrder(root);
	}

	public void printInOrder(Node current) {

		if (current != null) {
			printInOrder(current.before);
			System.out.println((current.fullName));
			Main.textAreaOutput += current.fullName + "\n";
			printInOrder(current.after);
		}

	}

	// (breadth first)

	public void printBreadthFirst() {
		// calculate height of root
		int h = height(root);
		// loop through
		for (int i = 1; i <= h; i++)
			printBreadthFirst(root, i);
	}

	public void printBreadthFirst(Node current, int level) {
		// if tree is null then return;
		if (current == null)
			return;

		// if level is 1 then print
		if (level == 1)
			Main.textAreaOutput += current.fullName + "\n";

		else if (level > 1) {
			printBreadthFirst(current.before, level - 1);
			printBreadthFirst(current.after, level - 1);
		}
	}

	// get height of tree

	public int height() {
		return height(this.root);
	}

	public int height(Node root) {
		if (root == null)
			return 0;
		else {
			int beforeHeight = height(root.before);
			int afterHeight = height(root.after);

			if (beforeHeight > afterHeight)
				return beforeHeight + 1;
			else
				return afterHeight + 1;
		}
	}

	// get width of tree

	public int width() {
		int max = 0;

		for (int i = 0; i <= this.height(); i++) {
			int temp = width(this.root, i);
			if (temp > max)
				max = temp;
		}

		return max;
	}

	public int width(Node current, int level) {
		if (current == null)
			return 0;

		if (level == 1)
			return 1;
		else if (level > 1)
			return width(current.before, level - 1) + width(current.after, level - 1);
		return 0;
	}

	// get diameter of tree

	public int diameter(Node root) {
		return 0;
	}

	//

	// pre order

	public void printPreOrder() {
		printPreOrder(root);
	}

	public void printPreOrder(Node current) {

		if (current != null) {
			System.out.println((current.fullName));
			Main.textAreaOutput += current.fullName + "\n";
			printPreOrder(current.before);
			printPreOrder(current.after);
		}

	}

	// post order

	public void printPostOrder() {
		printPostOrder(root);
	}

	public void printPostOrder(Node current) {

		if (current != null) {
			System.out.println((current.fullName));
			Main.textAreaOutput += current.fullName + "\n";
			printPostOrder(current.before);
			printPostOrder(current.after);
		}

	}

	// print name longer than user input

	public void printNameLongerThanUserInput(int num) {
		printNameUserInput(this.root, num, true);
	}

	public void printNameShorterThanUserInput(int num) {
		printNameUserInput(this.root, num, false);
	}

	public void printNameUserInput(Node current, int num, boolean longer) {

		if (current != null) {
			System.out.println((current.name));
			printNameUserInput(current.before, num, longer);
			if (longer) {
				if (current.name.length() > num) {
					Main.textAreaOutput += current.name + "\n";
				}
			} else {
				if (current.name.length() < num) {
					Main.textAreaOutput += current.name + "\n";
				}
			}
			printNameUserInput(current.after, num, longer);
		}
	}

	// first name longer than surname

	public void printNameLongerThanSurname() {

		printNameLongerThanName(root, false);

	}

	// surname longer than first name

	public void printSurnameLongerThanName() {

		printNameLongerThanName(root, true);

	}

	public void printNameLongerThanName(Node current, boolean surname) {

		if (current != null) {
			printNameLongerThanName(current.before, surname);
			if (surname) {
				if (current.name2.length() > current.name.length()) {
					Main.textAreaOutput += current.fullName + "\n";
				}
			} else {
				if (current.name.length() > current.name2.length()) {
					Main.textAreaOutput += current.fullName + "\n";
				}
			}
			printNameLongerThanName(current.after, surname);
		}
	}

	// print indented

	public void printIndent() {
		printIndent(root, "");
	}

	public void printIndent(Node current, String indent) {
		if (current == null)
			return;
		System.out.println(indent + current.fullName);
		Main.textAreaOutput += indent + current.fullName + "\n";
		printIndent(current.before, indent + "	");
		printIndent(current.after, indent + "	");
	}

	// monitor skew

	public void monitorSkew() {
		if (root != null && root.before != null && root.after != null) {

			counter = 1;

			int beforeNum = monitorSkew(root.before);

			counter = 1;

			int afterNum = monitorSkew(root.after);

			String report = "Left tree from root = " + beforeNum + "\nRight tree from root = " + afterNum;

			if (beforeNum == afterNum)
				Main.textAreaOutput += report + "\nTree is balanced";
			else if (beforeNum > afterNum)
				Main.textAreaOutput += report + "\nSkewed in left tree direction";
			else
				Main.textAreaOutput += report + "\nSkewed in right tree direction";

			Main.textAreaOutput += "\nHeight of tree: " + this.height() + "\nWidth of tree: " + this.width();

		} else if (root.before == null) {
			counter = 1;

			int afterNum = monitorSkew(root.after);

			String report = "Left node empty\nRight tree from root = " + afterNum;

			Main.textAreaOutput = report;
		} else if (root.after == null) {
			counter = 1;

			int beforeNum = monitorSkew(root.before);

			String report = "Left tree from root = " + beforeNum + "\nRight node empty";

			Main.textAreaOutput = report;
		}
		// only consists of root node
		else {
			Main.textAreaOutput = "Tree only consists of root node";
		}
	}

	public int monitorSkew(Node current) {
		// loop through entire root's children
		if (current != null) {

			// add 1 to counter in each tree
			counter++;

			monitorSkew(current.before);
			monitorSkew(current.after);
		}

		return counter;
	}

	// TODO: balance tree function
	public Node balanceTree(Node root) {

		Vector<Node> nodes = new Vector<>();
		storeNodes(root, nodes);
	
		return buildTree(nodes, 0, nodes.size() - 1);
	}

	// store nodes in vector of nodes
	public void storeNodes(Node root, Vector<Node> nodes) {
		if (root == null)
			return;

		storeNodes(root.before, nodes);
		nodes.add(root);
		storeNodes(root.after, nodes);
	}

	// build tree from vector of nodes
	public Node buildTree(Vector<Node> nodes, int start, int end) {
		
		if (start > end)
			return null;

		int mid = (end - start) / 2 + start;
		Node node = nodes.get(mid);

		node.before = buildTree(nodes, start, mid - 1);
		node.after = buildTree(nodes, mid + 1, end);

		// for debugging
		
		System.out.println("Node : " + node.toString());
		
		if (node.before != null)
			System.out.println("before node for " + node.toString() + ": " + node.before.toString());
		else
			System.out.println("no before node for " + node.toString());
		if (node.after != null)
			System.out.println("after node for " + node.toString() + ": " + node.after.toString());
		else
			System.out.println("no after node for " + node.toString());
		
		System.out.println("###");

		return node;
	}

	// graph

	public void drawGraph(GraphicsContext gc) {
		double canvasWidth = gc.getCanvas().getWidth();

		double startingX = (canvasWidth / 2);
		double startingY = 12;

		gc.setTextAlign(TextAlignment.CENTER);

		drawGraph(root, gc, startingX, startingY, startingX, startingY);
	}

	public void drawGraph(Node current, GraphicsContext gc, double currentX, double currentY, double oldX,
			double oldY) {

		// if end of node
		if (current == null)
			return;

		gc.setFill(Color.color(Math.random(), Math.random(), Math.random()));
		gc.strokeText(current.fullName, currentX, currentY);
		double newXBefore = currentX - 100;
		double newXAfter = currentX + 100;
		double newY = currentY + 30;
		gc.strokeLine(currentX, currentY, oldX, oldY);
		drawGraph(current.before, gc, newXBefore, newY, currentX, currentY);
		drawGraph(current.after, gc, newXAfter, newY, currentX, currentY);

	}

	// clear tree

	public void clearTree() {
		this.root = null;
	}

	// clone tree

	public BinarySearchTree cloneTree() {
		BinarySearchTree clone = new BinarySearchTree();

		clone.root = this.root;

		clone.printInOrder();

		return clone;
	}

}
