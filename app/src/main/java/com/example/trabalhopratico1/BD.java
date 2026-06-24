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
    private static final int DATABASE_VERSION = 3;

    public BD(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
                        "estado TEXT CHECK(estado IN ('Aberto','Em andamento','Conclu\u00EDdo')) DEFAULT 'Aberto'," +
                        "tipo TEXT," +
                        "solucao TEXT," +
                        "imagePath TEXT" +
                        ")"
        );

        Log.i("##", "Tabela demandas criada com sucesso");
    }

    public long salvarDados(String titulo, String descricao, String local, String estado, String imagePath){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descricao", descricao);
        values.put("local", local);
        values.put("estado", estado);
        values.put("imagePath", imagePath);
        long id = db.insert("demandas", null, values);
        db.close();
        Log.i("##", "Dados inseridos com sucesso, id=" + id);
        return id;
    }

    public long salvarDados(String titulo, String descricao, String local, String estado, String imagePath, String data){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("descricao", descricao);
        values.put("local", local);
        values.put("estado", estado);
        values.put("imagePath", imagePath);
        values.put("date", data);
        long id = db.insert("demandas", null, values);
        db.close();
        Log.i("##", "Dados inseridos com sucesso, id=" + id);
        return id;
    }

    public ArrayList<Demandas> getDemandas(){
        ArrayList<Demandas> demandas = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("demandas", null, null, null, null, null, "_id DESC");

        if(cursor.moveToFirst()) {
            do {
                demandas.add(cursorToDemanda(cursor));
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
            Demandas demanda = cursorToDemanda(cursor);
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
                demandas.add(cursorToDemanda(cursor));
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
                demandas.add(cursorToDemanda(cursor));
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
                demandas.add(cursorToDemanda(cursor));
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

    public int getTotalChamados() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM demandas", null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return total;
    }

    public int getChamadosPorEstado(String estado) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM demandas WHERE estado = ?", new String[]{estado});
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return total;
    }

    private Demandas cursorToDemanda(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
        String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
        String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
        String local = cursor.getString(cursor.getColumnIndexOrThrow("local"));
        String tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));
        String estado = cursor.getString(cursor.getColumnIndexOrThrow("estado"));
        String solucao = cursor.getString(cursor.getColumnIndexOrThrow("solucao"));
        String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("imagePath"));
        return new Demandas(id, titulo, date, descricao, local, tipo, estado, solucao, imagePath);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE demandas ADD COLUMN solucao TEXT");
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE demandas ADD COLUMN imagePath TEXT");
            db.execSQL("CREATE TABLE IF NOT EXISTS demandas_nova(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "titulo TEXT," +
                    "date DATE DEFAULT CURRENT_DATE," +
                    "descricao TEXT," +
                    "local TEXT," +
                    "estado TEXT CHECK(estado IN ('Aberto','Em andamento','Conclu\u00EDdo')) DEFAULT 'Aberto'," +
                    "tipo TEXT," +
                    "solucao TEXT," +
                    "imagePath TEXT" +
                    ")");
            db.execSQL("INSERT INTO demandas_nova SELECT _id, titulo, date, descricao, local, " +
                    "CASE estado WHEN 'Em Atendimento' THEN 'Em andamento' WHEN 'Concluido' THEN 'Conclu\u00EDdo' ELSE estado END, " +
                    "tipo, solucao, imagePath FROM demandas");
            db.execSQL("DROP TABLE demandas");
            db.execSQL("ALTER TABLE demandas_nova RENAME TO demandas");
        }
    }
}
