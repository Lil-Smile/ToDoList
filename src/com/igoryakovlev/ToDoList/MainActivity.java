package com.igoryakovlev.ToDoList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import org.w3c.dom.Text;

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
    private static int trigger = 1; //1 - all, 2 - done, 3 - not yet
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
                if (cursor.getString(doneColIndex).equals("true"))
                {
                    tmp = true;
                }
                m.put(DBHelper.DONE_OR_NOT,tmp);
                data.add(m);
            } while (cursor.moveToNext());
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,data,R.layout.view_for_list,from,to);
        lvListofJobs.setAdapter(simpleAdapter);
        lvListofJobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cbJob);
                TextView textView = (TextView) view.findViewById(R.id.tvJob);
                checkBox.setChecked(!checkBox.isChecked());
                boolean done = checkBox.isChecked();
                String todo = textView.getText().toString();
                dbHelper.updateDb(todo, done);

            }
        });
        registerForContextMenu(lvListofJobs);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Удалить запись");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            String todo = (String)data.get(acmi.position).get(DBHelper.JOBS_NAME);
            dbHelper.deleteFromDb(todo);
            data.remove(acmi.position);
            SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(),data,R.layout.view_for_list,from,to);
            lvListofJobs.setAdapter(simpleAdapter);
            return true;
        }
        return super.onContextItemSelected(item);
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
                        Cursor cursor = null;
                        if (trigger == 1)
                        {
                            cursor = dbHelper.getAll();
                        } else if (trigger == 2)
                        {
                            cursor = dbHelper.getDone();
                        }else if (trigger == 3)
                        {
                            cursor = dbHelper.getNotDone();
                        } else
                        {
                            cursor = dbHelper.getAll();
                        }

                        if (cursor.moveToFirst())
                        {
                            int jobColIndex = cursor.getColumnIndex(DBHelper.JOBS_NAME);
                            int doneColIndex = cursor.getColumnIndex(DBHelper.DONE_OR_NOT);
                            do
                            {
                                HashMap<String,Object> m = new HashMap<String, Object>();
                                m.put(DBHelper.JOBS_NAME,cursor.getString(jobColIndex));
                                boolean tmp = false;
                                if (cursor.getString(doneColIndex).equals("true"))
                                {
                                    tmp = true;
                                }
                                m.put(DBHelper.DONE_OR_NOT,tmp);
                                tmpData.add(m);
                            } while (cursor.moveToNext());
                        }
                        data=tmpData;
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
                trigger = 1;
                buttonNotDone.setBackground(getResources().getDrawable(android.R.drawable.button_onoff_indicator_off));
                buttonDone.setBackground(getResources().getDrawable(android.R.drawable.button_onoff_indicator_off));
                buttonAll.setBackground(getResources().getDrawable(android.R.drawable.button_onoff_indicator_on));
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
                        if (cursor.getString(doneColIndex).equals("true"))
                        {
                            tmp = true;
                        }
                        m.put(DBHelper.DONE_OR_NOT,tmp);
                        dataTmp.add(m);
                    } while (cursor.moveToNext());
                }
                this.data=dataTmp;
                SimpleAdapter simpleAdapter = new SimpleAdapter(this,dataTmp,R.layout.view_for_list,from,to);
                lvListofJobs.setAdapter(simpleAdapter);
                break;
            }
            case R.id.buttonDone:
            {
                trigger = 2;
                buttonNotDone.setBackground(getResources().getDrawable(android.R.drawable.button_onoff_indicator_off));
                buttonDone.setBackground(getResources().getDrawable(android.R.drawable.button_onoff_indicator_on));
                buttonAll.setBackground(getResources().getDrawable(android.R.drawable.button_onoff_indicator_off));
                ArrayList<HashMap<String,Object>> dataTmp = new ArrayList<HashMap<String, Object>>();
                Cursor cursor = dbHelper.getDone();
                if (cursor.moveToFirst())
                {
                    int jobColIndex = cursor.getColumnIndex(DBHelper.JOBS_NAME);
                    do
                    {
                        HashMap<String,Object> m = new HashMap<String, Object>();
                        m.put(DBHelper.JOBS_NAME,cursor.getString(jobColIndex));
                        boolean tmp = true;
                        m.put(DBHelper.DONE_OR_NOT,tmp);
                        dataTmp.add(m);
                    } while (cursor.moveToNext());
                }
                this.data=dataTmp;
                SimpleAdapter simpleAdapter = new SimpleAdapter(this,dataTmp,R.layout.view_for_list, from, to);
                lvListofJobs.setAdapter(simpleAdapter);
                break;
            }
            case R.id.buttonNotDone:
            {
                trigger = 3;
                buttonNotDone.setBackground(getResources().getDrawable(android.R.drawable.button_onoff_indicator_on));
                buttonDone.setBackground(getResources().getDrawable(android.R.drawable.button_onoff_indicator_off));
                buttonAll.setBackground(getResources().getDrawable(android.R.drawable.button_onoff_indicator_off));
                ArrayList<HashMap<String,Object>> dataTmp = new ArrayList<HashMap<String, Object>>();
                Cursor cursor = dbHelper.getNotDone();
                if (cursor.moveToFirst())
                {
                    int jobColIndex = cursor.getColumnIndex(DBHelper.JOBS_NAME);
                    do
                    {
                        HashMap<String,Object> m = new HashMap<String, Object>();
                        m.put(DBHelper.JOBS_NAME,cursor.getString(jobColIndex));
                        boolean tmp = false;
                        m.put(DBHelper.DONE_OR_NOT,tmp);
                        dataTmp.add(m);
                    } while (cursor.moveToNext());
                }
                this.data=dataTmp;
                SimpleAdapter simpleAdapter = new SimpleAdapter(this,dataTmp,R.layout.view_for_list,from,to);
                lvListofJobs.setAdapter(simpleAdapter);
                break;
            }
        }
    }
}
