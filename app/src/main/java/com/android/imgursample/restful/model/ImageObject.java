package com.android.imgursample.restful.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kaplanf on 11/11/2016.
 */

public class ImageObject implements Serializable {

    @SerializedName("link")
    public String link;

    @SerializedName("in_gallery")
    public boolean in_gallery;

    @SerializedName("score")
    public int score;

    @SerializedName("id")
    public String id;

    @SerializedName("cover")
    public String cover;

    @SerializedName("title")
    public String title;

    @SerializedName("is_ad")
    public boolean is_ad;

    @SerializedName("layout")
    public String layout;

    @SerializedName("cover_height")
    public int cover_height;

    @SerializedName("points")
    public int points;

    @SerializedName("topic_id")
    public String topic_id;

    @SerializedName("account_url")
    public String account_url;

    @SerializedName("cover_width")
    public int cover_width;

    @SerializedName("datetime")
    public long datetime;

    @SerializedName("comment_count")
    public int comment_count;

    @SerializedName("topic")
    public String topic;

    @SerializedName("account_id")
    public int account_id;

    @SerializedName("privacy")
    public String privacy;

    @SerializedName("favorite")
    public boolean favorite;

    @SerializedName("section")
    public String section;

    @SerializedName("nsfw")
    public boolean nsfw;

    @SerializedName("views")
    public int views;

    @SerializedName("downs")
    public int downs;

    @SerializedName("is_album")
    public boolean is_album;

    @SerializedName("ups")
    public int ups;

    @SerializedName("images_count")
    public int images_count;

    @SerializedName("type")
    public String type;

    @SerializedName("description")
    public String description;
}
