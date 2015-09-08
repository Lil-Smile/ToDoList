package com.igoryakovlev.ToDoList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Smile on 07.09.15.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static int VERSION = 1;
    private static String TABLE_NAME = "to_do_table";
    public static String JOBS_NAME = "jobs_name";
    public static String DONE_OR_NOT = "done_or_not";

    public DBHelper(Context context)
    {
        super(context,"todoDB",null, VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        String executionString = "CREATE TABLE "+TABLE_NAME+" (id integer autoincrement, "+JOBS_NAME+" text, "+DONE_OR_NOT+" text);";
        db.execSQL(executionString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getAll()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,null,null,null,null,null,null,null);
        return cursor;
    }

    public Cursor getDone()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = DONE_OR_NOT+" like ? ";
        String[] selectionArgs = {"true"};
        Cursor cursor = db.query(TABLE_NAME,null,selection,selectionArgs,null,null,null);
        return cursor;
    }

    public Cursor getNotDone()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = DONE_OR_NOT+" like ? ";
        String[] selectionArgs = {"false"};
        Cursor cursor = db.query(TABLE_NAME,null,selection,selectionArgs,null,null,null);
        return cursor;
    }


    public void writeToDB(String todo)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(JOBS_NAME,todo);
        cv.put(DONE_OR_NOT,"false");
        db.insert(TABLE_NAME, null, cv);
    }

    public void updateDb(String todo, boolean done)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(JOBS_NAME,todo);
        if (done)
        {
            cv.put(DONE_OR_NOT, "true");
        } else
        {
            cv.put(DONE_OR_NOT, "false");
        }
        String[] args = {todo};
        db.update(TABLE_NAME,cv,JOBS_NAME+" like ? ",args);
    }


}
