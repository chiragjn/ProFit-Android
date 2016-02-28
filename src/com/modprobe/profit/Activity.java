package com.modprobe.profit;

public class Activity {
    
    //private variables
    int _id,_sid;
    SubCat _parent;
    int _duration, _intensity, _fitons;
    String date;
    public int sectionManager;
    public int sectionFirstPosition;

    
    boolean isHeader = false; // not related to db
    // Empty constructor
    public Activity(){
         
    }
    // constructor
    public Activity(int id, SubCat parent, int duration, int intensity , int sid , int fitons){
        this._id = id;
        this._duration = duration;
        this._parent = parent;
        this._intensity = intensity;
        this._fitons = fitons;
        this._sid = sid;
    }
    
    public Activity(boolean isHeader, String date,int sectionManger, int sectionFirstPosition) {
    	this.isHeader = isHeader; 
    	this.sectionFirstPosition = sectionFirstPosition;
    	this.sectionManager = sectionManger;
    	this.date = date;
    }
     
    // constructor
    public Activity(SubCat parent, int duration, int intensity , int fitons){
        this._duration = duration;
        this._parent = parent;
        this._intensity = intensity;
        this._fitons = fitons;
    }
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return this._parent._name;
    }
     
}