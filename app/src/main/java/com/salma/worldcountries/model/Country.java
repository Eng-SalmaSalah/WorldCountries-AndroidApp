package com.salma.worldcountries.model;

public class Country {
    String rank;
    String name;
    String population;
    String flagImgUrlStr;

    public Country() {
        this.rank = null;
        this.name=null;
        this.population=null;
        this.flagImgUrlStr=null;
    }

    public Country(String rank, String name, String population, String flagImgUrlStr) {
        this.rank = rank;
        this.name = name;
        this.population = population;
        this.flagImgUrlStr = flagImgUrlStr;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getFlagImgUrlStr() {
        return flagImgUrlStr;
    }

    public void setFlagImgUrlStr(String flagImgUrlStr) {
        this.flagImgUrlStr = flagImgUrlStr;
    }
}
