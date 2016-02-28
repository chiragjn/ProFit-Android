package com.modprobe.profit;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LogSessionFragment extends Fragment {

	public LogSessionFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_session_add,
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
		Button createNewButton = (Button) rootView
				.findViewById(R.id.create_new);
		createNewButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AddSessionFragment asf = new AddSessionFragment();
				getActivity().getSupportFragmentManager().beginTransaction()
						.replace(R.id.container, asf).addToBackStack(null)
						.commit();

			}
		});
		return rootView;
	}

}