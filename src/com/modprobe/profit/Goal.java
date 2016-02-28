package com.modprobe.profit;

public class Goal {

	// private variables
	int _id, _sid, _now;
	String _name,_fitons;

	// Empty constructor
	public Goal() {

	}

	// constructor
	public Goal(int id, String name, String fitons, int sid, int now) {
		this._id = id;
		this._name = name;
		this._fitons = fitons;
		this._sid = sid;
		this._now = now;
	}

	// constructor
	public Goal(String name, String fitons, int now) {
		this._name = name;
		this._now = now;
		this._fitons = fitons;
	}

	@Override
	public String toString() {
		return this._name;
	}

}