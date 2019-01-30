package com.example.yanec.onexbet;

import com.example.yanec.onexbet.InternetClasses.Response;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIServer {
    String URL = "https://part.upnp.xyz/PartLive/";

    @GET("GetAllFeedGames")
    Call<List<Response>> getAllMatches();

}
