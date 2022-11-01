package com.example.mowgliupgrade.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Cast {

    private int castId;
    private String castName, profilePath, character;

    public Cast(){

    }

    public Cast(int castId, String castName, String profilePath, String character) {
        this.castId = castId;
        this.castName = castName;
        this.profilePath = profilePath;
        this.character = character;
    }

    public Cast (JSONObject jsonObject) throws JSONException {
        castId = jsonObject.getInt("id");
        castName = jsonObject.getString("name");
        character = jsonObject.getString("character");
        profilePath = jsonObject.getString("profile_path");
    }

    public void setCastId(int castId) {
        this.castId = castId;
    }

    public int getCastId() {
        return castId;
    }

    public void setCastName(String castName) {
        this.castName = castName;
    }

    public String getCastName() {
        return castName;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCharacter() {
        return character;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getProfilePath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", profilePath);

    }

    public static List<Cast> fromJsonArray(JSONArray jsonArray) throws JSONException {
        //Method creates a list of cast from the given JSON array
        List<Cast> castList = new ArrayList<>();
        JSONObject c;
        for (int i =0; i<jsonArray.length(); i++ ){
            c = jsonArray.getJSONObject(i);
            castList.add(new Cast(c));
        }
        return castList;
    }

    @Override
    public String toString() {
        return "Cast{" +
                "castId=" + castId +
                ", castName='" + castName + '\'' +
                ", profilePath='" + profilePath + '\'' +
                ", character='" + character + '\'' +
                '}';
    }
}
