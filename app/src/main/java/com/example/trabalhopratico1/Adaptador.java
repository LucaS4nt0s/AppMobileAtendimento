package com.example.trabalhopratico1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class Adaptador extends RecyclerView.Adapter<RecyHolder>{
    private ArrayList<Demandas> demandas;
    private int layoutId;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public Adaptador(ArrayList<Demandas> demandas) {
        this.demandas = demandas;
        this.layoutId = R.layout.layout_chamado;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new RecyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyHolder holder, int position) {
        Demandas demanda = demandas.get(position);
        holder.textViewID.setText(String.valueOf(demanda.getId()));
        holder.textViewTitulo.setText(demanda.getTitulo());
        holder.textViewLocal.setText(demanda.getLocal());
        holder.textViewStatus.setText(demanda.getEstado());

        String imagePath = demanda.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                if (bitmap != null) {
                    holder.imageViewThumb.setVisibility(View.VISIBLE);
                    holder.imageViewThumb.setImageBitmap(bitmap);
                }
            }
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return demandas.size();
    }
}
