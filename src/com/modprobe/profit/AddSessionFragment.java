//10740601

package com.modprobe.profit;

import java.util.ArrayList;
import java.util.List;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddSessionFragment extends Fragment {

	ListView lv;
	List<SessionActivity> activities = new ArrayList<SessionActivity>();
	Session s = new Session();
	ArrayAdapter<SessionActivity> adapter;

	public AddSessionFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_session_add,
				container, false);
		final AppCompatEditText name = (AppCompatEditText) rootView
				.findViewById(R.id.name);
		lv = (ListView) rootView.findViewById(R.id.exercises_list_view);
		adapter = new ArrayAdapter<SessionActivity>(getActivity(),
				android.R.layout.simple_list_item_2, android.R.id.text1,
				activities) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				view.setBackgroundColor(getResources().getColor(
						R.color.colorPrimaryDark));
				TextView text1 = (TextView) view
						.findViewById(android.R.id.text1);
				text1.setTextColor(getResources().getColor(R.color.white));
				TextView text2 = (TextView) view
						.findViewById(android.R.id.text2);
				text2.setTextColor(getResources().getColor(R.color.white));
				text1.setText(activities.get(position)._subcat._name);
				text2.setText(activities.get(position)._duration + " minutes");
				return view;
			}
		};
		lv.setAdapter(adapter);
		Button addExerciseButton = (Button) rootView
				.findViewById(R.id.add_exercise);
		addExerciseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager manager = getActivity()
						.getSupportFragmentManager();
				Fragment frag = manager.findFragmentByTag("");
				if (frag != null) {
					manager.beginTransaction().remove(frag).commit();
				}
				AddExerciseDialogFragment aedf = new AddExerciseDialogFragment(
						AddSessionFragment.this);
				aedf.show(manager, "fragment_edit_name");

			}
		});

		Button saveSessionButton = (Button) rootView
				.findViewById(R.id.save_session);
		saveSessionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(name.getText().toString().equals("") || name.getText().toString() == null){
					Toast.makeText(getActivity(), "Field Empty", Toast.LENGTH_SHORT).show();
					return;
				}
				else{
					String nameString = name.getText().toString();
					SessionDataSource sds = new SessionDataSource(getActivity());
					sds.open();
					Session ss = sds.createSession(nameString, (int)(Math.random() * 50002));
					sds.close();
					SessionActivityDataSource sads = new SessionActivityDataSource(getActivity());
					sads.open();
					for(int i=0;i<activities.size();i++){
						sads.createSessionActivity(ss, activities.get(i)._subcat,  activities.get(i)._duration, (int)(Math.random() * 50002));
					}
					sads.close();
				}
					getActivity().getSupportFragmentManager().beginTransaction().remove(AddSessionFragment.this).commit();
			}
		});

		return rootView;
	}

}