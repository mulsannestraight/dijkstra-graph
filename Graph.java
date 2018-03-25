import java.nio.file.Files;
import java.util.Arrays;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;

public class Graph {

  int size; 
  SLinkedList[] array;

  public static void main(String[] args) {
    Graph graph = new Graph("city.txt");
    //graph.printGraph();
    graph.shortestPath("a");
    //System.out.println(graph.getVertices());
  }
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
    for (int i = 0; i < size; i++) {
      array[i] = new SLinkedList();
    }
    // putting each element in respective SLinkedList[] index
    int currentIndex = 0;
    for (int i = 0; i < string.length(); i = i + 3) {
      int tempIndex = findLinkedListIndex(string.charAt(i));
      // no linked list is created for this char yet
      if (tempIndex == -1) {
        Node newNode = new Node(stringFromChar(string.charAt(i)), 0);
        array[currentIndex] = new SLinkedList(newNode);
        array[currentIndex].insert(stringFromChar(string.charAt(i + 1)), intFromChar(string.charAt(i + 2)));
        currentIndex++;
      } else {
        array[tempIndex].insert(stringFromChar(string.charAt(i + 1)), intFromChar(string.charAt(i + 2)));
      }
    }
  }
  public void shortestPath(String origin) {
    Magic m1 = new Magic(array[0].getHead().getName(), false, "");
    Magic m2 = new Magic(array[1].getHead().getName(), false, "");
    Magic m3 = new Magic(array[2].getHead().getName(), false, "");
    Magic m4 = new Magic(array[3].getHead().getName(), false, "");
    Magic m5 = new Magic(array[4].getHead().getName(), false, "");
    Magic m6 = new Magic(array[5].getHead().getName(), false, "");
    Magic m7 = new Magic(array[1].getHead().getNext().getNext().getName(), false, "");
    Magic m8 = new Magic(array[4].getHead().getNext().getNext().getName(), false, "");

    Magic[] candidates = {m1, m2, m3, m4, m5, m6, m7, m8};
    int index = findMagicIndex(origin, candidates);
    candidates[index].setWeight(0);
    updateNeighborsOfNode(origin, candidates);
    for (int i = 0; i < candidates.length; i++) {
      System.out.print("Index " + i + ": (" + candidates[i].getName() + ", " + candidates[i].getWeight() + ")");
      System.out.print(" -- (Visited: " + candidates[i].getVisited() + ")");
      System.out.print(" -- (Previous: \"" + candidates[i].getPrevious() + "\")\n");
    }
  }
  public int findMagicIndex(String name, Magic[] array) {
    int j = -1;
    for (int i = 0; i < array.length; i++) {
      if (name.equals(array[i].getName())) {
        j = i;
        break;
      } 
    }
    return j;
  }
  public Object updateNeighborsOfNode(String startPoint, Magic[] candidates) {
    int index = findLinkedListIndex(startPoint.charAt(0));
    if (index == -1) {
      return null;
    }
    SLinkedList linkedList = array[index];
    Node startNode = linkedList.getHead().getNext();
    int j;

    for (int i = 0; i < linkedList.size - 1; i++) {
      j = findMagicIndex(startNode.getName(), candidates);
      if (candidates[j].getWeight() > startNode.getValue() && !candidates[j].getVisited()) {
        candidates[j].setWeight(startNode.getValue());
        candidates[j].setPrevious(startPoint);
      }
      startNode = startNode.getNext();
    }
    candidates[findMagicIndex(startPoint, candidates)].setVisited(true);
    return startNode;
  }
  public String getVertices() {
    return "";
  }
  /*public Magic findSmallestUnvisitedMagic(Magic[] magic) {
    Magic smallTemp = null;
    for (int i = 0; i < magic.length; i++) {
      if (magic[i].getWeight() != Integer.MAX_VALUE && !magic[i].getVisited()) {
        smallTemp = magic[i];
        break;
      }
    }
    for (int i = 1; i < magic.length; i++) {
      if (smallTemp.getShortestPath() > magic[i].getShortestPath() && !magic[i].getVisited()) {
        smallTemp = magic[i];
      }
    }
    return smallTemp;
  } */
  // Magic Class
  public static class Magic {
    String name;
    boolean visited;
    int weight;
    String previous;
    public Magic(String name, boolean visited, String previous) {
      this.name = name;
      this.visited = visited;
      this.previous = previous;
      this.weight = Integer.MAX_VALUE;
    }
    public void setVisited(boolean visited) {
      this.visited = visited;
    }
    public void setWeight(int weight) {
      this.weight = weight;
    }
    public void setPrevious(String previous) {
      this.previous = previous;
    }
    public boolean getVisited() {
      return this.visited;
    }
    public int getWeight() {
      return this.weight;
    }
    public String getPrevious() {
      return this.previous;
    }
    public String getName() {
      return this.name;
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
    Node current;
    for (int i = 0; i < size; i++) {
      current = array[i].getHead();
      if (current != null) {
        if (current.getName().equals(stringFromChar(c)))
          return i;
      }
    }
    return -1;
  }
  public void printGraph() {
    for (int i = 0; i < size; i++) {
      SLinkedList current = array[i];
      Node temp = current.getHead();
      System.out.print("Index " + i + ":  ");
      for (int j = 0; j < current.getSize(); j++) {
        System.out.print("(" + temp.getName() + ", " + temp.getValue() + ") --> ");
        temp = temp.getNext();
      }
      System.out.println(temp);
    }
  }

  /*
    ---------- SLinkedList Class ----------
  */
  public static class SLinkedList {
    int size = 0;
    Node head;
    
    // Intended for initial SLinkedList creation ONLY
    public SLinkedList(Node head) {
      this.head = head;
      this.size++;
    }
    public SLinkedList() {
      this.head = null;
    }
    public int getSize() {
      return size;
    }
    public Node getHead() {
      return head;
    }
    public void setHead(Node head) {
      this.head = head;
    }
    public void insert(String name, int value) {
      Node newNode = new Node(name, value);
      if (size == 0) {
        head = newNode;
        size++;
      } else {
        Node current = head;
        for (int i = 0; i < size - 1; i++) {
          current = current.getNext();
        }
        current.setNext(newNode);
        size++;
      }
    }
    public void insert(Node node) {
      if (size == 0) {
        head = node;
        size++;
      } else {
        Node current = head;
        for (int i = 0; i < size - 1; i++) {
          current = current.getNext();
        }
        current.setNext(node);
        size++;
      }
    }
  }

  /*
    Node Class to be used in SLinkedList Class
  */
  public static class Node {
    String name;
    int value;
    Node next;

    public Node(String name, int value) {
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
    public Node getNext() {
      return next;
    }
    public void setNext(Node next) {
      this.next = next;
    }
  }
}
