package tg.licorne.entraideagro.SqlController;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tg.licorne.entraideagro.model.Rapports;
import tg.licorne.entraideagro.sqlLIte.DatabaseHelper;

/**
 * Created by Admin on 18/06/2018.
 */

public class RapportRecuController {
    DatabaseHelper databaseHelper;
    Context context;
    SQLiteDatabase wdb, rdb;

    public static final String TABLE_RAPPORT_RECU = "rapport_recu_sql";

    public static final String COLUMN_RAPPORT_RECU_ID = "id";
    public static final String COLUMN_RAPPORT_RECU_SUJET= "sujet";
    public static final String COLUMN_RAPPORT_RECU_DATE= "date";
    public static final String COLUMN_RAPPORT_RECU_TOKEN= "token";

    public RapportRecuController(Context context) {
        databaseHelper = new DatabaseHelper(context, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        rdb = databaseHelper.getReadableDatabase();
        wdb = databaseHelper.getWritableDatabase();
        this.context = context;
    }

    public boolean addRapportRecu(Rapports rapports){
        boolean status= false;
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_RAPPORT_RECU_ID, rapports.getId());
        contentValues.put(COLUMN_RAPPORT_RECU_SUJET, rapports.getSujet());
        contentValues.put(COLUMN_RAPPORT_RECU_DATE, rapports.getDate());
        contentValues.put(COLUMN_RAPPORT_RECU_TOKEN, rapports.getToken());

        long st = wdb.insert(TABLE_RAPPORT_RECU, null, contentValues);

        if (st!=-1){
            status = true;
        }
        return status;
    }

    public List<Rapports> selectAllRapportRecu(){
        List<Rapports> rapportsList = new ArrayList<>();
        String[] projection = {
                COLUMN_RAPPORT_RECU_ID,
                COLUMN_RAPPORT_RECU_SUJET,
                COLUMN_RAPPORT_RECU_DATE,
                COLUMN_RAPPORT_RECU_TOKEN
        };
        Cursor cursor = rdb.query(TABLE_RAPPORT_RECU, projection, null, null, null, null, null);
        while (cursor.moveToNext()){
            Rapports rapports = new Rapports(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
            rapportsList.add(rapports);
        }
        return rapportsList;
    }
}
