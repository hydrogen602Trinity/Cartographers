package com.ctmrepository.model;

public class SearchFilterLong extends SearchFilter {
    long lower_bound_long;
    long upper_bound_long;

    public SearchFilterLong() {
        search_filter = "";
        lower_bound_long = 0;
        upper_bound_long = 0;
    }

    public SearchFilterLong(String search_filter, long lower_bound_long, long upper_bound_long) {
        this.search_filter = search_filter;
        this.lower_bound_long = lower_bound_long;
        this.upper_bound_long = upper_bound_long;
    }

    public long getLower_bound_long() {
        return this.lower_bound_long;
    }

    public void setLower_bound_long(long lower_bound_long) {
        this.lower_bound_long = lower_bound_long;
    }

    public long getUpper_bound_long() {
        return this.upper_bound_long;
    }

    public void setUpper_bound_long(long upper_bound_long) {
        this.upper_bound_long = upper_bound_long;
    }

    public boolean isInBounds(long value) {
        return value >= lower_bound_long
                && value <= upper_bound_long;
    }

    public boolean isInBounds(int value) {
        return value >= lower_bound_long
                && value <= upper_bound_long;
    }

    @Override
    public String toString() {
        return search_filter + " Filter [" + lower_bound_long + "," + upper_bound_long + "]";
    }
}
