package pers.domnli.invest.fragment;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pers.domnli.invest.InvestApplication;
import pers.domnli.invest.notification.BillReminder;
import pers.domnli.invest.repository.local.InvestDao;

public class BillRemindViewModel extends ViewModel {
    MutableLiveData<BillReminder.Remind> remindLiveData;
    InvestDao dao;

    public BillRemindViewModel(){
        remindLiveData = new MutableLiveData<>();
        dao = InvestApplication.getAppDataBase().dao();
    }

    void getRemind(){
        AsyncTask.execute(()->{
            BillReminder.Remind remind = new BillReminder().getRemind();
            remindLiveData.postValue(remind);
        });

    }

}
