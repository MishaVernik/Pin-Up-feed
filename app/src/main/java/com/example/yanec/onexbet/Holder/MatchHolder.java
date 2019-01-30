package com.example.yanec.onexbet.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.yanec.onexbet.InternetClasses.Response;
import com.example.yanec.onexbet.R;

public class MatchHolder extends RecyclerView.ViewHolder {

    TextView tvSportInfo;
    TextView tvMatchInfo;
    TextView tvTimeInfo;

    public MatchHolder(@NonNull View itemView) {
        super(itemView);
        initialize(itemView);
    }

    private void initialize(View rootView){
        tvSportInfo = rootView.findViewById(R.id.tvSportInfo);
        tvMatchInfo = rootView.findViewById(R.id.tvMatchInfo);
        tvTimeInfo = rootView.findViewById(R.id.tvTimeInfo);
    }

    public void bind(Response response){
        tvSportInfo.setText(response.s + " | " + response.a + " | " + response.c);
        tvMatchInfo.setText(response.getMatchInfo());
        tvTimeInfo.setText(response.getData());
    }

}
