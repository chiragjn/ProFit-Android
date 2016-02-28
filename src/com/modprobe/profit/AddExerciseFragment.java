package com.modprobe.profit;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.Slider;

public class AddExerciseFragment extends Fragment {
	public AddExerciseFragment() {
	}

	AppCompatEditText durationEditText;
	Slider intensitySlider;
	Spinner categorySpinner;
	Spinner subCategorySpinner;
	List<Category> allCategories;
	List<SubCat> currentSubCategories;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add_exercise,
				container, false);

		durationEditText = (AppCompatEditText) rootView
				.findViewById(R.id.duration);
		intensitySlider = (Slider) rootView.findViewById(R.id.intensity);
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
				TextView selectedText = (TextView) parent.getChildAt(0);
				if (selectedText != null) {
					selectedText.setTextColor(Color.WHITE);
				}
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

		Button saveButton = (Button) rootView.findViewById(R.id.add_history);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ActivityDataSource ads = new ActivityDataSource(getActivity());
				ads.open();
				SubCat subcat = currentSubCategories.get(subCategorySpinner
						.getSelectedItemPosition());
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
				int intensity = intensitySlider.getValue();
				int cal_fac = subcat._exertion;
				int fitons = Helper.getFitons(duration, intensity, cal_fac);
				Activity justCreatedActivity = ads.createActivity(subcat,
						duration, fitons, intensity,
						(int) (Math.random() * 1000000));
				ads.close();
				// TODO
				FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.remove(AddExerciseFragment.this);
				fragmentManager.popBackStack(null,
						FragmentManager.POP_BACK_STACK_INCLUSIVE);
				MainActivityFragment mainActivityFragment = new MainActivityFragment();
				fragmentTransaction
						.remove(AddExerciseFragment.this)
						.add(R.id.container,
								new FeedbackFragment(intensity, Helper
										.getFitons(duration, intensity,
												subcat._exertion),
										subcat._parent._name, subcat._name))
						.addToBackStack(null).commit();

			}
		});
		return rootView;

	}
}
