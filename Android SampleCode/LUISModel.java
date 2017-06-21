package com.linyuting.luistest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by A on 17-4-2017.
 */

public class LUISModel {
    private String query;

    public String getQuery() {
        return this.query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    private TopScoringIntent topScoringIntent;

    public TopScoringIntent getTopScoringIntent() {
        return this.topScoringIntent;
    }

    public void setTopScoringIntent(TopScoringIntent topScoringIntent) {
        this.topScoringIntent = topScoringIntent;
    }

    private ArrayList<Intent> intents;

    public ArrayList<Intent> getIntents() {
        return this.intents;
    }

    public void setIntents(ArrayList<Intent> intents) {
        this.intents = intents;
    }

    private ArrayList<Entity> entities;

    public ArrayList<Entity> getEntities() {
        return this.entities;
    }

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public LUISModel(JSONObject jsonObject) {

        try {
            // query
            this.query = jsonObject.getString("query");

            // topScoringIntent
            this.topScoringIntent = new TopScoringIntent(
                    jsonObject.getJSONObject("topScoringIntent").getString("intent"),
                    jsonObject.getJSONObject("topScoringIntent").getDouble("score")
            );

            // intents
            ArrayList<Intent> intents = new ArrayList<Intent>();
            JSONArray jsonIntent = jsonObject.getJSONArray("intents");
            for (int i = 0; i < jsonIntent.length(); i++) {
                JSONObject tmp = (JSONObject) jsonIntent.get(i);
                intents.add(
                        new Intent(
                                tmp.getString("intent"),
                                tmp.getDouble("score")
                        )
                );
            }
            this.intents = intents;

            // entities
            ArrayList<Entity> entities = new ArrayList<Entity>();
            JSONArray jsonEntity = jsonObject.getJSONArray("entities");
            for (int i = 0; i < jsonEntity.length(); i++) {
                JSONObject tmp = (JSONObject) jsonEntity.get(i);
                entities.add(
                        new Entity(
                                tmp.getString("entity"),
                                tmp.getString("type"),
                                tmp.getInt("startIndex"),
                                tmp.getInt("endIndex"),
                                tmp.getDouble("score")
                        )
                );
            }
            this.entities = entities;

        } catch (Exception e) {
        }
    }
}


class TopScoringIntent {
    public TopScoringIntent(String intent, double score) {
        this.intent = intent;
        this.score = score;
    }

    private String intent;

    public String getIntent() {
        return this.intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    private double score;

    public double getScore() {
        return this.score;
    }

    public void setScore(double score) {
        this.score = score;
    }


}

class Intent {
    private String intent;

    public Intent(String intent, double score) {
        this.intent = intent;
        this.score = score;
    }

    public String getIntent() {
        return this.intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    private double score;

    public double getScore() {
        return this.score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}

class Entity {
    public Entity(String entity, String type, int startIndex, int endIndex, double score) {
        this.entity = entity;
        this.type = type;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.score = score;
    }

    private String entity;

    public String getEntity() {
        return this.entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private int startIndex;

    public int getStartIndex() {
        return this.startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    private int endIndex;

    public int getEndIndex() {
        return this.endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    private double score;

    public double getScore() {
        return this.score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}