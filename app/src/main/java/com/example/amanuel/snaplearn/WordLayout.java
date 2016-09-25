package com.example.amanuel.snaplearn;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Amanuel on 12/22/2015.
 */
public class WordLayout extends RelativeLayout{
    private ImageView image;
    private TextView tvEnglishWord;
    private TextView tvAmharicMeaning;
    private Spinner catSpinner;
    private Button btnPlayAccent;

    private Word word;
    private DictionaryDB db;
    private Context context;

    public WordLayout(Context context) {   // used by Android tools
        super(context);
    }

    public WordLayout(Context context, Word w) {
        super(context);

        // set context and get db object
        this.context = context;
        db = new DictionaryDB(context);

        // inflate the layout
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_view_word, this, true);

        // get references to widgets
        //image = (ImageView) findViewById(R.id.ivPhoto);
        tvEnglishWord = (TextView) findViewById(R.id.wordTextView);
        //tvAmharicMeaning = (TextView) findViewById(R.id.translationTextView);
        catSpinner = (Spinner) findViewById(R.id.categorySpinner);
       // btnPlayAccent = (Button)findViewById(R.id.btnAccent);

        setWord(w);
    }

    public void setWord(Word w) {

        word = w;
        tvEnglishWord.setText(word.getEnglishWord());
        //tvAmharicMeaning.setText(word.getAmharicMeaning());
       // String path = word.getPhoto();
        String imgPath = word.getPhoto();
        final String audioPath = word.getAudio();
       // File imgFile  = new File(imgPath);
        //image.setImageURI(Uri.fromFile(imgFile));

       /* btnPlayAccent.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
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
            }
        });
        */
    }
}
