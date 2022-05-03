/**
 * This is the class to set the individual image layout used in the adapter
 *
 * NO EDITING NEEDED
 *
 * created by Harry Akitt 16/03/2022
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
