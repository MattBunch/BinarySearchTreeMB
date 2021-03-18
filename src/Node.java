
public class Node {
	
	String name, name2, fullName;
	Node before, after;
	
	Node(String name, String surname){
		this.name = name;
		this.name2 = surname;
		this.fullName = this.name + " " + this.name2;
		before = null;
		after = null;
	}
	
	public String toString() {
		return this.fullName;
	}
	
}
