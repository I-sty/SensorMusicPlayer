package com.szollosi.sensormusicplayer.fragments;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;
import com.szollosi.sensormusicplayer.Constants;

import java.util.ArrayList;
import java.util.List;

public class FragmentUtil {


  /**
   * Appends the specified element to the end of this list and remove the first element if the list exceeded the preset
   * size.
   *
   * @param list The list.
   * @param item The element to append.
   */
  public static synchronized void addItemToList(@NonNull final List<Entry> list,
      @NonNull Entry item) {
    if (list.size() >= Constants.MAX_DATA_POINTS) {
      list.remove(0);
    }
    list.add(item);
  }

  public static List<Entry> getPeakWindow(@NonNull final List<Entry> list) {
    final int size = list.size();
    if (size == 0) {
      return new ArrayList<>();
    }
    return list.subList(0, size);
  }

}
