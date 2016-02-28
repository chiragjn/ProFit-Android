package com.modprobe.profit;

import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class LogSessionFragment extends Fragment {

	public LogSessionFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_session_log,
				container, false);

		ListView lv = (ListView) rootView.findViewById(R.id.sessions_list_view);
		SessionDataSource sds = new SessionDataSource(getActivity());
		sds.open();
		final List<Session> sessionItems = sds.getAllSessions();
		sds.close();
		// Create the array adapter to bind the array to the listView

		ArrayAdapter<Session> adapter = new ArrayAdapter<Session>(
				getActivity(), android.R.layout.simple_list_item_1,
				sessionItems) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				view.setBackgroundColor(getResources().getColor(
						R.color.colorPrimaryDark));
				TextView text1 = (TextView) view
						.findViewById(android.R.id.text1);
				text1.setTextColor(getResources().getColor(R.color.white));

				text1.setText(sessionItems.get(position)._name);
				return view;
			}
		};
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("Enter Exertion Level");

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
								int intensity = Integer.parseInt(input
										.getText().toString());
								// dialog.cancel();
								// getActivity()
								// .getSupportFragmentManager()
								// .beginTransaction()
								// .addToBackStack(null)
								// .add(R.id.container,
								// new FeedbackFragment(weight))
								// .addToBackStack(null).commit();

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

			}

		});
		Button createNewButton = (Button) rootView
				.findViewById(R.id.create_new);
		createNewButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AddSessionFragment asf = new AddSessionFragment();
				getActivity().getSupportFragmentManager().beginTransaction()
						.add(R.id.container, asf).addToBackStack(null).commit();

			}
		});
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}