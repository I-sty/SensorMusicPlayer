package com.kalosis.sensormusicplayer.utility;

public final class Utility {

  /** The size of the ring/circular buffer */
  public static final int CIRCULAR_BUFFER_SIZE = 2 << 9;

  /** The time interval to refresh the graph-view */
  public static final short DELAY_REFRESH = 64;

  private Utility() {}
}
