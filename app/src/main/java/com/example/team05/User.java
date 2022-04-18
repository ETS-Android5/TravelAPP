package com.example.team05;

public class User {
    private String name;
    //isSelect is used to judge the item in the adapter and set the get set method
    private boolean isSelect;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
