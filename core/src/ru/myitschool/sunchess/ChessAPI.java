package ru.myitschool.sunchess;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChessAPI {
    @GET("/sunchess.php")
    Call<ChessData> createNetGame(@Query("q") String q, @Query("name") String name);

}
