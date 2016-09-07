package com.showcase.imgurapp.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.showcase.imgurapp.Constants;
import com.showcase.imgurapp.R;
import com.showcase.imgurapp.api.ApiService;
import com.showcase.imgurapp.bus.MainThreadBus;
import com.showcase.imgurapp.bus.event.PostsAvailableEvent;
import com.showcase.imgurapp.bus.event.ResponseFailureEvent;
import com.showcase.imgurapp.model.ImgurPost;
import com.showcase.imgurapp.ui.adapter.GalleryAdapter;
import com.showcase.imgurapp.ui.adapter.holder.ImgurPostHolder;
import com.showcase.imgurapp.ui.listener.OnListEndListener;
import com.showcase.imgurapp.util.Utils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class MainFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener, OnListEndListener, View.OnClickListener {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.gallery_recycler_view)
    RecyclerView galleryRecyclerView;

    private GalleryAdapter galleryAdapter;
    private List<ImgurPost> posts = new ArrayList<>();
    private int mPage = 0;
    private boolean isLoading;
    private Snackbar activeSnackbar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        initViews();
        return rootView;
    }

    private void initViews() {
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(Constants.COLUMNS, StaggeredGridLayoutManager.VERTICAL);
        galleryRecyclerView.setLayoutManager(layoutManager);
        galleryRecyclerView.setHasFixedSize(true);
        refreshLayout.setOnRefreshListener(this);
        galleryAdapter = new GalleryAdapter(this, posts);
        galleryRecyclerView.setAdapter(galleryAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        MainThreadBus.getInstance().register(this);
        if (posts.isEmpty()) {
            loadPosts();
        }
    }

    @Override
    public void onPause() {
        MainThreadBus.getInstance().unregister(this);
        super.onPause();
    }

    @Subscribe
    public void onPostsAvailable(PostsAvailableEvent event) {
        Timber.d("onPostsAvailable %d", mPage);
        isLoading = false;
        if (event.getPosts() != null) {
            if (mPage == 0) {
                posts.clear();
            }
            // prevent from duplicating posts - do not add post if there's another one with the same ID in the list
            for (ImgurPost post : event.getPosts()) {
                if (posts.contains(post)) {
                    Timber.d("duplicate found ID:%s %s", post.getId(), post.getTitle());
                } else {
                    posts.add(post);
                }
            }
            mPage++;
        }
        galleryAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
        if (activeSnackbar != null && activeSnackbar.isShown()) {
            activeSnackbar.dismiss();
        }
    }

    @Subscribe
    public void onResponseFailure(ResponseFailureEvent event) {
        Timber.e(event.getThrowable(), "onResponseFailure");
        isLoading = false;
        refreshLayout.setRefreshing(false);
        if (Utils.hasNetworkConnection(getContext())) {
            activeSnackbar = Snackbar.make(refreshLayout, R.string.something_went_wrong, Snackbar.LENGTH_SHORT);
        } else {
            activeSnackbar = Snackbar.make(refreshLayout, R.string.check_connection, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.try_again, this);
        }
        activeSnackbar.show();
    }

    private void loadPosts() {
        loadPosts(false);
    }

    private void loadPosts(boolean fromFirstPage) {
        Timber.d("loadPosts page:%d", mPage);
        if (isLoading) {
            return;
        }
        if (fromFirstPage) {
            mPage = 0;
        }
        if (mPage == 0) {
            refreshLayout.setRefreshing(true);
        }
        isLoading = true;
        ApiService.getInstance().getPosts(Constants.API_HOT, Constants.API_VIRAL, mPage, true);
    }

    @Override
    public void onRefresh() {
        loadPosts(true);
    }

    @Override
    public void onListEnd() {
        Timber.d("onListEnd");
        if (mPage > 0) {
            loadPosts();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Timber.d("onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        ImgurPostHolder.IMAGE_WIDTH = 0;
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (ImgurPostHolder.IMAGE_WIDTH_PORTRAIT > 0) {
                ImgurPostHolder.IMAGE_WIDTH = ImgurPostHolder.IMAGE_WIDTH_PORTRAIT;
            }
        } else { // ORIENTATION_LANDSCAPE
            if (ImgurPostHolder.IMAGE_WIDTH_LANDSCAPE > 0) {
                ImgurPostHolder.IMAGE_WIDTH = ImgurPostHolder.IMAGE_WIDTH_LANDSCAPE;
            }
        }
        // update gallery items - recreate views (resize images)
        galleryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        // on click on "Try again" after failure to fetch Imgur posts
        loadPosts();
    }
}
