import java.nio.file.Files;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;

public class Graph {
public static final int INT_MAX	= 50;
  public static void main(String[] args) {
    Graph graph = new Graph("city.txt");
    graph.print();
  }

  int size; // total vertices
  SLinkedList[] array;

  public Graph(int size, SLinkedList[] array) {
    this.size = size;
    this.array = array;
  }
  public Graph(String fileName) {
    String string = readFile(fileName);
    string = cleanString(string);
    
    // get size of the SLinkedList array
    size = 1;
    char temp = string.charAt(0);
    for (int i = 3; i < string.length(); i = i + 3) {
      if (temp != string.charAt(i)) size++;
      temp = string.charAt(i);
    }
    
    array = new SLinkedList[size];
    array[0] = new SLinkedList(stringFromChar(string.charAt(0)), -1);
    SLinkedList first = new SLinkedList(stringFromChar(string.charAt(1)), intFromChar(string.charAt(2)));
    array[0].setNext(first);

    // putting each element in respective SLinkedList[] index
    int currentIndex = 1;
    for (int i = 3; i < string.length(); i = i + 3) {
      int tempIndex = findLinkedListIndex(string.charAt(i));
      // no linked list is created for this char yet
      if (tempIndex == -1) {
        array[currentIndex] = new SLinkedList(stringFromChar(string.charAt(i)), -1);
        SLinkedList s = new SLinkedList(stringFromChar(string.charAt(i+1)), intFromChar(string.charAt(i+2)));
        array[currentIndex].setNext(s);
        currentIndex++;
      } else {
        SLinkedList s = new SLinkedList(stringFromChar(string.charAt(i+1)), intFromChar(string.charAt(i+2)));
        SLinkedList tempLL = array[tempIndex];

        while (tempLL.next != null) {
          tempLL = tempLL.next;
        }
        tempLL.setNext(s);
      }
    }
  }
  private SLinkedList shortnode(SLinkedList checkme){ //finds shortest unvisited node or returns null
  	//we need to compare our current path length (to a given node) and update if this node has shorter pathlength + edge
  	SLinkedList a = checkme;
  	SLinkedList	b = a;
  	if(a.getNext()!=null){
  			while(a.getNext()!=null){ 
    		a = a.getNext();
    				if(!visited[findLinkedListIndex(a.getName())] && (a.getValue() + currentPathWeight <= pathlength[findLinkedListIndex(a.getName())]){ //checks for unvisited/shortest
    					pathlength[findLinkedListIndex(a.getName()] = currentPathWeight + a.getValue(); //updates array
    					b = a;
    				}

  				}
  	}
  	if(b == a){
  		return null;
  	}
  	return b;
	}
  public void shortestPath() {
    int temp;
    int pathlength[];
    int currentPathWeight;
    boolean[] visited new Boolean[array.size];
    Stack stack = new Stack();
    LinkedList first = array[0]; //sets starting node as first assigned vertex
    stack.push(first);
    
    	do{
    		current = stack.peek();
    		if(shortnode(current)!=null){ 
    				stack.push(shortnode); //pushes node with shortest edge onto stack
    				visited[findLinkedListIndex(shortnode.getName())] = true;
            currentPathWeight = currentPathWeight + stack.peek().getValue(); //updates total length of stack
    		}
    		else{
    				
    			currentPathWeight = currentPathWeight - stack.pop().getValue(); //updates total length of stack
          //***WIP*** Store the path/destination to print
    		}
    				
    			}while(stack!=stack.empty())
  		}
  }
  public String readFile(String filename) {
    File f = new File(filename);
    try {
      byte[] bytes = Files.readAllBytes(f.toPath());
      return new String(bytes, "UTF-8");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }
  public String cleanString(String string) {
    string = string.replaceAll("\\s", "");
    return string;
  }
  public String stringFromChar(char c) {
    return Character.toString(c);
  }
  public int intFromChar(char c) {
    return Character.getNumericValue(c);
  }
  // Find out what character belongs to what index in the array
  public int findLinkedListIndex(char c) {
    for (int i = 0; i < size; i++) {
      if (array[i] != null) {
        if (array[i].getName().equals(stringFromChar(c)))
          return i;
      }
    }
    return -1;
  }
  
  /*
    ---------- SLinkedList Class ----------
  */
  public static class SLinkedList {
    String name;
    int value;
    SLinkedList next;

    public SLinkedList(String name, int value, SLinkedList next) {
      this.name = name;
      this.value = value;
      this.next = next;
    }

    public SLinkedList(String name, int value) {
      this.name = name;
      this.value = value;
      this.next = null;
    }

    public String getName() {
      return name;
    }

    public int getValue() {
      return value;
    }

    public SLinkedList getNext() {
      return next;
    }

    public void setNext(SLinkedList next) {
      this.next = next;
    }

  }
  public void print() {
    for (int i = 0; i < size; i++) {
      SLinkedList current = array[i];
      System.out.print(i + " || ");
      while (current != null) {
        System.out.print("(" + current.getName() + ", " + current.getValue() + ") --> ");
        current = current.next;
      }
      if (current == null) {
        System.out.println("null");
      }
    }
  }
}
