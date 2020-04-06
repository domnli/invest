package pers.domnli.invest.repository.local.migration;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migration_1_2 extends Migration {

    public Migration_1_2() {
        super(1, 2);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `BillingSerial` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `bank` TEXT, `year` INTEGER, `month` INTEGER, `day` INTEGER, `money` INTEGER)");
    }
}
