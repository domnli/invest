package pers.domnli.invest.repository.local.migration;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migration_2_3 extends Migration {

    public Migration_2_3() {
        super(2, 3);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        database.execSQL("DROP TABLE `BillingSerial`");
        database.execSQL("CREATE TABLE IF NOT EXISTS `BillingSerial` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `bank` TEXT, `year` INTEGER, `month` INTEGER, `day` INTEGER, `money` REAL)");
    }
}
