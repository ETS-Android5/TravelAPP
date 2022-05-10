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
    private String description;


    public slideItems(int image, String caption,String description) {
        this.image = image;
        this.caption = caption;
        this.description = description;
    }

    public int getFeatured_image() {
        return image;
    }

    public String getCaption() {
        return caption;
    }

    public String getDescription() {
        return description;
    }
}
