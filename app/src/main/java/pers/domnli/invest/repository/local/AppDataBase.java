package pers.domnli.invest.repository.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import pers.domnli.invest.repository.entity.Bank;
import pers.domnli.invest.repository.entity.LoanMonthly;

@Database(entities = {Bank.class, LoanMonthly.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract InvestDao dao();
}
