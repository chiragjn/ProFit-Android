package com.modprobe.profit;

public class SessionActivity {
    
    //private variables
    int _id,_sid;
    int _duration;
    Session _parent;
    SubCat _subcat;
    // Empty constructor
    public SessionActivity(){
         
    }
    // constructor
    public SessionActivity(int id, int duration, Session parent, SubCat subcat, int sid){
        this._id = id;
        this._duration = duration;
        this._subcat = subcat;
        this._parent = parent;
        this._sid = sid;
    }
     
    // constructor
    public SessionActivity( int duration, Session parent, SubCat subcat){
        this._duration = duration;
        this._subcat = subcat;
        this._parent = parent;
    }
    
    @Override
	public String toString() {
		return this._subcat._name;
	}
    
}