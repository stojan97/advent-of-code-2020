package aoc.day23;

import java.util.ArrayList;
import java.util.List;

import aoc.utils.FileReader;

public class Solution {

  private static Node FIRST_NODE;

  /**
   * MORAL OF THE STORY: Java sucks at linked lists.
   */
  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day23");

    String input = lines.get(0);
    long start = System.currentTimeMillis();
    String sequenceAfterOne = simulateHundredMoves(input);
    long productOfLabels = simulateTenMillionMoves(input);

    long end = System.currentTimeMillis();
    System.out.println("Time: " + (end - start) / 1000 + "seconds");
    System.out.println("Part 1: " + sequenceAfterOne);
    System.out.println("Part 2: " + productOfLabels);
  }

  private static String simulateHundredMoves(String cups) {

    int val = Integer.parseInt(cups.charAt(0) + "");
    Node prevNode = new Node(val, null, null);
    FIRST_NODE = prevNode;

    for (int i = 1; i < cups.length(); i++) {
      val = Integer.parseInt(cups.charAt(i) + "");
      Node newNode = new Node(val, prevNode, null);
      prevNode.next = newNode;
      prevNode = newNode;
    }

    Node node = simulateMoves(FIRST_NODE, 100, 10);

    StringBuilder res = new StringBuilder();

    while (true) {
      node = node.nextNode();
      if (node.value == 1) {
        break;
      }
      res.append(node.value);
    }

    return res.toString();
  }

  private static long simulateTenMillionMoves(String cups) {

    int val = Integer.parseInt(cups.charAt(0) + "");
    int maxCup = val;
    Node prevNode = new Node(val, null, null);
    FIRST_NODE = prevNode;

    for (int i = 1; i < cups.length(); i++) {
      val = Integer.parseInt(cups.charAt(i) + "");
      maxCup = Math.max(maxCup, val);
      Node newNode = new Node(val, prevNode, null);
      prevNode.next = newNode;
      prevNode = newNode;
    }

    for (int i = maxCup + 1; i <= 1000000; i++) {
      Node newNode = new Node(i, prevNode, null);
      prevNode.next = newNode;
      prevNode = newNode;
    }

    Node node = simulateMoves(FIRST_NODE, 10000000, 1000001);

    long product = 1L;
    node = node.nextNode();
    product *= node.value;
    node = node.nextNode();
    product *= node.value;

    return product;
  }

  private static Node simulateMoves(Node currentNode, int movesLimit, int size) {

    Node[] iterators = new Node[size];
    Node iteratingNode = currentNode;

    while (iteratingNode != null) {
      iterators[iteratingNode.value] = iteratingNode;
      iteratingNode = iteratingNode.next;
    }

    for (int move = 1; move <= movesLimit; move++) {

      int currentValue = currentNode.value;

      List<Integer> nextThreeElements = getNextThreeElements(currentNode, iterators);

      boolean isMax = false;
      int changingValue = currentValue - 1;

      int lowestValue = getMinValue(iterators);
      int maxValue = getMaxValue(iterators);
      Node destinationNode;

      while (true) {
        destinationNode = iterators[changingValue];

        if (changingValue < lowestValue) {
          isMax = true;
          break;
        }

        if (destinationNode == null) {
          changingValue--;
          continue;
        }

        if (changingValue == destinationNode.value) {
          break;
        }
      }

      if (isMax) {
        destinationNode = iterators[maxValue];
      }

      Integer first = nextThreeElements.get(0);
      Integer second = nextThreeElements.get(1);
      Integer third = nextThreeElements.get(2);

      Node nodeAfterDestinationNode = destinationNode.next;
      Node firstNode = new Node(first, destinationNode, null);
      destinationNode.next = firstNode;
      Node secondNode = new Node(second, firstNode, null);
      firstNode.next = secondNode;
      Node thirdNode = new Node(third, secondNode, nodeAfterDestinationNode);
      secondNode.next = thirdNode;

      if (nodeAfterDestinationNode != null) {
        nodeAfterDestinationNode.prev = thirdNode;
      }

      iterators[first] = firstNode;
      iterators[second] = secondNode;
      iterators[third] = thirdNode;

      currentNode = currentNode.nextNode();
    }

    return iterators[1];
  }

  private static Integer getMinValue(Node[] iterators) {

    for (int i = 1; i <= 4; i++) {
      if (iterators[i] != null) {
        return i;
      }
    }

    throw new RuntimeException("Should be one of the first four");
  }

  private static Integer getMaxValue(Node[] iterators) {

    for (int i = iterators.length - 1; i >= iterators.length - 4; i--) {
      if (iterators[i] != null) {
        return i;
      }
    }

    throw new RuntimeException("Should be one of the last four");
  }

  private static List<Integer> getNextThreeElements(Node current, Node[] iterators) {

    List<Integer> threeElements = new ArrayList<>();

    current = current.nextNode();
    int next = current.value;
    threeElements.add(next);
    current.remove();
    iterators[next] = null;

    current = current.nextNode();
    next = current.value;
    threeElements.add(next);
    current.remove();
    iterators[next] = null;

    current = current.nextNode();
    next = current.value;
    threeElements.add(next);
    current.remove();
    iterators[next] = null;

    return threeElements;
  }

  private static class Node {

    int value;
    Node prev;
    Node next;

    public Node(int value, Node prev, Node next) {

      this.value = value;
      this.prev = prev;
      this.next = next;
    }

    public Node nextNode() {
      if (next == null) {
        return FIRST_NODE;
      }
      return next;
    }

    public void remove() {

      if (prev == null) {
        FIRST_NODE = next;
        next.prev = null;
        return;
      }

      if (next != null) {
        prev.next = next;
        next.prev = prev;
      } else {
        prev.next = null;
      }
    }

  }

}
