package com.amelendez.lgo.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.amelendez.lgo.activities.DetailActivity;
import com.amelendez.lgo.languo.R;
import com.amelendez.lgo.storage.api.StorageProvider;
import com.amelendez.lgo.storage.dao.Languo;

import java.util.List;

public class LanguoListAdapter extends ArrayAdapter<Languo> {

    private LayoutInflater viewInflater;
    private StorageProvider storageProvider;

    public LanguoListAdapter(Context context, List<Languo> data) {
       super(context, R.layout.list_view_item, data);
        viewInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        storageProvider = new StorageProvider(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if(convertView == null)
        {
            // Inflate the view which converts the item xml into a view
            view = viewInflater.inflate(R.layout.list_view_item, parent, false);
        }

        // Get the item in the adapter
        Languo languo = getItem(position);

        // Get the item views
        TextView term = (TextView) view.findViewById(R.id.item_termTextView);
        TextView definition = (TextView) view.findViewById(R.id.item_definitionTextView);
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.item_deleteImageButton);

        // Set listener behavior for navigation to detail activity
        SetListenerForGoToDetailScreen(view, term, definition);

        // Set listener behavior for delete item
        SetListenerForDeleteButton(deleteButton);

        // Populate the row's xml with info from the item
        term.setText(languo.getTerm());
        definition.setText(languo.getDefinition());

        // Return the generated view
        return view;
    }

    private void SetListenerForGoToDetailScreen(View view, TextView term, TextView definition)
    {
        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView termView = (TextView)v;
                String termKey = (String) termView.getText();
                GoToLanguoDetail(termKey);
            }
        });

        definition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToLanguoDetail(GetTermKey(v));
            }
        });
    }

    private void GoToLanguoDetail(String termKey)
    {
        Intent intent;
        intent = new Intent(getContext(), DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(storageProvider.LANGUO_KEY_NAME, termKey);
        intent.putExtras(bundle);
        getContext().startActivity(intent);
}

    private void SetListenerForDeleteButton(View view)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the delete dialog
                CreateConfirmationDialog(v);
            }
        });
    }

    private String GetTermKey(View v)
    {
        View parentView = (View)v.getParent();
        TextView termView = (TextView)parentView.findViewById(R.id.item_termTextView);
        return (String) termView.getText();
    }

    private void CreateConfirmationDialog(View v)
    {
        final Context context = v.getContext();
        final String termKey = GetTermKey(v);
        final ListView listView = (ListView)v.getParent().getParent();
        final View v1= v;

        // TODO: improve this code
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage("Are you sure you would like to delete the item?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                storageProvider.DeleteLanguo(termKey);
                LanguoListAdapter adapter = (LanguoListAdapter) listView.getAdapter();
                int postition = listView.getPositionForView(v1);
                adapter.remove(adapter.getItem(postition));
                adapter.notifyDataSetChanged();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.create().show();
    }
}