package pers.domnli.invest.repository.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Bank {

    @PrimaryKey
    @ColumnInfo
    @NonNull
    private String bank;

    @ColumnInfo
    @NonNull
    private Integer billingDay;

    @ColumnInfo
    @NonNull
    private Integer dueDay;

    @ColumnInfo
    @NonNull
    private Integer quota;

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public Integer getBillingDay() {
        return billingDay;
    }

    public void setBillingDay(Integer billingDay) {
        this.billingDay = billingDay;
    }

    public Integer getDueDay() {
        return dueDay;
    }

    public void setDueDay(Integer dueDay) {
        this.dueDay = dueDay;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }
}
