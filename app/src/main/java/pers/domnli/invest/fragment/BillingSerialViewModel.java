package pers.domnli.invest.fragment;

import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pers.domnli.invest.InvestApplication;
import pers.domnli.invest.notification.BillReminder;
import pers.domnli.invest.repository.entity.BillingSerial;
import pers.domnli.invest.repository.local.InvestDao;

public class BillingSerialViewModel extends ViewModel {
    MutableLiveData<List<BillingSerial>> serialLiveData;
    InvestDao dao;

    public BillingSerialViewModel(){
        serialLiveData = new MutableLiveData<>();
        dao = InvestApplication.getAppDataBase().dao();
    }

    void loadMore(String bank,Integer limit,Integer offset){
        AsyncTask.execute(()->{
            List<BillingSerial> serials = dao.findBillingSerial(bank,limit,offset);
            serialLiveData.postValue(serials);
        });
    }

}
