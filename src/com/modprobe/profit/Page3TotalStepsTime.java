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

public class Page3TotalStepsTime extends Fragment {
	View view;

	public Page3TotalStepsTime() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.page3, container, false);

		DecoView arcView = (DecoView) view.findViewById(R.id.dynamicArcView);
		// Create background track
		arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 53, 45, 78))
				.setRange(0, 100, 100).setInitialVisibility(true)
				.setLineWidth(20f).build());

		// Create data series track
		SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 252,
				108, 81)).setRange(0, 100, 0).setLineWidth(20f).build();

		int series1Index = arcView.addSeries(seriesItem1);
		arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW,
				true).setDuration(0).build());

		arcView.addEvent(new DecoEvent.Builder(30).setIndex(series1Index)
				.setDelay(1000).build());
		// END

		// START
		DecoView arcView1 = (DecoView) view.findViewById(R.id.dynamicArcView1);
		// Create background track
		arcView1.addSeries(new SeriesItem.Builder(Color.argb(255, 53, 45, 78))
				.setRange(0, 100, 100).setInitialVisibility(true)
				.setLineWidth(20f).build());

		// Create data series track
		SeriesItem seriesItem1a1 = new SeriesItem.Builder(Color.argb(255, 255,
				206, 84)).setRange(0, 100, 0).setLineWidth(20f).build();

		int series1Indexa1 = arcView1.addSeries(seriesItem1a1);

		arcView1.addEvent(new DecoEvent.Builder(75).setIndex(series1Indexa1)
				.setDelay(1000).build());
		// END

		// START
		DecoView arcView2 = (DecoView) view.findViewById(R.id.dynamicArcView2);
		// Create background track
		arcView2.addSeries(new SeriesItem.Builder(Color.argb(255, 53, 45, 78))
				.setRange(0, 100, 100).setInitialVisibility(true)
				.setLineWidth(20f).build());

		// Create data series track
		SeriesItem seriesItem1a2 = new SeriesItem.Builder(Color.argb(255, 72,
				207, 173)).setRange(0, 100, 0).setLineWidth(20f).build();

		int series1Indexa2 = arcView2.addSeries(seriesItem1a2);

		arcView2.addEvent(new DecoEvent.Builder(90).setIndex(series1Indexa2)
				.setDelay(1000).build());
		// END

		TextView stepsTodayTextView = (TextView) view
				.findViewById(R.id.fitoncounter);
		TextView stepsWeekTextView = (TextView) view
				.findViewById(R.id.fitoncounter1);
		TextView stepsMonthTextView = (TextView) view
				.findViewById(R.id.fitoncounter2);
		stepsTodayTextView.setText(1665 + " Steps");
		stepsWeekTextView.setText(14381 + " Steps");
		stepsMonthTextView.setText(48324 + " Steps");
		return view;
	}
}
