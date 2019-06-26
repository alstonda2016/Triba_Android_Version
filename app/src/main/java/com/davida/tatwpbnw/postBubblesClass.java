package com.davida.tatwpbnw;

import java.io.Serializable;

/**
 * Created by davida on 1/8/18.
 */

public class postBubblesClass implements Serializable {
   public bubbleClass bubble1;
   public bubbleClass bubble2;
   public bubbleClass bubble3;
  public   bubbleClass bubble4;
   public bubbleClass bubble5;


    public postBubblesClass() {
    }

    public postBubblesClass(bubbleClass bubble1, bubbleClass bubble2,
                            bubbleClass bubble3, bubbleClass bubble4, bubbleClass bubble5) {
        this.bubble1 = bubble1;
        this.bubble2 = bubble2;
        this.bubble3 = bubble3;
        this.bubble4 = bubble4;
        this.bubble5 = bubble5;

    }
}
