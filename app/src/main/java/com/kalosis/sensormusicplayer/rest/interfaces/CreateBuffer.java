package com.kalosis.sensormusicplayer.rest.interfaces;

import android.support.annotation.Nullable;

import com.kalosis.sensormusicplayer.data.Shape;

public interface CreateBuffer extends Error {

  void onCreated(@Nullable Shape shape);
}
