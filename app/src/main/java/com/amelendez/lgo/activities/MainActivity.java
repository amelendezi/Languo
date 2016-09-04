package com.amelendez.lgo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.amelendez.lgo.adapters.LanguoListAdapter;
import com.amelendez.lgo.languo.R;
import com.amelendez.lgo.storage.api.StorageProvider;
import com.amelendez.lgo.storage.dao.Languo;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<Languo> languos;
    private ArrayAdapter<Languo> adapter;
    private StorageProvider storageProvider;

    private void Initialize()
    {
        listView = (ListView) findViewById(R.id.mainListView);
        storageProvider = new StorageProvider(this.getBaseContext());
        RenderListView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initialize();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        RenderListView();
    }

    public void newLanguoAction(View view) {
        Intent i = new Intent(getApplicationContext(), NewLanguoActivity.class);
        startActivity(i);
    }

    private void RenderListView()
    {
        languos = storageProvider.GetAllLanguos();
        adapter = new LanguoListAdapter(this, languos);
        listView = (ListView) findViewById(R.id.mainListView);
        listView.setAdapter(adapter);
    }
}