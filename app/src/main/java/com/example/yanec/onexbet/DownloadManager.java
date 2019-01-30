package com.example.yanec.onexbet;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.yanec.onexbet.InternetClasses.Response;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class DownloadManager {

    private OnDownload onDownload;

    public DownloadManager(OnDownload successDownload) {
        this.onDownload = successDownload;
    }

    public void downloadInformation(final Context context, final OnTryAgain onTryAgain){
        onDownload.onStartDownload();
        Call<List<Response>> responseCall = RetrofitSingleton.getInstance().getAllMatches();

        responseCall.enqueue(new Callback<List<Response>>() {
            @Override
            public void onResponse(Call<List<Response>> call, retrofit2.Response<List<Response>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        onDownload.onSuccessDownload(response.body());
                        Log.d("myLogs", "SUCCESS");
                    }else{
                        Log.d("myLogs", "Fail body == NULL");
                    }
                }
                onDownload.onFinishDownload();
            }

            @Override
            public void onFailure(Call<List<Response>> call, Throwable t) {
                Log.d("myLogs", "ERROR");
                Log.d("myLogs", "RETRYING...");
                onTryAgain.retry();
            }
        });
    }

}
