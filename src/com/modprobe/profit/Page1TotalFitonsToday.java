package com.modprobe.profit;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Page1TotalFitonsToday extends Fragment {
	View view;

	public Page1TotalFitonsToday() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.page1, container, false);
		int totalFitonsToday = Helper.getTodayFitons(getActivity());
		int totalGoalFitonsToday = Helper.getGoalFitons(getActivity());
		int percent = (int) Math
				.floor(((totalFitonsToday * 1.0) / (totalGoalFitonsToday * 1.0)) * 100);
		DecoView arcView = (DecoView) view.findViewById(R.id.dynamicArcView);
		// Create background track
		arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 53, 45, 78))
				.setRange(0, 100, 100).setInitialVisibility(true)
				.setLineWidth(32f).build());

		// Create data series track
		SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 72,
				207, 173)).setRange(0, 100, 0).setLineWidth(32f).build();

		int series1Index = arcView.addSeries(seriesItem1);
		arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW,
				true).setDuration(0).build());

		arcView.addEvent(new DecoEvent.Builder(percent).setIndex(series1Index)
				.setDelay(1000).build());
		// END
		TextView fitonsTextView = (TextView) view
				.findViewById(R.id.fitoncounter);
		fitonsTextView.setText(totalFitonsToday + "/" + totalGoalFitonsToday);
		TextView percentTextView = (TextView) view
				.findViewById(R.id.textViewPerc);
		percentTextView.setText(percent + "%");
		return view;
	}
}
