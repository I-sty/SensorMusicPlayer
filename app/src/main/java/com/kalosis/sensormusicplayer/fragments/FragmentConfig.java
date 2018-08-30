package com.kalosis.sensormusicplayer.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kalosis.sensormusicplayer.R;
import com.kalosis.sensormusicplayer.data.Buffer;
import com.kalosis.sensormusicplayer.data.MyDataPoint;
import com.kalosis.sensormusicplayer.rest.RESTClient;
import com.kalosis.sensormusicplayer.rest.interfaces.CreateBuffer;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class FragmentConfig extends PreferenceFragment {

  private static final byte DELAY_WRITE_TIME = 100;

  private static final String TAG = FragmentConfig.class.getName();

  private static final byte THRESHOLD_ANALYZE = 2;

  private TextView elapsedTime;

  private List<MyDataPoint> list = Collections.emptyList();

  private Button recordButton;

  private TextView recordItems;

  private RESTClient restClient = new RESTClient();

  private View rootView;

  private final View.OnClickListener analyzeClickListener = v -> analyzeList();

  private long startTime;

  private Button stopSendButton;

  private final Runnable writeTime = new Runnable() {
    @Override
    public void run() {
      list = FragmentXYZ.getPeakWindow();
      recordItems.setText(String.valueOf(list.size()));
      long diff = Calendar.getInstance().getTimeInMillis() - startTime;
      String format = String.format(Locale.getDefault(), "%%0%dd", 2);
      diff = diff / 1000;
      String seconds = String.format(format, diff % 60);
      String minutes = String.format(format, diff / 60);
      elapsedTime.setText(String.format(Locale.getDefault(), "%s:%s", minutes, seconds));
      stopSendButton.postDelayed(this, DELAY_WRITE_TIME);
    }
  };

  private final View.OnClickListener recordItemsClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      if (recordButton.getText().equals(v.getContext().getString(R.string.label_stop))) {
        list = FragmentXYZ.getPeakWindow();
        recordItems.setText(String.valueOf(list.size()));
        recordButton.setText(R.string.label_record);
        stopSendButton.removeCallbacks(writeTime);
        stopSendButton.setEnabled(true);
        analyzeList();
      } else {
        FragmentXYZ.clearPeakWindow();
        startTime = Calendar.getInstance().getTimeInMillis();
        stopSendButton.postDelayed(writeTime, DELAY_WRITE_TIME);
        recordButton.setText(R.string.label_stop);
        stopSendButton.setEnabled(false);
      }
    }
  };

  private final View.OnClickListener sendItems = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      stopSendButton.setEnabled(false);
      stopSendButton.removeCallbacks(writeTime);
      restClient.createBuffer(new Buffer(list), getActivity(), new CreateBuffer() {
        @Override
        public void onCreated(double distance) {
          stopSendButton.post(() -> Snackbar.make(rootView,
              stopSendButton.getContext().getString(R.string.label_buffer_created) + "\nDistance: " + distance,
              Snackbar.LENGTH_SHORT).show());
        }

        @Override
        public void onError(String message) {
          Log.e(TAG, "[onError] message: " + message);
          stopSendButton.post(() -> Snackbar.make(rootView,
              stopSendButton.getContext().getString(R.string.label_can_not_save_signal) + "\n" + message,
              Snackbar.LENGTH_LONG).show());
        }
      });
    }
  };

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.fragment_config, container, false);
    recordButton = rootView.findViewById(R.id.fragmentConfigButtonRecord);
    recordButton.setOnClickListener(recordItemsClickListener);
    stopSendButton = rootView.findViewById(R.id.fragmentConfigButtonStopSend);
    stopSendButton.setEnabled(false);
    stopSendButton.setOnClickListener(sendItems);
    elapsedTime = rootView.findViewById(R.id.fragmentConfigTextViewElapsedTimeValue);
    recordItems = rootView.findViewById(R.id.fragmentConfigTextViewRecordedItemsValue);
    Button analyzeButton = rootView.findViewById(R.id.fragmentConfigButtonAnalyze);
    analyzeButton.setOnClickListener(analyzeClickListener);
    return rootView;
  }

  private void analyzeList() {
    if (list.isEmpty()) {
      Snackbar.make(rootView, R.string.label_empty_list, Snackbar.LENGTH_SHORT).show();
      return;
    }

    int k = 0;

    for (MyDataPoint myDataPoint : list) {
      if (myDataPoint.getY() < THRESHOLD_ANALYZE) {
        ++k;
      }
    }
    int percentage = k * 100 / list.size();
    Snackbar.make(rootView, String.format(Locale.getDefault(), "%d%% of the list is empty", percentage),
        Snackbar.LENGTH_LONG).show();
  }
}
