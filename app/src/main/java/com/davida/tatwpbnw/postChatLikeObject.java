package com.davida.tatwpbnw;

import java.io.Serializable;

/**
 * Created by davida on 8/22/18.
 */

public class postChatLikeObject implements Serializable {


   public String rating = "";
    public String postLink = "";
    public String likedUserID = "";
    public String commentID = "";
    public  String postID = "";

    public  postLocationsClass postLocations;


    public postChatLikeObject() {
    }




    public postChatLikeObject(String Rating, String PostLink, postLocationsClass PostLocations ,String LikedUserID,String PostID,String CommentID ) {
        this.rating = Rating;
         this.postLink = PostLink;
         this.likedUserID = LikedUserID;
         this.commentID = CommentID;
         this.postID = PostID;

        this.postLocations = PostLocations;



    }

}
