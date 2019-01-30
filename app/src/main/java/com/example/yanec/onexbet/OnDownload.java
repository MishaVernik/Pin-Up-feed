package com.example.yanec.onexbet;

import com.example.yanec.onexbet.InternetClasses.Response;

import java.util.List;

public interface OnDownload {
    void onSuccessDownload(List<Response> response);
    void onStartDownload();
    void onFinishDownload();
}
