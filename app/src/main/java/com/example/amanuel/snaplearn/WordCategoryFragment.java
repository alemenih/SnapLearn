package com.example.amanuel.snaplearn;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Amanuel on 12/22/2015.
 */
public class WordCategoryFragment extends Fragment {
    private ListView taskListView;
    private String currentTabTag;
    private Spinner spinner;
    private DictionaryDB db;
    ArrayList<Word> words;
    Context context;
    String audioPath = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.child_layout,
                container, false);

        context = getActivity().getApplicationContext();
        spinner = (Spinner)view.findViewById(R.id.categorySpinner);
        db = new DictionaryDB(context);


        ArrayList<Category> category = db.getCategory();
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(
                getContext(), R.layout.spinner_list, category );

        spinner.setAdapter(adapter);
        // get references to widgets
        taskListView = (ListView) view.findViewById (R.id.wordListView);

        // get the current tab
        TabHost tabHost = (TabHost) container.getParent().getParent();
        spinner.setSelection(1);
        currentTabTag = spinner.getSelectedItem().toString();

        // refresh the task list view
        refreshTaskList();

        // return the view


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentTabTag = spinner.getSelectedItem().toString();
                words = db.getWords(currentTabTag);
               /* Collections.sort(words, new Comparator<Word>() {
                    @Override
                    public int compare(Word lhs, Word rhs) {
                        return lhs.getEnglishWord().compareTo(rhs.getEnglishWord());
                    }
                });
                */
                WordListAdapter wla = new WordListAdapter(getContext(), words);
                for (Word w: words) {
                    Log.e("Current Category", currentTabTag);
                    Log.e("My List", w.getEnglishWord());
                    Log.e("Image path", w.getAudio());
                }
                refreshTaskList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                Intent intent = new Intent(getActivity(), WordDisplay.class);
                intent.putExtra("english_word", words.get(position).getEnglishWord());
                intent.putExtra("amharic_meaning", words.get(position).getAmharicMeaning());
                intent.putExtra("audio_path", words.get(position).getAudio());
                intent.putExtra("image_path", words.get(position).getPhoto());
                startActivity(intent);
                */

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.word_dialog);
                dialog.setTitle(words.get(position).getEnglishWord());

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.tvAmhTrans);
                text.setText(words.get(position).getAmharicMeaning());
                ImageView image = (ImageView) dialog.findViewById(R.id.ivDlgImg);

                String strImgPath = words.get(position).getPhoto();
                Bitmap theImg = BitmapFactory.decodeFile(strImgPath);
                image.setImageBitmap(theImg);

                audioPath = words.get(position).getAudio();
                Button btnPronunciation = (Button) dialog.findViewById(R.id.btnAmhPron);
                // if button is clicked, close the custom dialog

                btnPronunciation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) throws IllegalArgumentException,SecurityException,IllegalStateException {
                        MediaPlayer m = new MediaPlayer();

                        try {
                            m.setDataSource(audioPath);
                        }

                        catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            m.prepare();
                        }

                        catch (IOException e) {
                            e.printStackTrace();
                        }

                        m.start();
                        //m.release();

                    }
                });

                Button done = (Button)dialog.findViewById(R.id.btnDone);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        return view;
    }

    public void refreshTaskList() {
        // get task list for current tab from database
        ArrayList<Word> words = db.getWords(currentTabTag);
        // create adapter and set it in the ListView widget
        WordListAdapter adapter = new WordListAdapter(context, words);
        taskListView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshTaskList();
    }
}
