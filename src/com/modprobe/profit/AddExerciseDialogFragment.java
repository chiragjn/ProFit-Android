package com.modprobe.profit;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.rey.material.widget.Slider;

public class AddExerciseDialogFragment extends DialogFragment {

	
	
	AppCompatEditText durationEditText;
	Slider intensitySlider;
	Spinner categorySpinner;
	Spinner subCategorySpinner;
	List<Category> allCategories;
	List<SubCat> currentSubCategories;
	AddSessionFragment addSessionFrgamentInstance;
	
	public AddExerciseDialogFragment(AddSessionFragment asfi) {
		addSessionFrgamentInstance = asfi;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.dialog_fragment_select_exercise, container, false);
		getDialog().setTitle("ADD A EXERCISE");
		durationEditText = (AppCompatEditText) rootView
				.findViewById(R.id.duration);
		categorySpinner = (Spinner) rootView
				.findViewById(R.id.category_spinner);
		subCategorySpinner = (Spinner) rootView
				.findViewById(R.id.subcategory_spinner);
		CategoryDataSource cds = new CategoryDataSource(getActivity());
		allCategories = cds.getAllCategories();
		cds.close();
		ArrayList<String> tempCategories = new ArrayList<String>();
		for (int i = 0; i < allCategories.size(); i++) {
			tempCategories.add(allCategories.get(i)._name);
		}
		String[] categoryItems = new String[tempCategories.size()];
		tempCategories.toArray(categoryItems);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, categoryItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categorySpinner.setAdapter(adapter);

		categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TextView selectedText = (TextView) parent.getChildAt(0);
				// if (selectedText != null) {
				// selectedText.setTextColor(Color.WHITE);
				// }
				SubCatDataSource scds = new SubCatDataSource(getActivity());
				scds.open();
				currentSubCategories = scds.getSubCats(allCategories.get(pos));
				scds.close();
				ArrayList<String> tempSubCategories = new ArrayList<String>();
				for (int i = 0; i < currentSubCategories.size(); i++) {
					tempSubCategories.add(currentSubCategories.get(i)._name);
				}
				String[] subCategoryItems = new String[tempSubCategories.size()];
				tempSubCategories.toArray(subCategoryItems);
				ArrayAdapter<String> subCatAdapter = new ArrayAdapter<String>(
						getActivity(), android.R.layout.simple_spinner_item,
						subCategoryItems);
				subCatAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				subCategorySpinner.setAdapter(subCatAdapter);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		subCategorySpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						// TextView selectedText = (TextView)
						// parent.getChildAt(0);
						// if (selectedText != null) {
						// selectedText.setTextColor(Color.WHITE);
						// }
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		Button saveButton = (Button) rootView.findViewById(R.id.add_button);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ActivityDataSource ads = new ActivityDataSource(getActivity());
				ads.open();
				SubCat subcat = currentSubCategories.get(subCategorySpinner
						.getSelectedItemPosition());
				ads.close();
				int duration = 0;
				if (durationEditText.getText().toString() == null
						|| durationEditText.getText().toString().equals("")) {
					Toast.makeText(getActivity(), "Some fields are empty",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					duration = Integer.parseInt(durationEditText.getText()
							.toString());
				}

				addSessionFrgamentInstance.activities.add(new SessionActivity(duration, addSessionFrgamentInstance.s, subcat));
				addSessionFrgamentInstance.adapter.notifyDataSetChanged();
				addSessionFrgamentInstance.lv.invalidate();
				dismiss();
			}
		});
		Button cancelButton = (Button) rootView
				.findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dismiss();
			}

		});

		return rootView;
	}

}