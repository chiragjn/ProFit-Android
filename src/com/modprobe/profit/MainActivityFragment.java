package com.modprobe.profit;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.bowyer.app.fabtransitionlayout.BottomSheetLayout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

	public MainActivityFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
		SmartTabLayout viewPagerTab = (SmartTabLayout) rootView
				.findViewById(R.id.viewpagertab);
		viewPager.setOffscreenPageLimit(2);
		setup(viewPagerTab);

		FragmentPagerItems pages = new FragmentPagerItems(getActivity());
		pages.add(FragmentPagerItem.of(getString(R.string.tab_no_title),
				HomeFragment.class));
		pages.add(FragmentPagerItem.of(getString(R.string.tab_no_title),
				StatsFragment.class));
		pages.add(FragmentPagerItem.of(getString(R.string.tab_no_title),
				GoalsFragment.class));
		pages.add(FragmentPagerItem.of(getString(R.string.tab_no_title),
				ProfileFragment.class));

		final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
				getActivity().getSupportFragmentManager(), pages);
		viewPager.setAdapter(adapter);
		viewPagerTab.setViewPager(viewPager);
		viewPagerTab.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == 1) {
					StatsFragment statsFragment = (StatsFragment) adapter
							.getPage(arg0);
					statsFragment.setupColorsToSpinners();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

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

	public void setup(SmartTabLayout layout) {

		final LayoutInflater inflater = LayoutInflater
				.from(layout.getContext());
		final Resources res = layout.getContext().getResources();

		layout.setCustomTabView(new SmartTabLayout.TabProvider() {
			@Override
			public View createTabView(ViewGroup container, int position,
					PagerAdapter adapter) {
				ImageView icon = (ImageView) inflater.inflate(
						R.layout.custom_tab_icon2, container, false);
				switch (position) {
				case 0:
					icon.setImageDrawable(res
							.getDrawable(R.drawable.ic_home_white_24dp));
					break;
				case 1:
					icon.setImageDrawable(res.getDrawable(R.drawable.graph));
					break;
				case 2:
					icon.setImageDrawable(res.getDrawable(R.drawable.rating));
					break;
				case 3:
					icon.setImageDrawable(res
							.getDrawable(R.drawable.user_profile2));
					break;
				default:
					throw new IllegalStateException("Invalid position: "
							+ position);
				}
				return icon;
			}
		});
	}
}
