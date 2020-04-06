package pers.domnli.invest;

import android.app.AlarmManager;
import android.app.Application;

import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;

import java.util.Calendar;

import androidx.room.Room;
import pers.domnli.invest.notification.AlarmRegister;
import pers.domnli.invest.notification.NotificationRegister;
import pers.domnli.invest.repository.local.AppDataBase;
import pers.domnli.invest.repository.local.migration.Migration_1_2;
import pers.domnli.invest.repository.local.migration.Migration_2_3;

public class InvestApplication extends Application {
    private static AppDataBase mAppDataBase;
    private static Application sApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        QMUISwipeBackActivityManager.init(this);

        mAppDataBase = Room.databaseBuilder(getApplicationContext(),AppDataBase.class,"app_database.db")
                .addMigrations(new Migration_1_2())
                .addMigrations(new Migration_2_3())
                .build();

        AlarmRegister.register(this);
        NotificationRegister.register(this);
    }

    public static AppDataBase getAppDataBase(){
        return mAppDataBase;
    }

    public static Application getApplication(){
        return sApplication;
    }
}
