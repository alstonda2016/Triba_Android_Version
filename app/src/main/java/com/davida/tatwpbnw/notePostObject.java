package com.davida.tatwpbnw;

import java.io.Serializable;

/**
 * Created by davida on 8/21/18.
 */

public class notePostObject implements Serializable {


    public String postNoterID;
    public String postID;
    public postLocationsClass postLocations;
    public  postSpecDetails postSpecs;




    public notePostObject() {
    }

    public notePostObject(  String PostNoterID,
                                   String PostID,
                                   postLocationsClass PostLocations,
                            postSpecDetails PostSpecs) {

        this.postNoterID = PostNoterID;
       this.postLocations = PostLocations;
        this.postSpecs = PostSpecs;
        this.postID = PostID;



    }
}
