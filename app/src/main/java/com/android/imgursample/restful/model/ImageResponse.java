package com.android.imgursample.restful.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kaplanf on 13/11/2016.
 */

public class ImageResponse implements Serializable {

    @SerializedName("data")
    public ArrayList<ImageObject> imageObjects;

    @SerializedName("success")
    public boolean success;

    @SerializedName("status")
    public int status;
}
