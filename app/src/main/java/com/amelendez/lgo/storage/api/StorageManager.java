package com.amelendez.lgo.storage.api;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.amelendez.lgo.storage.daos.ContextDescription;
import com.amelendez.lgo.storage.daos.Example;
import com.amelendez.lgo.storage.daos.Languo;
import com.amelendez.lgo.storage.daos.ContextDescriptionDao;
import com.amelendez.lgo.storage.daos.ExampleDao;
import com.amelendez.lgo.storage.daos.LanguoDao;
import com.amelendez.lgo.storage.daos.DaoMaster;
import com.amelendez.lgo.storage.daos.DaoSession;
import com.amelendez.lgo.storage.daos.Meaning;
import com.amelendez.lgo.storage.daos.MeaningDao;

import java.util.List;

public class StorageManager {

    // DB configurations
    private static final String DB_NAME = "languo-db11";
    private final SQLiteDatabase db;

    // Implementation Specific
    private LanguoDao languoDao;
    private MeaningDao meaningDao;
    private ContextDescriptionDao contextDescriptionDao;
    private ExampleDao exampleDao;

    public StorageManager(final Context context)
    {
        SQLiteDatabase.CursorFactory cursorFactory = null;
        final DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, cursorFactory);
        db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        // Implementation
        languoDao = daoSession.getLanguoDao();
        meaningDao = daoSession.getMeaningDao();
        contextDescriptionDao = daoSession.getContextDescriptionDao();
        exampleDao = daoSession.getExampleDao();
    }

    public Long InsertLanguo(final Languo languo)
    {
        Long id = languoDao.insert(languo);
        return id;
    }

    public Long InsertContextDescription(final ContextDescription contextDescription)
    {
        Long id = contextDescriptionDao.insert(contextDescription);
        return id;
    }

    public Long InsertMeaning(final Meaning meaning)
    {
        if(meaning.getLanguoId() > 0)
        {
            Long id = meaningDao.insert(meaning);
            return id;
        }
        return new Long(-1);
    }

    public Long InsertExample(final Example example)
    {
        if(example.getMeaningId() > 0)
        {
            Long id = exampleDao.insert(example);
            return id;
        }
        return new Long(-1);
    }

    public Languo GetLanguoById(Long id)
    {
        Languo languo = languoDao.load(id);
        return languo;
    }

    public Languo GetLanguoByValue(String value)
    {
        List<Languo> languos = languoDao.queryBuilder().where(LanguoDao.Properties.Value.eq(value)).orderAsc(LanguoDao.Properties.Id).list();
        return languos.get(0);
    }

    public int UpdateLanguo(Languo languo)
    {
        if(languo.getId() > 0)
        {
            languoDao.update(languo);
            return 1;
        }
        return -1;
    }

    public int CascadeDeleteLanguoByValue(String value)
    {
        Languo languoToDelete = GetLanguoByValue(value);
        if(languoToDelete != null)
        {
            // Delete the meaning of the languo
            List<Meaning> meaningsToDelete = languoToDelete.getMeanings();
            final int size = meaningsToDelete.size();
            for (int i = 0; i < size; i++)
            {
                Meaning meaningToDelete = meaningsToDelete.get(i);
                if(CascadeDeleteMeaning(meaningToDelete) < 0 )
                {
                    return -1;
                }
            }

            // Delete the languo
            languoDao.delete(languoToDelete);

            // Successful path
            return 1;
        }
        return -1;
    }
    
    public int CascadeDeleteMeaning(Meaning meaningToDelete)
    {
        if(meaningToDelete != null) {
            // Delete the context description
            if (DeleteContextDescription(meaningToDelete.getContextDescription()) < 0) {
                return -1;
            }

            // Delete the examples
            List<Example> examplesToDelete = meaningToDelete.getExamples();
            final int size = examplesToDelete.size();
            for (int i = 0; i < size; i++) {
                Example exampleToDelete = examplesToDelete.get(i);
                if(DeleteExample(exampleToDelete) < 0)
                {
                    return -1;
                }
            }

            // Successful path
            return 1;
        }
        return -1;
    }

    public int DeleteContextDescription(ContextDescription contextDescriptionToDelete)
    {
        if(contextDescriptionToDelete != null)
        {
            contextDescriptionDao.delete(contextDescriptionToDelete);

            // Successful path
            return 1;
        }
        return -1;
    }

    public int DeleteExample(Example exampleToDelete)
    {
        if(exampleToDelete != null)
        {
            exampleDao.delete(exampleToDelete);

            // Successful path
            return 1;
        }
        return -1;
    }
}
