package com.modprobe.profit;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FeedbackFragment extends Fragment {

	int type;
	int intensity;
	int fitons;
	String subcat;
	String cat;
	String sessionname;
	int weight;
	View view;
	private TextView heading;


	public FeedbackFragment(int intensity, int fitons, String cat, String subcat) {
		this.intensity = intensity;
		this.fitons = fitons;
		this.cat = cat;
		this.subcat = subcat;
		this.type = 0;
	}

	public FeedbackFragment(int intensity, int fitons, String sessionname) {
		this.intensity = intensity;
		this.fitons = fitons;
		this.sessionname = sessionname;
		this.type = 1;
	}

	public FeedbackFragment(int weight) {
		this.weight = weight;
		this.type = 3;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (type == 0) {
			view = inflater.inflate(R.layout.fragment_feeddback, container, false);
			heading = (TextView)view.findViewById(R.id.heading);
			heading.setText("Activity Evaluation");
			TextView msgtv = (TextView) view.findViewById(R.id.textViewMessage);
			TextView sctv = (TextView) view.findViewById(R.id.textViewSubcat);
			TextView ctv = (TextView) view.findViewById(R.id.textViewCat);
			TextView caltv = (TextView) view.findViewById(R.id.textViewCalories);
			TextView itv = (TextView) view.findViewById(R.id.textViewIntensity);

			DecoView fdv = (DecoView) view.findViewById(R.id.decoViewFeedback);

			msgtv.setText("You got +" + fitons + " Fitons");
			sctv.setText(subcat);
			ctv.setText(cat);
			caltv.setText(Helper.getCals(fitons) + " calories");
			itv.setText("Intensity: " + intensity);
			int num = Helper.getTodayFitons(getActivity()) - fitons;
			int den = Helper.getGoalFitons(getActivity());

			int done = (num * 100) / den;

			fdv.addSeries(new SeriesItem.Builder(Color.argb(255, 53, 45, 78)).setRange(0, 100, 100)
					.setInitialVisibility(true).setLineWidth(32f).build());

			// Create data series track
			SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 72, 207, 173)).setRange(0, 100, 0)
					.setLineWidth(32f).build();
			int series1Index = fdv.addSeries(seriesItem1);
			SeriesItem seriesItem2 = new SeriesItem.Builder(Color.argb(255, 252, 108, 81)).setRange(0, 100, done)
					.setLineWidth(32f).build();
			int series1Index2 = fdv.addSeries(seriesItem2);

			fdv.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true).setDuration(10).build());
			Log.e("fitons", ((fitons * 100 / den)) + "");
			fdv.addEvent(new DecoEvent.Builder(done).setIndex(series1Index2).setDelay(1000).setDuration(1000).build());
			fdv.addEvent(new DecoEvent.Builder(((fitons * 100) / den) + done).setIndex(series1Index).setDuration(1000)
					.setDelay(1000).build());
		} else if (type == 1) {
			view = inflater.inflate(R.layout.fragment_session_done, container, false);
			heading = (TextView)view.findViewById(R.id.heading);
			heading.setText("Session Evaluation");
			TextView msgtv = (TextView) view.findViewById(R.id.textViewMessage);
			TextView sctv = (TextView) view.findViewById(R.id.textViewSubcat);
			TextView caltv = (TextView) view.findViewById(R.id.textViewCalories);
			TextView itv = (TextView) view.findViewById(R.id.textViewIntensity);

			DecoView fdv = (DecoView) view.findViewById(R.id.decoViewFeedback);
			msgtv.setText("You got +" + fitons + " Fitons");
			sctv.setText(sessionname);
			caltv.setText(Helper.getCals(fitons) + " calories");
			itv.setText("Intensity: " + intensity);
			int num = Helper.getTodayFitons(getActivity()) - fitons;
			int den = Helper.getGoalFitons(getActivity());

			int done = (num * 100) / den;

			fdv.addSeries(new SeriesItem.Builder(Color.argb(255, 53, 45, 78)).setRange(0, 100, 100)
					.setInitialVisibility(true).setLineWidth(32f).build());

			// Create data series track
			SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 72, 207, 173)).setRange(0, 100, 0)
					.setLineWidth(32f).build();
			int series1Index = fdv.addSeries(seriesItem1);
			SeriesItem seriesItem2 = new SeriesItem.Builder(Color.argb(255, 252, 108, 81)).setRange(0, 100, done)
					.setLineWidth(32f).build();
			int series1Index2 = fdv.addSeries(seriesItem2);

			fdv.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true).setDuration(10).build());
			Log.e("fitons", ((fitons * 100 / den)) + "");
			fdv.addEvent(new DecoEvent.Builder(done).setIndex(series1Index2).setDelay(1000).setDuration(1000).build());
			fdv.addEvent(new DecoEvent.Builder(((fitons * 100) / den) + done).setIndex(series1Index).setDuration(1000)
					.setDelay(1000).build());
		} else {
			view = inflater.inflate(R.layout.fragment_weight_done, container, false);
			heading = (TextView)view.findViewById(R.id.heading);
			heading.setText("Weight Log");
			TextView wtv = (TextView) view.findViewById(R.id.textViewWeight);
			wtv.setText(weight + " kg");
		}

		Button doneButton = (Button) view.findViewById(R.id.buttonDone);
		doneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().beginTransaction().remove(FeedbackFragment.this).commit();
			}
		});

		return view;
	}

}
