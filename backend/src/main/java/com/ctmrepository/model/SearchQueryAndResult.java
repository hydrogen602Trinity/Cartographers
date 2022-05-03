package com.ctmrepository.model;

import java.util.List;

public class SearchQueryAndResult {
    public String q;
    public int max_pages;
    public boolean strict;
    public List<Long> maps;

    public SearchQueryAndResult(String q, int max_pages, boolean strict, List<Long> maps) {
        this.q = q;
        this.max_pages = max_pages;
        this.strict = strict;
        this.maps = maps;
    }

    public String toString() {
        return strict ? "Strict Search \""+q+"\":: "+maps.toString() : "Fuzzy Search \""+q+"\":: "+maps.toString();
    }
}
