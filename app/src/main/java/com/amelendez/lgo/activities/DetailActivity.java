package com.amelendez.lgo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amelendez.lgo.languo.R;
import com.amelendez.lgo.storage.api.StorageProvider;
import com.amelendez.lgo.storage.dao.Languo;
import com.amelendez.lgo.util.LevelToMessageMapper;

public class DetailActivity extends AppCompatActivity {

    private StorageProvider storageProvider;
    private LevelToMessageMapper mapper;
    private Languo languo;
    private String key;
    private TextView term;
    private TextView definition;
    private TextView example;
    private TextView levelMessage;
    private RatingBar level;
    private TextView hits;

    private void Initialize()
    {
        storageProvider = new StorageProvider(this);
        mapper = new LevelToMessageMapper();
        term = (TextView) findViewById(R.id.detailTermTextView);
        definition = (TextView) findViewById(R.id.detailDefinitionTextView);
        example = (TextView) findViewById(R.id.detailExampleTextView);
        levelMessage = (TextView) findViewById(R.id.detailLevelMessageTextView);
        level = (RatingBar) findViewById(R.id.detailLevelRatingBar);
        hits = (TextView) findViewById(R.id.detailHitsTextView);
        SetListeners();
        LoadState();
        RenderView();
    }

    private void SetListeners()
    {
        level.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                languo.setLevel(rating);
                storageProvider.UpdateLanguo(languo);
                levelMessage.setText(mapper.GetMessage(rating));
            }
        });
    }

    private void LoadState()
    {
        LoadTermKeyFromBundle();
        languo = storageProvider.GetLanguoByTerm(key);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languo_detail);
        Initialize();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void RenderView() {
        term.setText(languo.getTerm());
        definition.setText(languo.getDefinition());
        example.setText(languo.getExample());
        hits.setText(String.valueOf(languo.getHits()));
        level.setRating(languo.getLevel());
        levelMessage.setText(mapper.GetMessage(languo.getLevel()));
    }

    private void LoadTermKeyFromBundle()
    {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            key = bundle.getString("term");
        }
    }
}
