package com.nit.alarm_firebaseui;

public class Alarm {

    public String mlabel;
    public String mtime;
    public String media_uri;


    public Alarm(String label,String time,String media)
    {
        this.mlabel = label;
        this.mtime = time;
        this.media_uri = media;
    }

    public String getMlabel()
    {
        return mlabel;
    }
    public void setMlabel(String label)
    {
        this.mlabel = label;
    }


    public String getMtime()
    {
        return mtime;
    }
    public void setMtime(String time)
    {
        this.mtime = time;
    }

    public String getMedia_uri(){ return media_uri; }
    public void setMedia_uri(String media){ this.media_uri = media; }



}
