package com.modprobe.profit;

public class Suggestion {
	Goal _goal;
	SubCat _subcat;
	int _duration,_intensity;
	
	public Suggestion(){
		
	}
	
	public Suggestion(Goal goal, SubCat subcat, int duration , int intensity){
		this._goal = goal;
		this._subcat = subcat;
		this._duration = duration;
		this._intensity = intensity;
	}
}
