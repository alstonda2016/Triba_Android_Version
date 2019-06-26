package com.davida.tatwpbnw;

import android.app.Application;

/**
 * Created by davida on 8/21/18.
 */


public class globalClass extends Application {

    private int notedPostsNumber;
    private    boolean shouldReset ;



    public int getNotedPostsNumber() {
        return notedPostsNumber;
    }



    public void setNotedPostsNumber(int NotedPostsNumber) {
        notedPostsNumber = NotedPostsNumber;
    }
    private static globalClass instance = new globalClass();

    // Getter-Setters
    public static globalClass getInstance() {
        return instance;
    }

}

