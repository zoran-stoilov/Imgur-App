package com.showcase.imgurapp.api;

import com.showcase.imgurapp.model.ImgurResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServiceInterface {

    /**
     * @param section   optional	hot | top | user - defaults to hot
     * @param sort      optional	viral | top | time | rising (only available with user section)
     *                  - defaults to viral
     * @param page      optional	integer - the data paging number
     * @param showViral optional	true | false - Show or hide viral images from the 'user' section.
     *                  Defaults to true
     * @return ImgurResponse with data (Imgur posts).
     */
    // https://api.imgur.com/3/gallery/{section}/{sort}/{page}?showViral=bool
    @GET("gallery/{section}/{sort}/{page}")
    Call<ImgurResponse> getPosts(
            @Path("section") String section,
            @Path("sort") String sort,
            @Path("page") int page,
            @Query("showViral") boolean showViral
    );
}
