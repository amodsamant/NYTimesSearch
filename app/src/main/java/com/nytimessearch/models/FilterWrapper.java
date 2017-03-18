package com.nytimessearch.models;

public class FilterWrapper {

    String beginDate = null;
    String sort = null;
    boolean arts;
    boolean fashionStyle;
    boolean sports;

    public FilterWrapper() {

    }

    public FilterWrapper(String beginDate, String sort, boolean arts,
                         boolean fashionStyle, boolean sports) {
        this.beginDate = beginDate;
        this.sort = sort;
        this.arts = arts;
        this.fashionStyle = fashionStyle;
        this.sports = sports;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean isArts() {
        return arts;
    }

    public void setArts(boolean arts) {
        this.arts = arts;
    }

    public boolean isFashionStyle() {
        return fashionStyle;
    }

    public void setFashionStyle(boolean fashionStyle) {
        this.fashionStyle = fashionStyle;
    }

    public boolean isSports() {
        return sports;
    }

    public void setSports(boolean sports) {
        this.sports = sports;
    }

    public String getArts() {
        return "Arts";
    }

    public String getFashionStyle() {
        return "Fashion & Style";
    }

    public String getSports() {
        return "Sports";
    }

}
