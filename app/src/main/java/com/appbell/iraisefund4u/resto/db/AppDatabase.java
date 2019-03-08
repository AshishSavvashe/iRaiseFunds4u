package com.appbell.iraisefund4u.resto.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.appbell.iraisefund4u.resto.db.entity.AppConfigData;
import com.appbell.iraisefund4u.resto.db.handler.AppConfigDataDao;
import com.appbell.iraisefund4u.resto.db.handler.CommonDBHandler;
import com.appbell.iraisefund4u.resto.db.handler.VoucherBookDataDao;
import com.appbell.iraisefund4u.resto.db.handler.VoucherDataDao;
import com.appbell.iraisefund4u.resto.ui.VoucherBookData;
import com.appbell.iraisefund4u.resto.ui.VoucherData;

@Database(entities = {AppConfigData.class, VoucherBookData.class , VoucherData.class}, version = CommonDBHandler.DB_VERSION_4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase database;

    public static final String DATABASE_NAME = "iraisefund4udb";

    public static AppDatabase getAppDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            .allowMainThreadQueries()
                            .addMigrations(Migration_1_2,MIGRATION_2_3,MIGRATION_3_4)
                            .build();
        }
        return database;
    }


    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {}

    public abstract AppConfigDataDao getAppConfigDao();

    public abstract VoucherBookDataDao getVoucherBookDao();

    public abstract VoucherDataDao getVoucherDataDao();


    public static final Migration Migration_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE IF NOT EXISTS `VOUCHER_BOOK_MASTER` (`_id` INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "`BOOK_ID` INTEGER NOT NULL ," +
                    "`GROUP_ID` TEXT," +
                    "`CUSTOMER_EMAIL` TEXT," +
                    "`CUSTOMER_PHONE` TEXT," +
                    "`CUSTOMER_NAME` TEXT," +
                    "`BOOK_STATUS` TEXT ," +
                    "`BOOK_CODE` TEXT," +
                    "`CAMPAING_ID` INTEGER NOT NULL  ," +
                    "`CAMPAING_TAG_LINE` TEXT, " +
                    "`FILE_NAME` TEXT," +
                    "`VOUCHER_START_TIME` INTEGER NOT NULL  ," +
                    "`VOUCHER_END_TIME` INTEGER NOT NULL  ," +
                    "`LAST_SYNCTIME` INTEGER NOT NULL)") ;

            

            database.execSQL("CREATE TABLE IF NOT EXISTS `VOUCHER_DATA_MASTER` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`VOUCHER_ID` INTEGER NOT NULL, " +
                    "`RESTAURANT_ID` INTEGER NOT NULL, " +
                    "`VOUCHER_CODE` TEXT, " +
                    "`SALE_VALUE` FLOAT NOT NULL, " +
                    "`VOUCHER_VALUE` FLOAT NOT NULL, " +
                    "`EXPIRY_DATE` INTEGER NOT NULL, " +
                    "`VALIDITY` INTEGER NOT NULL," +
                    " `VOUCHER_USE` TEXT, " +
                    "`SOLD_BY` TEXT, " +
                    "`VOUCHER_STATUS` TEXT, " +
                    "`VOUCHER_DESC` TEXT, " +
                    "`IMAGE_NAME` TEXT, " +
                    "`COMMISSION` FLOAT NOT NULL, " +
                    "`GROUP_ID` TEXT, " +
                    "`COMMENT` TEXT, " +
                    "`CUSTOMER_NAME` TEXT, " +
                    "`CUSTOMER_EMAIL` TEXT, " +
                    "`CUSTOMER_PHONE` TEXT, " +
                    "`SERIAL_NUMBER` INTEGER NOT NULL, " +
                    "`PURPOSE` TEXT, " +
                    "`ACTIVATION_DAYS` INTEGER NOT NULL, " +
                    "`START_DATE` INTEGER NOT NULL, " +
                    "`BOOK_ID` INTEGER NOT NULL, " +
                    "`MAX_ALLOWED_COUNT` INTEGER NOT NULL, " +
                    "`USED_COUNT` INTEGER NOT NULL, " +
                    "`NOTES` TEXT, " +
                    "`PURCHES_DATE` INTEGER NOT NULL, " +
                    "`START_REDEEM_DATE` TEXT, " +
                    "`FACILITY_ID` INTEGER NOT NULL, " +
                    "`COMPAING_ID` INTEGER NOT NULL, " +
                    "`VOUCHER_MSG` TEXT, " +
                    "`VOUCHER_ALIES` TEXT, " +
                    "`RESTAURANT_NAME` TEXT, " +
                    "`RESTAURANT_ADDRESS` TEXT, " +
                    "`PHONE1` TEXT, " +
                    "`RESTAURANT_WEBSITE_URL` TEXT, " +
                    "`DEMAND_OBJECT_TYPE` TEXT, " +
                    "`DEMAND_OBJECT_ID` INTEGER NOT NULL, " +
                    "`GROUP_TOTAL_QTY` INTEGER NOT NULL, " +
                    "`GROUP_SOLD_QTY` INTEGER NOT NULL, " +
                    "`AVAL_IDS` TEXT, " +
                    "`AVIABLR_VCIMAGENAME` TEXT, " +
                    "`USAGECOUNTSTR` TEXT, " +
                    "`COMAINGNAME` TEXT, " +
                    "`RESTAURANTLONG` FLOAT NOT NULL, " +
                    "`RESTAURANTLAT` FLOAT NOT NULL,"+
                    "`VOUCHER_RESTODATA` TEXT,"+
                    "`VOUCHER_STATUS_ID` INTEGER NOT NULL)");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE VOUCHER_BOOK_MASTER "
                    + " ADD COLUMN TERMS_AND_CONDITION TEXT");

            database.execSQL("ALTER TABLE VOUCHER_BOOK_MASTER "
                    + " ADD COLUMN TIME_ZONE_PROP TEXT");

            database.execSQL("ALTER TABLE VOUCHER_DATA_MASTER "
                    + " ADD COLUMN VOUCHER_REDEEM_DATE TEXT");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE VOUCHER_BOOK_MASTER "
                    + " ADD COLUMN `LAST_DELETED_VOUCHERBOOK_TIME` INTEGER default 0 NOT NULL");

        }
    };
}
