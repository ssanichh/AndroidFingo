package com.ameskate.fingo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataManager {
    private Context mContext;
    private SharedPreferences mPrefs;

    public DataManager(Context context){
        this.mContext = context;
        mPrefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void getGameData(final GameDataCallback gameCallback){
        getData(new DataCallback() {
            @Override
            public void result(String res) {
                ArrayList<GameMode> modes = parseResponse(res);
                gameCallback.result(modes);
            }
        });
    }

    public ArrayList<GameMode> getOfflineGameData(){
        writeGameModeBackup(null);
        String data = getDataFromLocalStorage();
        return parseResponse(data);
    }

    private void getData(final DataCallback callback){
        if(NetworkUtils.hasConnection(mContext)){
            getDataFromFirebase(new FirebaseCallback() {
                @Override
                public void result(String json) {
                    if(json == null){
                        callback.result(getDataFromLocalStorage());
                    }else {
                        writeGameModeBackup(json);
                        callback.result(getDataFromLocalStorage());
                    }
                }
            });
        }else{
            callback.result(getDataFromLocalStorage());
        }
    }

    private String getDataFromLocalStorage(){
        String backup = getGameModeBackup();
        if(backup == null){
            try {
                writeGameModeBackup(FileUtils.readFromAssets(mContext, Constants.INIT_FILE));
                backup = getGameModeBackup();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return backup;
    }

    private void getDataFromFirebase(final FirebaseCallback fCallback){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> value = (Map<String, String>)dataSnapshot.getValue();
                JSONObject json = new JSONObject(value);
                Log.d("FIREBASE_TEST", "Result json = "+json.toString());
                fCallback.result(json.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fCallback.result(null);
            }
        });
    }

    private void writeGameModeBackup(String backup){
        mPrefs.edit().putString(Constants.GAME_MODE_BACKUP, backup).apply();
    }

    private String getGameModeBackup(){
        return mPrefs.getString(Constants.GAME_MODE_BACKUP, null);
    }

    private ArrayList<GameMode> parseResponse(String result){
        ArrayList<GameMode> gameModes = new ArrayList<>();
        try{
            JSONObject json = new JSONObject(result);
            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                GameMode mode = new GameMode();
                mode.setName(key);

                Map<Integer, String> buttons = null;
                Object btns = json.get(key);

                if(btns instanceof JSONObject){
                    JSONObject j = (JSONObject) btns;
                    if(!j.isNull("unicorn")) mode.setUnicorn(j.getString("unicorn"));
                    buttons = parseButtons(j);
                }else if(btns instanceof JSONArray){
                    JSONArray j = (JSONArray) btns;
                    buttons = parseButtons(j);
                }

                if(buttons!= null && buttons.size() == 24) {
                    mode.setButtons(buttons);
                    gameModes.add(mode);
                }
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        return gameModes;
    }

    private Map<Integer, String> parseButtons(JSONObject btns) throws JSONException{
        Map<Integer, String> buttons = new HashMap<>();

        for(int i=0; i<24; i++){
            if(!btns.isNull(""+i)) buttons.put(i, btns.getString(""+i));
        }

        return buttons;
    }

    private Map<Integer, String> parseButtons(JSONArray btns) throws JSONException{
        Map<Integer, String> buttons = new HashMap<>();

        for(int i=0; i<btns.length(); i++){
            buttons.put(i, btns.getString(i));
        }

        return buttons;
    }

    private interface FirebaseCallback{
        void result(String json);
    }

    private interface DataCallback{
        void result(String res);
    }

    public interface GameDataCallback{
        void result(ArrayList<GameMode> res);
    }
}
