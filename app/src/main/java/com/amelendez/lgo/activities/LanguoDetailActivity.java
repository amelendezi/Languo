package com.amelendez.lgo.activities;

import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amelendez.lgo.languo.R;
import com.amelendez.lgo.storage.api.StorageProvider;
import com.amelendez.lgo.storage.dao.Languo;

public class LanguoDetailActivity extends AppCompatActivity {

    private StorageProvider storageProvider;
    private Languo languo;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadActivityState();
        setContentView(R.layout.activity_languo_detail);
        RenderView();
    }

    private void RenderView() {
        TextView termView = (TextView) findViewById(R.id.detail_termTextView);
        termView.setText(languo.getTerm());

        TextView definitionView = (TextView) findViewById(R.id.detail_definitionTextView);
        definitionView.setText(languo.getDefinition());

        TextView exampleView = (TextView) findViewById(R.id.detail_exampleTextView);
        exampleView.setText(languo.getExample());

        TextView hitsView = (TextView) findViewById(R.id.detail_hitsTextView);
        hitsView.setText(String.valueOf(languo.getHits()));

        RatingBar levelView = (RatingBar) findViewById(R.id.detail_levelRatingBar);
        Log.d("Level", String.valueOf(languo.getLevel()));
        levelView.setRating(languo.getLevel());
    }

    private void LoadActivityState()
    {
        LoadTermKeyFromBundle();
        storageProvider = new StorageProvider(this);
        languo = storageProvider.GetLanguoByTerm(key);
    }

    private void LoadTermKeyFromBundle()
    {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            key = bundle.getString("term");
        }
        // TODO: what happens if key cannot be loaded?
    }
}
