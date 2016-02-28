package com.modprobe.profit;

import java.util.List;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GoalsNowAdapter extends BaseAdapter {

	Context context;
	List<Goal> goals;
	View view;
	LayoutInflater inflater;

	public GoalsNowAdapter(Context context, List<Goal> goals) {
		this.context = context;
		this.goals = goals;
	}

	@Override
	public int getCount() {
		return goals.size();
	}

	@Override
	public Goal getItem(int position) {
		return goals.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.goalsnow_item, null);
		} else {
			view = convertView;
		}

		TextView goalNameTV = (TextView) view.findViewById(R.id.textViewGoalName);
		goalNameTV.setText(goals.get(position)._name);

		// Start
		DecoView arcView1 = (DecoView) view.findViewById(R.id.decoViewGoalNow);
		// Create background track
		arcView1.addSeries(new SeriesItem.Builder(Color.argb(255, 73, 61, 103)).setRange(0, 100, 100)
				.setInitialVisibility(true).setLineWidth(10f).build());

		// Create data series track
		SeriesItem seriesItem1a1 = new SeriesItem.Builder(Color.argb(255, 72, 207, 173)).setRange(0, 100, 0)
				.setLineWidth(10f).build();

		int series1Indexa1 = arcView1.addSeries(seriesItem1a1);
		// TODO add the goals percentage from shyams method here
		int num = 0;
		CategoryDataSource cds = new CategoryDataSource(context);
		cds.open();
		List<Category> cats = cds.getAllCategories();
		cds.close();
		for(Category cat : cats) {
			num += Helper.getTodayFitonsForCategory(context, cat);
		}
		String fitons[] = goals.get(position)._fitons.split(",");
		int fits = 0;
		for(String s : fitons) {
			fits += Integer.parseInt(s.trim());
		}
		
		num = (100*num)/ fits;
		arcView1.addEvent(new DecoEvent.Builder(num).setIndex(series1Indexa1).setDelay(1000).build());
		TextView percTV = (TextView) view.findViewById(R.id.textViewPerc);
		percTV.setText(num + "%");
		// END

		return view;
	}

	public void add(Goal goal) {
		goals.add(goal);		
	}

}
