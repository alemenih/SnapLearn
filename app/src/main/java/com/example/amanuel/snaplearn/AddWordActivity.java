package com.example.amanuel.snaplearn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class AddWordActivity extends Fragment{

    private EditText etEnglishWord;
    private EditText etAmharicMeaning;
    private Spinner spinnerCategory;
    private ImageView ivPreview;
    private DictionaryDB db;
    private Word word;

    private String imgPath = "";
    private String audioPath = "";

    static final int CAMERA_PIC_REQUEST = 1337;
    private Uri fileUri;

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data){
    if(requestCode == CAMERA_PIC_REQUEST){
        imgPath = fileUri.getPath().toString();
        File imgFile  = new File(imgPath);
        ivPreview.setImageURI(Uri.fromFile(imgFile));
    }

    if (requestCode == 1){
        if (resultCode == Activity.RESULT_OK){
            audioPath = data.getStringExtra("result");
           // Toast.makeText(getContext(), audioPath, Toast.LENGTH_LONG).show();
        }
    }
}

public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                         Bundle savedInstanceState){
    View view = inflater.inflate(R.layout.fragment_add_word, container, false);
    etEnglishWord = (EditText)view.findViewById(R.id.etEnglishWord);
    etAmharicMeaning = (EditText)view.findViewById(R.id.etAmharicMeaning);
    spinnerCategory = (Spinner)view.findViewById(R.id.spinnerCategory);
    Button btnRecordAudio = (Button) view.findViewById(R.id.btnRecordAudio);
    Button btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
    ivPreview = (ImageView)view.findViewById(R.id.imageViewPreview);
    Button btnTakePhoto = (Button) view.findViewById(R.id.btnTakePhoto);
    db = new DictionaryDB(getContext());

    ArrayList<Category> category = db.getCategory();
    ArrayAdapter<Category> adapter = new ArrayAdapter<>(
            getContext(), R.layout.spinner_list, category );

    spinnerCategory.setAdapter(adapter);

    btnTakePhoto.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = getOutputMediaFileUri();

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, CAMERA_PIC_REQUEST);
        }
    });

    btnRecordAudio.setOnClickListener(new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), AudioRecordTest.class);
            startActivityForResult(intent, 1);
        }
    });

    btnSubmit.setOnClickListener(new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if (isValid()) {
                saveToDB();
                resetFields();
            }
            else
                Toast.makeText(getContext(), "Error! Please fill in all fields!", Toast.LENGTH_SHORT).show();
        }
    });

    return view;
}


    public void handleSubmit(View view){

    }
    private void saveToDB() {
        // get data from widgets
        word = new Word();
        int listID = spinnerCategory.getSelectedItemPosition() + 1;
        String englishWord = etEnglishWord.getText().toString();
        String amharicMeaning = etAmharicMeaning.getText().toString();

        word.setCategoryId(listID);
        word.setEnglishWord(englishWord);
        word.setAmharicMeaning(amharicMeaning);
        word.setPhoto(imgPath);
        word.setAudio(audioPath);

        db.insertWord(word);

        Context context = this.getContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, "Data successfully stored! ", duration);
        toast.show();
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/SnapLearn/images/");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }

    private static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    boolean isValid(){
        return etEnglishWord.getText().toString() != "" &&
                etAmharicMeaning.getText().toString() != "" &&
                imgPath != "" &&
                audioPath != "";
    }

    void resetFields(){
        etEnglishWord.setText("");
        etAmharicMeaning.setText("");
        imgPath = "";
        ivPreview.setImageResource(R.drawable.no_preview);
        audioPath = "";
    }

}