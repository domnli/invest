package pers.domnli.invest.repository.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BillingSerial {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    private Integer id;

    @ColumnInfo
    private String bank;

    @ColumnInfo
    private Integer year;

    @ColumnInfo
    private Integer month;

    @ColumnInfo
    private Integer day;

    @ColumnInfo
    private Float money;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }
}
