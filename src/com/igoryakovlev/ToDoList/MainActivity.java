package com.igoryakovlev.ToDoList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity implements View.OnClickListener{
    /**
     * Called when the activity is first created.
     */

    Button buttonAdd;
    Button buttonAll;
    Button buttonDone;
    Button buttonNotDone;

    ListView lvListofJobs;

    DBHelper dbHelper;

    String[] from = {DBHelper.DONE_OR_NOT,DBHelper.JOBS_NAME};
    int[] to = {R.id.cbJob, R.id.tvJob};

    ArrayList<HashMap<String,Object>> data = new ArrayList<HashMap<String, Object>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        buttonAdd = (Button)findViewById(R.id.buttonAdd);
        buttonAll = (Button)findViewById(R.id.buttonAll);
        buttonDone = (Button)findViewById(R.id.buttonDone);
        buttonNotDone = (Button)findViewById(R.id.buttonNotDone);
        lvListofJobs = (ListView)findViewById(R.id.lvListOfJobs);

        buttonDone.setOnClickListener(this);
        buttonNotDone.setOnClickListener(this);
        buttonAll.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.getAll();
        if (cursor.moveToFirst())
        {
            int jobColIndex = cursor.getColumnIndex(DBHelper.JOBS_NAME);
            int doneColIndex = cursor.getColumnIndex(DBHelper.DONE_OR_NOT);
            do
            {
                HashMap<String,Object> m = new HashMap<String, Object>();
                m.put(DBHelper.JOBS_NAME,cursor.getString(jobColIndex));
                boolean tmp = false;
                if (cursor.getString(doneColIndex)=="true")
                {
                    tmp = true;
                }
                m.put(DBHelper.DONE_OR_NOT,tmp);
                data.add(m);
            } while (cursor.moveToNext());
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,data,R.layout.view_for_list,from,to);
        lvListofJobs.setAdapter(simpleAdapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.buttonAdd:
            {
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_todo_dialog);
                dialog.setTitle(getString(R.string.newTask));
                Button buttonOk = (Button)dialog.findViewById(R.id.buttonOK);
                Button buttonCancel = (Button)dialog.findViewById(R.id.buttonCancel);
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText)dialog.findViewById(R.id.etNewToDo);
                        String todo = editText.getText().toString();
                        dbHelper.writeToDB(todo);
                        ArrayList<HashMap<String,Object>> tmpData = new ArrayList<HashMap<String, Object>>();
                        Cursor cursor = dbHelper.getAll();
                        if (cursor.moveToFirst())
                        {
                            int jobColIndex = cursor.getColumnIndex(DBHelper.JOBS_NAME);
                            int doneColIndex = cursor.getColumnIndex(DBHelper.DONE_OR_NOT);
                            do
                            {
                                HashMap<String,Object> m = new HashMap<String, Object>();
                                m.put(DBHelper.JOBS_NAME,cursor.getString(jobColIndex));
                                boolean tmp = false;
                                if (cursor.getString(doneColIndex)=="true")
                                {
                                    tmp = true;
                                }
                                m.put(DBHelper.DONE_OR_NOT,tmp);
                                tmpData.add(m);
                            } while (cursor.moveToNext());
                        }

                        SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(),tmpData,R.layout.view_for_list,from,to);
                        lvListofJobs.setAdapter(simpleAdapter);
                        dialog.dismiss();

                    }
                });
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;
            }
            case R.id.buttonAll:
            {
                ArrayList<HashMap<String,Object>> dataTmp = new ArrayList<HashMap<String, Object>>();
                Cursor cursor = dbHelper.getAll();
                if (cursor.moveToFirst())
                {
                    int jobColIndex = cursor.getColumnIndex(DBHelper.JOBS_NAME);
                    int doneColIndex = cursor.getColumnIndex(DBHelper.DONE_OR_NOT);
                    do
                    {
                        HashMap<String,Object> m = new HashMap<String, Object>();
                        m.put(DBHelper.JOBS_NAME,cursor.getString(jobColIndex));
                        boolean tmp = false;
                        if (cursor.getString(doneColIndex)=="true")
                        {
                            tmp = true;
                        }
                        m.put(DBHelper.DONE_OR_NOT,tmp);
                        dataTmp.add(m);
                    } while (cursor.moveToNext());
                }

                SimpleAdapter simpleAdapter = new SimpleAdapter(this,dataTmp,R.layout.view_for_list,from,to);
                lvListofJobs.setAdapter(simpleAdapter);
                break;
            }
            case R.id.buttonDone:
            {
                ArrayList<HashMap<String,Object>> dataTmp = new ArrayList<HashMap<String, Object>>();
                Cursor cursor = dbHelper.getDone();
                if (cursor.moveToFirst())
                {
                    int jobColIndex = cursor.getColumnIndex(DBHelper.JOBS_NAME);
                    int doneColIndex = cursor.getColumnIndex(DBHelper.DONE_OR_NOT);
                    do
                    {
                        HashMap<String,Object> m = new HashMap<String, Object>();
                        m.put(DBHelper.JOBS_NAME,cursor.getString(jobColIndex));
                        boolean tmp = false;
                        if (cursor.getString(doneColIndex)=="true")
                        {
                            tmp = true;
                        }
                        m.put(DBHelper.DONE_OR_NOT,tmp);
                        dataTmp.add(m);
                    } while (cursor.moveToNext());
                }

                SimpleAdapter simpleAdapter = new SimpleAdapter(this,dataTmp,R.layout.view_for_list,from,to);
                lvListofJobs.setAdapter(simpleAdapter);
                break;
            }
            case R.id.buttonNotDone:
            {
                ArrayList<HashMap<String,Object>> dataTmp = new ArrayList<HashMap<String, Object>>();
                Cursor cursor = dbHelper.getNotDone();
                if (cursor.moveToFirst())
                {
                    int jobColIndex = cursor.getColumnIndex(DBHelper.JOBS_NAME);
                    int doneColIndex = cursor.getColumnIndex(DBHelper.DONE_OR_NOT);
                    do
                    {
                        HashMap<String,Object> m = new HashMap<String, Object>();
                        m.put(DBHelper.JOBS_NAME,cursor.getString(jobColIndex));
                        boolean tmp = false;
                        if (cursor.getString(doneColIndex)=="true")
                        {
                            tmp = true;
                        }
                        m.put(DBHelper.DONE_OR_NOT,tmp);
                        dataTmp.add(m);
                    } while (cursor.moveToNext());
                }

                SimpleAdapter simpleAdapter = new SimpleAdapter(this,dataTmp,R.layout.view_for_list,from,to);
                lvListofJobs.setAdapter(simpleAdapter);
                break;
            }
        }
    }
}
