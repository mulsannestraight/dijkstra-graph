import java.nio.file.Files;
import java.util.Stack;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;

public class Graph {

  int size; // total vertices
  SLinkedList[] array;

  public static void main(String[] args) {
    Graph graph = new Graph("city.txt");
    graph.print();
    graph.shortestPath("a");
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
    System.out.println("Size of array: " + size);
    for (int i = 0; i < size; i++) {
      System.out.println("Size of LinkedList at index " + i + ": " + array[i].getSize());
    } 
    System.out.println("The smallest value is: " + findSmallestNode(array[5]).getValue());
  }
  public void shortestPath(String origin) {
    
  }
  public Node findSmallestNode(SLinkedList linkedList) {
    Node smallest = linkedList.getHead().getNext();
    Node current = linkedList.getHead().getNext();
    for (int i = 0; i < linkedList.getSize() - 1; i++) {
      if (smallest.getValue() > current.getValue()) {
        smallest = current;
      }
      current = current.getNext();
    }
    return smallest;
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
  public void print() {
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
