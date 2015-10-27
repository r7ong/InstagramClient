package com.rtong.instagramclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.support.v4.app.DialogFragment;
import android.widget.ListView;

import java.util.ArrayList;

public class EditNameDialog extends DialogFragment {

    private ListView lvComments;
    static ArrayList<String> todoItems = new ArrayList<String>();
    ArrayAdapter<CharSequence> commentAdapter;


    public EditNameDialog() {
    }

    public static EditNameDialog newInstance(String title, ArrayList<String> todoItems) {
        EditNameDialog.todoItems = todoItems;
        EditNameDialog frag = new EditNameDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.comments, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CharSequence[] bar = new CharSequence[EditNameDialog.todoItems.size()];
        for(int i = 0; i < EditNameDialog.todoItems.size(); i++){
            bar[i] = Html.fromHtml(EditNameDialog.todoItems.get(i));
        }

        commentAdapter = new ArrayAdapter<CharSequence>(getContext(),android.R.layout.simple_list_item_1, bar);
        lvComments = (ListView) view.findViewById(R.id.lvComments);
        lvComments.setAdapter(commentAdapter);

        commentAdapter.notifyDataSetChanged();
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}