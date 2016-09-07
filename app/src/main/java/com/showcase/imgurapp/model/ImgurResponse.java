package com.showcase.imgurapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ImgurResponse {

    @SerializedName("data")
    private List<ImgurPost> data = new ArrayList<>();
    @SerializedName("success")
    private Boolean success;
    @SerializedName("status")
    private Integer status;

    public List<ImgurPost> getData() {
        return data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Integer getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ImgurResponse{" +
                "data=" + data +
                ", success=" + success +
                ", status=" + status +
                '}';
    }
}
