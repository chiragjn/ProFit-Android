package com.modprobe.profit;

public class WeightLog {

	// private variables
	int _id, _sid;
	String _date;
	int _weight;

	// Empty constructor
	public WeightLog() {

	}

	// constructor
	public WeightLog(int id, String date, int weight, int sid) {
		this._id = id;
		this._date = date;
		this._weight = weight;
		this._sid = sid;
	}

	// constructor
	public WeightLog(String date,  int weight) {
		this._date = date;
		this._weight = weight;
	}

	@Override
	public String toString() {
		return this._date;
	}

}