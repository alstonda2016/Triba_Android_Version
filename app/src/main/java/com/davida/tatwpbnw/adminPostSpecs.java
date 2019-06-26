package com.davida.tatwpbnw;

/**
 * Created by davida on 9/1/18.
 */

public class adminPostSpecs {

    postClass post;
    Long postRemovalTime;
    int postRemovalReason;
    String postRemover;
    String postCreatorID;

    public adminPostSpecs() {


    }

    public adminPostSpecs(postClass Post,
                          Long PostRemovalTime,
                          int PostRemovalReason,
                          String PostRemover,
                          String PostCreatorID) {

        this.post = Post;
        this.postRemovalTime = PostRemovalTime;
        this.postRemovalReason = PostRemovalReason;
        this.postRemover = PostRemover;
        this.postCreatorID = PostCreatorID;


    }
}


