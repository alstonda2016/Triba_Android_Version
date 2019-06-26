package com.davida.tatwpbnw;

import java.io.Serializable;

/**
 * Created by davida on 3/20/18.
 */

public class postSpecDetails implements Serializable {

    public  boolean isActive = true;
    public boolean hasBeenCleared = false;
    public long postTime = -1;
    public long postRemovalTime = -1;
    public String postRemover = "";
    public int postRemovalReason = -1;


    public postSpecDetails() {
    }

    public postSpecDetails( Boolean IsActive,
            Boolean HasBeenCleared,
            Long PostTime,
            Long PostRemovalTime, String PostRemover, int PostRemovalReason) {

            this.isActive = IsActive;
            this.hasBeenCleared = HasBeenCleared;
            this.postTime = PostTime;
            this.postRemovalTime = PostRemovalTime;
        this.postRemover = PostRemover;
        this.postRemovalReason = PostRemovalReason;



    }


    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setHasBeenCleared(Boolean hasBeenCleared) {
        this.hasBeenCleared = hasBeenCleared;
    }

    public void setPostRemovalReason(int postRemovalReason) {
        this.postRemovalReason = postRemovalReason;
    }

    public void setPostRemovalTime(Long postRemovalTime) {
        this.postRemovalTime = postRemovalTime;
    }

    public void setPostRemover(String postRemover) {
        this.postRemover = postRemover;
    }

    public void setPostTime(Long postTime) {
        this.postTime = postTime;
    }


    public boolean verifyPost(){


        if(postTime == -1){

        return false;
        }
        if(postRemovalTime == -1){

            return false;

        }
        if(postRemover.toString().isEmpty()){
            return false;


        }
        if(postRemovalReason == -1){
            return false;

        }

        return true;

    }
}
