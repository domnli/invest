package pers.domnli.invest.repository.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"bank","year","month"})
public class LoanMonthly {
    @ColumnInfo
    @NonNull
    private String bank;

    @ColumnInfo
    @NonNull
    private Integer year;

    @ColumnInfo
    @NonNull
    private Integer month;

    @ColumnInfo
    private String tail;

    @ColumnInfo
    @NonNull
    private String loan;

    @ColumnInfo
    @NonNull
    private Boolean repaid;

    @NonNull
    public String getBank() {
        return bank;
    }

    public void setBank(@NonNull String bank) {
        this.bank = bank;
    }

    @NonNull
    public Integer getYear() {
        return year;
    }

    public void setYear(@NonNull Integer year) {
        this.year = year;
    }

    @NonNull
    public Integer getMonth() {
        return month;
    }

    public void setMonth(@NonNull Integer month) {
        this.month = month;
    }

    public String getTail() {
        return tail;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }

    @NonNull
    public String getLoan() {
        return loan;
    }

    public void setLoan(@NonNull String loan) {
        this.loan = loan;
    }

    @NonNull
    public Boolean getRepaid() {
        return repaid;
    }

    public void setRepaid(@NonNull Boolean repaid) {
        this.repaid = repaid;
    }
}
