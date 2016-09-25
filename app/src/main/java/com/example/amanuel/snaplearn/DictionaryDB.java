package com.example.amanuel.snaplearn;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Amanuel on 12/11/2015.
 */
public class DictionaryDB {
    // database constants
    public static final String DB_NAME = "dictionary.db";
    public static final int    DB_VERSION = 1;

    // list table constants
    public static final String CATEGORY_TABLE = "category";

    public static final String CATEGORY_ID = "_id";
    public static final int    CATEGORY_ID_COL = 0;

    public static final String CATEGORY_NAME = "category_name";
    public static final int    CATEGORY_NAME_COL = 1;

    // task table constants
    public static final String WORD_TABLE = "word";

    public static final String WORD_ID = "_id";
    public static final int    WORD_ID_COL = 0;

    public static final String WORD_CATEGORY_ID = "category_id";
    public static final int    WORD_CATEGORY_ID_COL = 1;

    public static final String ENGLISH_WORD = "english_word";
    public static final int    ENGLISH_WORD_COL = 2;

    public static final String AMHARIC_MEANING = "amharic_meaning";
    public static final int    AMHARIC_MEANING_COL = 3;

    public static final String PHOTO = "photo";
    public static final int    PHOTO_COL = 4;

    public static final String PRONOUNCIATION = "pronounciation";
    public static final int    PRONOUNCIATION_COL = 5;

    // CREATE and DROP TABLE statements
    public static final String CREATE_CATEGORY_TABLE =
            "CREATE TABLE " + CATEGORY_TABLE + " (" +
                    CATEGORY_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CATEGORY_NAME + " TEXT    UNIQUE)";

    public static final String CREATE_WORD_TABLE =
            "CREATE TABLE " + WORD_TABLE + " (" +
                    WORD_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    WORD_CATEGORY_ID    + " INTEGER, " +
                    ENGLISH_WORD       + " TEXT, " +
                    AMHARIC_MEANING      + " TEXT, " +
                    PHOTO  + " TEXT, " +
                    PRONOUNCIATION     + " TEXT)";

    public static final String DROP_CATEGORY_TABLE =
            "DROP TABLE IF EXISTS " + CATEGORY_TABLE;

    public static final String DROP_WORD_TABLE =
            "DROP TABLE IF EXISTS " + WORD_TABLE;

    //CHANGE THIS LINE
   public static final String WORD_MODIFIED =
            "com.example.amanuel.snaplearn.WORD_MODIFIED";

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            // create tables
            db.execSQL(CREATE_CATEGORY_TABLE);
            db.execSQL(CREATE_WORD_TABLE);

            // insert lists
            db.execSQL("INSERT INTO category VALUES (1, 'Biology')");
            db.execSQL("INSERT INTO category VALUES (2, 'Clothing')");
            db.execSQL("INSERT INTO category VALUES (3, 'Electronics')");
            db.execSQL("INSERT INTO category VALUES (4, 'Food')");
            db.execSQL("INSERT INTO category VALUES (5, 'Furniture')");
            db.execSQL("INSERT INTO category VALUES (6, 'Household')");
            db.execSQL("INSERT INTO category VALUES (7, 'Nature')");
            db.execSQL("INSERT INTO category VALUES (9, 'Others')");
            db.execSQL("INSERT INTO category VALUES (8, 'Stationary')");

