package com.ctmrepository.model;

import java.util.List;

public class SearchPage {
    private int max_pages;
    public List<MinecraftMap> mapList;

    public SearchPage(int max_pages, List<MinecraftMap> map) {
        this.max_pages = max_pages;
        this.mapList = map;
    }

    public int getMaxPages() {
        return max_pages;
    }

    public String toString() {
        return "Max Page: "+max_pages+" \nList: "+mapList.toString();
    }
}
