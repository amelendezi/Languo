package com.amelendez.lgo.storage.api;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.amelendez.lgo.storage.dao.DaoMaster;
import com.amelendez.lgo.storage.dao.DaoSession;
import com.amelendez.lgo.storage.dao.Languo;
import com.amelendez.lgo.storage.dao.LanguoDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StorageProvider {

    public final String LANGUO_KEY_NAME = "term";
    private static final String DB_NAME = "languo-db";
    private final SQLiteDatabase db;
    private LanguoDao languoDao;

    public StorageProvider(final Context context)
    {
        SQLiteDatabase.CursorFactory cursorFactory = null;
        final DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, cursorFactory);
        db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        languoDao = daoSession.getLanguoDao();
    }

    public void SaveLanguo(final Languo languo)
    {
        SetInsertTimestamps(languo);
        languoDao.insert(languo);
    }

    public boolean DoesLanguoExist(String term)
    {
        return GetLanguoByTerm(term) != null;
    }

    public Languo GetLanguoByTerm(String term)
    {
        List<Languo> languos = languoDao.queryBuilder().where(LanguoDao.Properties.Term.eq(term)).orderAsc(LanguoDao.Properties.Id).list();
        return languos.get(0);
    }

    public List<Languo> GetAllLanguos()
    {
        List<Languo> languos = languoDao.queryBuilder().orderDesc(LanguoDao.Properties.ChangedDate).list();
        if(languos == null)
        {
            return new ArrayList<Languo>();
        }
        return languos;
    }

    public void UpdateLanguo(Languo updateLanguo)
    {
        SetUpdateTimestamps(updateLanguo);
        languoDao.update(updateLanguo);
    }

    public void DeleteLanguo(String term)
    {
        Languo toDeleteLanguo = GetLanguoByTerm(term);
        languoDao.delete(toDeleteLanguo);
    }

    public static void ClearAll(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(
                context.getApplicationContext(), DB_NAME, null);
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        devOpenHelper.onUpgrade(db,0,0);
    }

    private void SetInsertTimestamps(Languo languo)
    {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        languo.setInsertDate(currentDate);
        languo.setChangedDate(currentDate);
    }

    private void SetUpdateTimestamps(Languo languo)
    {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        languo.setChangedDate(currentDate);
    }
}