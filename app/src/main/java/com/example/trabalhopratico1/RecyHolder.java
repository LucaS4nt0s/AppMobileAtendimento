package com.example.trabalhopratico1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyHolder extends RecyclerView.ViewHolder{
    protected TextView textViewID, textViewTitulo, textViewLocal, textViewStatus;
    protected ImageView imageViewThumb;

    public RecyHolder(@NonNull View itemView) {
        super(itemView);
        textViewID = itemView.findViewById(R.id.textViewID);
        textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
        textViewLocal = itemView.findViewById(R.id.textViewLocal);
        textViewStatus = itemView.findViewById(R.id.textViewStatus);
        imageViewThumb = itemView.findViewById(R.id.imageViewThumb);
    }
}
