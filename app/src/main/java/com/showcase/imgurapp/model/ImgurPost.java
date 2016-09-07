package com.showcase.imgurapp.model;

import com.google.gson.annotations.SerializedName;

public class ImgurPost {

    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("datetime")
    private int datetime;
    @SerializedName("type")
    private String type;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;
    @SerializedName("cover")
    private String cover;
    @SerializedName("cover_width")
    private int coverWidth;
    @SerializedName("cover_height")
    private int coverHeight;
    @SerializedName("account_url")
    private String accountUrl;
    @SerializedName("account_id")
    private int accountId;
    @SerializedName("privacy")
    private String privacy;
    @SerializedName("layout")
    private String layout;
    @SerializedName("views")
    private int views;
    @SerializedName("link")
    private String link;
    @SerializedName("ups")
    private int ups;
    @SerializedName("downs")
    private int downs;
    @SerializedName("points")
    private int points;
    @SerializedName("score")
    private int score;
    @SerializedName("is_album")
    private boolean isAlbum;
    @SerializedName("vote")
    private Object vote;
    @SerializedName("favorite")
    private boolean favorite;
    @SerializedName("nsfw")
    private boolean nsfw;
    @SerializedName("section")
    private String section;
    @SerializedName("comment_count")
    private int commentCount;
    @SerializedName("topic")
    private String topic;
    @SerializedName("topic_id")
    private int topicId;
    @SerializedName("images_count")
    private int imagesCount;
    @SerializedName("in_gallery")
    private boolean inGallery;
    @SerializedName("is_ad")
    private boolean isAd;

    public String getLink() {
        return link;
    }

    public boolean isAlbum() {
        return isAlbum;
    }

    public String getTitle() {
        return title;
    }

    public int getPoints() {
        return points;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ImgurPost{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", datetime=" + datetime +
                ", type=" + type +
                ", width=" + width +
                ", height=" + height +
                ", cover='" + cover + '\'' +
                ", coverWidth=" + coverWidth +
                ", coverHeight=" + coverHeight +
                ", views=" + views +
                ", link='" + link + '\'' +
                ", ups=" + ups +
                ", downs=" + downs +
                ", points=" + points +
                ", score=" + score +
                ", isAlbum=" + isAlbum +
                ", imagesCount=" + imagesCount +
                ", topic='" + topic + '\'' +
                ", section='" + section + '\'' +
                ", commentCount=" + commentCount +
                '}';
    }
}
