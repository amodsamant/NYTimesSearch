
package com.nytimessearch.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Multimedium {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("subtype")
    @Expose
    private String subtype;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }


}
