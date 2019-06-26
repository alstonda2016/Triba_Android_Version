package com.davida.tatwpbnw;

import java.io.Serializable;

/**
 * Created by davida on 8/21/18.
 */

public class postChatObject implements Serializable {


   public boolean postWasFlagged = false;
    public String postCreatorID = "";
    //boolean postHasBubbles = false;
    public String postText = "";
    public String postID = "";
    public double postLat = 0.0;
    public double postLong = 0.0;
    public long postTime = 0;
    public long postTimeInverse = 0;
    public String postUserName = "";
    public  String postType = "";
    public  String postDescription = "";
    public String creatorName = "";
    public  int ratingBlend = 0;
    public int ratingBlendInverse = 0;
    public long postVisibilityStartTime = 0;
    public long postVisibilityEndTime= 0;
    public  int textCount= 0;
    public  int textCountInverse= 0;
    public  postLocationsClass postLocations;
    public  postSpecDetails postSpecs;
    public  String postBranch1 = "unavailableForSomeReason";
    public   String postBranch2 = "unavailableForSomeReason";
    public   int chatVersionType= 1;
    public   double messageRanking = 0;
    public   double ratingRanking = 0;
    public   boolean postIsActive = true;
    public   String originalAuthorID = "";
    public   String originalPostID = "";

    public postChatObject() {
    }


    public postChatObject(String PostCreatorID ,
                          String PostText,
                          String PostID ,
                         Double PostLat ,
                         Double PostLong ,
                         long PostTime ,
                          long PostTimeInverse ,
                        String  PostUserName ,
                         String PostType ,
                         String PostDESCRIPTION,
                         String CreatorName, postLocationsClass PostLocations) {

        this.postCreatorID = PostCreatorID;
        this.postText = PostText;
        this.postID = PostID;
        this.postLat = PostLat;
        this.postLong = PostLong;
        this.postTime = PostTime;
        this.postTimeInverse = PostTimeInverse;
        this.postUserName = PostUserName;
        this.postType = PostType;
        this.postDescription = PostDESCRIPTION;
        this.creatorName = CreatorName;
        this.postLocations = PostLocations;
    }



    public postChatObject(String PostCreatorID ,
                          String PostText,
                          String PostID ,
                          Double PostLat ,
                          Double PostLong ,
                          long PostTime ,
                          long PostTimeInverse ,
                          String  PostUserName ,
                          String PostType ,
                          String PostDESCRIPTION,
                          String CreatorName,
                          int RatingBlend,
                          postLocationsClass PostLocations,
                          long PostVisibilityStartTime,
                          long PostVisibilityEndTime,
                          double MessageRanking,
                          double RatingRanking,
                          boolean PostIsActive,
                          postSpecDetails PostSpecs,
                          String OriginalAuthorID,
                          boolean PostWasFlagged,
                          String OriginalPostID,
                          int ChatVersionType

                          ) {

        this.postCreatorID = PostCreatorID;
        this.postText = PostText;
        this.postID = PostID;
        this.postLat = PostLat;
        this.postLong = PostLong;
        this.postTime = PostTime;
        this.postTimeInverse = PostTimeInverse;
        this.postUserName = PostUserName;
        this.postType = PostType;
        this.postDescription = PostDESCRIPTION;
        this.creatorName = CreatorName;
        this.ratingBlend = RatingBlend;
        this.postLocations = PostLocations;
        this.postVisibilityStartTime = PostVisibilityStartTime;
        this.postVisibilityEndTime = PostVisibilityEndTime;
        this.messageRanking = MessageRanking;
        this.ratingRanking = RatingRanking;
        this.postIsActive = PostIsActive;
        this.postSpecs = PostSpecs;
        this.originalAuthorID = OriginalAuthorID;
        this.postWasFlagged = PostWasFlagged;
        this.originalPostID = OriginalPostID;
        this.chatVersionType = ChatVersionType;
    }


}
