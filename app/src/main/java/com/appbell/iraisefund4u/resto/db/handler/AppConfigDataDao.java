package com.appbell.iraisefund4u.resto.db.handler;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.appbell.iraisefund4u.resto.db.entity.AppConfigData;

@Dao
public interface AppConfigDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createAppConfigData(AppConfigData appConfigData);

    @Query("select * from APP_CONFIG_MASTER")
    AppConfigData getAppConfigData();

    @Query("delete from APP_CONFIG_MASTER")
    void deleteAppConfigData();

    @Query("UPDATE APP_CONFIG_MASTER SET USER_LOGIN_PASSWORD =:newPassword")
    void updateUserPassword(String newPassword);

}
