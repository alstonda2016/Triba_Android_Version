package com.davida.tatwpbnw;

import java.io.Serializable;

/**
 * Created by davida on 1/8/18.
 */




//Ignore this completely






public class bubbleClass implements Serializable {

    String bubbleID ;
    String bubbleName ;
    int bubbleType ;
    String bubbleState;
    String bubbleCity ;
    String bubbleBorough;
    String bubbleDescription;
    String bubbleImageID;
    String bubbleActiveStatus;
    Double[] bubbleLats;
    Double[] bubbleLongs;




    public bubbleClass() {

    }
    public bubbleClass(String bubbleID,
                       String bubbleName ,
                       int bubbleType ,
                       String bubbleState,
                       String bubbleCity ,
                       String bubbleBorough) {
    this.bubbleID = bubbleID;
        this.bubbleName = bubbleName;
        this.bubbleType = bubbleType;
        this.bubbleState = bubbleState;
        this.bubbleCity = bubbleCity;
        this.bubbleBorough = bubbleBorough;
        this.bubbleImageID = "";

    }

    public String getBubbleID() {
        return bubbleID;
    }

    public int getBubbleType() {
        return bubbleType;
    }

    public String getBubbleBorough() {
        return bubbleBorough;
    }

    public String getBubbleCity() {
        return bubbleCity;
    }

    public String getBubbleName() {
        return bubbleName;
    }

    public String getBubbleState() {
        return bubbleState;
    }

   /* public Double[] getBubbleLats() {
        return bubbleLats;
    }

    public Double[] getBubbleLongs() {
        return bubbleLongs;
    }
*/
    public String getBubbleActiveStatus() {
        return bubbleActiveStatus;
    }

    public String getBubbleDescription() {
        return bubbleDescription;
    }

    public String getBubbleImageID() {
        return bubbleImageID;
    }
}

