package com.kalosis.sensormusicplayer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.kalosis.sensormusicplayer.R;
import com.kalosis.sensormusicplayer.data.MyDataPoint;

public class FragmentXYZ extends Fragment {

  private static final String TAG = FragmentXYZ.class.getName();

  private GraphView graphView;

  public void addToGraphView(Context context, LineGraphSeries<MyDataPoint> seriesX,
      LineGraphSeries<MyDataPoint> seriesY, LineGraphSeries<MyDataPoint> seriesZ) {
    graphView.addSeries(seriesX);
    seriesX.setColor(ContextCompat.getColor(context, R.color.colorAxeX));
    seriesX.setTitle(context.getString(R.string.tab_text_x));
    graphView.addSeries(seriesY);
    seriesY.setColor(ContextCompat.getColor(context, R.color.colorAxeY));
    seriesY.setTitle(context.getString(R.string.tab_text_y));
    graphView.addSeries(seriesZ);
    seriesZ.setColor(ContextCompat.getColor(context, R.color.colorAxeZ));
    seriesZ.setTitle(context.getString(R.string.tab_text_z));
  }

  public GraphView getGraphView() {
    return graphView;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_section_xyz, container, false);
    graphView = rootView.findViewById(R.id.graph_xyz);
    graphView.getViewport().setXAxisBoundsManual(true);
    graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
    graphView.getGridLabelRenderer().setNumVerticalLabels(8);
    graphView.getLegendRenderer().setVisible(true);
    graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    return rootView;
  }
}
