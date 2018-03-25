import java.nio.file.Files;
import java.util.Arrays;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.*;

public class Graph {
  int size; 
  SLinkedList[] array;

  public static void main(String[] args) {
    Graph graph = new Graph("city.txt");
    graph.printGraph();
    graph.shortestPath("a");
    //System.out.println(graph.getVertices());
  }
  public Graph(int size, SLinkedList[] array) {
    this.size = size;
    this.array = array;
  }
  public Graph(String fileName) {
    ArrayList<String> list = stringList(fileName);
    ArrayList<String> nodeList = new ArrayList<String>();

    nodeList.add(list.get(0));
    // By the end of loop, nodeList will contain all elements at indices that are multiples of 3 from 'list'
    for (int i = 3; i < list.size(); i++) {
      if (i % 3 == 0) {
        nodeList.add(list.get(i));
      }
    }
    // convert a list into a set will get rid of duplicate elements
    // then convert the set back to list
    Set<String> set = new HashSet<String>(nodeList);
    nodeList = new ArrayList<String>(new HashSet<String>(nodeList));
    
    this.size = nodeList.size(); 

    array = new SLinkedList[size];
    for (int i = 0; i < size; i++) {
      array[i] = new SLinkedList();
    }

    // putting each element in respective SLinkedList[] index
    int currentIndex = 0;
    for (int i = 0; i < list.size(); i = i + 3) {
      int tempIndex = findLinkedListIndex(list.get(i));
      if (tempIndex == -1) {
        Node newNode = new Node(list.get(i), 0);
        array[currentIndex] = new SLinkedList(newNode);
        array[currentIndex].insert(list.get(i + 1), Integer.parseInt(list.get(i + 2)));
        currentIndex++;
      } else {
        array[tempIndex].insert(list.get(i+1), Integer.parseInt(list.get(i+2)));
      }
    }
    System.out.println("LINKED LIST SIZE: " + array.length);
    for (int i = 0; i < array.length; i++) {
      System.out.println(array[i].getSize());
    }
  }
  public ArrayList<String> stringList(String fileName) {
    String string = readFile(fileName);
    ArrayList<String> list = new ArrayList<String>();
    for (int i = 0; i < string.length(); i++) {
      list.add(i, "");
    }

    char temp = '\u0000';
    int index = 0;
    for (int i = 0; i < string.length(); i++) {
      temp = string.charAt(i);
      if (Character.isLetter(temp)) {
        list.set(index, Character.toString(temp));
        index++;
      } else if (Character.isDigit(temp)) {
        String s = list.get(index) + Character.toString(temp);
        list.set(index, s);
      } else if (temp == '\n') {
        index++;
      }
    }
    for (int i = string.length() - 1; i >= 0; i--) {
      if (list.get(i).equals("")) {
        list.remove(i);
      }
    }
    return list;
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

    int smallestIndex = findSmallestUnvisitedMagic(candidates);
    updateNeighborsOfNode(candidates[smallestIndex].getName(), candidates);

    int smallestIndex2 = findSmallestUnvisitedMagic(candidates);
    updateNeighborsOfNode(candidates[smallestIndex2].getName(), candidates);

    int smallestIndex3 = findSmallestUnvisitedMagic(candidates);
    updateNeighborsOfNode(candidates[smallestIndex3].getName(), candidates);

    int smallestIndex4 = findSmallestUnvisitedMagic(candidates);
    updateNeighborsOfNode(candidates[smallestIndex4].getName(), candidates);

    int smallestIndex5 = findSmallestUnvisitedMagic(candidates);
    updateNeighborsOfNode(candidates[smallestIndex5].getName(), candidates);
    printMagicArray(candidates);
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
      candidates[findMagicIndex(startPoint, candidates)].setVisited(true);
      return null;
    }
    Magic start = candidates[findMagicIndex(startPoint, candidates)];
    SLinkedList linkedList = array[index];
    Node startNode = linkedList.getHead().getNext();
    int j;

    for (int i = 0; i < linkedList.size - 1; i++) {
      j = findMagicIndex(startNode.getName(), candidates);
      if (candidates[j].getWeight() > (startNode.getValue() + start.getWeight()) && !candidates[j].getVisited()) {
        candidates[j].setWeight(startNode.getValue() + start.getWeight());
        candidates[j].setPrevious(startPoint);
      }
      startNode = startNode.getNext();
    }
    candidates[findMagicIndex(startPoint, candidates)].setVisited(true);
    return startNode;
  }
  public void printMagicArray(Magic[] candidates) {
    for (int i = 0; i < candidates.length; i++) {
      System.out.print("Index " + i + ": (" + candidates[i].getName() + ", " + candidates[i].getWeight() + ")");
      System.out.print(" -- (Visited: " + candidates[i].getVisited() + ")");
      System.out.print(" -- (Previous: \"" + candidates[i].getPrevious() + "\")\n");
    }
  }
  public String getVertices() {
    return "";
  }
  public int findSmallestUnvisitedMagic(Magic[] magic) {
    int smallTemp = -1;
    Magic smallestMagic = null;
    for (int i = 0; i < magic.length; i++) {
      if (magic[i].getWeight() != Integer.MAX_VALUE && !magic[i].getVisited()) {
        smallTemp = i;
        break;
      }
    }
    if (smallTemp == -1) {
      return smallTemp;
    }
    smallestMagic = magic[smallTemp];
    for (int i = 0; i < magic.length; i++) {
      if (smallestMagic.getWeight() > magic[i].getWeight() && !magic[i].getVisited()) {
        smallTemp = i;
      }
    }
    return smallTemp;
  }
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
  // Find out what character belongs to what index in the array
  public int findLinkedListIndex(String c) {
    Node current;
    for (int i = 0; i < size; i++) {
      current = array[i].getHead();
      if (current != null) {
        if (current.getName().equals(c))
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
