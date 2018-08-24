package com.kalosis.sensormusicplayer.fragments;

import android.support.v4.app.Fragment;


public abstract class GraphFragment extends Fragment {

  protected static final short DELAY_REFRESH = 50;

  protected static final int CIRCULAR_BUFFER_SIZE = 2 << 8;
}
