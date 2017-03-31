package com.ameskate.fingo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class GameMode implements Parcelable, Comparable<GameMode> {
    private String name;
    private Map<Integer, String> buttons;
    private String unicorn;

    public GameMode(){
        name = null;
        buttons = null;
        unicorn = null;
    }

    public GameMode(String name, Map<Integer, String> buttons, String unicorn){
        this.name = name;
        this.buttons = buttons;
        this.unicorn = unicorn;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Integer, String> getButtons() {
        return buttons;
    }

    public void setButtons(Map<Integer, String> buttons) {
        this.buttons = buttons;
    }

    public String getUnicorn() {
        return unicorn;
    }

    public void setUnicorn(String unicorn) {
        this.unicorn = unicorn;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.buttons.size());
        for (Map.Entry<Integer, String> entry : this.buttons.entrySet()) {
            dest.writeValue(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeString(this.unicorn);
    }

    protected GameMode(Parcel in) {
        this.name = in.readString();
        int buttonsSize = in.readInt();
        this.buttons = new HashMap<Integer, String>(buttonsSize);
        for (int i = 0; i < buttonsSize; i++) {
            Integer key = (Integer) in.readValue(Integer.class.getClassLoader());
            String value = in.readString();
            this.buttons.put(key, value);
        }
        this.unicorn = in.readString();
    }

    public static final Parcelable.Creator<GameMode> CREATOR = new Parcelable.Creator<GameMode>() {
        @Override
        public GameMode createFromParcel(Parcel source) {
            return new GameMode(source);
        }

        @Override
        public GameMode[] newArray(int size) {
            return new GameMode[size];
        }
    };

    @Override
    public int compareTo(@NonNull GameMode o) {
        return this.getName().compareTo(o.getName());
    }
}
