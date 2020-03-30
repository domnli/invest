package pers.domnli.invest.notification;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.util.Calendar;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import pers.domnli.invest.MainActivity;
import pers.domnli.invest.R;
import pers.domnli.invest.fragment.BillRemindFragment;
import pers.domnli.invest.repository.entity.Bank;

public class BillNotifyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AsyncTask.execute(()->{
            BillReminder.Remind remind = new BillReminder().getRemind();
            StringBuilder billingBank = new StringBuilder();
            StringBuilder dueBank = new StringBuilder();
            for (Bank bank:remind.getBillingBanks()){
                billingBank.append(bank.getBank()).append("、");
            }
            for (Bank bank:remind.getDueBanks()){
                dueBank.append(bank.getBank()).append("、");
            }

            Calendar calendar = Calendar.getInstance();
            System.out.println(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND));

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            Intent mainAtyIntent = MainActivity.of(context, BillRemindFragment.class);
            mainAtyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainAtyIntent, 0);

            Calendar tmp = Calendar.getInstance();
            if(billingBank.length() > 0){
                billingBank.deleteCharAt(billingBank.length()-1);
                NotificationCompat.Builder billingBuilder = new NotificationCompat.Builder(context, NotificationRegister.BILL_REMIND_CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("有银行可记录当月账单金额")
                        .setContentText(billingBank.toString())
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                notificationManager.notify(tmp.get(Calendar.MILLISECOND+1), billingBuilder.build());
            }

            if(dueBank.length() > 0){
                dueBank.deleteCharAt(dueBank.length()-1);
                NotificationCompat.Builder dueBuilder = new NotificationCompat.Builder(context, NotificationRegister.BILL_REMIND_CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("有银行临近还款日")
                        .setContentText(dueBank.toString())
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                notificationManager.notify(tmp.get(Calendar.MILLISECOND+2), dueBuilder.build());
            }
        });

    }
}
