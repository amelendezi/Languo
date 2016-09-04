package com.amelendez.lgo.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    public void editAction(View view)
    {
        // Create edit dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_languo);

        // Change dialog size
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.98);
        dialog.getWindow().setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Load state into dialog
        EditText editTerm = (EditText) dialog.findViewById(R.id.editTermEditText);
        editTerm.setText(term.getText());
        EditText editDefinition = (EditText) dialog.findViewById(R.id.editDefinitionEditText);
        editDefinition.setText(definition.getText());
        EditText editExample = (EditText) dialog.findViewById(R.id.editExampleEditView);
        editExample.setText(example.getText());

        Button cancel = (Button) dialog.findViewById(R.id.editCancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button save = (Button) dialog.findViewById(R.id.editSaveChangesButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText changedTerm = (EditText) v.findViewById(R.id.editTermEditText);
                languo.setTerm(changedTerm.getText().toString());
                EditText changedDefinition = (EditText) v.findViewById(R.id.editDefinitionEditText);
                languo.setDefinition(changedDefinition.getText().toString());
                EditText changedExample = (EditText) v.findViewById(R.id.editExampleEditView);
                languo.setExample(changedExample.getText().toString());
                storageProvider.UpdateLanguo(languo);
                RenderView();
                dialog.dismiss();
            }
        });

        dialog.show();
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
