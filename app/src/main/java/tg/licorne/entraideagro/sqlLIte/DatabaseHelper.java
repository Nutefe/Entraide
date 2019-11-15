package tg.licorne.entraideagro.sqlLIte;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Admin on 18/06/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    Context context;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "agro_server.db";

    public static final String TABLE_USER = "user_sql";
    public static final String TABLE_AGENT = "agent_sql";
    public static final String TABLE_ACTIVITER = "activiter_sql";
    public static final String TABLE_RAPPORT_SEND = "rapport_send_sql";
    public static final String TABLE_RAPPORT_RECU = "rapport_recu_sql";

    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NOM= "nom";
    private static final String COLUMN_USER_PRENOM = "prenom";
    private static final String COLUMN_USER_ADRESSE = "adresse";
    private static final String COLUMN_USER_TELEPHONE = "telephone";
    private static final String COLUMN_USER_EQUIPE = "equipe";
    private static final String COLUMN_USER_AVATAR = "avatar";

    private static final String COLUMN_AGENT_ID = "id";
    private static final String COLUMN_AGENT_NOM= "nom";
    private static final String COLUMN_AGENT_TYPE = "type";
    private static final String COLUMN_AGENT_NUM = "num";

    private static final String COLUMN_ACTIVITER_ID = "id";
    private static final String COLUMN_ACTIVITER_NOM= "nomActivite";
    private static final String COLUMN_ACTIVITER_DESC= "description";

    private static final String COLUMN_RAPPORT_SEND_ID = "id";
    private static final String COLUMN_RAPPORT_SEND_SUJET= "sujet";
    private static final String COLUMN_RAPPORT_SEND_DATE= "date";
    private static final String COLUMN_RAPPORT_SEND_TOKEN= "token";

    private static final String COLUMN_RAPPORT_RECU_ID = "id";
    private static final String COLUMN_RAPPORT_RECU_SUJET= "sujet";
    private static final String COLUMN_RAPPORT_RECU_DATE= "date";
    private static final String COLUMN_RAPPORT_RECU_TOKEN= "token";

    private String CREATE_USER_TABLE = "CREATE TABLE "+ TABLE_USER +" ( "
            + COLUMN_USER_ID +" INTEGER , "+ COLUMN_USER_NOM +" TEXT , "
            + COLUMN_USER_PRENOM +" TEXT , "+ COLUMN_USER_ADRESSE +" TEXT , "
            + COLUMN_USER_TELEPHONE +" TEXT , "+ COLUMN_USER_EQUIPE +" TEXT , "
            + COLUMN_USER_AVATAR+ " TEXT )";

    private String CREATE_AGENT_TABLE = "CREATE TABLE "+ TABLE_AGENT +" ( "
            + COLUMN_AGENT_ID +" INTEGER , "+ COLUMN_AGENT_NOM +" TEXT , "
            + COLUMN_AGENT_TYPE+ " TEXT , "+ COLUMN_AGENT_NUM+ " TEXT )";

    private String CREATE_ACTIVITER_TABLE = "CREATE TABLE "+ TABLE_ACTIVITER +" ( "
            + COLUMN_ACTIVITER_ID +" INTEGER , "+ COLUMN_ACTIVITER_NOM +" TEXT , "+ COLUMN_ACTIVITER_DESC+ " TEXT )";

    private String CREATE_RAPPORT_SEND_TABLE = "CREATE TABLE "+ TABLE_RAPPORT_SEND +" ( "
            + COLUMN_RAPPORT_SEND_ID +" INTEGER , "+ COLUMN_RAPPORT_SEND_SUJET +" TEXT , "
            + COLUMN_RAPPORT_SEND_DATE +" TEXT , "+ COLUMN_RAPPORT_SEND_TOKEN+ " TEXT )";

    private String CREATE_RAPPORT_RECU_TABLE = "CREATE TABLE "+ TABLE_RAPPORT_RECU +" ( "
            + COLUMN_RAPPORT_RECU_ID +" INTEGER , "+ COLUMN_RAPPORT_RECU_SUJET +" TEXT , "
            + COLUMN_RAPPORT_RECU_DATE +" TEXT , "+ COLUMN_RAPPORT_RECU_TOKEN+ " TEXT )";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS "+ TABLE_USER;
    private String DROP_AGENT_TABLE = "DROP TABLE IF EXISTS "+ TABLE_AGENT;
    private String DROP_ACTIVITER_TABLE = "DROP TABLE IF EXISTS "+ TABLE_ACTIVITER;
    private String DROP_RAPPORT_SEND_TABLE = "DROP TABLE IF EXISTS "+ TABLE_RAPPORT_SEND;
    private String DROP_RAPPORT_RECU_TABLE = "DROP TABLE IF EXISTS "+ TABLE_RAPPORT_RECU;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(CREATE_AGENT_TABLE);
        sqLiteDatabase.execSQL(CREATE_ACTIVITER_TABLE);
        sqLiteDatabase.execSQL(CREATE_RAPPORT_SEND_TABLE);
        sqLiteDatabase.execSQL(CREATE_RAPPORT_RECU_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_USER_TABLE);
        sqLiteDatabase.execSQL(DROP_AGENT_TABLE);
        sqLiteDatabase.execSQL(DROP_ACTIVITER_TABLE);
        sqLiteDatabase.execSQL(DROP_RAPPORT_SEND_TABLE);
        sqLiteDatabase.execSQL(DROP_RAPPORT_RECU_TABLE);
        onCreate(sqLiteDatabase);
    }
}
