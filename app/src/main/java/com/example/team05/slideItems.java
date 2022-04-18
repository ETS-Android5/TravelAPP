/**
 * This is the class to set the individual image layout used in the adapter
 *
 * NO EDITING NEEDED
 *
 * created by Harry Akitt 16/03/2022
 * **/

package com.example.team05;

public class slideItems {

    private int featured_image;
    public slideItems(int hero) {
        this.featured_image = hero;
    }

    public int getFeatured_image() {
        return featured_image;
    }
}
