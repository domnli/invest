package pers.domnli.invest.common;

import java.util.List;

import pers.domnli.invest.repository.entity.Bank;
import pers.domnli.invest.repository.entity.BillingSerial;
import pers.domnli.invest.repository.entity.LoanMonthly;

public class DataBackUp {
    private List<Bank> banks;
    private List<LoanMonthly> loanMonthlies;
    private List<BillingSerial> billingSerials;

    public List<Bank> getBanks() {
        return banks;
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }

    public List<LoanMonthly> getLoanMonthlies() {
        return loanMonthlies;
    }

    public void setLoanMonthlies(List<LoanMonthly> loanMonthlies) {
        this.loanMonthlies = loanMonthlies;
    }

    public List<BillingSerial> getBillingSerials() {
        return billingSerials;
    }

    public void setBillingSerials(List<BillingSerial> billingSerials) {
        this.billingSerials = billingSerials;
    }
}
