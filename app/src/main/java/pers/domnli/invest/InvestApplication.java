package pers.domnli.invest;

import android.app.AlarmManager;
import android.app.Application;

import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;

import java.util.Calendar;

import androidx.room.Room;
import pers.domnli.invest.notification.AlarmRegister;
import pers.domnli.invest.notification.NotificationRegister;
import pers.domnli.invest.repository.local.AppDataBase;

public class InvestApplication extends Application {
    private static AppDataBase mAppDataBase;
    private static Application sApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        QMUISwipeBackActivityManager.init(this);

        mAppDataBase = Room.databaseBuilder(getApplicationContext(),AppDataBase.class,"app_database.db")
                .fallbackToDestructiveMigration()
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
