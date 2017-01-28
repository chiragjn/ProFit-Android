package com.modprobe.profit;

import java.io.File;
import java.util.Collections;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.pkmmte.view.CircularImageView;

public class ProfileFragment extends Fragment {

	View view;
	TextView nameTextView;
	TextView levelTextView;
	String name;
	String level;
	CircularImageView civ;

	// public static final in V

	public ProfileFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.profile_fragment, container, false);
		nameTextView = (TextView) view.findViewById(R.id.textViewName);
		levelTextView = (TextView) view.findViewById(R.id.textViewLevel);
		civ = (CircularImageView) view.findViewById(R.id.profile_image);
		SharedPreferences pref = AppController.getInstance().prefs;
		nameTextView.setText(pref.getString("name", " "));
		levelTextView
				.setText("Level: "
						+ (int) (Math.floor(Helper
								.getAllTimeFitons(getActivity()) / 1000) + 1));

		// START
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

		arcView.addEvent(new DecoEvent.Builder(50).setIndex(series1Index)
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

		arcView1.addEvent(new DecoEvent.Builder(60).setIndex(series1Indexa1)
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

		arcView2.addEvent(new DecoEvent.Builder(75).setIndex(series1Indexa2)
				.setDelay(1000).build());
		// END

		TextView cardiofitons = (TextView) view.findViewById(R.id.fitoncounter);
		TextView sportsfitons = (TextView) view
				.findViewById(R.id.fitoncounter1);
		TextView gymfitons = (TextView) view.findViewById(R.id.fitoncounter2);
		CategoryDataSource cds = new CategoryDataSource(getActivity());
		cds.open();
		List<Category> lc = cds.getAllCategories();
		cds.close();
		cardiofitons.setText(Helper.getAllTimeFitonsForCategory(getActivity(),
				lc.get(0)) + " Fitons");
		sportsfitons.setText(Helper.getAllTimeFitonsForCategory(getActivity(),
				lc.get(1)) + " Fitons");

		gymfitons.setText(Helper.getAllTimeFitonsForCategory(getActivity(),
				lc.get(2))
				+ " Fitons");

		RecyclerView historyList = (RecyclerView) view
				.findViewById(R.id.historyList);
		historyList.addItemDecoration(new HistoryRecyclerViewItemSpacing(50));

		historyList.setHasFixedSize(true);
		LinearLayoutManager llm = new LinearLayoutManager(getActivity());
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		historyList.setLayoutManager(llm);
		ActivityDataSource ads = new ActivityDataSource(getActivity());
		//
		// CategoryDataSource cds = new CategoryDataSource(getActivity());
		// cds.open();
		// Category cc = cds.createCategory("Sports", 8, 0);
		// cds.close();
		//
		// SubCatDataSource sds = new SubCatDataSource(getActivity());
		// sds.open();
		// SubCat ss = sds.createSubCat(cc, "Tennis", 9, 0);
		// sds.close();
		//
		// ads.open();
		// ads.createActivity(ss, 600, 100, 6, 0);
		// ads.close();

		ads.open();
		List<Activity> hl = ads.getAllActivites();
		ads.close();

		Collections.reverse(hl);
		hl.add(hl.size() - 1 - 2, new Activity(true, "26 Feb", 0, 1));
		hl.add(hl.size() - 1 - 4, new Activity(true, "27 Feb", 0, 4));
		hl.add(0, new Activity(true, "28 Feb", 0, 7));
		// hl.add(new MyActivities("Swimming", "Sports", 5, 200, 600, 1200,
		// "Yesterday"));
		// hl.add(new MyActivities("Swimming", "Sports", 6, 300, 600, 1200,
		// "Yesterday"));
		// hl.add(new MyActivities("Swimming", "Sports", 7, 500, 600, 1200,
		// "Yesterday"));
		// hl.add(new MyActivities("Swimming", "Sports", 8, 600, 600, 1200,
		// "Yesterday"));
		// Log.e("Act", hl.toString());

		HistoryListAdapter hla = new HistoryListAdapter(getActivity(), hl);
		historyList.setAdapter(hla);

		Button editButton = (Button) view.findViewById(R.id.editButton);

		editButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Call chirags edit here
				EditProfileFragment edf = new EditProfileFragment();
				FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.replace(R.id.container, edf)
						.addToBackStack(null).commit();
			}
		});

		SharedPreferences sharedPreferences = AppController.getInstance().prefs;
		if (sharedPreferences.getString("image", null) != null
				&& !sharedPreferences.getString("image", null).equals("")) {
			Log.e("LOGS", sharedPreferences.getString("image", null));
			setupImageViewFromFile(sharedPreferences.getString("image", null));
			// TODO
		}
		return view;
	}

	public void setupImageViewFromFile(String filePath) {
		try {
			File imgFile = new File(filePath);
			if (imgFile.exists()) {
				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());
				civ.setImageBitmap(myBitmap);
				//myBitmap.recycle();
			}

		} catch (Exception e) {
			Toast.makeText(getActivity(),
					"Failed to set Image.Please try again", Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();
		}
	}

}
