package com.kalosis.sensormusicplayer.rest;

import android.support.annotation.NonNull;

import com.kalosis.sensormusicplayer.data.Buffer;
import com.kalosis.sensormusicplayer.rest.interfaces.CreateBuffer;
import com.kalosis.sensormusicplayer.rest.interfaces.GetBuffers;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RESTClient {
  private static final String HTTP_PROTOCOL = "http://";

  private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

  private static final String RESPONSE_EMPTY = "The response body is null";

  private static final String RESPONSE_FAILED = "Response failed";

  /**
   * 193.231.162.22
   * 192.168.0.4
   */
  private static final String SERVER_ADDRESS = "192.168.0.4";

  private static final short SERVER_PORT = 3000;

  private final OkHttpClient client = new OkHttpClient();

  private final Moshi moshi = new Moshi.Builder().build();

  private final JsonAdapter<Buffer> bufferAdapter = moshi.adapter(Buffer.class);

  private final Type type = Types.newParameterizedType(List.class, Buffer.class);

  private final JsonAdapter<List<Buffer>> buffersAdapter = moshi.adapter(type);

  public void createBuffer(Buffer bufferList, @NonNull CreateBuffer listener) {
    RequestBody body = RequestBody.create(JSON, bufferAdapter.toJson(bufferList));
    Request request = new Request.Builder().url(getAbsoluteUrl("buffers")).post(body).build();
    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        listener.onError(e.getMessage());
      }

      @Override
      public void onResponse(Call call, Response response) {
        if (response.isSuccessful()) {
          listener.onCreated();
        } else {
          listener.onError(RESPONSE_FAILED);
        }
      }
    });
  }

  public void getBuffers(GetBuffers listener) {
    Request request = new Request.Builder().url(getAbsoluteUrl("buffers")).build();

    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        listener.onError(e.getMessage());
      }

      @Override
      public void onResponse(Call call, Response response) {
        if (response.isSuccessful()) {
          ResponseBody body = response.body();
          if (body != null) {
            try {
              String string = body.string();
              listener.onBuffers(buffersAdapter.fromJson(string));
            } catch (Exception e) {
              listener.onError(e.getMessage());
            }
            return;
          } else {
            listener.onError(RESPONSE_EMPTY);
          }
        }
        listener.onError(RESPONSE_FAILED);
      }
    });
  }

  private static String getAbsoluteUrl(@NonNull String buffers) {
    return HTTP_PROTOCOL + SERVER_ADDRESS + ":" + SERVER_PORT + "/" + buffers;
  }
}
