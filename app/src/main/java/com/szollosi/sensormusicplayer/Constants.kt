package com.szollosi.sensormusicplayer

object Constants {

    const val DELAY_REFRESH: Short = 200

  const val MAX_DATA_POINTS = 40

  const val PEAK_THRESHOLD = 7

  /** Skip values smaller than this value  */
  const val THRESHOLD = 0.3f

  internal const val NUMBER_OF_AVAILABLE_SCREENS: Byte = 4

  internal const val DELAY_TO_LISTEN_SENSOR = 600

  private const val DELAY_CALC_PEAK: Short = 1500
}
