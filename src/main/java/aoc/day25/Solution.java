package aoc.day25;

import java.util.List;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day25");
    long cardPublicKey = Long.parseLong(lines.get(0));
    long doorPublicKey = Long.parseLong(lines.get(1));

    long encryptionKey = getEncryptionKey(cardPublicKey, doorPublicKey);

    System.out.println("Encryption key: " + encryptionKey);
  }

  private static long getEncryptionKey(long cardPublicKey, long doorPublicKey) {

    long value = 1;
    long loopSize = 1;
    long subjectNumber = 7;
    long cardLoopSize = -1;
    long doorLoopSize = -1;

    while (cardLoopSize == -1 || doorLoopSize == -1) {

      value *= subjectNumber;
      value %= 20201227;

      if (value == cardPublicKey) {
        cardLoopSize = loopSize;
      }

      if (value == doorPublicKey) {
        doorLoopSize = loopSize;
      }

      loopSize++;
    }

    long cardEncryptionKey = 1;
    long doorEncryptionKey = 1;

    for (int i = 1; i <= doorLoopSize; i++) {
      cardEncryptionKey *= cardPublicKey;
      cardEncryptionKey %= 20201227;
    }

    for (int i = 1; i <= cardLoopSize; i++) {
      doorEncryptionKey *= doorPublicKey;
      doorEncryptionKey %= 20201227;
    }

    if (cardEncryptionKey != doorEncryptionKey) {
      throw new RuntimeException("Encryption keys should match");
    }

    return cardEncryptionKey;
  }
}
