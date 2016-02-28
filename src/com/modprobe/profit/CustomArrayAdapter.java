/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.modprobe.profit;

import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is a custom array adapter used to populate the listview whose items will
 * expand to display extra content in addition to the default display.
 */
public class CustomArrayAdapter extends
		ArrayAdapter<SuggestionExpandingListViewItem> {

	private List<SuggestionExpandingListViewItem> mData;
	private int mLayoutViewResourceId;
	public Context mContext;
	ViewPager vpInstance;
	public CustomArrayAdapter(Context context, int layoutViewResourceId,
			List<SuggestionExpandingListViewItem> data,ViewPager vp) {
		super(context, layoutViewResourceId, data);
		mData = data;
		mContext = context;
		mLayoutViewResourceId = layoutViewResourceId;
		vpInstance = vp;
	}

	/**
	 * Populates the item in the listview cell with the appropriate data. This
	 * method sets the thumbnail image, the title and the extra text. This
	 * method also updates the layout parameters of the item's view so that the
	 * image and title are centered in the bounds of the collapsed view, and
	 * such that the extra text is not displayed in the collapsed state of the
	 * cell.
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final SuggestionExpandingListViewItem object = mData.get(position);

		if (convertView == null) {
			LayoutInflater inflater = ((AppCompatActivity) getContext())
					.getLayoutInflater();
			convertView = inflater
					.inflate(mLayoutViewResourceId, parent, false);
		}

		LinearLayout linearLayout = (LinearLayout) (convertView
				.findViewById(R.id.item_linear_layout));
		LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT,
				object.getCollapsedHeight());
		linearLayout.setLayoutParams(linearLayoutParams);

		TextView taskTextView = (TextView) convertView.findViewById(R.id.task);
		TextView intensityTextView = (TextView) convertView
				.findViewById(R.id.intensity);
		TextView durationTextView = (TextView) convertView
				.findViewById(R.id.duration);
		TextView descriptionTextView = (TextView) convertView
				.findViewById(R.id.description);
		taskTextView.setText(object.task);
		intensityTextView.setText(object.intensity);
		durationTextView.setText(object.duration);
		descriptionTextView.setText(object.description);
//		linearLayout.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				if(mData.get(position).isExpanded()){
//					arg0.getP
//				}
//				else{
//					
//				}
//				
//			}
//		});
		Button doneButton = (Button) convertView.findViewById(R.id.done);
		Button nopeButton = (Button) convertView.findViewById(R.id.nope);

		doneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO
				Toast.makeText(mContext, "Alright We'll log it!",
						Toast.LENGTH_SHORT).show();
				ActivityDataSource ads = new ActivityDataSource(mContext);
				ads.open();
				Suggestion ss = mData.get(position).suggestion;
				ads.createActivity(ss._subcat, ss._duration, Helper.getFitons(
						ss._duration, ss._intensity, ss._subcat._exertion),
						ss._intensity, (int) (Math.random() * 1000000));
				ads.close();
				mData.remove(position);
				notifyDataSetChanged();
				vpInstance.getAdapter().notifyDataSetChanged();
			}
		});

		nopeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO
				Toast.makeText(mContext, "Maybe next time!", Toast.LENGTH_SHORT)
						.show();
				mData.remove(position);
				notifyDataSetChanged();

			}
		});

		convertView.setLayoutParams(new ListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT,
				AbsListView.LayoutParams.WRAP_CONTENT));

		ExpandingLayout expandingLayout = (ExpandingLayout) convertView
				.findViewById(R.id.expanding_layout);
		expandingLayout.setExpandedHeight(object.getExpandedHeight());
		expandingLayout.setSizeChangedListener(object);

		if (!object.isExpanded()) {
			expandingLayout.setVisibility(View.GONE);
		} else {
			expandingLayout.setVisibility(View.VISIBLE);
		}

		return convertView;
	}
}