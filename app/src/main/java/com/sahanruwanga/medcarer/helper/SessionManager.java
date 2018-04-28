package com.sahanruwanga.medcarer.helper;

/**
 * Created by Sahan Ruwanga on 3/5/2018.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    private SharedPreferences sharedPreferences;

    private Editor editor;
    private Context context;

    // Shared sharedPreferences mode
    private int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "MedCarerLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        this.setContext(context);
        setSharedPreferences(this.getContext().getSharedPreferences(PREF_NAME, getPRIVATE_MODE()));
        setEditor(getSharedPreferences().edit());
    }

    //region Loogin Session Handle
    public void setLogin(boolean isLoggedIn) {

        getEditor().putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        getEditor().commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return getSharedPreferences().getBoolean(KEY_IS_LOGGEDIN, false);
    }
    //endregion

    //region Getters and Setters
    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getPRIVATE_MODE() {
        return PRIVATE_MODE;
    }

    public void setPRIVATE_MODE(int PRIVATE_MODE) {
        this.PRIVATE_MODE = PRIVATE_MODE;
    }
    //endregion
}