            // insert sample tasks
           /*db.execSQL("INSERT INTO word VALUES (1, 1, 'Book', " +
                    "'መጽሃፍ', '0', '0')");
            db.execSQL("INSERT INTO word VALUES (2, 1, 'Shirt', " +
                    "'ሸሚዝ', '0', '0')");
                    */
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            Log.d("Dictionary", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

            Log.d("Dictionary", "Deleting all data!");
            db.execSQL(DictionaryDB.DROP_CATEGORY_TABLE);
            db.execSQL(DictionaryDB.DROP_WORD_TABLE);
            onCreate(db);
        }
    }

    // database object and database helper object
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private Context context;

    // constructor
    public DictionaryDB(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    // private methods
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    private void broadcastWordModified() {
        Intent intent = new Intent(WORD_MODIFIED);
        context.sendBroadcast(intent);
    }

    // public methods
    public ArrayList<Category> getCategory() {
        ArrayList<Category> categories = new ArrayList<Category>();
        openReadableDB();
        Cursor cursor = db.query(CATEGORY_TABLE,
                null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Category category = new Category();
            category.setId(cursor.getInt(CATEGORY_ID_COL));
            category.setName(cursor.getString(CATEGORY_NAME_COL));

            categories.add(category);
        }
        cursor.close();
        closeDB();
        return categories;
    }

    public Category getCategory(String name) {
        String where = CATEGORY_NAME + "= ?";
        String[] whereArgs = { name };

        openReadableDB();
        Cursor cursor = db.query(CATEGORY_TABLE, null,
                where, whereArgs, null, null, null);
        Category category = null;
        cursor.moveToFirst();
        category = new Category(cursor.getInt(CATEGORY_ID_COL),
                cursor.getString(CATEGORY_NAME_COL));
        cursor.close();
        this.closeDB();

        return category;
    }

    public ArrayList<Word> getWords(String categoryName) {
    /*    String where =
                TASK_LIST_ID + "= ? AND " +
                      TASK_HIDDEN + "!='1'";
    */
        String where = WORD_CATEGORY_ID + "= ?";
        String orderBy =  ENGLISH_WORD;
        long categoryID = getCategory(categoryName).getId();
        String[] whereArgs = { Long.toString(categoryID) };

        this.openReadableDB();
        Cursor cursor = db.query(WORD_TABLE, null,
                where, whereArgs,
                null, null, orderBy);
        ArrayList<Word> words = new ArrayList<Word>();
        while (cursor.moveToNext()) {
            words.add(getWordFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();
        this.closeDB();
        return words;
    }

    public Word getWord(long id) {
        String where = WORD_ID + "= ?";
        String[] whereArgs = { Long.toString(id) };

        this.openReadableDB();
        Cursor cursor = db.query(WORD_TABLE,
                null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        Word word = getWordFromCursor(cursor);
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return word;
    }

    private static Word getWordFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                Word word = new Word(
                        cursor.getInt(WORD_ID_COL),
                        cursor.getInt(WORD_CATEGORY_ID_COL),
                        cursor.getString(ENGLISH_WORD_COL),
                        cursor.getString(AMHARIC_MEANING_COL),
                        cursor.getString(PHOTO_COL),
                        cursor.getString(PRONOUNCIATION_COL));
                return word;
            }
            catch(Exception e) {
                return null;
            }
        }
    }

    public long insertWord(Word word) {
        ContentValues cv = new ContentValues();
        cv.put(WORD_CATEGORY_ID, word.getCategoryId());
        cv.put(ENGLISH_WORD, word.getEnglishWord());
        cv.put(AMHARIC_MEANING, word.getAmharicMeaning());
        cv.put(PHOTO, word.getPhoto());
        cv.put(PRONOUNCIATION, word.getAudio());

        this.openWriteableDB();
        long rowID = db.insert(WORD_TABLE, null, cv);
        this.closeDB();

        broadcastWordModified();

        return rowID;
    }

    public int updateWord(Word word) {
        ContentValues cv = new ContentValues();
        cv.put(WORD_CATEGORY_ID, word.getCategoryId());
        cv.put(ENGLISH_WORD, word.getEnglishWord());
        cv.put(AMHARIC_MEANING, word.getAmharicMeaning());
        cv.put(PHOTO, word.getPhoto());
        cv.put(PRONOUNCIATION, word.getAudio());

        String where = WORD_ID + "= ?";
        String[] whereArgs = { String.valueOf(word.getId()) };

        this.openWriteableDB();
        int rowCount = db.update(WORD_TABLE, cv, where, whereArgs);
        this.closeDB();

        broadcastWordModified();

        return rowCount;
    }

    public int deleteWord(long id) {
        String where = WORD_ID + "= ?";
        String[] whereArgs = { String.valueOf(id) };

        this.openWriteableDB();
        int rowCount = db.delete(WORD_TABLE, where, whereArgs);
        this.closeDB();

        broadcastWordModified();

        return rowCount;
    }

    public String[] getTopEnglishWords(int wordCount) {
        String where = WORD_MODIFIED + "= '0'";
        String orderBy = ENGLISH_WORD + " DESC";
        this.openReadableDB();
        Cursor cursor = db.query(WORD_TABLE, null,
                where, null, null, null, orderBy);

        String[] taskNames = new String[wordCount];
        for (int i = 0; i < wordCount; i++) {
            if (cursor.moveToNext()) {
                Word word = getWordFromCursor(cursor);
                taskNames[i] = word.getEnglishWord();
            }
        }

        if (cursor != null)
            cursor.close();
        db.close();

        return taskNames;
    }

    /*
     * Methods for content provider
     * NOTE: You don't close the DB connection after executing
     * a query, insert, update, or delete operation
     */
    public Cursor genericQuery(String[] projection, String where,
                               String[] whereArgs, String orderBy) {
        this.openReadableDB();
        return db.query(WORD_TABLE, projection, where, whereArgs, null, null, orderBy);
    }

    public long genericInsert(ContentValues values) {
        this.openWriteableDB();
        return db.insert(WORD_TABLE, null, values);
    }

    public int genericUpdate(ContentValues values, String where,
                             String[] whereArgs) {
        this.openWriteableDB();
        return db.update(WORD_TABLE, values, where, whereArgs);
    }

    public int genericDelete(String where, String[] whereArgs) {
        this.openWriteableDB();
        return db.delete(WORD_TABLE, where, whereArgs);
    }
}
