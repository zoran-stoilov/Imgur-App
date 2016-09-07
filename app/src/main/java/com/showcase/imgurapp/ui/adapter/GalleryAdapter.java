package com.showcase.imgurapp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.showcase.imgurapp.R;
import com.showcase.imgurapp.model.ImgurPost;
import com.showcase.imgurapp.ui.adapter.holder.ImgurPostHolder;
import com.showcase.imgurapp.ui.listener.OnListEndListener;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<ImgurPostHolder> {

    private Fragment mFragment;
    private List<ImgurPost> posts;

    public GalleryAdapter(Fragment fragment, List<ImgurPost> posts) {
        this.mFragment = fragment;
        this.posts = posts;
    }

    @Override
    public ImgurPostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_item, parent, false);
        return new ImgurPostHolder(view);
    }

    @Override
    public void onBindViewHolder(ImgurPostHolder holder, int position) {
        holder.bindView(mFragment.getContext(), posts.get(position));
        if ((position == getItemCount() - 1)) { // when the last item becomes visible
            if (mFragment instanceof OnListEndListener) {
                ((OnListEndListener) mFragment).onListEnd();
            }
        }
    }

    @Override
    public int getItemCount() {
        if (posts == null) {
            return 0;
        } else {
            return posts.size();
        }
    }
}
