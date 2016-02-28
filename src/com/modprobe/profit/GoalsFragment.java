package com.modprobe.profit;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class GoalsFragment extends Fragment {
	List<Goal> goalsNow;
	GoalsNowAdapter goalNowAdapter;
	GoalsFutureAdapter goalFutureAdapter;
	ListView gflv;
	ListView gnlv;
	TextView newGoalTV;
	View view;
	public GoalsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_goals, container, false);
		gnlv = (ListView) view.findViewById(R.id.listViewGoalsNow);
		gflv = (ListView) view.findViewById(R.id.listViewGoalsFuture);
		goalsNow = new ArrayList<Goal>();
		newGoalTV = (TextView) view.findViewById(R.id.newGoalTv);
		List<Goal> goalsFuture = new ArrayList<Goal>();
		GoalDataSource gds = new GoalDataSource(getActivity());
		List<Goal> all = gds.getAllGoals();
		for(Goal goal : all) {
			if(goal._now == 1) {
				goalsNow.add(goal);
			} else {
				goalsFuture.add(goal);
			}
		}
		goalNowAdapter = new GoalsNowAdapter(getActivity(), goalsNow);
		goalFutureAdapter = new GoalsFutureAdapter(getActivity(), goalsFuture, this);
		gnlv.setAdapter(goalNowAdapter);
		gflv.setAdapter(goalFutureAdapter);
		goalNowAdapter.notifyDataSetChanged();
		goalFutureAdapter.notifyDataSetChanged();
		Utility.setListViewHeightBasedOnChildren(gnlv);
		Utility.setListViewHeightBasedOnChildren(gflv);
		return view;
	}
	
	public void removeFutureAddNow(Goal goal) {
		Log.e("before", goalsNow.toString());
		goalsNow.add(goal);
		goalNowAdapter.notifyDataSetChanged();
		goalFutureAdapter.notifyDataSetChanged();
		Utility.setListViewHeightBasedOnChildren(gnlv);
		Utility.setListViewHeightBasedOnChildren(gflv);
		
	}

}
