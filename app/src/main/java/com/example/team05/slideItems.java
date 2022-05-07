/**
 ***** Description *****
 * This is the class to set the individual image layout used in the adapter
 *
 ***** Author(s)  *****
 * Harry Akitt
 * -Key functionality
 *
 * **/

package com.example.team05;

public class slideItems {

    private int image;
    private String caption;


    public slideItems(int image, String caption) {
        this.image = image;
        this.caption = caption;
    }

    public int getFeatured_image() {
        return image;
    }

    public String getCaption() {
        return caption;
    }
}
