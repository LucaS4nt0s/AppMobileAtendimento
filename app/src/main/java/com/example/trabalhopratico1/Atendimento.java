package com.example.trabalhopratico1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Atendimento extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private BD bd;
    private int id;
    private FloatingActionButton floatingActionButton;
    private ChipGroup grupoChip;
    private Button btnAlterarChamado;
    private EditText editTextSolucao;
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

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        floatingActionButton = findViewById(R.id.floatingActionButton3);
        floatingActionButton.setOnClickListener(this);
        grupoChip = findViewById(R.id.grupoChip3);
        grupoChip.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup chipGroup, @NonNull List<Integer> list) {
                if(chipGroup == grupoChip){
                    if(list.contains(R.id.chip8)){
                        estadoSelecionado = "Aberto";
                    }
                    if(list.contains(R.id.chip9)){
                        estadoSelecionado = "Em Atendimento";
                    }
                    if(list.contains(R.id.chip10)) {
                        estadoSelecionado = "Concluido";
                    }
                }
            }
        });

        editTextSolucao = findViewById(R.id.editTextSolucao);

        btnAlterarChamado = findViewById(R.id.btnAlterarChamado);
        btnAlterarChamado.setOnClickListener(this);

        this.recyclerView = findViewById(R.id.recyclerView);
        bd = new BD(this);
        Demandas demandaEncontrada = bd.getDemanda(id);
        if (demandaEncontrada != null) {
            estadoDoChamado = demandaEncontrada.getEstado();
            editTextSolucao.setText(demandaEncontrada.getSolucao());

            ArrayList<Demandas> demandas = new ArrayList<>();
            demandas.add(demandaEncontrada);
            Adaptador adaptador = new Adaptador(demandas);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adaptador);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == floatingActionButton){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        if(v == btnAlterarChamado){
            solucao = editTextSolucao.getText().toString();

            if(estadoSelecionado.isEmpty()){
                Toast.makeText(this, "Selecione um estado", Toast.LENGTH_SHORT).show();
                return;
            }
            if(estadoSelecionado.equals(estadoDoChamado) && solucao.isEmpty()){
                Toast.makeText(this, "Sem alteração", Toast.LENGTH_SHORT).show();
                return;
            }
            if(solucao.isEmpty() && estadoSelecionado.equals("Concluido")){
                editTextSolucao.setError("Se o chamado estiver concluido, preencha a solução");
                return;
            }

            if(!estadoSelecionado.equals(estadoDoChamado) && !solucao.isEmpty()){
                bd.atualizarEstadoSolucaoDemanda(id, estadoSelecionado, solucao);
            } else if (!estadoSelecionado.equals(estadoDoChamado)) {
                bd.atualizarEstadoDemanda(id, estadoSelecionado);
            } else if (!solucao.isEmpty()) {
                bd.atualizarSolucaoDemanda(id, solucao);
            }

            bd.close();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
