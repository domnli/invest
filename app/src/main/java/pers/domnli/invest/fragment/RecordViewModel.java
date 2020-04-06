package pers.domnli.invest.fragment;

import android.os.AsyncTask;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pers.domnli.invest.InvestApplication;
import pers.domnli.invest.repository.entity.Bank;
import pers.domnli.invest.repository.entity.BillingSerial;
import pers.domnli.invest.repository.entity.LoanMonthly;
import pers.domnli.invest.repository.local.InvestDao;

public class RecordViewModel extends ViewModel {
    MutableLiveData<List<Bank>> banksLiveData;
    MutableLiveData<Proportion> proportionLiveData;
    InvestDao dao;

    public RecordViewModel(){
        banksLiveData = new MutableLiveData<>();
        proportionLiveData = new MutableLiveData<>();
        dao = InvestApplication.getAppDataBase().dao();
    }

    void getAllBank(){
        AsyncTask.execute(()->{
            List<Bank> banks = dao.getAllBank();

            banksLiveData.postValue(banks);
        });
    }

    void getBillProportion(String bankName){
        AsyncTask.execute(()->{
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            Bank bank = dao.getBank(bankName);

            // 上期未还 + 当期账单总额
            List<LoanMonthly> loans = dao.findNonPaidLoanBefore(bankName,month);
            BigDecimal noPaidLoan = new BigDecimal(0);
            for (LoanMonthly loan :
                    loans) {
                noPaidLoan = noPaidLoan.add(new BigDecimal(loan.getLoan()));
            }

            List<BillingSerial> serials = new ArrayList<>();
            if (day > bank.getBillingDay()){
                // 当月billingDay 至 下月billingDay
                calendar.add(Calendar.MONTH,1);
                int nextYear = calendar.get(Calendar.YEAR);
                int nextMonth = calendar.get(Calendar.MONTH) + 1;
                List<BillingSerial> serialDayAfter = dao.findSerialDayAfter(bankName,year, month, bank.getBillingDay());
                List<BillingSerial> serialDayBefore = dao.findSerialDayBefore(bankName,nextYear, nextMonth, bank.getBillingDay());
                serials.addAll(serialDayAfter);
                serials.addAll(serialDayBefore);

            }else{
                // 上月billingDay 至 当月billingDay
                calendar.add(Calendar.MONTH,-1);
                int prevYear = calendar.get(Calendar.YEAR);
                int prevMonth = calendar.get(Calendar.MONTH) + 1;
                List<BillingSerial> serialDayAfter = dao.findSerialDayAfter(bankName,prevYear, prevMonth, bank.getBillingDay());
                List<BillingSerial> serialDayBefore = dao.findSerialDayBefore(bankName,year,month,bank.getBillingDay());
                serials.addAll(serialDayAfter);
                serials.addAll(serialDayBefore);
            }


            for (BillingSerial serial:
                 serials) {
                noPaidLoan = noPaidLoan.add(new BigDecimal(serial.getMoney()));
            }

            BigDecimal last = noPaidLoan.divide(new BigDecimal(bank.getQuota()),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));

            Proportion proportion = new Proportion();
            proportion.setQuota(new BigDecimal(bank.getQuota()));
            proportion.setNoPaidLoan(noPaidLoan);
            proportion.setUseProportion(last.toBigInteger().intValue());
            proportionLiveData.postValue(proportion);
        });
    }

    public void addBillingSerial(BillingSerial serial) {
        AsyncTask.execute(()->{
            dao.insert(serial);
        });
    }

    class Proportion{
        private BigDecimal quota;
        private BigDecimal noPaidLoan;
        private Integer useProportion;

        public BigDecimal getQuota() {
            return quota;
        }

        public void setQuota(BigDecimal quota) {
            this.quota = quota;
        }

        public BigDecimal getNoPaidLoan() {
            return noPaidLoan;
        }

        public void setNoPaidLoan(BigDecimal noPaidLoan) {
            this.noPaidLoan = noPaidLoan;
        }

        public Integer getUseProportion() {
            return useProportion;
        }

        public void setUseProportion(Integer useProportion) {
            this.useProportion = useProportion;
        }
    }

}
