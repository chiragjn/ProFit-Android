package com.modprobe.profit;

import java.util.List;

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

public class Page2TotalFitonsPerCategory extends Fragment {
	View view;

	public Page2TotalFitonsPerCategory() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.page2, container, false);
		CategoryDataSource cds = new CategoryDataSource(getActivity());
		cds.open();
		List<Category> lc = cds.getAllCategories();
		cds.close();

		int cardioPercent = (int) Math
				.floor(((Helper.getTodayFitonsForCategory(getActivity(),
						lc.get(0)) * 1.0f) / (Helper.getGoalFitonsForCategory(
						getActivity(), lc.get(0)) * 1.0f)) * 100);
		int sportsPercent = (int) Math
				.floor(((Helper.getTodayFitonsForCategory(getActivity(),
						lc.get(1)) * 1.0f) / (Helper.getGoalFitonsForCategory(
						getActivity(), lc.get(1)) * 1.0f)) * 100);
		int gymPercent = (int) Math
				.floor(((Helper.getTodayFitonsForCategory(getActivity(),
						lc.get(2)) * 1.0f) / (Helper.getGoalFitonsForCategory(
						getActivity(), lc.get(2)) * 1.0f)) * 100);

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

		arcView.addEvent(new DecoEvent.Builder(cardioPercent)
				.setIndex(series1Index).setDelay(1000).build());
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

		arcView1.addEvent(new DecoEvent.Builder(sportsPercent)
				.setIndex(series1Indexa1).setDelay(1000).build());
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

		arcView2.addEvent(new DecoEvent.Builder(gymPercent)
				.setIndex(series1Indexa2).setDelay(1000).build());
		// END

		TextView cardiofitons = (TextView) view.findViewById(R.id.fitoncounter);
		TextView sportsfitons = (TextView) view
				.findViewById(R.id.fitoncounter1);
		TextView gymfitons = (TextView) view.findViewById(R.id.fitoncounter2);
		cardiofitons.setText(Helper.getTodayFitonsForCategory(getActivity(),
				lc.get(0))
				+ " Fitons");
		sportsfitons.setText(Helper.getTodayFitonsForCategory(getActivity(),
				lc.get(1))
				+ " Fitons");

		gymfitons.setText(Helper.getTodayFitonsForCategory(getActivity(),
				lc.get(2))
				+ " Fitons");
		return view;
	}
}
