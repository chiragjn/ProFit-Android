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

/**
 * This custom object is used to populate the list adapter. It contains a
 * reference to an image, title, and the extra text to be displayed.
 * Furthermore, it keeps track of the current state (collapsed/expanded) of the
 * corresponding item in the list, as well as store the height of the cell in
 * its collapsed state.
 */
public class SuggestionExpandingListViewItem implements OnSizeChangedListener {

	String task;
	String duration;
	boolean mIsExpanded;
	String intensity;
	String description;
	private int mCollapsedHeight;
	private int mExpandedHeight;
	Suggestion suggestion;

	public SuggestionExpandingListViewItem(Suggestion s) {
		suggestion = s;
        task = s._subcat._name;
        duration = s._duration +  " min";
        mCollapsedHeight = 210;
        mIsExpanded = false;
        intensity = "Intensity level: " + s._intensity;
        //TODO
        int fitons = Helper.getFitons(s._duration,s._intensity, s._subcat._exertion);
        description = "Do/Play  " + task 
        		+ " for " + s._duration 
        		+ " minutes with a intensity level of " 
        		+ s._intensity
        		+ " to progress towards your \"" + s._goal._name + " \" goal and get " + fitons +" Fitons!";
        mExpandedHeight = -1;
    }

	public boolean isExpanded() {
		return mIsExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		mIsExpanded = isExpanded;
	}

	public int getCollapsedHeight() {
		return mCollapsedHeight;
	}

	public void setCollapsedHeight(int collapsedHeight) {
		mCollapsedHeight = collapsedHeight;
	}

	public int getExpandedHeight() {
		return mExpandedHeight;
	}

	public void setExpandedHeight(int expandedHeight) {
		mExpandedHeight = expandedHeight;
	}

	@Override
	public void onSizeChanged(int newHeight) {
		setExpandedHeight(newHeight);
	}
}
