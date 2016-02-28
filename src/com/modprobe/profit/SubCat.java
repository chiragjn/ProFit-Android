package com.modprobe.profit;

public class SubCat {
    
    //private variables
    int _id,_sid,_exertion;
    String _name;
    Category _parent;
     
    // Empty constructor
    public SubCat(){
         
    }
    // constructor
    public SubCat(int id, String name, int exertion, Category parent,int sid){
        this._id = id;
        this._name = name;
        this._exertion = exertion;
        this._parent = parent;
        this._sid = sid;
    }
     
    // constructor
    public SubCat(String name, int exertion, Category parent){
        this._name = name;
        this._exertion = exertion;
        this._parent = parent;
    }
    
    @Override
	public String toString() {
		return this._name;
	}
    
}