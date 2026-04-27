package com.example.trabalhopratico1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adaptador extends RecyclerView.Adapter<RecyHolder>{
    private ArrayList<Demandas> demandas;
    private int layoutId;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        default void onButtonClick(int position) {}
        default void onItemClick(int position) {}
    }

    public Adaptador(ArrayList<Demandas> demandas) {
        this.demandas = demandas;
        this.layoutId = R.layout.layout_chamado; // Layout padrão
    }

    public Adaptador(ArrayList<Demandas> demandas, int layoutId) {
        this.demandas = demandas;
        this.layoutId = layoutId;
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
        holder.textViewDescricao.setText(demanda.getDescricao());
        holder.textViewLocal.setText(demanda.getLocal());
        holder.textViewDate.setText(demanda.getDate());
        holder.textViewStatus.setText(demanda.getEstado());

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
