package com.showcase.imgurapp.api;

import com.showcase.imgurapp.Constants;
import com.showcase.imgurapp.bus.MainThreadBus;
import com.showcase.imgurapp.bus.event.PostsAvailableEvent;
import com.showcase.imgurapp.bus.event.ResponseFailureEvent;
import com.showcase.imgurapp.model.ImgurResponse;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Singleton class providing an instance ready to communicate with the API
 */
public class ApiService {

    private static ApiService apiService;
    private ApiServiceInterface apiInterface;

    private ApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        apiInterface = retrofit.create(ApiServiceInterface.class);
    }

    public static ApiService getInstance() {
        if (apiService == null) {
            apiService = new ApiService();
        }
        return apiService;
    }

    public void getPosts(String section, String sort, int page, boolean showViral) {
        apiInterface.getPosts(section, sort, page, showViral).enqueue(new Callback<ImgurResponse>() {
            @Override
            public void onResponse(Call<ImgurResponse> call, Response<ImgurResponse> response) {
                Timber.d("%s %d %s", response.isSuccessful(), response.code(), response.message());
                if (response.body() != null) {
                    MainThreadBus.getInstance().post(new PostsAvailableEvent(response.body()));
                }
                if (response.errorBody() != null) {
                    Timber.e(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ImgurResponse> call, Throwable t) {
                MainThreadBus.getInstance().post(new ResponseFailureEvent(t));
            }
        });
    }
}
