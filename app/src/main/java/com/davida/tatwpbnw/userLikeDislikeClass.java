package com.davida.tatwpbnw;

/**
 * Created by davida on 1/13/18.
 */

public class userLikeDislikeClass {
    public String rating;
   public String postLink;
    public String likedUserID;
    public String postID;
    public postLocationsClass postLocations;
    public String commentID;




    public userLikeDislikeClass() {
    }

    public userLikeDislikeClass(String uRating, String uLink, String lID, String pID, postLocationsClass pLC, String CommentID) {

        this.rating = uRating;
        this.postLink = uLink;
        this.likedUserID = lID;
        this.postID = pID;
        this.postLocations = pLC;
        this.commentID = CommentID;
    }

}
