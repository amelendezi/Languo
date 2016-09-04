package com.amelendez.lgo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.amelendez.lgo.languo.R;
import com.amelendez.lgo.storage.api.StorageProvider;
import com.amelendez.lgo.storage.dao.Languo;

public class NewLanguoActivity extends AppCompatActivity {

    private StorageProvider storageProvider;
    private Languo languo;
    private EditText term;
    private EditText definition;
    private EditText example;
    private RatingBar rating;

    private void Initialize()
    {
        languo = new Languo();
        storageProvider = new StorageProvider(this);
        term = (EditText) findViewById(R.id.newTermEditText);
        definition = (EditText) findViewById(R.id.newDefinitionEditText);
        example = (EditText) findViewById(R.id.newExampleEditText);
        rating = (RatingBar) findViewById(R.id.newRatingBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_languo);
        Initialize();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void saveLanguoAction(View view)
    {
        LoadActivityState();
        storageProvider.SaveLanguo(languo);
        finish();
    }

    private void LoadActivityState()
    {
        languo.setTerm(term.getText().toString());
        languo.setDefinition(definition.getText().toString());
        languo.setExample(example.getText().toString());
        languo.setLevel(rating.getRating());
        languo.setHits(0);
    }
}