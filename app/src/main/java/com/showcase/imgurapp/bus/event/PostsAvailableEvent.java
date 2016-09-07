package com.showcase.imgurapp.bus.event;

import com.showcase.imgurapp.model.ImgurPost;
import com.showcase.imgurapp.model.ImgurResponse;

import java.util.List;

public class PostsAvailableEvent {

    private ImgurResponse mImgurResponse;

    public PostsAvailableEvent(ImgurResponse imgurResponse) {
        this.mImgurResponse = imgurResponse;
    }

    public boolean getSuccess() {
        return mImgurResponse.getSuccess();
    }

    public int getStatus() {
        return mImgurResponse.getStatus();
    }

    public List<ImgurPost> getPosts() {
        if (mImgurResponse == null) {
            return null;
        }
        return mImgurResponse.getData();
    }
}
