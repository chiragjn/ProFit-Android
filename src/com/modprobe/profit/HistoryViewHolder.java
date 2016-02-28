package com.modprobe.profit;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HistoryViewHolder extends ViewHolder {
	TextView tvSubcat;
	TextView tvCat;
	TextView tvMisc;
	TextView tvDuration;
	TextView tvFittons;
	TextView tvExpcal;
	Button shareButton;
	public HistoryViewHolder(View itemView) {
		super(itemView);
		tvSubcat = (TextView) itemView.findViewById(R.id.textViewSubcat);
		tvCat = (TextView) itemView.findViewById(R.id.textViewCat);
		tvMisc = (TextView) itemView.findViewById(R.id.textViewMisc);
		tvFittons = (TextView) itemView.findViewById(R.id.textViewFittons);
		shareButton = (Button) itemView.findViewById(R.id.shareButton);
	}

}
