package com.modprobe.profit;

import java.util.ArrayList;
import java.util.List;

import pers.medusa.circleindicator.widget.CircleIndicator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment {

	List<Goal> allGoals;

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);
		ViewPager vp = (ViewPager) rootView.findViewById(R.id.charts_viewpager);
		HomeCarouselAdapter hca = new HomeCarouselAdapter(getActivity()
				.getSupportFragmentManager());
		vp.setOffscreenPageLimit(2);
		vp.setAdapter(hca);
		CircleIndicator ci = (CircleIndicator) rootView
				.findViewById(R.id.viewpager_indicator);
		ci.setViewPager(vp);
		ExpandingListView elv = (ExpandingListView) rootView
				.findViewById(R.id.main_list_view);
		// fetch data here
		List<SuggestionExpandingListViewItem> expli = new ArrayList<SuggestionExpandingListViewItem>();
		GoalDataSource gds = new GoalDataSource(getActivity());
		allGoals = gds.getAllGoals();
		gds.close();
		for (int i = 0; i < allGoals.size(); i++) {
			if (allGoals.get(i)._now == 1) {
				List<Suggestion> suggestionsForAGoal = Helper
						.getSuggestionForGoal(getActivity(), allGoals.get(i));
				for (int j = 0; j < suggestionsForAGoal.size(); j++) {
					// Log.e("TAG",suggestionsForAGoal.get(j)._subcat._name);
					expli.add(new SuggestionExpandingListViewItem(
							suggestionsForAGoal.get(j)));
				}
			}
		}

		CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(),
				R.layout.expanding_list_view_item, expli, vp);
		elv.setAdapter(adapter);

		return rootView;

	}

}
