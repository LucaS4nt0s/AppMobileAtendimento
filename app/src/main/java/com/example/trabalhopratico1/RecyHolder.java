package com.example.trabalhopratico1;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyHolder extends RecyclerView.ViewHolder{
    protected TextView textViewID, textViewTitulo, textViewDescricao, textViewLocal, textViewDate, textViewStatus;

    public RecyHolder(@NonNull View itemView) {
        super(itemView);
        textViewID = itemView.findViewById(R.id.textViewID);
        textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
        textViewDescricao = itemView.findViewById(R.id.textViewDescricao);
        textViewLocal = itemView.findViewById(R.id.textViewLocal);
        textViewDate = itemView.findViewById(R.id.textViewData);
        textViewStatus = itemView.findViewById(R.id.textViewStatus);
    }
}
