package com.szollosi.sensormusicplayer.fragments

import com.github.mikephil.charting.data.Entry
import com.szollosi.sensormusicplayer.Constants
import kotlin.collections.ArrayList

object FragmentUtil {


  /**
   * Appends the specified element to the end of this list and remove the first element if the list exceeded the preset
   * size.
   *
   * @param list The list.
   * @param item The element to append.
   */
  @Synchronized
  fun addItemToList(list: ArrayList<Entry>,
                    item: Entry) {
    if (list.size >= Constants.MAX_DATA_POINTS) {
      list.drop(1)
    }
    list.add(item)
  }

  fun getPeakWindow(list: List<Entry>): List<Entry> {
    val size = list.size
    return if (size == 0) {
      ArrayList()
    } else list.subList(0, size)
  }

}
