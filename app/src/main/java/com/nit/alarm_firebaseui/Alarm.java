package com.nit.alarm_firebaseui;

public class Alarm {

    public String mlabel;
    public String mtime;


    public Alarm(String label,String time)
    {
        this.mlabel = label;
        this.mtime = time;
    }

    public String getMlabel()
    {
        return mlabel;
    }

    public String getMtime()
    {
        return mtime;
    }
    public void setMlabel(String label)
    {
        this.mlabel = label;
    }

    public void setMtime(String time)
    {
        this.mtime = time;
    }

}
