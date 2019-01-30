package com.example.yanec.onexbet.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yanec.onexbet.Holder.MatchHolder;
import com.example.yanec.onexbet.InternetClasses.Response;
import com.example.yanec.onexbet.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Response> info;

    public RecyclerAdapter(List<Response> info) {
        this.info = info;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new MatchHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MatchHolder holder = (MatchHolder) viewHolder;
        holder.bind(info.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return info.size();
    }
}
