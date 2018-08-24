package com.kalosis.sensormusicplayer.rest.interfaces;

import com.kalosis.sensormusicplayer.data.Buffer;

import java.util.List;

public interface GetBuffers extends Error {
  void onBuffers(List<Buffer> bufferList);
}
