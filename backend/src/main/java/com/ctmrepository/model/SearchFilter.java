package com.ctmrepository.model;

public class SearchFilter {
    String search_filter;
    String lower_bound;
    String upper_bound;

    public SearchFilter() {
        search_filter = "";
        lower_bound = "";
        upper_bound = "";
    }

    public SearchFilter(String search_filter, String lowerBound, String upperBound) {
        this.search_filter = search_filter;
        this.lower_bound = lowerBound;
        this.upper_bound = upperBound;
    }

    public String getSearch_filter() {
        return this.search_filter;
    }

    public void setSearch_filter(String search_filter) {
        this.search_filter = search_filter;
    }

    public String getLower_bound() {
        return this.lower_bound;
    }

    public void setLower_bound(String lower_bound) {
        this.lower_bound = lower_bound;
    }

    public String getUpper_bound() {
        return this.upper_bound;
    }

    public void setUpper_bound(String upper_bound) {
        this.upper_bound = upper_bound;
    }

    public boolean isInBounds(String value) {
        return value.compareTo(lower_bound) >= 0
                && value.compareTo(upper_bound) <= 0;
    }

    public String toString() {
        return search_filter + " Filter [" + lower_bound + "," + upper_bound + "]";
    }
}
