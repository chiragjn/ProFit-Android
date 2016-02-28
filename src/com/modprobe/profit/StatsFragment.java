package com.modprobe.profit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class StatsFragment extends Fragment {

	View view;
	private LineChart c1, c3;
	private BarChart c2;
	Spinner spinner1, spinner2, spinner3;

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_stats, container, false);
		ActivityDataSource ads = new ActivityDataSource(getActivity());
		ads.open();
		List<Activity> lis = ads.getAllActivites();
		ads.close();
		WeightLogDataSource wlds = new WeightLogDataSource(getActivity());
		wlds.open();
		List<WeightLog> wls = wlds.getAllWeightLogs();
		wlds.close();
		lineChartInit(R.id.chart1, "Time", "Fitons", lis);
		barChartInit(R.id.chart2, "SubCategory", "Fitons", lis);
		lineChartInit2(R.id.chart3, "Time", "Weight", wls);
		setSpinners();
		setupColorsToSpinners();
		return view;
	}

	public void setupColorsToSpinners() {
		TextView selectedText = (TextView) spinner1.getChildAt(0);
		if (selectedText != null) {
			selectedText.setTextColor(Color.WHITE);
		}
		selectedText = (TextView) spinner2.getChildAt(0);
		if (selectedText != null) {
			selectedText.setTextColor(Color.WHITE);
		}
		selectedText = (TextView) spinner3.getChildAt(0);
		if (selectedText != null) {
			selectedText.setTextColor(Color.WHITE);
		}
	}

	private void setSpinners() {
		// TODO Auto-generated method stub
		spinner1 = (Spinner) view.findViewById(R.id.spinner1);

		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				TextView selectedText = (TextView) parent.getChildAt(0);
				if (selectedText != null) {
					selectedText.setTextColor(Color.WHITE);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		spinner2 = (Spinner) view.findViewById(R.id.spinner2);

		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				TextView selectedText = (TextView) parent.getChildAt(0);
				if (selectedText != null) {
					selectedText.setTextColor(Color.WHITE);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		spinner3 = (Spinner) view.findViewById(R.id.spinner3);

		spinner3.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				TextView selectedText = (TextView) parent.getChildAt(0);
				if (selectedText != null) {
					selectedText.setTextColor(Color.WHITE);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		Button b1 = (Button) view.findViewById(R.id.shareButton1);
		Button b2 = (Button) view.findViewById(R.id.shareButton2);
		Button b3 = (Button) view.findViewById(R.id.shareButton3);

		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				c1.saveToPath("c1", "/profit");
				String fileName = "c1.png";
				String externalStorageDirectory = Environment
						.getExternalStorageDirectory().toString();
				String myDir = externalStorageDirectory + "/profit/"; // the
				// file will be in saved_images
				Uri uri = Uri.parse("file:///" + myDir + fileName);
				Intent shareIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						(String) v.getTag(R.string.app_name));
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						"Here is my ProFit Graph \n\nShared via ProFit App");
				shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

				PackageManager pm = v.getContext().getPackageManager();
				List<ResolveInfo> activityList = pm.queryIntentActivities(
						shareIntent, 0);
				for (final ResolveInfo app : activityList) {
					Log.e("appname", "" + app.activityInfo.name);
					if ("com.twitter.android.composer.ComposerActivity"
							.equals(app.activityInfo.name)) {
						final ActivityInfo activity = app.activityInfo;
						final ComponentName name = new ComponentName(
								activity.applicationInfo.packageName,
								activity.name);
						shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
						shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
						shareIntent.setComponent(name);
						v.getContext().startActivity(shareIntent);
						break;
					}
				}
			}
		});

		b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				c2.saveToPath("c2", "/profit");
				String fileName = "c2.png";
				String externalStorageDirectory = Environment
						.getExternalStorageDirectory().toString();
				String myDir = externalStorageDirectory + "/profit/"; // the
				// file will be in saved_images
				Uri uri = Uri.parse("file:///" + myDir + fileName);
				Intent shareIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						(String) v.getTag(R.string.app_name));
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						"Here is my ProFit Graph \n\nShared via ProFit App");
				shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

				PackageManager pm = v.getContext().getPackageManager();
				List<ResolveInfo> activityList = pm.queryIntentActivities(
						shareIntent, 0);
				for (final ResolveInfo app : activityList) {
					Log.e("appname", "" + app.activityInfo.name);
					if ("com.twitter.android.composer.ComposerActivity"
							.equals(app.activityInfo.name)) {
						final ActivityInfo activity = app.activityInfo;
						final ComponentName name = new ComponentName(
								activity.applicationInfo.packageName,
								activity.name);
						shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
						shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
						shareIntent.setComponent(name);
						v.getContext().startActivity(shareIntent);
						break;
					}
				}
			}
		});

		b3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				c3.saveToPath("c3", "/profit");
				String fileName = "c3.png";
				String externalStorageDirectory = Environment
						.getExternalStorageDirectory().toString();
				String myDir = externalStorageDirectory + "/profit/"; // the
				// file will be in saved_images
				Uri uri = Uri.parse("file:///" + myDir + fileName);
				Intent shareIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						(String) v.getTag(R.string.app_name));
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						"Here is my ProFit Graph \n\nShared via ProFit App");
				shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

				PackageManager pm = v.getContext().getPackageManager();
				List<ResolveInfo> activityList = pm.queryIntentActivities(
						shareIntent, 0);
				for (final ResolveInfo app : activityList) {
					Log.e("appname", "" + app.activityInfo.name);
					if ("com.twitter.android.composer.ComposerActivity"
							.equals(app.activityInfo.name)) {
						final ActivityInfo activity = app.activityInfo;
						final ComponentName name = new ComponentName(
								activity.applicationInfo.packageName,
								activity.name);
						shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
						shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
						shareIntent.setComponent(name);
						v.getContext().startActivity(shareIntent);
						break;
					}
				}
			}
		});

	}

	private void barChartInit(int chart2, String string, String string2,
			List<Activity> lis) {
		// TODO Auto-generated method stub
		BarChart mChart = (BarChart) view.findViewById(chart2);
		c2 = mChart;
		mChart.setDrawBarShadow(false);
		mChart.setDrawValueAboveBar(true);
		mChart.setDescription("Fitons by Type");
		mChart.setDescriptionColor(Color.WHITE);
		mChart.setBorderColor(Color.WHITE);
		mChart.setDrawGridBackground(false);

		XAxis xAxis = mChart.getXAxis();
		xAxis.setPosition(XAxisPosition.BOTTOM);
		xAxis.setDrawGridLines(false);
		xAxis.setSpaceBetweenLabels(2);
		mChart.getXAxis().setAxisLineColor(Color.WHITE);
		mChart.getXAxis().setTextColor(Color.WHITE);
		mChart.getXAxis().setGridColor(Color.WHITE);

		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.setLabelCount(8, false);
		leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
		leftAxis.setSpaceTop(15f);
		leftAxis.setAxisMinValue(0f);

		leftAxis.setAxisLineColor(Color.WHITE);
		leftAxis.setGridColor(Color.WHITE);
		leftAxis.setZeroLineColor(Color.WHITE);
		leftAxis.setTextColor(Color.WHITE);

		mChart.getAxisRight().setEnabled(false);

		SubCatDataSource sds = new SubCatDataSource(getActivity());
		List<SubCat> scs = sds.getAllSubCats();
		sds.close();

		// Insert data
		ArrayList<String> xVals = new ArrayList<String>();
		int counter = 0;
		HashMap<String, Integer> pos = new HashMap<String, Integer>();
		for (SubCat sc : scs) {
			xVals.add(sc._name);
			pos.put(sc._name, counter);
			counter++;
		}
		int cnt[] = new int[xVals.size()];
		for (int i = 0; i < xVals.size(); i++)
			cnt[i] = 0;
		for (Activity act : lis) {
			cnt[pos.get(act._parent._name)] += act._fitons;
		}

		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

		for (int i = 0; i < xVals.size(); i++) {
			yVals1.add(new BarEntry(cnt[i], i));
		}

		BarDataSet set1 = new BarDataSet(yVals1, "");
		set1.setBarSpacePercent(35f);
		set1.setColor(getResources().getColor(R.color.yellowstats));
		set1.setValueTextColor(Color.WHITE);
		set1.setHighLightColor(Color.WHITE);
		ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
		dataSets.add(set1);

		BarData data = new BarData(xVals, dataSets);
		data.setValueTextSize(10f);

		mChart.setData(data);
		mChart.animateY(2000);

	}

	protected void lineChartInit(int id, String xunits, String yunits,
			List<Activity> activites) {

		LineChart mChart = (LineChart) view.findViewById(id);
		c1 = mChart;
		mChart.setDrawGridBackground(false);
		mChart.setDescription(xunits + " vs " + yunits);
		mChart.setDescriptionColor(Color.WHITE);
		mChart.setNoDataTextDescription("You need to provide data for the chart.");
		float avg = 0;// Compute from data
		int max = 0;
		for (Activity a : activites) {
			avg += a._fitons;
			max = Math.max(max, a._fitons);
		}
		avg /= 2;
		float upperlimit = (float) (1.5 * avg);
		float lowerlimit = (float) (0.8 * avg);
		max = Math.max(max, (int) upperlimit);
		float maxfit = max + 50;
		float minfit = 0;
		LimitLine ll1 = new LimitLine(upperlimit, "Upper Limit");
		ll1.setLineWidth(4f);
		ll1.enableDashedLine(10f, 10f, 0f);
		ll1.setLabelPosition(LimitLabelPosition.LEFT_TOP);

		ll1.setLineColor(Color.WHITE);
		ll1.setTextColor(Color.WHITE);
		ll1.setTextSize(10f);

		LimitLine ll2 = new LimitLine(lowerlimit, "Lower Limit");
		ll2.setLineWidth(4f);
		ll2.setTextColor(Color.WHITE);
		ll2.enableDashedLine(10f, 10f, 0f);
		ll2.setLabelPosition(LimitLabelPosition.LEFT_BOTTOM);
		ll2.setLineColor(Color.WHITE);
		ll2.setTextSize(10f);

		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.setAxisLineColor(Color.WHITE);
		leftAxis.setGridColor(Color.WHITE);
		leftAxis.setZeroLineColor(Color.WHITE);
		leftAxis.setTextColor(Color.WHITE);
		leftAxis.removeAllLimitLines(); // reset all limit lines to avoid
										// overlapping lines
		leftAxis.addLimitLine(ll1);
		leftAxis.addLimitLine(ll2);
		leftAxis.setAxisMaxValue(maxfit);
		leftAxis.setAxisMinValue(minfit);
		// leftAxis.setYOffset(20f);
		// leftAxis.enableGridDashedLine(10f, 10f, 0f);
		leftAxis.setDrawZeroLine(true);

		// limit lines are drawn behind data (and not on top)
		leftAxis.setDrawLimitLinesBehindData(false);

		mChart.getXAxis().setAxisLineColor(Color.WHITE);
		mChart.getXAxis().setTextColor(Color.WHITE);
		mChart.getXAxis().setGridColor(Color.WHITE);
		mChart.getXAxis().setPosition(XAxisPosition.BOTTOM);
		mChart.getAxisRight().setEnabled(false);

		// inser Data

		ArrayList<String> xVals = new ArrayList<String>();
		float val1 = 0, val2 = 0, val3 = 0;
		for (int i = 0; i < 2; i++) {
			val1 += activites.get(i)._fitons;
		}
		for (int i = 2; i < 4; i++) {
			val2 += activites.get(i)._fitons;
		}
		for (int i = 4; i < activites.size(); i++) {
			val3 += activites.get(i)._fitons;
		}

		xVals.add("26/2/16");
		xVals.add("27/2/16");
		xVals.add("28/2/16");
		ArrayList<Entry> yVals = new ArrayList<Entry>();

		yVals.add(new Entry(val1, 0));
		yVals.add(new Entry(val2, 1));
		yVals.add(new Entry(val3, 2));

		// create a dataset and give it a type
		LineDataSet set1 = new LineDataSet(yVals, "");

		// set1.setFillAlpha(110);
		// set1.setFillColor(Color.RED);

		// set the line to be drawn like this "- - - - - -"
		set1.enableDashedLine(10f, 5f, 0f);
		set1.enableDashedHighlightLine(10f, 5f, 0f);
		set1.setColor(Color.WHITE);
		set1.setCircleColor(Color.WHITE);
		set1.setValueTextColor(Color.WHITE);
		set1.setLineWidth(1f);
		set1.setCircleRadius(3f);
		set1.setDrawCircleHole(false);
		set1.setValueTextSize(9f);
		Drawable drawable = ContextCompat.getDrawable(getActivity(),
				R.drawable.fade_red);
		set1.setFillDrawable(drawable);
		set1.setDrawFilled(true);

		ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
		dataSets.add(set1); // add the datasets

		// create a data object with the datasets
		LineData data = new LineData(xVals, dataSets);

		// set data
		mChart.setData(data);
		mChart.setBorderColor(Color.WHITE);
		mChart.animateX(2000);

	}

	protected void lineChartInit2(int id, String xunits, String yunits,
			List<WeightLog> weights) {

		LineChart mChart = (LineChart) view.findViewById(id);
		c3 = mChart;
		mChart.setDrawGridBackground(false);
		mChart.setDescription(xunits + " vs " + yunits);
		mChart.setDescriptionColor(Color.WHITE);
		mChart.setNoDataTextDescription("You need to provide data for the chart.");
		float avg = 0;// Compute from data
		int max = 0;
		for (WeightLog a : weights) {
			avg += a._weight;
			max = Math.max(max, a._weight);
		}
		avg /= 3;
		float maxfit = max + 10;
		float minfit = 0;

		YAxis leftAxis = mChart.getAxisLeft();
		leftAxis.setAxisLineColor(Color.WHITE);
		leftAxis.setGridColor(Color.WHITE);
		leftAxis.setZeroLineColor(Color.WHITE);
		leftAxis.setTextColor(Color.WHITE);
		leftAxis.removeAllLimitLines();
		leftAxis.setAxisMaxValue(maxfit);
		leftAxis.setAxisMinValue(minfit);
		// leftAxis.setYOffset(20f);
		// leftAxis.enableGridDashedLine(10f, 10f, 0f);
		leftAxis.setDrawZeroLine(true);

		// limit lines are drawn behind data (and not on top)
		leftAxis.setDrawLimitLinesBehindData(false);

		mChart.getXAxis().setAxisLineColor(Color.WHITE);
		mChart.getXAxis().setTextColor(Color.WHITE);
		mChart.getXAxis().setGridColor(Color.WHITE);
		mChart.getXAxis().setPosition(XAxisPosition.BOTTOM);
		mChart.getAxisRight().setEnabled(false);

		// inser Data

		ArrayList<String> xVals = new ArrayList<String>();

		xVals.add("26/2/16");
		xVals.add("27/2/16");
		xVals.add("28/2/16");
		ArrayList<Entry> yVals = new ArrayList<Entry>();

		yVals.add(new Entry(weights.get(0)._weight, 0));
		yVals.add(new Entry(weights.get(1)._weight, 1));
		yVals.add(new Entry(weights.get(2)._weight, 2));

		// create a dataset and give it a type
		LineDataSet set1 = new LineDataSet(yVals, "");

		// set1.setFillAlpha(110);
		// set1.setFillColor(Color.RED);

		// set the line to be drawn like this "- - - - - -"
		set1.enableDashedLine(10f, 5f, 0f);
		set1.enableDashedHighlightLine(10f, 5f, 0f);
		set1.setColor(Color.WHITE);
		set1.setCircleColor(Color.WHITE);
		set1.setValueTextColor(Color.WHITE);
		set1.setLineWidth(1f);
		set1.setCircleRadius(3f);
		set1.setDrawCircleHole(false);
		set1.setValueTextSize(9f);
		Drawable drawable = ContextCompat.getDrawable(getActivity(),
				R.drawable.fade_green);
		set1.setFillDrawable(drawable);
		set1.setDrawFilled(true);

		ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
		dataSets.add(set1); // add the datasets

		// create a data object with the datasets
		LineData data = new LineData(xVals, dataSets);

		// set data
		mChart.setData(data);
		mChart.setBorderColor(Color.WHITE);
		mChart.animateX(2000);

	}

}
