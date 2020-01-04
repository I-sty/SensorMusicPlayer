package com.szollosi.sensormusicplayer.fragments

import androidx.collection.CircularArray
import com.github.mikephil.charting.data.Entry
import com.szollosi.sensormusicplayer.MyConstants

object MyFragmentUtil {


  /**
   * Appends the specified element to the end of this list and remove the first element if the list exceeded the preset
   * size.
   *
   * @param list The list.
   * @param item The element to append.
   */
  @Synchronized
  fun addItemToList(list: CircularArray<Entry>,
                    item: Entry) {
      if (list.size() >= MyConstants.MAX_DATA_POINTS) {
      list.popFirst()
    }
    list.addLast(item)
  }

  fun getPeakWindow(list: CircularArray<Entry>): List<Entry> {
//    val size = list.size()
//    return if (size == 0) {
    return ArrayList()
//    } else list.subList(0, size)
  }

}
