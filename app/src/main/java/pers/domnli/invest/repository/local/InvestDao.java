package pers.domnli.invest.repository.local;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import pers.domnli.invest.repository.entity.Bank;
import pers.domnli.invest.repository.entity.BillingSerial;
import pers.domnli.invest.repository.entity.LoanMonthly;

@Dao
public interface InvestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Bank bank);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LoanMonthly loanMonthly);

    @Insert
    void insert(BillingSerial serial);


    @Query("select * from bank where bank = :bank")
    Bank getBank(String bank);

    @Query("select * from loanMonthly where bank=:bank and year=:year and month=:month")
    LoanMonthly getLoanMonthly(String bank,Integer year,Integer month);

    @Query("select * from bank")
    List<Bank> getAllBank();

    @Query("select * from loanmonthly")
    List<LoanMonthly> getAllLoanMonthly();

    @Query("select * from billingserial")
    List<BillingSerial> getAllBillingSerial();

    @Query("select bank from loanmonthly where year=:year and month=:month")
    List<String> findMonthLoanBankName(Integer year,Integer month);

    @Query("select * from loanmonthly where bank=:bank and repaid=0 and month <= :month")
    List<LoanMonthly> findNonPaidLoanBefore(String bank,Integer month);

    @Query("select * from billingserial where bank=:bank and year=:year and month=:month and day <=:day")
    List<BillingSerial> findSerialDayBefore(String bank,Integer year,Integer month,Integer day);

    @Query("select * from billingserial where bank=:bank and year=:year and month=:month and day >:day")
    List<BillingSerial> findSerialDayAfter(String bank,Integer year,Integer month,Integer day);

    @Delete
    void delete(LoanMonthly loanMonthly);
}
