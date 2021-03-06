package com.example.user.how_about_a_cafe;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SharedPref {

    /* Deklarasi dulu nama file dan string yang akan di buat dalam folder sharedpreferences */
    /* Nama file nya */
    private static final String PREFS_NAME = "FILE_PREFERENCES";

    /* String item list utama */
    private static final String FAVORITES = "ITEM_FAVORITE";

    /* Single string untuk ambil posisi (index) */
    private static final String INDEX = "INDEX_LIST";

    /* Metode save list */
    private void saveFavorites(Context context, List<ListItem> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites); // konversi ke string json

        editor.putString(FAVORITES, jsonFavorites);
        editor.apply();
    }


    /* Metode menambah item ke list sharedpreferences*/
    public void addFavorite(Context context, ListItem Item) {
        ArrayList<ListItem> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<>();

        favorites.add(Item); // menambahkan item ke list
        saveFavorites(context, favorites); // memanggil metode saveFavorites untuk di simpan dalam file
    }

    /* Metode untuk mengapus item di list */
    public void removeFavorite(Context context, int pos) {
        ArrayList<ListItem> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(pos); // menghapus item berdasarkan posisi index (int)
            saveFavorites(context, favorites);
        }
    }

    /* Metode untuk memanggil list untuk di display dalam recyclerview */
    public ArrayList<ListItem> getFavorites(Context context) {
        SharedPreferences settings;
        List<ListItem> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();

            /* mengambil arraylist dari file yang telah disimpan */
            ListItem[] favoriteItems = gson.fromJson(jsonFavorites,
                    ListItem[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<>(favorites);
        } else
            return null;

        return (ArrayList<ListItem>) favorites;
    }


    /* Metode ntuk mengambil posisi item, dibuat single string pada list baru */
    public void addIndex(Context context, String string) {
        List<String> names;
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME,
                MODE_PRIVATE);
        String json = preferences.getString(INDEX, null);
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType(); // mengatur tipe single string pada list
        names = gson.fromJson(json, type);
        if (names == null) {
            names = new ArrayList<>();
        }
        names.add(string); // menambahkan item string, harus dipanggil bersamaan dengan metode addFavorite
        SharedPreferences.Editor editor = preferences.edit();

        String listJson = gson.toJson(names);
        editor.putString(INDEX, listJson);
        editor.apply();
    }


    /* Metode untuk menghapus, sama sih kaya diatas, cuman ini buat remove string nya */
    public void removeIndex(Context context, String string) {
        List<String> names;
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME,
                MODE_PRIVATE);
        String json = preferences.getString(INDEX, null);
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        names = gson.fromJson(json, type);
        if (names == null) {
            names = new ArrayList<>();
        }
        names.remove(string); // nah ini ngapus single string
        SharedPreferences.Editor editor = preferences.edit();

        String listJson = gson.toJson(names);
        editor.putString(INDEX, listJson);
        editor.apply();
    }

    /* Metode untuk mengambil posisi di keberapa dia item nya */
    public int setIndex(Context context, String string) {
        List<String> names;
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME,
                MODE_PRIVATE);
        String json = preferences.getString(INDEX, null);
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        names = gson.fromJson(json, type);
        int position = 0;
        if (names != null) {
            position = names.indexOf(string);
        }
        return position; // return ke hasil
    }
}
