package com.modprobe.profit;

public class Category {

	// private variables
	int _id, _sid;
	String _name;
	int _exertion;

	// Empty constructor
	public Category() {

	}

	// constructor
	public Category(int id, String name, int exertion, int sid) {
		this._id = id;
		this._name = name;
		this._exertion = exertion;
		this._sid = sid;
	}
	
	public Category(String name, int exertion) {
		this._name = name;
		this._exertion = exertion;
	}

	

	@Override
	public String toString() {
		return this._name;
	}

}