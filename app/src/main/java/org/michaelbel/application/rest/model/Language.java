package org.michaelbel.application.rest.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

@SuppressWarnings("all")
public class Language extends RealmObject implements Serializable {

    @SerializedName("iso_639_1")
    public String language;

    @SerializedName("name")
    public String name;
}