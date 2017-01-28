package com.modprobe.profit;

import java.util.ArrayList;
import java.util.List;

import pers.medusa.circleindicator.widget.CircleIndicator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.bowyer.app.fabtransitionlayout.BottomSheetLayout;

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

		final BottomSheetLayout mBottomSheetLayout = (BottomSheetLayout) rootView
				.findViewById(R.id.bottom_sheet);
		ListView mMenuList = (ListView) rootView.findViewById(R.id.list_menu);
		FloatingActionButton mFab = (FloatingActionButton) rootView
				.findViewById(R.id.fab);
		ArrayList<BottomSheet> bottomSheets = new ArrayList<>();
		bottomSheets.add(BottomSheet.to().setBottomSheetMenuType(
				BottomSheet.BottomSheetMenuType.ACTIVITY));
		bottomSheets.add(BottomSheet.to().setBottomSheetMenuType(
				BottomSheet.BottomSheetMenuType.SESSION));
		bottomSheets.add(BottomSheet.to().setBottomSheetMenuType(
				BottomSheet.BottomSheetMenuType.WEIGHT));
		BottomSheetAdapter bottomSheetAdapter = new BottomSheetAdapter(
				getActivity(), bottomSheets);
		mMenuList.setAdapter(bottomSheetAdapter);
		mBottomSheetLayout.setFab(mFab);
		mFab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mBottomSheetLayout.expandFab();

			}
		});

		mMenuList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				 FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				 mBottomSheetLayout.contractFab();
				switch (position) {
				case 0:
					AddExerciseFragment aef = new AddExerciseFragment();
					fragmentTransaction.addToBackStack(null).add(R.id.container, aef)
							.addToBackStack(null).commit();
					break;
				case 1:
					LogSessionFragment asf = new LogSessionFragment();
					fragmentTransaction.addToBackStack(null).add(R.id.container, asf)
							.addToBackStack(null).commit();
					break;
				case 2:
					// TODO Auto-generated method stub
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setTitle("Please enter your weight");

					// Set up the input
					final EditText input = new EditText(getActivity());
					// Specify the type of input expected; this, for example,
					// sets the input as a password, and will mask the text
					input.setInputType(InputType.TYPE_CLASS_NUMBER);
					builder.setView(input);

					// Set up the buttons
					builder.setPositiveButton("Log",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									int weight = Integer.parseInt(input
											.getText().toString());
									WeightLogDataSource wlds = new WeightLogDataSource(
											getActivity());
									wlds.open();
									wlds.createWeightLog("28/2/16", weight,
											(int) (Math.random() * 50002));
									wlds.close();
									// dialog.cancel();
									getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.container,new FeedbackFragment(weight))
									.addToBackStack(null).commit();

								}
							});
					builder.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});

					builder.show();
					break;
				default:
					break;
				}

			}

		});
		
		return rootView;

	}

}
