package com.example.trabalhopratico1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BD extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "banco_demandas.db";
    private static final int DATABASE_VERSION = 2;

    public BD(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public BD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS demandas(" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "titulo TEXT," +
                        "date DATE DEFAULT CURRENT_DATE," +
                        "descricao TEXT," +
                        "local TEXT," +
                        "estado TEXT CHECK(estado IN ('Aberto','Em Atendimento','Concluido')) DEFAULT 'Aberto'," +
                        "tipo TEXT CHECK(tipo IN ('Infraestrutura','TI'))," +
                        "solucao TEXT" +
                        ")"
        );

        Log.i("##", "Tabela demandas criada com sucesso");
    }

    public void salvarDados(String titulo, String descricao, String local, String tipo){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descricao", descricao);
        values.put("local", local);
        values.put("tipo", tipo);
        db.insert("demandas", null, values);
        db.close();
        Log.i("##", "Dados inseridos com sucesso");
    }

    public void salvarDados(String titulo, String descricao, String local, String tipo, String data){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descricao", descricao);
        values.put("local", local);
        values.put("tipo", tipo);
        values.put("date", data);
        db.insert("demandas", null, values);
        db.close();
        Log.i("##", "Dados inseridos com sucesso");
    }

    public ArrayList<Demandas> getDemandas(){
        ArrayList<Demandas> demandas = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("demandas", null, null, null, null, null, null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
                String local = cursor.getString(cursor.getColumnIndexOrThrow("local"));
                String tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));
                String estado = cursor.getString(cursor.getColumnIndexOrThrow("estado"));
                String solucao = cursor.getString(cursor.getColumnIndexOrThrow("solucao"));
                Demandas demanda = new Demandas(id, titulo, date, descricao, local, tipo, estado, solucao);
                demandas.add(demanda);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return demandas;
    }

    public Demandas getDemanda(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("demandas", null, "_id = ?", new String[]{String.valueOf(id)}, null, null, null);

        if(cursor.moveToFirst()) {
            String titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
            String local = cursor.getString(cursor.getColumnIndexOrThrow("local"));
            String tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));
            String estado = cursor.getString(cursor.getColumnIndexOrThrow("estado"));
            String solucao = cursor.getString(cursor.getColumnIndexOrThrow("solucao"));
            Demandas demanda = new Demandas(id, titulo, date, descricao, local, tipo, estado, solucao);
            cursor.close();
            db.close();
            return demanda;
        }

        cursor.close();
        db.close();
        return null;
    }

    public ArrayList<Demandas> getDemandasFiltradasPorEstado(List<String> estados){
        ArrayList<Demandas> demandas = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder query = new StringBuilder("SELECT * FROM demandas WHERE estado IN (");
        for (int i = 0; i < estados.size(); i++) {
            query.append("'").append(estados.get(i)).append("'");
            if (i < estados.size() - 1) query.append(",");
        }
        query.append(")");
        Cursor cursor = db.rawQuery(query.toString(), null);
        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
                String local = cursor.getString(cursor.getColumnIndexOrThrow("local"));
                String tipoDemanda = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));
                String estado = cursor.getString(cursor.getColumnIndexOrThrow("estado"));
                String solucao = cursor.getString(cursor.getColumnIndexOrThrow("solucao"));
                Demandas demanda = new Demandas(id, titulo, date, descricao, local, tipoDemanda, estado, solucao);
                demandas.add(demanda);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return demandas;
    }

    public ArrayList<Demandas> getDemandasFiltradasPorData(String data){
        ArrayList<Demandas> demandas = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("demandas", null, "date = ?", new String[]{data}, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
                String local = cursor.getString(cursor.getColumnIndexOrThrow("local"));
                String tipoDemanda = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));
                String estado = cursor.getString(cursor.getColumnIndexOrThrow("estado"));
                String solucao = cursor.getString(cursor.getColumnIndexOrThrow("solucao"));
                Demandas demanda = new Demandas(id, titulo, date, descricao, local, tipoDemanda, estado, solucao);
                demandas.add(demanda);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return demandas;
    }

    public ArrayList<Demandas> getDemandasFiltradasPorDataEstado(String data, List<String> estados){
        ArrayList<Demandas> demandas = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder query = new StringBuilder("SELECT * FROM demandas WHERE date = ? AND estado IN (");
        for (int i = 0; i < estados.size(); i++) {
            query.append("'").append(estados.get(i)).append("'");
            if (i < estados.size() - 1) query.append(",");
        }
        query.append(")");
        Cursor cursor = db.rawQuery(query.toString(), new String[]{data});
        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
                String local = cursor.getString(cursor.getColumnIndexOrThrow("local"));
                String tipoDemanda = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));
                String estado = cursor.getString(cursor.getColumnIndexOrThrow("estado"));
                String solucao = cursor.getString(cursor.getColumnIndexOrThrow("solucao"));
                Demandas demanda = new Demandas(id, titulo, date, descricao, local, tipoDemanda, estado, solucao);
                demandas.add(demanda);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return demandas;
    }

    public void atualizarEstadoDemanda(int id, String estado){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("estado", estado);
        db.update("demandas", values, "_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void atualizarEstadoSolucaoDemanda(int id, String estado, String solucao) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("estado", estado);
        values.put("solucao", solucao);
        db.update("demandas", values, "_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void atualizarSolucaoDemanda(int id, String solucao) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("solucao", solucao);
        db.update("demandas", values, "_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deletarDemanda(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("demandas", "_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE demandas ADD COLUMN solucao TEXT");
        }
    }
}
