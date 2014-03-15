package com.poker.shared;

import java.util.Arrays;
import java.util.List;

import com.google.common.*;
/**
 * @author yzibin@google.com (Yoav Zibin)
 * 
 */
public final class Utils {
  private Utils() {
  }

  public static class EqualityArrayWrapper {
    private Object[] arr;
    
    public EqualityArrayWrapper(Object[] arr) {
      this.arr = arr;      
    }
    
    @Override
    public boolean equals(Object other) {
      if (!(other instanceof EqualityArrayWrapper)) {
        return false;
      }
      return equals((EqualityArrayWrapper) other);
    }

    public boolean equals(EqualityArrayWrapper other) {
      return Arrays.deepEquals(arr, other.arr);
    }

    
  }

  public static class EqualityIntArrayWrapper {
    private int[] arr;
    
    public EqualityIntArrayWrapper(int[] arr) {
      this.arr = arr;      
    }
    
    @Override
    public boolean equals(Object other) {
      if (!(other instanceof EqualityIntArrayWrapper)) {
        return false;
      }
      return equals((EqualityIntArrayWrapper) other);
    }

    public boolean equals(EqualityIntArrayWrapper other) {
      return Arrays.equals(arr, other.arr);
    }

   
  }
  
  /**
   * deepCopy() utility methods. Makes a copy of a 2D array (note that the elements are copied by
   * reference).
   */
  public static <T> void array2dCopy(T[][] src, T[][] target) {
    for (int i = 0; i < target.length; i++) {
      System.arraycopy(src[i], 0, target[i], 0, target[i].length);
    }
  }


  public static <T> String print(T[] arr) {
    StringBuilder res = new StringBuilder();
    for (int i = 0; i < arr.length; i++) {
      if (i != 0) {
        res.append("|");
      }
      res.append(arr[i] == null ? " " : arr[i]);
    }
    return res.toString();
  }

  public static String print(int[] arr) {
    return Arrays.toString(arr);
  }

  public static <T> void shuffle(List<T> list, IntGenerator generator) {
    for (int i = list.size(); i > 1; i--) {
      int random = generator.getInt(0, i);
      T tmp = list.get(i - 1);
      list.set(i - 1, list.get(random));
      list.set(random, tmp);
    }
  }


}


