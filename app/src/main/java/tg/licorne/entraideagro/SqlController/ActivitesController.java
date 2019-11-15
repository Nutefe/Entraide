package tg.licorne.entraideagro.SqlController;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tg.licorne.entraideagro.model.Activites;
import tg.licorne.entraideagro.sqlLIte.DatabaseHelper;

/**
 * Created by Admin on 18/06/2018.
 */

public class ActivitesController {
    DatabaseHelper databaseHelper;
    Context context;
    SQLiteDatabase wdb, rdb;

    public static final String TABLE_ACTIVITER = "activiter_sql";

    public static final String COLUMN_ACTIVITER_ID = "id";
    public static final String COLUMN_ACTIVITER_NOM= "nomActivite";
    public static final String COLUMN_ACTIVITER_DESC= "description";

    public ActivitesController(Context context) {
        databaseHelper = new DatabaseHelper(context, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        rdb = databaseHelper.getReadableDatabase();
        wdb = databaseHelper.getWritableDatabase();
        this.context = context;
    }

    public boolean addActivites(Activites  activites){
        boolean status= false;
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ACTIVITER_ID, activites.getId());
        contentValues.put(COLUMN_ACTIVITER_NOM, activites.getNomActivite());
        contentValues.put(COLUMN_ACTIVITER_DESC, activites.getDescription());

        long st = wdb.insert(TABLE_ACTIVITER, null, contentValues);

        if (st!=-1){
            status = true;
        }
        return status;
    }

    public List<Activites> selectAllActivites(){
        List<Activites> activitesList = new ArrayList<>();
        String[] projection = {
                COLUMN_ACTIVITER_ID,
                COLUMN_ACTIVITER_NOM,
                COLUMN_ACTIVITER_DESC
        };
        Cursor cursor = rdb.query(TABLE_ACTIVITER, projection, null, null, null, null, null);
        while (cursor.moveToNext()){
            Activites activites = new Activites(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)
            );
            activitesList.add(activites);
        }
        return activitesList;
    }
}
