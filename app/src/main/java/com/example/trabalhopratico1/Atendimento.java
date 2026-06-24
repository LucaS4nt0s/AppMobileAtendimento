package com.example.trabalhopratico1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.ChipGroup;

import java.io.File;
import java.util.List;

public class Atendimento extends AppCompatActivity implements View.OnClickListener {
    private BD bd;
    private int id;
    private ChipGroup grupoChip;
    private Button btnAlterarChamado;
    private EditText editTextSolucao;
    private ImageView imageViewFoto;
    private TextView txtTitulo, txtDescricao, txtLocal, txtStatus;
    private String solucao = "";
    private String estadoSelecionado = "";
    private String estadoDoChamado;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_atendimento);
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

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        txtTitulo = findViewById(R.id.txtDetalheTitulo);
        txtDescricao = findViewById(R.id.txtDetalheDescricao);
        txtLocal = findViewById(R.id.txtDetalheLocal);
        txtStatus = findViewById(R.id.txtDetalheStatus);

        grupoChip = findViewById(R.id.grupoChip3);
        grupoChip.setOnCheckedStateChangeListener((chipGroup, list) -> {
            if (list.contains(R.id.chip8)) {
                estadoSelecionado = "Aberto";
            } else if (list.contains(R.id.chip9)) {
                estadoSelecionado = "Em andamento";
            } else if (list.contains(R.id.chip10)) {
                estadoSelecionado = "Conclu\u00EDdo";
            }
        });

        editTextSolucao = findViewById(R.id.editTextSolucao);
        imageViewFoto = findViewById(R.id.imageViewFoto);

        btnAlterarChamado = findViewById(R.id.btnAlterarChamado);
        btnAlterarChamado.setOnClickListener(this);

        bd = new BD(this);
        Demandas demanda = bd.getDemanda(id);
        if (demanda != null) {
            estadoDoChamado = demanda.getEstado();
            txtTitulo.setText(demanda.getTitulo());
            txtDescricao.setText(demanda.getDescricao());
            txtLocal.setText(demanda.getLocal());
            txtStatus.setText(demanda.getEstado());
            editTextSolucao.setText(demanda.getSolucao());

            String imagePath = demanda.getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    if (bitmap != null) {
                        imageViewFoto.setVisibility(View.VISIBLE);
                        imageViewFoto.setImageBitmap(bitmap);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnAlterarChamado) {
            solucao = editTextSolucao.getText().toString();

            if (estadoSelecionado.isEmpty()) {
                Toast.makeText(this, "Selecione um estado", Toast.LENGTH_SHORT).show();
                return;
            }
            if (estadoSelecionado.equals(estadoDoChamado) && solucao.isEmpty()) {
                Toast.makeText(this, "Sem alteração", Toast.LENGTH_SHORT).show();
                return;
            }
            if (solucao.isEmpty() && estadoSelecionado.equals("Conclu\u00EDdo")) {
                editTextSolucao.setError("Se o chamado estiver conclu\u00EDdo, preencha a solu\u00E7\u00E3o");
                return;
            }

            if (!estadoSelecionado.equals(estadoDoChamado) && !solucao.isEmpty()) {
                bd.atualizarEstadoSolucaoDemanda(id, estadoSelecionado, solucao);
            } else if (!estadoSelecionado.equals(estadoDoChamado)) {
                bd.atualizarEstadoDemanda(id, estadoSelecionado);
            } else if (!solucao.isEmpty()) {
                bd.atualizarSolucaoDemanda(id, solucao);
            }

            bd.close();
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
