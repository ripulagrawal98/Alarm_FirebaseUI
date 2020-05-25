package com.nit.alarm_firebaseui;

public class Alarm implements Comparable<Alarm> {

    public String mlabel;
    public String mtime;
    public String media_uri;
    public String mDate;
    public String mday_month, mmonth,myear;
    public String mtotal_minutes;


    public Alarm(String label,String time,String media,String date,String day_month,String month,String year,String total_minutes)
    {
        this.mlabel = label;
        this.mtime = time;
        this.media_uri = media;
        this.mDate = date;
        this.mday_month = day_month;
        this.mmonth = month;
        this.myear = year;
        this.mtotal_minutes = total_minutes;

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

    public String getDate(){ return mDate;}
    public void setDate(String date){ this.mDate = date; }

    public String getMday_month(){ return mday_month; }
    public void setMday_month(String day_month){ this.mday_month = day_month; }

    public String getMmonth(){ return mmonth;}

    public void setMmonth(String mmonth) {
        this.mmonth = mmonth;
    }

    public String getMyear(){ return myear;}

    public void setMyear(String myear) {
        this.myear = myear;
    }

    public String getMtotal_minutes(){ return mtotal_minutes;}

    public void setMtotal_minutes(String mtotal_minutes) {
        this.mtotal_minutes = mtotal_minutes;
    }

    @Override
    public int compareTo(Alarm o) {

        int compare = mtotal_minutes.compareTo(o.mtotal_minutes);
        if(compare == 0)
        {
//            compare = Integer.compare()
        }
//        int compare = mmonth.compareTo(o.mmonth);

        return 0;
    }
}
