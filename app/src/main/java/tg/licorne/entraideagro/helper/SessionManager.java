package tg.licorne.entraideagro.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.List;

import tg.licorne.entraideagro.controllers.LoginActivity;
import tg.licorne.entraideagro.model.Login;

/**
 * Created by Admin on 08/10/2017.
 */

public class SessionManager {

    private static String TAG = SessionManager.class.getSimpleName();

    private static final String SHARED_PREF_NAME = "AndroidHiveLogin";
    private static final String KEY_ID_ROLE = "keyusername";
    private static final String KEY_TOKEN = "keystoken";

    private static SessionManager mInstance;
    private static Context mCtx;

    Editor editor;

    private static final String PREF_NAME = "AndroidHiveLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIN";


    public SessionManager(Context context){
        mCtx = context;
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SessionManager(context);
        }
        return mInstance;
    }

    public void userLogin(Login login) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, login.getToken());
        editor.putInt(KEY_ID_ROLE, login.getId_role());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOKEN, null) != null;
    }

    public Login getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Login(
                sharedPreferences.getString(KEY_TOKEN, null),
                sharedPreferences.getInt(KEY_ID_ROLE, 0)
        );
    }

    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }

}
