package com.modprobe.profit;

public class Session {

	// private variables
	int _id, _sid;
	String _name;

	// Empty constructor
	public Session() {

	}

	// constructor
	public Session(int id, String name, int sid) {
		this._id = id;
		this._name = name;
		this._sid = sid;
	}

	// constructor
	public Session(String name) {
		this._name = name;
	}

	@Override
	public String toString() {
		return this._name;
	}

}