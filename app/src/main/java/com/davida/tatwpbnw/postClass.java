package com.davida.tatwpbnw;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by davida on 12/31/17.
 */
//public class postClass implements Serializable {

public class postClass implements Serializable {





  public  boolean postWasFlagged = false;
    public String postCreatorID = "";
      //boolean postHasBubbles = false;
    public  String postText = "";
    public  String postID = "";
    public double postLat = 0.0;
    public double postLong = 0.0;
    public long postTime = 0;
    public long postTimeInverse = 0;
    public String postUserName = "";
    public String postType = "";
    public String postDescription = "";
    public String creatorName = "";
    public int ratingBlend = 0;
    public int ratingBlendInverse = 0;
    public long postVisibilityStartTime = 0;
    public long postVisibilityEndTime= 0;
    public int textCount= 0;
    public int textCountInverse= 0;
    public postLocationsClass postLocations;
    public postSpecDetails postSpecs;
    public String postBranch1 = "unavailableForSomeReason";
    public String postBranch2 = "unavailableForSomeReason";
    public int postVersionType= 0;
    public int messageRanking = 0;
    public int ratingRanking = 0;

    public String postLocationString = "";
    public long postEventStartTime = 0;
    public long postEventEndTime = 0;
    public String postTimeDetailsString = "";




    public postClass() {
    }





   /* public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(ratingRanking);
    }

    public static final Parcelable.Creator<postClass> CREATOR
            = new Parcelable.Creator<postClass>() {
        public postClass createFromParcel(Parcel in) {
            return new postClass(in);
        }

        public postClass[] newArray(int size) {
            return new postClass[size];
        }
    };

    private postClass(Parcel in) {
        ratingRanking = in.readInt();
    }
*/


    /*

    "postCreatorID":userID as AnyObject,
            "postText":postText as AnyObject,
            "postID":postID as AnyObject
                ,"postLat":fullLat as AnyObject
                ,"postLong":fullLong as AnyObject,
            "postTime":postTime as AnyObject,
            "postTimeInverse":(postTime * -1)as AnyObject,
                 "postUserName":userName as AnyObject,
            "postType":postType as AnyObject,
            "postDescription":postDesc as AnyObject,
            "creatorName":userFullName as AnyObject,
            "postLocations":postLocations as AnyObject,
            "ratingBlend":1 as AnyObject,
                 "textCount":0 as AnyObject,
                 "messageRanking":0 as AnyObject,
                 "ratingRanking":ratingRanking as AnyObject,
            "postVisibilityStartTime":postTime as AnyObject,
            "postVisibilityEndTime":postVisEndTime as AnyObject,
            "postBranch1":postTimeZone as AnyObject,
            "postBranch2":postLatRegionNODecimal as AnyObject,
            "postWasFlagged":false as AnyObject,
                 "postSpecs": postSpecs.toFBObject() as AnyObject,
                 "postVersionType":1 as AnyObject
*/




    public postClass( boolean PostWasFlagged, String PostCreatorID, String PostText,String PostID, double PostLat, double PostLong, long PostTime, long PostTimeInverse, String PostUserName,
                      String PostType, String PostDescription,String CreatorName, int RatingBlend,  long PostVisibilityStartTime , long PostVisibilityEndTime, int TextCount,  postLocationsClass PostLocations,
                      postSpecDetails PostSpecs, String PostBranch1 ,String PostBranch2 , int PostVersionType, int MessageRanking, int RatingRanking) {

        this.postWasFlagged = PostWasFlagged;
        this.postCreatorID = PostCreatorID;
        this.postText = PostText;
        this.postID = PostID;
        this.postLat = PostLat;
        this.postLong = PostLong;
        this.postTime = PostTime;
        this.postTimeInverse = PostTimeInverse;
        this.postUserName = PostUserName;
        this.postType = PostType;
        this.postDescription = PostDescription;
        this.creatorName = CreatorName;
        this.ratingBlend = RatingBlend;
        this.postVisibilityStartTime = PostVisibilityStartTime;
        this.postVisibilityEndTime= PostVisibilityEndTime;
        this.textCount= TextCount;
        this.postLocations = PostLocations;
        this.postSpecs = PostSpecs;
        this.postBranch1 = PostBranch1;
        this.postBranch2 = PostBranch2;
        this.postVersionType= PostVersionType;
        this.messageRanking = MessageRanking;
        this.ratingRanking = RatingRanking;

    }


//Has everything, shouldn't be used
    public postClass( boolean PostWasFlagged, String PostCreatorID, boolean PostHasBubbles, String PostText,String PostID, double PostLat, double PostLong, long PostTime, long PostTimeInverse, String PostUserName,
                      String PostType, String PostDescription,String CreatorName, int RatingBlend, int RatingBlendInverse , long PostVisibilityStartTime , long PostVisibilityEndTime, int TextCount, int TextCountInverse, postLocationsClass PostLocations,
                      postSpecDetails PostSpecs, String PostBranch1 ,String PostBranch2 , int PostVersionType, int MessageRanking, int RatingRanking) {

        this.postWasFlagged = PostWasFlagged;
        this.postCreatorID = PostCreatorID;
       // this.postHasBubbles = PostHasBubbles;
        this.postText = PostText;
        this.postID = PostID;
        this.postLat = PostLat;
        this.postLong = PostLong;
        this.postTime = PostTime;
        this.postTimeInverse = PostTimeInverse;
        this.postUserName = PostUserName;
        this.postType = PostType;
        this.postDescription = PostDescription;
        this.creatorName = CreatorName;
        this.ratingBlend = RatingBlend;
        this.ratingBlendInverse = RatingBlendInverse;
        this.postVisibilityStartTime = PostVisibilityStartTime;
        this.postVisibilityEndTime= PostVisibilityEndTime;
        this.textCount= TextCount;
        this.textCountInverse= TextCountInverse;
        this.postLocations = PostLocations;
        this.postSpecs = PostSpecs;
        this.postBranch1 = PostBranch1;
        this.postBranch2 = PostBranch2;
        this.postVersionType= PostVersionType;
        this.messageRanking = MessageRanking;
        this.ratingRanking = RatingRanking;

    }





