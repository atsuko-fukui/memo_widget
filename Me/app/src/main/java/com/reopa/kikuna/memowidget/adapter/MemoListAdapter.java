package com.reopa.kikuna.memowidget.adapter;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.reopa.kikuna.memowidget.R;
import com.reopa.kikuna.memowidget.entity.MemosEntity;

import java.util.List;
import java.util.Random;

/**
 * Created by arthurvc on 2015/07/22.
 */
public class MemoListAdapter extends ArrayAdapter<MemosEntity> {

    /**
     * Memolist layout resource ID.
     */
    private static final int ID_RESOURCE = R.layout.memos_list_fragment_layout;
    /**
     * Textview ID.
     */
    private static final int ID_LAYOUT_MEMOTEXT = R.id.memo_text;
    /**
     * Textview background ID list.
     */
    private static final int ID_DRAWABLE_MEMO_BG[] =
            {R.drawable.husen_0, R.drawable.husen_1, R.drawable.husen_2, R.drawable.husen_3};

    private LayoutInflater mInflater;
    private List<MemosEntity> mListItems;


    public MemoListAdapter(Context context, List<MemosEntity> items) {
        super(context, ID_RESOURCE, items);

        this.mListItems = items;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get contentView by arg.
        View view = convertView;

        // If contentView is null, create new View by LayoutInflater.
        if (view == null) {
            view = mInflater.inflate(ID_RESOURCE, null);
        }

        if(mListItems.size() == 0){
            return view;
        }

        // Get MemoEntity object that match the position.
        MemosEntity memo = mListItems.get(position);

        // Set memotext.
        TextView memoTextView = (TextView) view.findViewById(ID_LAYOUT_MEMOTEXT);
        setMemoText(memoTextView, memo.getMemoText());

        return view;
    }

    private void setMemoText(TextView view, String text) {
        // Set memo text.
        view.setText(text);

        // Create a randam number to select a background image.
        int num = text.length() % 4;
        view.setBackgroundResource(ID_DRAWABLE_MEMO_BG[num]);
    }
}
