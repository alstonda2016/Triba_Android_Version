package com.davida.tatwpbnw;

import java.io.Serializable;

/**
 * Created by davida on 12/31/17.
 */

public class postLocationsClass implements Serializable {
    public String box00;
    public String box01;
    public String box02;
    public String box10;
    public String box11;
    public String box12;
    public String box20;
    public String box21;
    public String box22;

    public postLocationsClass(){}

    public postLocationsClass(String b00, String b01, String b02, String b10, String b11, String b12, String b20,
                              String b21, String b22){
        this.box00 = b00;
        this.box01 = b01;
        this.box02 = b02;
        this.box10 = b10;
        this.box11 = b11;
        this.box12 = b12;
        this.box20 = b20;
        this.box21 = b21;
        this.box22 = b22;



    }

    public String getBox00() {
        return box00;
    }

    public String getBox01() {
        return box01;
    }

    public String getBox02() {
        return box02;
    }

    public String getBox10() {
        return box10;
    }

    public String getBox11() {
        return box11;
    }

    public String getBox12() {
        return box12;
    }

    public String getBox20() {
        return box20;
    }

    public String getBox21() {
        return box21;
    }

    public String getBox22() {
        return box22;
    }


    public boolean verifyPost(){

        if(box00.toString().isEmpty()){
            return  false;
        }
        if(box01.toString().isEmpty()){
            return  false;
        }if(box02.toString().isEmpty()){
            return  false;
        }if(box10.toString().isEmpty()){
            return  false;
        }if(box11.toString().isEmpty()){
            return  false;
        }if(box12.toString().isEmpty()){
            return  false;
        }if(box20.toString().isEmpty()){
            return  false;
        }if(box21.toString().isEmpty()){
            return  false;
        }
        if(box22.toString().isEmpty()){
            return  false;
        }

        return true;

    }
}
