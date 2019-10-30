package com.assignment.saints.assignment2;


import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import java.util.ArrayList;


public class Task implements Parcelable //Custom class holding all the task info while
{                                       //implementing Parcelable to be passed off in intents.
    private int day;
    private int month;
    private int year;
    private int minute;
    private int hours;
    private String comment;

    public Task(int day,int month,int year,int minute,int hours,String comment)
    {
        this.day = day;
        this.month = month;
        this.year = year;
        this.minute = minute;
        this.hours = hours;
        this.comment = comment;
    }

    public Task(Parcel source)  //reads from the parcel
    {
        day = source.readInt();
        month = source.readInt();
        year = source.readInt();
        hours = source.readInt();
        minute = source.readInt();
        comment = source.readString();
    }

    public int getDay()
    {
        return day;
    }

    public int getMonth()
    {
        return month;
    }

    public int getYear()
    {
        return year;
    }

    public int getMinute()
    {
        return minute;
    }

    public int getHours()
    {
        return hours;
    }

    public String getComment()
    {
        return comment;
    }

    public void setDay(int day)
    {
        this.day = day;
    }

    public void setMonth(int month)
    {
        this.month = month;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public void setMinute(int minute)
    {
        this.minute = minute;
    }

    public void setHours(int hours)
    {
        this.hours = hours;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public static void sort(ArrayList<Task> list) //sorting using bubble sort using total number of minutes as a compare value.
    {
        Task temp;
        if(list.size() > 1 )
        {
            for(int x = 0; x < list.size(); x++)
            {
                for(int i = 0; i < list.size()-1;i++)
                {
                    if(list.get(i).compareTo(list.get(i+1))>0)
                    {
                        temp = list.get(i);
                        list.set(i,list.get(i+1));
                        list.set(i+1,temp);
                    }
                }
            }

        }
    }


    public int compareTo(Task taskTwo) //the compare function using total minutes with longs to store the big values.
    {
        int result = 0;

        long one = year*525600 + month*43800 + day*1440 + hours*60 + minute;
        long two = taskTwo.getYear()*525600 + taskTwo.getMonth()*43800 + taskTwo.getDay()*1440 + taskTwo.getHours()*60 + taskTwo.getMinute();
        if(one > two)
        {
            result = 1;
        }
        if(one < two)
        {
            result = -1;
        }


        return result;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel pass, int flags)  //flattens all the data into a parcel
    {
        pass.writeInt(day);
        pass.writeInt(month);
        pass.writeInt(year);
        pass.writeInt(hours);
        pass.writeInt(minute);
        pass.writeString(comment);
    }

    public static final Creator<Task> CREATOR = new Creator<Task>()  //creates the parcelable
    {
        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }

        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }
    };
}
