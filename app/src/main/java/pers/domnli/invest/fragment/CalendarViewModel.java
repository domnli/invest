package pers.domnli.invest.fragment;

import android.os.AsyncTask;

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
    InvestDao dao;

    public CalendarViewModel(){
        banksLiveData = new MutableLiveData<>();
        dao = InvestApplication.getAppDataBase().dao();
    }

    void getAllBank(){
        AsyncTask.execute(()->{
            List<Bank> banks = dao.getAllBank();
            banksLiveData.postValue(banks);
        });
    }

}
