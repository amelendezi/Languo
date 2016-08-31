package com.amelendez.lgo.activities;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_languo);
        languo = new Languo();
        storageProvider = new StorageProvider(this);
    }

    public void saveLanguoAction(View view)
    {
        // Load state
        LoadActivityState();

        // TODO: Validate state

        // Store the new languo
        storageProvider.SaveLanguo(languo);

        // Go back to main activity
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    private void LoadActivityState()
    {
        EditText termView = (EditText) findViewById(R.id.new_termEditText);
        languo.setTerm(termView.getText().toString());

        EditText definitionView = (EditText) findViewById(R.id.new_definitionEditText);
        languo.setDefinition(definitionView.getText().toString());

        EditText exampleView = (EditText) findViewById(R.id.new_exampleEditText);
        languo.setExample(exampleView.getText().toString());

        RatingBar levelView = (RatingBar) findViewById(R.id.new_levelRatingBar);
        languo.setLevel(levelView.getRating());

        languo.setHits(0); // default value for new languos
    }
}
