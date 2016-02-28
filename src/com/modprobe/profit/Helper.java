package com.modprobe.profit;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

public class Helper {

	static int getFitons( int duration, int intensity, int cal_fac) {
		int ans;
		int weight = AppController.getInstance().prefs.getInt("weightKg", 50);
		int bodytypeint = AppController.getInstance().prefs.getInt("bodyType", 2);
		float body_type = Constants.bodyTypeFact[bodytypeint];
		ans = (int) ((float)(cal_fac * weight * duration * intensity) / (float)(body_type * 57 * 30));
		return ans/10;
	}
	
	static int getCals(int fitons) {
		int bodytypeint = AppController.getInstance().prefs.getInt("bodyType", 2);
		float body_type = Constants.bodyTypeFact[bodytypeint];
		float bmi = AppController.getInstance().prefs.getFloat("bmi", 30f);
		return (int) (fitons * bmi / body_type);
	}
	
	//AllTimes
	
		static int getAllTimeFitons(Context context){
			int ret = 0;
			ActivityDataSource ads = new ActivityDataSource(context);
			ads.open();
			List<Activity> lis = ads.getAllActivites();
			ads.close();
			for(Activity a : lis){
				ret+= a._fitons;
			}
			return ret;
		}
		
		static int getAllTimeFitonsForCategory(Context context, Category cat){
			int ret = 0;
			ActivityDataSource ads = new ActivityDataSource(context);
			ads.open();
			List<Activity> lis = ads.getAllActivites();
			ads.close();
			for(Activity a : lis){
				if(a._parent._parent._id == cat._id)
					ret+= a._fitons;
			}
			return ret;
		}
		
		static int getAllTimeFitonsForSubCat(Context context, SubCat sc){
			int ret = 0;
			ActivityDataSource ads = new ActivityDataSource(context);
			ads.open();
			List<Activity> lis = ads.getActivities(sc);
			ads.close();
			for(Activity a : lis){
				ret+= a._fitons;
			}
			return ret;
		}
		
		//Today
		static int getTodayFitons(Context context){
			int ret = 0;
			ActivityDataSource ads = new ActivityDataSource(context);
			ads.open();
			List<Activity> lis = ads.getAllActivites();
			ads.close();
			int counter = 0;
			for(Activity a : lis){
				if(counter>3)
					ret+= a._fitons;
				counter++;
			}
			return ret;
		}
		
		static int getTodayFitonsForCategory(Context context, Category cat){
			int ret = 0;
			ActivityDataSource ads = new ActivityDataSource(context);
			ads.open();
			List<Activity> lis = ads.getAllActivites();
			ads.close();
			int counter = 0;
			for(Activity a : lis){
				if(counter>3 && cat._id == a._parent._parent._id)
					ret+= a._fitons;
				counter++;
			}
			return ret;
		}
		
		static int getTodayFitonsForSubCat(Context context, SubCat sc){
			int ret = 0;
			ActivityDataSource ads = new ActivityDataSource(context);
			ads.open();
			List<Activity> lis = ads.getActivities(sc);
			ads.close();
			int counter = 0;
			for(Activity a : lis){
				if(counter>3)
					ret+= a._fitons;
				counter++;
			}
			return ret;
		}
		
		//Goal Related Methods
		
		static int getGoalFitons(Context context){
			int ret = 0;
			GoalDataSource gds = new GoalDataSource(context);
			gds.open();
			List<Goal> lis = gds.getAllGoals();
			gds.close();
			for(Goal g : lis){
				if(g._now==1){
					String arr[] = g._fitons.split(",");
					for(int i = 0 ; i<arr.length; i++)
						ret+=Integer.parseInt(arr[i]);
				}
				
			}
			return ret;
		}
		
		static int getGoalFitonsForCategory(Context context, Category cat){
			int ret = 0;
			GoalDataSource gds = new GoalDataSource(context);
			gds.open();
			List<Goal> lis = gds.getAllGoals();
			gds.close();
			for(Goal g : lis){
				if(g._now==1){
					String arr[] = g._fitons.split(",");
					ret+=Integer.parseInt(arr[cat._id-1]);
				}
				
			}
			return ret;
		}
		
		
		//Suggestion Generation Algo
		
		static ArrayList<Suggestion> getSuggestionForGoal(Context context , Goal goal){
			ArrayList<Suggestion> sugs = new ArrayList<Suggestion>();
			String arr[] = goal._fitons.split(",");
			int fits[] = new int[arr.length];
			for(int i = 0 ; i< arr.length; i++){
				fits[i] = Integer.parseInt(arr[i]);
			}
			
			//We need fits[i] from i-th Category
			// Check history from i-th Category and Assign Fitons Almost Equally amongst them
			ActivityDataSource ads = new ActivityDataSource(context);
			ads.open();
			List<Activity> lis = ads.getAllActivites();
			ads.close();
			
			List<SubCat>[] his = new List[arr.length];
			for(int i = 0 ; i< arr.length; i++){
				his[i] = new ArrayList<SubCat>();
			}
			
			for(Activity a : lis){
				his[a._parent._parent._id-1].add(a._parent);
			}
			for(int i = 0 ; i< arr.length ; i++){
				//remove duplicates from his[i]
				int vis[] = new int[1000];
				for(int j = 0 ; j< 1000; j++)	vis[j]=0;
				List<SubCat> keep = new ArrayList<SubCat>();
				for(SubCat s : his[i]){
					if(vis[s._id]==0){
						vis[s._id]=1;
						keep.add(s);
					}
				}
				
				his[i].clear();
				for(SubCat s : keep)	{his[i].add(s);Log.e("Keep",""+s);}
			}
			
			for(int i = 0 ; i< arr.length; i++){
				//Each Category
				int subcatcount = his[i].size();
				for(SubCat a : his[i]){
					// a would be subcat done before from category i
					int fitons = fits[i]/subcatcount;
					int intensity = 7;//Default
					int weight = AppController.getInstance().prefs.getInt("weightKg",50);
					float bt = Constants.bodyTypeFact[AppController.getInstance().prefs.getInt("bodyType", 2)];
//					int duration = 
					int duration =  (int) (bt*57*300*fitons);
					duration/=a._exertion*weight*intensity;
					Suggestion ss = new Suggestion(goal, a, duration, intensity);
					sugs.add(ss);
				}
			}
			return sugs;
		}
}
