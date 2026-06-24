package com.example.trabalhopratico1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.ChipGroup;

import java.io.File;

public class Atendimento extends AppCompatActivity implements View.OnClickListener {
    private String parseObjectId;
    private ChipGroup grupoChip;
    private Button btnAlterarChamado;
    private EditText editTextSolucao;
    private ImageView imageViewFoto;
    private TextView txtTitulo, txtDescricao, txtLocal, txtStatus;
    private String solucao = "";
    private String estadoSelecionado = "";
    private String estadoDoChamado;
    private ProgressDialog progressDialog;

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
        parseObjectId = intent.getStringExtra("parseObjectId");

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

        carregarDemanda();
    }

    private void carregarDemanda() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando chamado...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Back4AppHelper.getDemanda(parseObjectId, new Back4AppHelper.Callback<Demandas>() {
            @Override
            public void onSuccess(Demandas demanda) {
                progressDialog.dismiss();
                estadoDoChamado = demanda.getEstado();
                txtTitulo.setText(demanda.getTitulo());
                txtDescricao.setText(demanda.getDescricao());
                txtLocal.setText(demanda.getLocal());
                txtStatus.setText(demanda.getEstado());
                editTextSolucao.setText(demanda.getSolucao());

                String fotoString = demanda.getImagePath();
                if (fotoString != null && !fotoString.isEmpty()) {
                    try {
                        byte[] bytes = android.util.Base64.decode(fotoString, android.util.Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        if (bitmap != null) {
                            imageViewFoto.setVisibility(View.VISIBLE);
                            imageViewFoto.setImageBitmap(bitmap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Atendimento.this,
                        "Erro ao carregar chamado: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Salvando alterações...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Back4AppHelper.Callback<Void> callback = new Back4AppHelper.Callback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    progressDialog.dismiss();
                    Toast.makeText(Atendimento.this,
                            "Chamado atualizado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Atendimento.this,
                            "Erro ao atualizar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            };

            if (!estadoSelecionado.equals(estadoDoChamado) && !solucao.isEmpty()) {
                Back4AppHelper.atualizarEstadoSolucaoDemanda(parseObjectId, estadoSelecionado, solucao, callback);
            } else if (!estadoSelecionado.equals(estadoDoChamado)) {
                Back4AppHelper.atualizarEstadoDemanda(parseObjectId, estadoSelecionado, callback);
            } else if (!solucao.isEmpty()) {
                Back4AppHelper.atualizarEstadoSolucaoDemanda(parseObjectId, estadoDoChamado, solucao, callback);
            }
        }
    }
}
