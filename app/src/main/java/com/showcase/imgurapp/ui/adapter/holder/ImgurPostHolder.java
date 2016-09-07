package com.showcase.imgurapp.ui.adapter.holder;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.showcase.imgurapp.R;
import com.showcase.imgurapp.model.ImgurPost;
import com.showcase.imgurapp.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ImgurPostHolder extends RecyclerView.ViewHolder {

    public static int IMAGE_WIDTH = 0;
    public static int IMAGE_WIDTH_PORTRAIT = 0;
    public static int IMAGE_WIDTH_LANDSCAPE = 0;

    @BindView(R.id.list_item_image)
    ImageView mImageView;
    @BindView(R.id.list_item_title)
    TextView mTextTitle;
    @BindView(R.id.list_item_points)
    TextView mTextPoints;

    public ImgurPostHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindView(final Context context, final ImgurPost imgurPost) {
        mTextTitle.setText(imgurPost.getTitle());
        mTextPoints.setText(String.format(Locale.getDefault(), "%,d%s", imgurPost.getPoints(),
                context.getString(R.string._points)));
        Timber.d("%dx%d %s", imgurPost.getImageWidth(), imgurPost.getImageHeight(), imgurPost.getTitle());
        if (IMAGE_WIDTH > 0) {
            // if IMAGE_WIDTH has positive value, set the image view size before it's created
            displayImage(context, imgurPost, IMAGE_WIDTH);
        } else {
            // otherwise enqueue the process - the view needs to be created to get the correct measured width
            mImageView.post(new Runnable() {
                @Override
                public void run() {
                    int width = mImageView.getMeasuredWidth();
                    if (width > 0 && IMAGE_WIDTH == 0) {
                        setImageWidth(context, width);
                    } else {
                        width = IMAGE_WIDTH;
                    }
                    displayImage(context, imgurPost, width);
                }
            });
        }
    }

    private void displayImage(Context context, ImgurPost imgurPost, int width) {
        // set the proper height that fits the image aspect ratio
        int height;
        if (imgurPost.getImageHeight() > 0 && imgurPost.getImageWidth() > 0) {
            height = (int) (width * ((double) imgurPost.getImageHeight() / ((double) imgurPost.getImageWidth())));
        } else {
            height = width;
        }
        mImageView.getLayoutParams().height = height;
        Timber.d("%dx%d", width, height);
        if (width > 0 && height > 0) {
            int screenWidth = Utils.getScreenWidth(context);
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                screenWidth = Utils.getScreenHeight(context);
            }
            // scale down the image if
            // the width is greater than the screen width (it should take only half of the screen)
            // the height is greater than GL_MAX_TEXTURE_SIZE (it can't be displayed)
            while (width >= screenWidth || height > Utils.GL_MAX_TEXTURE_SIZE) {
                width /= 2;
                height /= 2;
                Timber.d("%dx%d %d", width, height, screenWidth);
            }
            Picasso.with(context)
                    .load(imgurPost.getImageLink())
                    .config(Bitmap.Config.RGB_565)
                    .resize(width, height)
                    .into(mImageView);
        } else {
            Picasso.with(context)
                    .load(imgurPost.getImageLink())
                    .config(Bitmap.Config.RGB_565)
                    .fit()
                    .into(mImageView);
        }
    }

    private void setImageWidth(Context context, int width) {
        if (width > 0) {
            IMAGE_WIDTH = width;
            double aspectRatio = (double) Utils.getScreenHeight(context) / Utils.getScreenWidth(context);
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                IMAGE_WIDTH_PORTRAIT = IMAGE_WIDTH;
                IMAGE_WIDTH_LANDSCAPE = (int) (IMAGE_WIDTH * aspectRatio);
            } else {
                IMAGE_WIDTH_LANDSCAPE = IMAGE_WIDTH;
                IMAGE_WIDTH_PORTRAIT = (int) (IMAGE_WIDTH * aspectRatio);
            }
        }
    }
}
