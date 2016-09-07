package com.showcase.imgurapp.bus.event;

import com.showcase.imgurapp.model.ImgurPost;
import com.showcase.imgurapp.model.ImgurResponse;

import java.util.ArrayList;
import java.util.List;

public class PostsAvailableEvent {

    private ImgurResponse mImgurResponse;
    private List<ImgurPost> mImgurPosts = new ArrayList<>();

    public PostsAvailableEvent(ImgurResponse imgurResponse) {
        this.mImgurResponse = imgurResponse;
        if (mImgurResponse != null && mImgurResponse.getData() != null) {
            mImgurPosts.clear();
            for (ImgurPost imgurPost : mImgurResponse.getData()) {
                if (!imgurPost.isAlbum()) {
                    mImgurPosts.add(imgurPost);
                }
            }
        }
    }

    public boolean getSuccess() {
        return mImgurResponse.getSuccess();
    }

    public int getStatus() {
        return mImgurResponse.getStatus();
    }

    public List<ImgurPost> getPosts() {
        return mImgurPosts;
    }
}
