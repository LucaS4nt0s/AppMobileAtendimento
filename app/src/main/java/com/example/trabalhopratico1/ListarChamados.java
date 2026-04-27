package com.example.trabalhopratico1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListarChamados extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private BD bd;
    private FloatingActionButton floatingActionButton;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar_chamados);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.recyclerView = findViewById(R.id.recyclerView);
        this.floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(this);
        bd = new BD(this);

        ArrayList<Demandas> demandas = bd.getDemandas();
        Adaptador adaptador = new Adaptador(demandas);


        adaptador.setOnItemClickListener(new Adaptador.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Demandas demanda = demandas.get(position);
                Intent intent = new Intent(ListarChamados.this, Atendimento.class);
                intent.putExtra("id", demanda.getId());

                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adaptador);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onClick(View v) {
        if(v == floatingActionButton){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bd != null) {
            bd.close();
        }
    }
}
