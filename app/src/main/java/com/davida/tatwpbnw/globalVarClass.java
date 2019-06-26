package com.davida.tatwpbnw;

import android.app.Application;

/**
 * Created by davida on 8/2/18.
 */

public class globalVarClass  extends Application {
    private  static  String name = "no";
    private  static   boolean shouldReset = false ;
    private  static   String postID = "" ;
    private  static   String addOrSubtractID = "" ;



    public String getName() {
        return name;
    }

    public static String getAddOrSubtractIDD() {
        return globalVarClass.addOrSubtractID;
    }

    public static String getPostIDD() {
        return globalVarClass.postID;
    }

    public static void setAddOrSubtractID(String addOrSubtractID) {
        globalVarClass.addOrSubtractID = addOrSubtractID;
    }

    public static void setPostID(String postID) {
        globalVarClass.postID = postID;
    }

    public static void setShouldReset(boolean shouldReset) {
        globalVarClass.shouldReset = shouldReset;
    }

    public static void setName(String PassedString) {
        globalVarClass.name = PassedString;
    }


    public static boolean getShouldReset(){

        return globalVarClass.shouldReset;
    }

    public static String getNameString(){

        return globalVarClass.name;
    }

}
