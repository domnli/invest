package pers.domnli.invest.fragment;

import android.os.AsyncTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pers.domnli.invest.InvestApplication;
import pers.domnli.invest.repository.entity.Bank;
import pers.domnli.invest.repository.entity.LoanMonthly;
import pers.domnli.invest.repository.local.InvestDao;

public class CalendarViewModel extends ViewModel {
    MutableLiveData<List<Bank>> banksLiveData;
    MutableLiveData<String> quotaTotal;
    MutableLiveData<String> todayUsed;
    InvestDao dao;

    public CalendarViewModel(){
        banksLiveData = new MutableLiveData<>();
        quotaTotal = new MutableLiveData<>();
        todayUsed = new MutableLiveData<>();
        dao = InvestApplication.getAppDataBase().dao();
    }

    void getAllBank(){
        AsyncTask.execute(()->{
            List<Bank> banks = dao.getAllBank();
            banksLiveData.postValue(banks);
        });
    }

    void getTotalQuota(){
        AsyncTask.execute(()->{
            Integer totalQuota = dao.getTotalQuota();
            quotaTotal.postValue(totalQuota.toString());
        });
    }

    void getTodayUsed(){
        Calendar calendar = Calendar.getInstance();

        AsyncTask.execute(()->{
            Float todayUsedMoney = dao.getUsedIn(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE));
            todayUsed.postValue(todayUsedMoney.toString());
        });
    }
}