    public Double getPostLat() {
        return postLat;
    }

    public Double getPostLong() {
        return postLong;
    }

    public Long getPostTime() {
        return postTime;
    }

    public postLocationsClass getPostLocations() {
        return postLocations;
    }

    public String getCreatorName() {
        return creatorName;
    }


    public String getPostID() {
        return postID;
    }

    public String getPostText() {
        return postText;
    }

    public String getPostType() {
        return postType;
    }

    public String getPostUserName() {
        return postUserName;
    }


    public int getRatingBlend() {
        return ratingBlend;
    }

    public postSpecDetails getPostSpecs() {
        return postSpecs;
    }

    public int getRatingBlendInverse() {
        return ratingBlendInverse;
    }

    public int getMessageRanking() {
        return messageRanking;
    }

    public int getPostVersionType() {
        return postVersionType;
    }

    public int getRatingRanking() {
        return ratingRanking;
    }

    public int getTextCount() {
        return textCount;
    }

    public int getTextCountInverse() {
        return textCountInverse;
    }

    public long getPostTimeInverse() {
        return postTimeInverse;
    }

    public long getPostVisibilityEndTime() {
        return postVisibilityEndTime;
    }

    public long getPostVisibilityStartTime() {
        return postVisibilityStartTime;
    }

    public String getPostBranch1() {
        return postBranch1;
    }

    public String getPostBranch2() {
        return postBranch2;
    }

    public String getPostCreatorID() {
        return postCreatorID;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public boolean verifyPost(){

        if(postID.toString().isEmpty()){
            Log.d("HERE","A");
            return false;
        }



        if(postCreatorID.toString().isEmpty()){
            Log.d("HERE","B");

            return  false;
        }

        //boolean postHasBubbles = false;
        if(postText.toString().isEmpty()){
            Log.d("HERE","C");

            return  false;

        }

        if(postID.toString().isEmpty()){
            Log.d("HERE","D");

            return  false;

        }

        if(postLat == 0.0){
            Log.d("HERE","E");

            return  false;

        }

        if(postLong == 0.0){
            Log.d("HERE","F");

            return  false;

        }

        if(postTime == 0 ){
            Log.d("HERE","G");

            return  false;

        }

        if(postTimeInverse == 0){
            Log.d("HERE","H");

            return  false;

        }

        if(postUserName.toString().isEmpty()){
            Log.d("HERE","I");

            return  false;

        }

        if(postType.toString().isEmpty()){
            Log.d("HERE","J");

            return  false;

        }
/*
        if(postDescription.toString().isEmpty()){
            Log.d("HERE","K");

            return  false;

        }
        */

        if(creatorName.toString().isEmpty()){
            Log.d("HERE","L");

            return  false;

        }


/*
        if(!postLocations.verifyPost()){
            return  false;

        }

        if(!postSpecs.verifyPost()){
            return  false;

        }
        */

    if(postVersionType != 0){
        if(postVersionType > 1){

            /*
            public String postLocationString = "";
            public long postEventStartTime = 0;
            public long postEventEndTime = 0;
            public String postTimeDetailsString = "";

            */
            //these are the variables that only exist in v2
            //it's fine just to not test for these since they're never used
            //Hopefully this will change in the future


        }

    }
    else{
        return false;
    }


        if(postBranch1.toString().isEmpty()){
            Log.d("HERE","M");

            return  false;

        }

        if(postBranch2.toString().isEmpty()){
            Log.d("HERE","N");

            return  false;

        }
    return true;




    }


}



