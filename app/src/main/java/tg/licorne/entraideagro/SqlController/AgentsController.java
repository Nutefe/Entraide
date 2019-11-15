package tg.licorne.entraideagro.SqlController;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tg.licorne.entraideagro.model.Agents;
import tg.licorne.entraideagro.sqlLIte.DatabaseHelper;

/**
 * Created by Admin on 18/06/2018.
 */

public class AgentsController {
    DatabaseHelper databaseHelper;
    Context context;
    SQLiteDatabase wdb, rdb;

    public static final String TABLE_AGENT = "agent_sql";

    public static final String COLUMN_AGENT_ID = "id";
    public static final String COLUMN_AGENT_NOM= "nom";
    public static final String COLUMN_AGENT_TYPE = "type";
    public static final String COLUMN_AGENT_NUM = "num";

    public AgentsController(Context context) {
        databaseHelper = new DatabaseHelper(context, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        rdb = databaseHelper.getReadableDatabase();
        wdb = databaseHelper.getWritableDatabase();
        this.context = context;
    }

    public boolean addAgents(Agents agents){
        boolean status= false;
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_AGENT_ID, agents.getId());
        contentValues.put(COLUMN_AGENT_NOM, agents.getNom());
        contentValues.put(COLUMN_AGENT_TYPE, agents.getType());
        contentValues.put(COLUMN_AGENT_NUM, agents.getNum());

        long st = wdb.insert(TABLE_AGENT, null, contentValues);

        if (st!=-1){
            status = true;
        }
        return status;
    }

    public List<Agents> selectAllAgents(){
        List<Agents> agentsList = new ArrayList<>();
        String[] projection = {
                COLUMN_AGENT_ID,
                COLUMN_AGENT_NOM,
                COLUMN_AGENT_TYPE,
                COLUMN_AGENT_NUM
        };
        Cursor cursor = rdb.query(TABLE_AGENT, projection, null, null, null, null, null);
        while (cursor.moveToNext()){
            Agents agents = new Agents(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
            agentsList.add(agents);
        }
        return agentsList;
    }
}
