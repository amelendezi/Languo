package com.amelendez.lgo.activities;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.amelendez.lgo.adapters.LanguoListAdapter;
import com.amelendez.lgo.languo.R;
import com.amelendez.lgo.storage.api.StorageProvider;
import com.amelendez.lgo.storage.dao.DaoMaster;
import com.amelendez.lgo.storage.dao.Languo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<Languo> adapter;
    private StorageProvider storageProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storageProvider = new StorageProvider(this.getBaseContext());
        LoadItems();
    }

    public void addLanguoAction(View view) {
        Intent i = new Intent(getApplicationContext(), NewLanguoActivity.class);
        startActivity(i);
    }

    private void LoadItems()
    {
        List<Languo> languos = storageProvider.GetAllLanguos();
        RenderListView(languos);
    }

    private void RenderListView(List<Languo> languos)
    {
        if(languos == null)
        {
            languos = new ArrayList<>();
        }
        adapter = new LanguoListAdapter(this, languos);
        ListView listView = (ListView) findViewById(R.id.main_listView);
        listView.setAdapter(adapter);
    }

    // DEV Options ---------------------------------------------------------------------------------
    public void devOps(View v)
    {
        storageProvider.ClearAll(this);
        Log.d("DevOps", "Cleared database");
    }
}
