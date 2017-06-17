package com.example.broadcastreceiverdemo;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sz132 on 2017/6/8.
 */

public class ActivityList {
    private static List<Activity> list = new ArrayList<>();

    public static void addActivity(Activity activity){
        list.add(activity);
    }

    public static void removeActivity(Activity activity){
        list.remove(activity);
    }

    public static void finishAllActivity(){
        if (list != null){
            for (Activity activity : list){
                if (!activity.isFinishing()){
                    activity.finish();
                }
            }
        }
    }
}
