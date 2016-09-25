package com.example.amanuel.snaplearn;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Amanuel on 12/22/2015.
 */
public class WordListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Word> words;

    public WordListAdapter(Context context, ArrayList<Word> words){
        this.context = context;
        this.words = words;
    }

    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public Object getItem(int position) {
        return words.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WordLayout wordLayout = null;
        Word word = words.get(position);

        if (convertView == null) {
            wordLayout = new WordLayout(context, word);
        }
        else {
            wordLayout = (WordLayout) convertView;
            wordLayout.setWord(word);
        }
        return wordLayout;
    }
}
