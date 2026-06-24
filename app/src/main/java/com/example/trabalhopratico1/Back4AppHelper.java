package com.example.trabalhopratico1;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class Back4AppHelper {

    public interface Callback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }

    public static void getDemandas(Callback<ArrayList<Demandas>> callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Chamado");
        query.orderByDescending("createdAt");
        query.findInBackground((objects, e) -> {
            if (e == null) {
                callback.onSuccess(parseObjectsToDemandas(objects));
            } else {
                callback.onError(e);
            }
        });
    }

    public static void getDemanda(String parseObjectId, Callback<Demandas> callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Chamado");
        query.getInBackground(parseObjectId, (object, e) -> {
            if (e == null) {
                callback.onSuccess(parseObjectToDemanda(object));
            } else {
                callback.onError(e);
            }
        });
    }

    public static void getDemandasFiltradasPorEstado(List<String> estados, Callback<ArrayList<Demandas>> callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Chamado");
        query.whereContainedIn("estado", estados);
        query.orderByDescending("createdAt");
        query.findInBackground((objects, e) -> {
            if (e == null) {
                callback.onSuccess(parseObjectsToDemandas(objects));
            } else {
                callback.onError(e);
            }
        });
    }

    public static void getDemandasFiltradasPorData(String data, Callback<ArrayList<Demandas>> callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Chamado");
        query.whereEqualTo("dataCadastro", data);
        query.orderByDescending("createdAt");
        query.findInBackground((objects, e) -> {
            if (e == null) {
                callback.onSuccess(parseObjectsToDemandas(objects));
            } else {
                callback.onError(e);
            }
        });
    }

    public static void getDemandasFiltradasPorDataEstado(String data, List<String> estados, Callback<ArrayList<Demandas>> callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Chamado");
        query.whereEqualTo("dataCadastro", data);
        query.whereContainedIn("estado", estados);
        query.orderByDescending("createdAt");
        query.findInBackground((objects, e) -> {
            if (e == null) {
                callback.onSuccess(parseObjectsToDemandas(objects));
            } else {
                callback.onError(e);
            }
        });
    }

    public static void atualizarEstadoDemanda(String parseObjectId, String estado, Callback<Void> callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Chamado");
        query.getInBackground(parseObjectId, (object, e) -> {
            if (e == null) {
                object.put("estado", estado);
                object.saveInBackground(e1 -> {
                    if (e1 == null) {
                        callback.onSuccess(null);
                    } else {
                        callback.onError(e1);
                    }
                });
            } else {
                callback.onError(e);
            }
        });
    }

    public static void atualizarEstadoSolucaoDemanda(String parseObjectId, String estado, String solucao, Callback<Void> callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Chamado");
        query.getInBackground(parseObjectId, (object, e) -> {
            if (e == null) {
                object.put("estado", estado);
                object.put("solucao", solucao);
                object.saveInBackground(e1 -> {
                    if (e1 == null) {
                        callback.onSuccess(null);
                    } else {
                        callback.onError(e1);
                    }
                });
            } else {
                callback.onError(e);
            }
        });
    }

    public static void getTotalChamados(Callback<Integer> callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Chamado");
        query.countInBackground((count, e) -> {
            if (e == null) {
                callback.onSuccess(count);
            } else {
                callback.onError(e);
            }
        });
    }

    public static void getChamadosPorEstado(String estado, Callback<Integer> callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Chamado");
        query.whereEqualTo("estado", estado);
        query.countInBackground((count, e) -> {
            if (e == null) {
                callback.onSuccess(count);
            } else {
                callback.onError(e);
            }
        });
    }

    private static Demandas parseObjectToDemanda(ParseObject obj) {
        String parseObjectId = obj.getObjectId();
        String titulo = obj.getString("titulo");
        String descricao = obj.getString("descricao");
        String local = obj.getString("local");
        String tipo = obj.getString("tipo");
        String estado = obj.getString("estado");
        String solucao = obj.getString("solucao");
        String dataCadastro = obj.getString("dataCadastro");
        String fotoString = obj.getString("fotoString");

        if (dataCadastro == null && obj.getDate("dataCadastro") != null) {
            dataCadastro = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                    .format(obj.getDate("dataCadastro"));
        }

        int idLocal = obj.getInt("idLocal");

        Demandas d = new Demandas(parseObjectId, titulo, dataCadastro, descricao, local,
                tipo, estado, solucao, fotoString);
        d.setId(idLocal);
        return d;
    }

    private static ArrayList<Demandas> parseObjectsToDemandas(List<ParseObject> objects) {
        ArrayList<Demandas> list = new ArrayList<>();
        if (objects != null) {
            for (ParseObject obj : objects) {
                list.add(parseObjectToDemanda(obj));
            }
        }
        return list;
    }
}
