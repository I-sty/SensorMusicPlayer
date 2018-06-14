package com.kalosis.sensormusicplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.graph)
  GraphView graphView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    fillGraphRandomValues();
  }

  /**
   * Fills the graph with random values
   */
  private void fillGraphRandomValues() {
    Random random = new Random();
    final int seriesSize = random.nextInt(100) + 1;
    ArrayList<DataPoint> dataPoints = new ArrayList<>();
    for (int i = 0; i < seriesSize; i++) {
      dataPoints.add(new DataPoint(random.nextInt(50), random.nextInt(50)));
    }
    Collections.sort(dataPoints, (o1, o2) -> (int) (o1.getX() - o2.getX()));
    graphView.setTitle(getString(R.string.title_graph));
    graphView.addSeries(new LineGraphSeries<>(dataPoints.toArray(new DataPoint[seriesSize])));
  }
}
