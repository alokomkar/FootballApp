package app.football.com.footballapp.utils.interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by cognitive on 10/24/16.
 */
public interface GetScoresInterface {

    @GET("enparadigm/{path}")
    Call<JsonObject> getFootballScores(@Path("path") String path);
}
