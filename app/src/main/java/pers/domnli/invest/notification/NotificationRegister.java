package pers.domnli.invest.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationRegister {
    public static final String BILL_REMIND_CHANNEL_ID = "账单提醒";
    public static final String DUE_REMIND_CHANNEL_ID = "还款提醒";

    public static void register(Context context){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

            NotificationChannel billingChannel = new NotificationChannel(BILL_REMIND_CHANNEL_ID, BILL_REMIND_CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            billingChannel.setDescription("提醒您记录已过账单日的银行的账单金额");
            notificationManager.createNotificationChannel(billingChannel);

            NotificationChannel dueChannel = new NotificationChannel(DUE_REMIND_CHANNEL_ID, DUE_REMIND_CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            dueChannel.setDescription("提醒您未来三天需要还款的银行");
            notificationManager.createNotificationChannel(dueChannel);
        }
    }
}
