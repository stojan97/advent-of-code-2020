package aoc.day22;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    List<List<String>> groups = FileReader.readLinesByGroups("day22");
    List<Integer> firstPlayer = parsePlayerDeck(groups.get(0));
    List<Integer> secondPlayer = parsePlayerDeck(groups.get(1));
    ArrayList<Integer> firstPlayerCopy = new ArrayList<>(firstPlayer);
    ArrayList<Integer> secondPlayerCopy = new ArrayList<>(secondPlayer);

    long winningPlayerScoreForRegularCombat = playRegularCombat(firstPlayer, secondPlayer);
    long winningPlayerScoreForRecurseCombat = playRecurseCombat(firstPlayerCopy, secondPlayerCopy, true).winningScore;

    System.out.println("Part 1: " + winningPlayerScoreForRegularCombat);
    System.out.println("Part 2: " + winningPlayerScoreForRecurseCombat);
  }

  private static long playRegularCombat(List<Integer> firstPlayer, List<Integer> secondPlayer) {

    while (!firstPlayer.isEmpty() && !secondPlayer.isEmpty()) {
      int firstPlayerTop = firstPlayer.remove(0);
      int secondPlayerTop = secondPlayer.remove(0);

      if (firstPlayerTop > secondPlayerTop) {
        firstPlayer.add(firstPlayerTop);
        firstPlayer.add(secondPlayerTop);
      } else {
        secondPlayer.add(secondPlayerTop);
        secondPlayer.add(firstPlayerTop);
      }
    }

    List<Integer> winningPlayerDeck = (firstPlayer.isEmpty()) ? secondPlayer : firstPlayer;

    long winningDeckScore = getWinningDeckScore(winningPlayerDeck);
    return winningDeckScore;
  }

  private static RecurseCombatResult playRecurseCombat(
    List<Integer> firstPlayer,
    List<Integer> secondPlayer,
    boolean isInit) {

    Set<String> seenFirst = new HashSet<>();
    Set<String> seenSecond = new HashSet<>();

    while (!firstPlayer.isEmpty() && !secondPlayer.isEmpty()) {

      if (seenFirst.contains(firstPlayer.toString()) || seenSecond.contains(secondPlayer.toString())) {
        long winningDeckScore = 0;
        if (isInit) {
          winningDeckScore = getWinningDeckScore(firstPlayer);
        }
        return new RecurseCombatResult(1, winningDeckScore);
      }

      seenFirst.add(firstPlayer.toString());
      seenSecond.add(secondPlayer.toString());

      int firstPlayerTop = firstPlayer.remove(0);
      int secondPlayerTop = secondPlayer.remove(0);

      RecurseCombatResult recurseCombatResult = new RecurseCombatResult(0, 0);

      if (firstPlayer.size() >= firstPlayerTop && secondPlayer.size() >= secondPlayerTop) {
        List<Integer> firstPlayerSubList = new ArrayList<>(firstPlayer.subList(0, firstPlayerTop));
        List<Integer> secondPlayerSubList = new ArrayList<>(secondPlayer.subList(0, secondPlayerTop));
        recurseCombatResult = playRecurseCombat(firstPlayerSubList, secondPlayerSubList, false);
      }

      if (recurseCombatResult.winner == 1) {
        firstPlayer.add(firstPlayerTop);
        firstPlayer.add(secondPlayerTop);
      } else if (recurseCombatResult.winner == 2) {
        secondPlayer.add(secondPlayerTop);
        secondPlayer.add(firstPlayerTop);
      } else {
        if (firstPlayerTop > secondPlayerTop) {
          firstPlayer.add(firstPlayerTop);
          firstPlayer.add(secondPlayerTop);
        } else {
          secondPlayer.add(secondPlayerTop);
          secondPlayer.add(firstPlayerTop);
        }
      }
    }

    List<Integer> winningPlayerDeck;
    int winningPlayer;
    if (firstPlayer.isEmpty()) {
      winningPlayer = 2;
      winningPlayerDeck = secondPlayer;
    } else {
      winningPlayer = 1;
      winningPlayerDeck = firstPlayer;
    }

    long winningDeckScore = 0;
    if (isInit) {
      winningDeckScore = getWinningDeckScore(winningPlayerDeck);
    }

    return new RecurseCombatResult(winningPlayer, winningDeckScore);
  }


  private static long getWinningDeckScore(List<Integer> winningPlayerDeck) {

    long score = 0;
    int size = winningPlayerDeck.size();

    for (Integer integer : winningPlayerDeck) {
      score += integer * size--;
    }

    return score;
  }

  private static List<Integer> parsePlayerDeck(List<String> player) {

    List<Integer> deck = new ArrayList<>();

    for (int i = 1; i < player.size(); i++) {
      deck.add(Integer.parseInt(player.get(i)));
    }

    return deck;
  }

  private static class RecurseCombatResult {

    int winner;
    long winningScore;

    public RecurseCombatResult(int winner, long winningScore) {

      this.winner = winner;
      this.winningScore = winningScore;
    }
  }

}
