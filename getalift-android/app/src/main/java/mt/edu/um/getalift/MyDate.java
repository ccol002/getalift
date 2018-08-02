package mt.edu.um.getalift;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

class MyDate{
    GregorianCalendar c;

    public MyDate(String str){
        int year = Integer.parseInt(str.substring(0,4));
        int month = Integer.parseInt(str.substring(5,7));
        int day = Integer.parseInt(str.substring(8,10));
        int hour = Integer.parseInt(str.substring(11,13)) + 2;
        int minute = Integer.parseInt(str.substring(14,16));
        c = new GregorianCalendar();
        c.set(year,month,day,hour,minute);
    }

    public MyDate(GregorianCalendar tmp){
        c = tmp;

        SimpleDateFormat ft =
                new SimpleDateFormat ("yyyy.MM.dd 'at' hh:mm");
    }

    public Calendar getC() {
        return c;
    }

    public void setC(GregorianCalendar c) {
        this.c = c;
    }

    public String getTextArriveAt(int minWalking){
        MyDate tmp = new MyDate(c);

        SimpleDateFormat ft =
                new SimpleDateFormat ("hh:mm a");
        tmp.getC().add(c.MINUTE,minWalking);

        return ft.format(c.getTime());
    }
}
