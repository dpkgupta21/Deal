package com.deal.exap.databasemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.deal.exap.model.CategoryDTO;
import com.deal.exap.model.DealDTO;
import com.deal.exap.model.FavoriteDTO;
import com.deal.exap.model.FollowingDTO;
import com.deal.exap.model.InterestDTO;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "exap.sqlite";
    private static final int DATABASE_VERSION = 1;

    private Dao<DealDTO, String> dealDao = null;
    private Dao<InterestDTO, String> interestDao = null;
    private Dao<CategoryDTO, String> categoryDao = null;
    private Dao<FavoriteDTO, String> favoriteDao = null;
    private Dao<FollowingDTO, String> followingDao = null;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        DatabaseInitializer initializer = new DatabaseInitializer(context);
        try {
            initializer.createDatabase();
            initializer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");

            TableUtils.createTable(connectionSource, DealDTO.class);
            TableUtils.createTable(connectionSource, InterestDTO.class);
            TableUtils.createTable(connectionSource, CategoryDTO.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");

            TableUtils.dropTable(connectionSource, DealDTO.class, true);
            TableUtils.dropTable(connectionSource, InterestDTO.class, true);
            TableUtils.dropTable(connectionSource, InterestDTO.class, true);
            onCreate(db);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }


    public Dao<DealDTO, String> getDealDao() throws SQLException {
        try {
            if (dealDao == null) {
                dealDao = BaseDaoImpl.createDao(getConnectionSource(), DealDTO.class);
                Log.d("", "");
            }
            return dealDao;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Dao<InterestDTO, String> getInterestDao() throws SQLException {
        try {
            if (interestDao == null) {
                interestDao = BaseDaoImpl.createDao(getConnectionSource(), InterestDTO.class);
                Log.d("", "");
            }
            return interestDao;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Dao<CategoryDTO, String> getCategoryDao() throws SQLException {
        try {
            if (categoryDao == null) {
                categoryDao = BaseDaoImpl.createDao(getConnectionSource(), CategoryDTO.class);
                Log.d("", "");
            }
            return categoryDao;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Dao<FavoriteDTO, String> getFavoriteDao() throws SQLException {
        try {
            if (favoriteDao == null) {
                favoriteDao = BaseDaoImpl.createDao(getConnectionSource(), FavoriteDTO.class);
                Log.d("", "");
            }
            return favoriteDao;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Dao<FollowingDTO, String> getFollowingDao() throws SQLException {
        try {
            if (followingDao == null) {
                followingDao = BaseDaoImpl.createDao(getConnectionSource(), FollowingDTO.class);
                Log.d("", "");
            }
            return followingDao;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void close() {
        super.close();
        dealDao = null;
        interestDao = null;
        categoryDao = null;
    }
}
