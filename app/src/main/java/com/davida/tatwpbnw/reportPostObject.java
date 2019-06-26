package com.davida.tatwpbnw;

import java.io.Serializable;

/**
 * Created by davida on 9/1/18.
 */

public class reportPostObject implements Serializable {



   public int postReportReason;
    public postClass postObject;
   public String postReporterID;



    public reportPostObject(){}

    public reportPostObject(   int PostReportReason,
            postClass PostObject,
            String PostReporterID ){

    this.postReportReason = PostReportReason;
        this.postObject = PostObject;
        this.postReporterID = PostReporterID;


    }
}
