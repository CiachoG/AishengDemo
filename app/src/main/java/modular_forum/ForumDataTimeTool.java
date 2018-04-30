package modular_forum;

import android.provider.CalendarContract;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ForumDataTimeTool {
    public static String timeChangeOver(Date targetDate){
        Date nowDate=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        switch (diffDayNumber(nowDate,targetDate)){
            case 0:     //在同一天
                return "今天"+sdf.format(targetDate);
            case 1:     //昨天发布的
                return "昨天"+sdf.format(targetDate);
            case 2:     //前天发布的
                return "前天"+sdf.format(targetDate);
            case -1:    //以前发布的
                sdf=new SimpleDateFormat("MM-dd HH:mm");
                return sdf.format(targetDate);
        }
        return "***";
    }

    //计算两个日期所在该天相差的天数
     private static int diffDayNumber(Date nowDate,Date targetDate){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(nowDate);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(targetDate);
        for(int i=0;i<=2;i++){  //当天、明天、后天
            if(i!=0)
                calendar.add(Calendar.DATE,1);

            int year1=calendar.get(Calendar.YEAR);
            int month1=calendar.get(Calendar.MONTH);
            int day1=calendar.get(Calendar.DAY_OF_MONTH);

            if(year==year1&&month==month1&&day==day1)
                return i;
        }
        return -1;
    }
}
