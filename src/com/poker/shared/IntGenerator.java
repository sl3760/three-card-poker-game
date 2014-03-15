package com.poker.shared;

/**
 * Generates a sequence of integers.
 * The {@link Controller} uses it in chance games. 
 * 
 * @author yzibin@google.com (Yoav Zibin)
 */
public interface IntGenerator {
  /**
   * Returns a number between from (inclusive) and to (exclusive).<br>
   * I.e., it returns a number x that satisfies:<br>
   * {@code from <= x < to}
   * throws IllegalArgumentException if from>=to.
   */
  int getInt(int from, int to) throws IllegalArgumentException;
}