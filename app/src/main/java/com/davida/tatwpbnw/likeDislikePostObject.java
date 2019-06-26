package com.davida.tatwpbnw;

/**
 * Created by davida on 8/21/18.
 */

public class likeDislikePostObject {


   public String rating;
   public String postLink;
   public postLocationsClass postLocations;
   public String likedUserID;
   public String postID;




    public likeDislikePostObject() {
    }

    public likeDislikePostObject(  String Rating,
            String PostLink,
            postLocationsClass PostLocations,
            String LikedUserID,
            String PostID) {

        this.rating = Rating;
        this.postLink = PostLink;
        this.postLocations = PostLocations;
        this.likedUserID = LikedUserID;
        this.postID = PostID;



    }
}
