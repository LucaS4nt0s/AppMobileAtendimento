package com.example.trabalhopratico1;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Estatisticas extends AppCompatActivity {

    private TextView txtTotal, txtAbertos, txtAndamento, txtConcluidos;
    private ProgressDialog progressDialog;

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

        carregarEstatisticas();
    }

    private void carregarEstatisticas() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando estatísticas...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Back4AppHelper.getTotalChamados(new Back4AppHelper.Callback<Integer>() {
            @Override
            public void onSuccess(Integer total) {
                txtTotal.setText(String.valueOf(total));
                carregarAbertos();
            }

            @Override
            public void onError(Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Estatisticas.this,
                        "Erro ao carregar estatísticas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarAbertos() {
        Back4AppHelper.getChamadosPorEstado("Aberto", new Back4AppHelper.Callback<Integer>() {
            @Override
            public void onSuccess(Integer abertos) {
                txtAbertos.setText(String.valueOf(abertos));
                carregarAndamento();
            }

            @Override
            public void onError(Exception e) {
                progressDialog.dismiss();
            }
        });
    }

    private void carregarAndamento() {
        Back4AppHelper.getChamadosPorEstado("Em andamento", new Back4AppHelper.Callback<Integer>() {
            @Override
            public void onSuccess(Integer andamento) {
                txtAndamento.setText(String.valueOf(andamento));
                carregarConcluidos();
            }

            @Override
            public void onError(Exception e) {
                progressDialog.dismiss();
            }
        });
    }

    private void carregarConcluidos() {
        Back4AppHelper.getChamadosPorEstado("Conclu\u00EDdo", new Back4AppHelper.Callback<Integer>() {
            @Override
            public void onSuccess(Integer concluidos) {
                progressDialog.dismiss();
                txtConcluidos.setText(String.valueOf(concluidos));
            }

            @Override
            public void onError(Exception e) {
                progressDialog.dismiss();
            }
        });
    }
}
