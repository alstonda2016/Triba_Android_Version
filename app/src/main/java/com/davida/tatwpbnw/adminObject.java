package com.davida.tatwpbnw;

/**
 * Created by davida on 9/2/18.
 */

public class adminObject {

    String moderatorID ;
    String moderatorName ;
    double postLat;
    double postLong ;


    public adminObject() {
    }

    public adminObject(  String ModeratorID,
            String ModeratorName,
            double PostLat,
            double PostLong ) {

        this.moderatorID = ModeratorID;
        this.moderatorName = ModeratorName;
        this.postLat = PostLat;
        this.postLong = PostLong;


    }

}
