package com.example.trabalhopratico1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListarChamados extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Demandas> demandas;
    private ProgressDialog progressDialog;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        this.recyclerView = findViewById(R.id.recyclerView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando chamados...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Back4AppHelper.getDemandas(new Back4AppHelper.Callback<ArrayList<Demandas>>() {
            @Override
            public void onSuccess(ArrayList<Demandas> result) {
                progressDialog.dismiss();
                demandas = result;
                Adaptador adaptador = new Adaptador(demandas);

                adaptador.setOnItemClickListener(position -> {
                    Demandas demanda = demandas.get(position);
                    Intent intent = new Intent(ListarChamados.this, Atendimento.class);
                    intent.putExtra("parseObjectId", demanda.getParseObjectId());
                    startActivity(intent);
                });

                recyclerView.setAdapter(adaptador);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(ListarChamados.this));
            }

            @Override
            public void onError(Exception e) {
                progressDialog.dismiss();
                android.widget.Toast.makeText(ListarChamados.this,
                        "Erro ao carregar chamados: " + e.getMessage(),
                        android.widget.Toast.LENGTH_LONG).show();
            }
        });
    }
}
