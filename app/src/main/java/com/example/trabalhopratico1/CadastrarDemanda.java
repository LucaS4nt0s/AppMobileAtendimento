package com.example.trabalhopratico1;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.List;

public class CadastrarDemanda extends AppCompatActivity implements View.OnClickListener{
    private Button btnCancelar, btnEnviarDemanda;
    private EditText editTextData;
    private ImageButton btnClearDate;
    private ChipGroup grupoChip;
    private TextInputEditText editTextTitulo, editTextDescricao, editTextLocal;
    private String selecionado = "";
    private String dataParaBanco = "";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastrar_demanda);
        
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextDescricao = findViewById(R.id.editTextDescricao);
        editTextLocal = findViewById(R.id.editTextLocal);

        editTextData = findViewById(R.id.editTextData);
        btnClearDate = findViewById(R.id.btnClearDate);

        if (editTextData != null) {
            editTextData.setFocusable(false);
            editTextData.setClickable(true);
            editTextData.setOnClickListener(this);
        }

        if (btnClearDate != null) {
            btnClearDate.setOnClickListener(this);
        }

        grupoChip = findViewById(R.id.chipGroup);
        if (grupoChip != null) {
            grupoChip.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
                @Override
                public void onCheckedChanged(@NonNull ChipGroup chipGroup, @NonNull List<Integer> list) {
                    if(list.contains(R.id.chip)){
                        selecionado = "Infraestrutura";
                    } else if(list.contains(R.id.chip2)){
                        selecionado = "TI";
                    }
                }
            });
        }

        btnCancelar = findViewById(R.id.btnCancelar);
        btnEnviarDemanda = findViewById(R.id.btnEnviarDemanda);

        if (btnCancelar != null) btnCancelar.setOnClickListener(this);
        if (btnEnviarDemanda != null) btnEnviarDemanda.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnClearDate) {
            editTextData.setText("");
            dataParaBanco = "";
        } else if(v.getId() == R.id.btnCancelar){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if(v.getId() == R.id.btnEnviarDemanda){
            enviarDemanda();
        } else if(v.getId() == R.id.editTextData){
            mostrarDatePicker();
        }
    }

    private void mostrarDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                CadastrarDemanda.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    editTextData.setText(String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year1));
                    dataParaBanco = String.format("%04d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }

    private void enviarDemanda() {
        if(editTextTitulo.getText().toString().isEmpty()){
            editTextTitulo.setError("Campo obrigatório");
            return;
        }
        if(editTextDescricao.getText().toString().isEmpty()){
            editTextDescricao.setError("Campo obrigatório");
            return;
        }
        if(editTextLocal.getText().toString().isEmpty()){
            editTextLocal.setError("Campo obrigatório");
            return;
        }
        if(selecionado.isEmpty()){
            Toast.makeText(this, "Selecione um tipo", Toast.LENGTH_SHORT).show();
            return;
        }

        String titulo = editTextTitulo.getText().toString();
        String descricao = editTextDescricao.getText().toString();
        String local = editTextLocal.getText().toString();
        String tipo = selecionado;
        String data = dataParaBanco;

        BD bd = new BD(this);

        if(!data.isEmpty()){
            bd.salvarDados(titulo, descricao, local, tipo, data);
        } else{
            bd.salvarDados(titulo, descricao, local, tipo);
        }
        
        Toast.makeText(this, "Demanda cadastrada com sucesso", Toast.LENGTH_SHORT).show();
        bd.close();

        editTextTitulo.setText("");
        editTextDescricao.setText("");
        editTextLocal.setText("");
        editTextData.setText("");
        dataParaBanco = "";
        selecionado = "";
        if (grupoChip != null) grupoChip.clearCheck();
        
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
