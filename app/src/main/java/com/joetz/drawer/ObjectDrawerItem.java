package com.joetz.drawer;

/**
 * This class holds all the information of a single item that can be loaded into the navigation drawer.
 */
public class ObjectDrawerItem {

    public int icon;
    public String name;

    // Constructor.
    public ObjectDrawerItem(int icon, String name) {

        this.icon = icon;
        this.name = name;
    }
}
