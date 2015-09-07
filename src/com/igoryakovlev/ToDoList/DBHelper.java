package com.igoryakovlev.ToDoList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Smile on 07.09.15.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static int VERSION = 1;

    public DBHelper(Context context)
    {
        super(context,"todoDB",null, VERSION);
    }


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
