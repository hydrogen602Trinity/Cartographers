package com.ctmrepository.model;

import java.util.List;

public class SearchResult {
    public int max_page;
    public List<MinecraftMap> data;

    public SearchResult(int max_page, List<MinecraftMap> data) {
        this.max_page = max_page;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Max Page: " + max_page + " \nList: " + data.toString();
    }
}
