package com.davida.tatwpbnw;

/**
 * Created by davida on 9/1/18.
 */

public class strikedPostObject {

    String postRemoverPosition;
    String postRemoverID ;
    int postRemovalReason ;
    postClass postObject ;

    public strikedPostObject(){}

    public strikedPostObject( String PostRemoverPosition,
            String PostRemoverID,
            int PostRemovalReason,
            postClass PostObject ){

        this.postRemoverPosition = PostRemoverPosition;
        this.postRemoverID = PostRemoverID;
        this.postRemovalReason = PostRemovalReason;
        this.postObject = PostObject;


    }


}
