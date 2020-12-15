package aoc.day13;

import java.util.ArrayList;
import java.util.List;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day13");
    int firstTimestamp = Integer.parseInt(lines.get(0));
    List<Bus> buses = parseInput(lines.get(1));

    int earliestBusMultiplied = getEarliestBusMultiplied(firstTimestamp, buses);
    long earliestTimestamp = getEarliestTimestamp(buses);
    System.out.println("Part 1: " + earliestBusMultiplied);
    System.out.println("Part 2: " + earliestTimestamp);
  }

  private static List<Bus> parseInput(String s) {

    String[] busIdsArray = s.split(",");
    List<Bus> busIds = new ArrayList<>();

    for (int i = 0; i < busIdsArray.length; i++) {
      String bus = busIdsArray[i];
      if (!bus.equals("x")) {
        busIds.add(new Bus(Integer.parseInt(bus), i));
      }
    }

    return busIds;
  }

  private static int getEarliestBusMultiplied(int firstTimestamp, List<Bus> buses) {

    int minBusId = Integer.MAX_VALUE;
    int minMinutes = Integer.MAX_VALUE;

    for (Bus bus : buses) {

      int rem = firstTimestamp % bus.busId;
      int minutes = (rem == 0) ? 0 : bus.busId - rem;

      if (minutes < minMinutes) {
        minMinutes = minutes;
        minBusId = bus.busId;
      }
    }

    return minBusId * minMinutes;
  }

  private static long getEarliestTimestamp(List<Bus> buses) {

    int index = 0;
    long timestamp = buses.get(0).busId;
    long increment = timestamp;

    while (index != buses.size() - 1) {

      Bus currentBus = buses.get(index);
      Bus nextBus = buses.get(index + 1);
      long nextTimestamp = timestamp + (nextBus.index - currentBus.index);

      if (nextTimestamp % nextBus.busId == 0) {
        index++;
        increment = lcm(increment, nextBus.busId);
        timestamp = nextTimestamp;
        continue;
      }

      timestamp += increment;
    }

    Bus lastBus = buses.get(index);
    return timestamp - lastBus.index;
  }

  private static long gcd(long currentBusId, long nextBusId) {

    if (nextBusId == 0) {
      return currentBusId;
    }

    return gcd(nextBusId, currentBusId % nextBusId);
  }

  private static long lcm(long currentBusId, long nextBusId) {

    return (currentBusId * nextBusId) / gcd(currentBusId, nextBusId);
  }

  private static class Bus {

    int busId;
    int index;

    public Bus(int busId, int index) {

      this.busId = busId;
      this.index = index;
    }
  }
}
