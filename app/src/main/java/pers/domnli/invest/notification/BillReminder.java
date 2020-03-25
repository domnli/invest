package pers.domnli.invest.notification;

import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pers.domnli.invest.InvestApplication;
import pers.domnli.invest.repository.entity.Bank;
import pers.domnli.invest.repository.local.InvestDao;

public class BillReminder {

    private InvestDao dao;

    public BillReminder(){
        dao = InvestApplication.getAppDataBase().dao();
    }

    public Remind getRemind(){
        Calendar prev1day = Calendar.getInstance();
        prev1day.add(Calendar.DAY_OF_MONTH,-1);
        Calendar next4day = Calendar.getInstance();
        next4day.add(Calendar.DAY_OF_MONTH,4);
        Calendar tmp = Calendar.getInstance();
        List<Bank> banks = dao.getAllBank();
        List<String> loanBanks = dao.findMonthLoanBankName(tmp.get(Calendar.YEAR),tmp.get(Calendar.MONTH)+1);

        List<Bank> billingBank = new ArrayList();
        List<Bank> dueBank = new ArrayList<>();
        for (Bank bank : banks) {
            tmp.set(Calendar.DAY_OF_MONTH,bank.getBillingDay());
            if(tmp.before(prev1day) && !loanBanks.contains(bank.getBank())){
                billingBank.add(bank);
            }
            tmp.set(Calendar.DAY_OF_MONTH,bank.getDueDay());
            if(tmp.after(prev1day) && tmp.before(next4day)){
                dueBank.add(bank);
            }
        }

        Remind remind = new Remind();
        remind.setBillingBanks(billingBank);
        remind.setDueBanks(dueBank);

        return remind;
    }

    public class Remind{
        private List<Bank> billingBanks;
        private List<Bank> dueBanks;

        public List<Bank> getBillingBanks() {
            return billingBanks;
        }

        public void setBillingBanks(List<Bank> billingBanks) {
            this.billingBanks = billingBanks;
            Collections.sort(this.billingBanks, (o1, o2) -> o1.getBillingDay().compareTo(o2.getBillingDay()));
        }

        public List<Bank> getDueBanks() {
            return dueBanks;
        }

        public void setDueBanks(List<Bank> dueBanks) {
            this.dueBanks = dueBanks;
            Collections.sort(this.dueBanks,(o1, o2) -> o1.getDueDay().compareTo(o2.getDueDay()));
        }
    }
}
