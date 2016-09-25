package com.example.amanuel.snaplearn;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Amanuel on 4/25/2016.
 */
public class WordDisplay extends Activity {

    Button pronounciation;
    TextView word, amharicEq;
    ImageView imageEq;
    private String outputFile = null;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_word);

        Intent intent = getIntent();

        String strEngWord = intent.getStringExtra("english_word");
        String strAmhMeaning = intent.getStringExtra("amharic_meaning");
        String strAudioPath = intent.getStringExtra("audio_path");
        String strImgPath = intent.getStringExtra("image_path");

        pronounciation = (Button)findViewById(R.id.btnPronounciation);
        //word = (TextView)findViewById(R.id.tvEnglishWrd);
        word = (TextView)findViewById(R.id.textView4);
        amharicEq = (TextView)findViewById(R.id.tvAmharicEq);
        imageEq = (ImageView)findViewById(R.id.ivImageEq);

        word.setText(strEngWord);
        amharicEq.setText(strAmhMeaning);
        outputFile = strAudioPath;

        Bitmap theImg = BitmapFactory.decodeFile(strImgPath);
        //imageEq.setRotation(90);
        imageEq.setImageBitmap(theImg);

        pronounciation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException,SecurityException,IllegalStateException {
                MediaPlayer m = new MediaPlayer();

                try {
                    m.setDataSource(outputFile);
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
                Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
