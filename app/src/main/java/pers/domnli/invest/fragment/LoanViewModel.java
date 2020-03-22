package pers.domnli.invest.fragment;

import android.os.AsyncTask;

import java.math.BigDecimal;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pers.domnli.invest.InvestApplication;
import pers.domnli.invest.repository.entity.Bank;
import pers.domnli.invest.repository.entity.LoanMonthly;
import pers.domnli.invest.repository.local.InvestDao;

public class LoanViewModel extends ViewModel {
    MutableLiveData<Bank> bankLiveData;
    MutableLiveData<LoanMonthly> loanMonthlyLiveData;
    InvestDao dao;

    public LoanViewModel(){
        bankLiveData = new MutableLiveData<>();
        loanMonthlyLiveData = new MutableLiveData<>();
        dao = InvestApplication.getAppDataBase().dao();
    }

    void getBankLoanMonthly(String bank,Integer year,Integer month){
        AsyncTask.execute(()->{
            Bank bankEntity = dao.getBank(bank);
            LoanMonthly loanMonthly = dao.getLoanMonthly(bank,year,month);

            bankLiveData.postValue(bankEntity);
            loanMonthlyLiveData.postValue(loanMonthly);
        });
    }

    void insertLoanMonthly(String bank, Integer year, Integer month, String loan){
        AsyncTask.execute(()->{
            LoanMonthly item = new LoanMonthly();
            item.setBank(bank);
            item.setYear(year);
            item.setMonth(month);
            item.setLoan(loan);
            item.setRepaid(false);
            dao.insert(item);
        });
    }

    void updateLoanMonthly(LoanMonthly item){
        AsyncTask.execute(()->dao.insert(item));
    }

}
