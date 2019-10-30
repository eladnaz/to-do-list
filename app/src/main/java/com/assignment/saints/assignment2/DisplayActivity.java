package com.assignment.saints.assignment2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.AlertDialog.Builder;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class DisplayActivity extends AppCompatActivity {
    Random rand = new Random();
    Spannable bullet = new SpannableString("\u2022   ");
    ArrayList<Task> taskList = new ArrayList<Task>();
    String[] months = new String[]{"January","February","March","April","May","June","July","August","September","October","November","December"};
    SharedPreferences storage;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        storage = PreferenceManager.getDefaultSharedPreferences(this); //shared preferences used to store the data offline when app is removed from stack.
        editor = storage.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Type type = new TypeToken<ArrayList<Task>>(){}.getType(); //Gson dependency is used due to preference manager not being able to store array lists by default.
        String json = storage.getString("list", null);
        if(json != null)
        {
            taskList = gson.fromJson(json,type);
            taskDisplay();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayActivity.this,AddActivity.class);
                intent.putParcelableArrayListExtra("list",taskList);
                startActivityForResult(intent,1);
            }
        });
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.clear)
        {
            AlertDialog dialog = new AlertDialog.Builder(DisplayActivity.this).create(); //this dialog lets user confirm they wish to wipe all the current tasks
            dialog.setTitle("Clear All");
            dialog.setMessage("Are you sure you want to clear all the tasks?");
            dialog.setButton(AlertDialog.BUTTON_POSITIVE,"Yes", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog,int id)
                {
                    taskList = new ArrayList<Task>();
                    taskDisplay();
                }
            });
            dialog.setButton(AlertDialog.BUTTON_NEGATIVE,"No",new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog,int id)
                {
                    return;
                }
            });
            dialog.show();
            return true;
        }
        else if(item.getItemId() == R.id.about)
        {
            Intent intent = new Intent(DisplayActivity.this,AboutActivity.class); //menu item to start About
            startActivity(intent);
            return true;

        }
        else if(item.getItemId() == R.id.howto)
        {
            Intent intent = new Intent(DisplayActivity.this,HowToActivity.class); //menu item to start How To Use guide
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) //gets back the list of tasks after add.
    {
        if(requestCode == 1)
        {
            if(resultCode == Activity.RESULT_OK)
                taskList = data.getParcelableArrayListExtra("list"); //the array list is received here every time Add or Update activity ends
            Task.sort(taskList);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume(); //function to display tasks is called here for consistency and smoother lifecycle flow
        taskDisplay();
    }

    public void taskDisplay()
    {
        LinearLayout linear = findViewById(R.id.linear);
        linear.removeAllViews(); //clear all the previous tasks b4 putting in the latest tasks
        ArrayList<Long> dateTestList = new ArrayList<Long>(); //stores the  same dates in total days for easier testing
        ArrayList<String> dateList = new ArrayList<String>(); //actual array of the dates.
        for(int i = 0; i < taskList.size(); i++)
        {
            Task task = taskList.get(i);
            long dateTotal = task.getDay() + task.getMonth()*32 + task.getYear()*365;
            String date = task.getDay() + "  " +  months[task.getMonth()] + "  " + task.getYear();

            if(i == 0)
            {
                dateTestList.add(dateTotal);
                dateList.add(date);
            }
            else
            {
                for(int x = 0; x < dateTestList.size(); x++) //the for loop takes in the unique individual dates and puts them in the test list.
                {
                    if(dateTotal == dateTestList.get(x))
                    {
                        break;
                    }
                    else if(x == dateTestList.size() - 1)
                    {
                        dateTestList.add(dateTotal);
                        dateList.add(date);
                    }
                }
            }
        }
        for(int i = 0; i < dateList.size(); i++) //prints out unique date titles and checks for Tasks that have the same date and puts them under their unique date titles
        {
            TextView dateTV = new TextView(this);
            int rndColor = Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            bullet.setSpan(new ForegroundColorSpan(rndColor), 0, bullet.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            dateTV.setText(bullet);
            Spannable date = new SpannableString(dateList.get(i));
            date.setSpan(new ForegroundColorSpan(Color.BLACK), 0, date.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            dateTV.append(date);
            dateTV.setBackgroundColor(getResources().getColor(R.color.bar));
            dateTV.setPadding(20, 0, 0, 0);
            dateTV.setTextSize(20);
            linear.addView(dateTV);

            for(int x = 0; x < taskList.size(); x++)
            {
                Task task = taskList.get(x);
                long dateTest = task.getDay() + task.getMonth()*32 + task.getYear()*365;
                if(dateTest == dateTestList.get(i))
                {
                    TextView content = new TextView(this);
                    String hourFormat = String.format("%02d",taskList.get(x).getHours());
                    String minuteFormat = String.format("%02d",taskList.get(x).getMinute());
                    final int finalX = x;
                    final TextView contentFinal = content;
                    String cntString = taskList.get(x).getComment() + " " + hourFormat + ":" + minuteFormat;
                    content.setText(cntString);
                    content.setPadding(30, 20, 0, 20);
                    content.setOnLongClickListener(new View.OnLongClickListener()
                    {
                        @Override
                        public boolean onLongClick(View v)
                        { //this long click will  trigger a dialog allowing users to delete or edit their task.
                            contentFinal.setBackgroundResource(R.drawable.text_pressed);
                            AlertDialog dialog = new AlertDialog.Builder(DisplayActivity.this).create();
                            dialog.setTitle("Choice");
                            dialog.setMessage("Would you like to edit or delete"); //edit code
                            dialog.setButton(AlertDialog.BUTTON_POSITIVE,"Edit", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog,int id)
                                {
                                    Intent editIntent = new Intent(DisplayActivity.this,AddActivity.class);
                                    editIntent.putParcelableArrayListExtra("list",taskList);
                                    editIntent.putExtra("pos",finalX);

                                    startActivityForResult(editIntent,1);
                                }
                            });
                            dialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Delete",new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog,int id)
                                { //delete code
                                    Iterator<Task> iter = taskList.iterator();
                                    while(iter.hasNext())
                                    {
                                        if(iter.next() == taskList.get(finalX))
                                        {
                                            iter.remove();
                                            taskDisplay();
                                            break;
                                        }
                                    }
                                }
                            });
                            dialog.show();
                            return false;
                        }
                    });
                    linear.addView(content);
                }
            }
        }
    }
    @Override
    public void onPause()
    {
        super.onPause(); //data is always saved every time the display activity which is also the main activity gets put on paused for consistent data storage.
        String json = gson.toJson(taskList);
        editor.putString("list",json);
        editor.commit();
    }
}
