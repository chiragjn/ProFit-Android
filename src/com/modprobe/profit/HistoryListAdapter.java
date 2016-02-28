package com.modprobe.profit;

import java.util.List;

import com.tonicartos.superslim.GridSLM;
import com.tonicartos.superslim.LinearSLM;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class HistoryListAdapter extends Adapter<HistoryViewHolder> {

	private static final int VIEW_TYPE_HEADER = 0x01;
	private static final int VIEW_TYPE_CONTENT = 0x00;
	private static final int LINEAR = 0;
	private List<Activity> historyList;
	private int mHeaderDisplay;
	private boolean mMarginsFixed;
	private final Context mContext;

	public HistoryListAdapter(Context context, List<Activity> historyList) {
		mContext = context;
		this.historyList = historyList;
	}

	@Override
	public int getItemCount() {
		return historyList.size();

	}

	@Override
	public void onBindViewHolder(HistoryViewHolder contactViewHolder, int i) {
		final Activity item = historyList.get(i);
		final View itemView = contactViewHolder.itemView;
		final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView
				.getLayoutParams());
		// Overrides xml attrs, could use different layouts too.
		if (item.isHeader) {
			lp.headerDisplay = mHeaderDisplay;
			if (lp.isHeaderInline() || (mMarginsFixed && !lp.isHeaderOverlay())) {
				lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
			} else {
				lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
			}

			lp.headerEndMarginIsAuto = !mMarginsFixed;
			lp.headerStartMarginIsAuto = !mMarginsFixed;
		}
		lp.setSlm(item.sectionManager == LINEAR ? LinearSLM.ID : GridSLM.ID);
		lp.setColumnWidth(mContext.getResources().getDimensionPixelSize(
				R.dimen.grid_column_width));
		lp.setFirstPosition(item.sectionFirstPosition);
		itemView.setLayoutParams(lp);
		if (item.isHeader) {
			contactViewHolder.tvSubcat.setText(item.date);
		} else {
			final Activity ci = historyList.get(i);
			AppController app = AppController.getInstance();
			contactViewHolder.tvCat.setText(ci._parent._parent._name + "");
			contactViewHolder.tvSubcat.setText(ci._parent._name + "");
			contactViewHolder.tvMisc.setText("Intensity " + ci._intensity
					+ ", " + ci._duration + " minutes, "
					+ Helper.getCals(ci._fitons) + " calories");
			contactViewHolder.tvFittons.setText(ci._fitons + "");
			contactViewHolder.shareButton
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent shareIntent = new Intent(
									android.content.Intent.ACTION_SEND);
							shareIntent.setType("text/plain");
							shareIntent.putExtra(
									android.content.Intent.EXTRA_SUBJECT,
									(String) v.getTag(R.string.app_name));
							shareIntent.putExtra(
									android.content.Intent.EXTRA_TEXT,
									(String) "I gained " + ci._fitons
											+ " Fitons for " + ci._duration
											+ " minutes of " + ci._parent._name
											+ " in " + ci._parent._parent._name
											+ "\n\nShared via ProFit App");

							PackageManager pm = v.getContext()
									.getPackageManager();
							List<ResolveInfo> activityList = pm
									.queryIntentActivities(shareIntent, 0);
							for (final ResolveInfo app : activityList) {
								Log.e("appname", app.activityInfo.name);
								if ("com.twitter.android.composer.ComposerActivity"
										.equals(app.activityInfo.name)) {
									final ActivityInfo activity = app.activityInfo;
									final ComponentName name = new ComponentName(
											activity.applicationInfo.packageName,
											activity.name);
									shareIntent
											.addCategory(Intent.CATEGORY_LAUNCHER);
									shareIntent
											.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
													| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
									shareIntent.setComponent(name);
									v.getContext().startActivity(shareIntent);
									break;
								}
							}

						}
					});

		}

		// contactViewHolder.tvExpcal.setText(ci.expcal+"");

		// Calc expected calories
		// Top category exhertion level
		// Sub category exertion level
		// Duration

	}

	@Override
	public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view; // =
					// LayoutInflater.from(arg0.getContext()).inflate(R.layout.history_item,
					// arg0, false);
		if (viewType == VIEW_TYPE_HEADER) {
			view = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.header_item, parent, false);
		} else {
			view = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.history_item, parent, false);
		}
		return new HistoryViewHolder(view);
	}

	@Override
	public int getItemViewType(int position) {
		return historyList.get(position).isHeader ? VIEW_TYPE_HEADER
				: VIEW_TYPE_CONTENT;
	}

	public void setHeaderDisplay(int headerDisplay) {
		mHeaderDisplay = headerDisplay;
		notifyHeaderChanges();
	}

	public void setMarginsFixed(boolean marginsFixed) {
		mMarginsFixed = marginsFixed;
		notifyHeaderChanges();
	}

	private void notifyHeaderChanges() {
		for (int i = 0; i < historyList.size(); i++) {
			Activity item = historyList.get(i);
			if (item.isHeader) {
				notifyItemChanged(i);
			}
		}
	}

}
