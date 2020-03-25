package pers.domnli.invest.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

public class AlarmRegister {

    private static AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;

    public static final int REQUEST_CODE = 0;

    public static void register(Context context){
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context,BillNotifyService.class);
        alarmIntent = PendingIntent.getService(context,REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 9);


//        if(Build.VERSION.SDK_INT > )
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,alarmIntent);
//        }
    }
}
