package com.assignment.saints.assignment2;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;


import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {


    int day,month,year,hours,minute;
    String comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        TimePicker time = findViewById(R.id.timePicker);
        time.setIs24HourView(true);
        Intent intent = getIntent();
        final ArrayList<Task> taskList = intent.getParcelableArrayListExtra("list"); //variables are final so that they can be used in the internal onClick methods.
        final int pos = intent.getIntExtra("pos",-1);
        Button add = (Button) findViewById(R.id.btn_add);
        Button cancel = (Button) findViewById(R.id.btn_cancel);
        final DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
        final TimePicker tp = (TimePicker) findViewById(R.id.timePicker);
        final EditText et = (EditText) findViewById(R.id.input_comments);

        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                comment = et.getText().toString();
                day = dp.getDayOfMonth();
                month = dp.getMonth();
                year = dp.getYear();

                if (Build.VERSION.SDK_INT >= 23 ) //Catering to different android apis
                    hours = tp.getHour();
                else
                    hours = tp.getCurrentHour();

                if (Build.VERSION.SDK_INT >= 23)
                    minute = tp.getMinute();
                else
                    minute = tp.getCurrentMinute();

                Task task = new Task(day,month,year,minute,hours,comment);
                taskList.add(task);
                Intent reIntent = new Intent();
                reIntent.putParcelableArrayListExtra("list",taskList); //parcelable allows sending arraylist in intents.
                setResult(Activity.RESULT_OK,reIntent);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent reIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,reIntent);
                finish();
            }
        });

        if(pos != -1) //-1 is the default value for Adding, if a position is detected, it will switch into update mode.
        {
            add.setText("Update");
            Task task = taskList.get(pos);
            et.setText(task.getComment());
            dp.updateDate(task.getYear(),task.getMonth(),task.getDay());
            if (Build.VERSION.SDK_INT >= 23 )
                tp.setHour(task.getHours());
            else
                tp.setCurrentHour(task.getHours());

            if (Build.VERSION.SDK_INT >= 23)
                tp.setMinute(task.getMinute());
            else
                tp.setCurrentMinute(task.getMinute());
            add.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    taskList.get(pos).setComment(et.getText().toString());
                    taskList.get(pos).setDay(dp.getDayOfMonth());
                    taskList.get(pos).setMonth(dp.getMonth());
                    taskList.get(pos).setYear(dp.getYear());

                    if (Build.VERSION.SDK_INT >= 23 )
                        taskList.get(pos).setHours(tp.getHour());
                    else
                        taskList.get(pos).setHours(tp.getCurrentHour());

                    if (Build.VERSION.SDK_INT >= 23)
                        taskList.get(pos).setMinute(tp.getMinute());
                    else
                        taskList.get(pos).setMinute(tp.getCurrentMinute());
                    Intent reIntent = new Intent();
                    reIntent.putParcelableArrayListExtra("list",taskList);
                    setResult(Activity.RESULT_OK,reIntent);
                    finish();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent reIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED,reIntent);
                    finish();
                }
            });
        }
    }
}
