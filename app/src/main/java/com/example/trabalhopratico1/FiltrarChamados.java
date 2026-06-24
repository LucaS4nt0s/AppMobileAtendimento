package com.example.trabalhopratico1;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FiltrarChamados extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private EditText editTextDate;
    private TextInputLayout textInputLayoutDate;
    private ImageButton btnClearDate;
    private Button btnFiltrar;
    private ChipGroup grupoChip;
    private String dataParaBanco = "";
    private List<String> estadosSelecionados = new ArrayList<>();
    private ProgressDialog progressDialog;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        textInputLayoutDate = findViewById(R.id.textInputLayoutDate);
        editTextDate = findViewById(R.id.editTextDate);
        btnClearDate = findViewById(R.id.btnClearDate);

        editTextDate.setFocusable(false);
        editTextDate.setClickable(true);
        editTextDate.setOnClickListener(this);

        btnClearDate.setOnClickListener(this);

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
                    if (id == R.id.chip6) estadosSelecionados.add("Em andamento");
                    if (id == R.id.chip7) estadosSelecionados.add("Conclu\u00EDdo");
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
        if(v == btnFiltrar){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Filtrando chamados...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Back4AppHelper.Callback<ArrayList<Demandas>> callback = new Back4AppHelper.Callback<ArrayList<Demandas>>() {
                @Override
                public void onSuccess(ArrayList<Demandas> lista) {
                    progressDialog.dismiss();
                    Adaptador adaptador = new Adaptador(lista);
                    adaptador.setOnItemClickListener(position -> {
                        Demandas d = lista.get(position);
                        Intent intent = new Intent(FiltrarChamados.this, Atendimento.class);
                        intent.putExtra("parseObjectId", d.getParseObjectId());
                        startActivity(intent);
                    });
                    recyclerView.setAdapter(adaptador);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(FiltrarChamados.this));
                }

                @Override
                public void onError(Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(FiltrarChamados.this,
                            "Erro ao filtrar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            };

            String data = dataParaBanco;
            List<String> estados = estadosSelecionados;

            if (data.isEmpty() && estados.isEmpty()) {
                Back4AppHelper.getDemandas(callback);
            } else if (!data.isEmpty() && !estados.isEmpty()) {
                Back4AppHelper.getDemandasFiltradasPorDataEstado(data, estados, callback);
            } else if (!data.isEmpty()) {
                Back4AppHelper.getDemandasFiltradasPorData(data, callback);
            } else {
                Back4AppHelper.getDemandasFiltradasPorEstado(estados, callback);
            }
        }
    }
}
