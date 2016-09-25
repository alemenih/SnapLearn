package com.example.amanuel.snaplearn;

import java.sql.Blob;
import java.util.Comparator;

/**
 * Created by Amanuel on 12/12/2015.
 */
public class Word implements Comparator<Word>{
    private long wordId;
    private long categoryId;
    private String englishWord;
    private String amharicMeaning;
    private String photo;
    private String audio;

    public static final String TRUE = "1";
    public static final String FALSE = "0";

    public Word() {
        englishWord = "";
        amharicMeaning = "";
        photo = null;
        audio = null;
    }

    public Word(long categoryId, String englishWord, String amharicMeaning,
                String photo, String audio) {
        this.categoryId = categoryId;
        this.englishWord = englishWord;
        this.amharicMeaning = amharicMeaning;
        this.photo = photo;
        this.audio = audio;
    }

    public Word(int wordId, int categoryId, String englishWord, String amharicMeaning,
                String photo, String audio) {
        this.wordId = wordId;
        this.categoryId = categoryId;
        this.englishWord = englishWord;
        this.amharicMeaning = amharicMeaning;
        this.photo = photo;
        this.audio = audio;    }

    public long getId() {
        return wordId;
    }

    public void setId(long wordId) {
        this.wordId = wordId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getEnglishWord() { return englishWord;  }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public String getAmharicMeaning() {
        return amharicMeaning;
    }

    public void setAmharicMeaning(String amharicMeaning) {
        this.amharicMeaning = amharicMeaning;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAudio(){
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    @Override
    public int compare(Word lhs, Word rhs) {
        return lhs.getEnglishWord().compareTo(rhs.englishWord);
    }
}
