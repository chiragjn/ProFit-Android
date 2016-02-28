package com.modprobe.profit;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class GoalsFutureAdapter extends BaseAdapter {

	List<Goal> goals;
	Context context;
	LayoutInflater inflater;
	View view;
	GoalsFragment home;
	public GoalsFutureAdapter(Context context, List<Goal> goals, GoalsFragment home) {
		this.context = context;
		this.goals = goals;
		this.home = home;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.goalfuture_item, null);
		} else {
			view = convertView;
		}
		
		TextView goalNameTV = (TextView) view.findViewById(R.id.textViewFutureGoalName);
		goalNameTV.setText(goals.get(position)._name);
		
		Button addGoal = (Button) view.findViewById(R.id.addGoalButton);
		
		addGoal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Goal goal = goals.get(position);
				GoalDataSource gds = new GoalDataSource(context);
				gds.open();
				gds.updateGoal(goal);
				gds.close();
				goals.remove(position);
				home.removeFutureAddNow(goal);
				
			}
		});

		return view;
	}

}
