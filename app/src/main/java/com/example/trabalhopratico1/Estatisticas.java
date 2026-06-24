package com.example.trabalhopratico1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Estatisticas extends AppCompatActivity {

    private TextView txtTotal, txtAbertos, txtAndamento, txtConcluidos;
    private BD bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_estatisticas);

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

        txtTotal = findViewById(R.id.txtTotal);
        txtAbertos = findViewById(R.id.txtAbertos);
        txtAndamento = findViewById(R.id.txtAndamento);
        txtConcluidos = findViewById(R.id.txtConcluidos);

        bd = new BD(this);

        carregarEstatisticas();
    }

    private void carregarEstatisticas() {
        int total = bd.getTotalChamados();
        int abertos = bd.getChamadosPorEstado("Aberto");
        int andamento = bd.getChamadosPorEstado("Em andamento");
        int concluidos = bd.getChamadosPorEstado("Conclu\u00EDdo");

        txtTotal.setText(String.valueOf(total));
        txtAbertos.setText(String.valueOf(abertos));
        txtAndamento.setText(String.valueOf(andamento));
        txtConcluidos.setText(String.valueOf(concluidos));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bd != null) {
            bd.close();
        }
    }
}
