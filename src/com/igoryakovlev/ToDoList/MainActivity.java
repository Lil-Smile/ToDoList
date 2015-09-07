package com.igoryakovlev.ToDoList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity implements View.OnClickListener{
    /**
     * Called when the activity is first created.
     */

    Button buttonAdd;
    Button buttonAll;
    Button buttonDone;
    Button buttonNotDone;

    ListView lvListofJobs;

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

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.buttonAdd:
            {
                break;
            }
            case R.id.buttonAll:
            {
                break;
            }
            case R.id.buttonDone:
            {
                break;
            }
            case R.id.buttonNotDone:
            {
                break;
            }
        }
    }
}
