package com.example.trabalhopratico1;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FiltrarChamados extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private BD bd;
    private FloatingActionButton floatingActionButton;
    private EditText editTextDate;
    private TextInputLayout textInputLayoutDate;
    private ImageButton btnClearDate;
    private Button btnFiltrar;
    private String data, estado;
    private ChipGroup grupoChip;
    private String dataParaBanco = "";
    private List<String> estadosSelecionados = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_filtrar_chamados);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.recyclerView = findViewById(R.id.recyclerView);
        bd = new BD(this);
        this.floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);

        textInputLayoutDate = findViewById(R.id.textInputLayoutDate);
        editTextDate = findViewById(R.id.editTextDate);
        btnClearDate = findViewById(R.id.btnClearDate);

        editTextDate.setFocusable(false);
        editTextDate.setClickable(true);
        editTextDate.setOnClickListener(this);
        
        btnClearDate.setOnClickListener(this);

        // Listener para resetar dataParaBanco quando o campo for limpo
        editTextDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    dataParaBanco = "";
                }
            }
        });

        btnFiltrar = findViewById(R.id.btnFiltrar);
        btnFiltrar.setOnClickListener(this);

        grupoChip = findViewById(R.id.grupoChip3);

        grupoChip.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup chipGroup, @NonNull List<Integer> list) {
                estadosSelecionados.clear();
                for(int id : list){
                    if (id == R.id.chip5) estadosSelecionados.add("Aberto");
                    if (id == R.id.chip6) estadosSelecionados.add("Em Atendimento");
                    if (id == R.id.chip7) estadosSelecionados.add("Concluido");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == btnClearDate) {
            editTextDate.setText("");
            dataParaBanco = "";
        }
        if(v == editTextDate) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    FiltrarChamados.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        editTextDate.setText(String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year1));

                        dataParaBanco = String.format("%04d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);
                    },
                    year,
                    month,
                    day
            );
            datePickerDialog.show();
        }
        if(v == floatingActionButton){
            bd.close();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        if(v == btnFiltrar){
            this.data = dataParaBanco;
            List<String> estados = estadosSelecionados;


            if(this.data.isEmpty() && estados.isEmpty()){
                ArrayList<Demandas> lista = bd.getDemandas();
                Adaptador adaptador = new Adaptador(lista);
                adaptador.setOnItemClickListener(new Adaptador.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Demandas d = lista.get(position);
                        Intent intent = new Intent(FiltrarChamados.this, Atendimento.class);
                        intent.putExtra("id", d.getId());
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adaptador);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }

            if(!this.data.isEmpty() && !estados.isEmpty()) {
                ArrayList<Demandas> lista = bd.getDemandasFiltradasPorDataEstado(this.data, estados);
                Adaptador adaptador = new Adaptador(lista);
                adaptador.setOnItemClickListener(new Adaptador.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Demandas d = lista.get(position);
                        Intent intent = new Intent(FiltrarChamados.this, Atendimento.class);
                        intent.putExtra("id", d.getId());
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adaptador);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }

            if(!this.data.isEmpty() && estados.isEmpty()) {
                ArrayList<Demandas> lista = bd.getDemandasFiltradasPorData(this.data);
                Adaptador adaptador = new Adaptador(lista);
                adaptador.setOnItemClickListener(new Adaptador.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Demandas d = lista.get(position);
                        Intent intent = new Intent(FiltrarChamados.this, Atendimento.class);
                        intent.putExtra("id", d.getId());
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adaptador);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }

            if(this.data.isEmpty() && !estados.isEmpty()) {
                ArrayList<Demandas> lista = bd.getDemandasFiltradasPorEstado(estados);
                Adaptador adaptador = new Adaptador(lista);
                adaptador.setOnItemClickListener(new Adaptador.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Demandas d = lista.get(position);
                        Intent intent = new Intent(FiltrarChamados.this, Atendimento.class);
                        intent.putExtra("id", d.getId());
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adaptador);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }

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
