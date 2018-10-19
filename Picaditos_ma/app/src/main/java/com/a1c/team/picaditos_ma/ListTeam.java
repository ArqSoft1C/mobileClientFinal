package com.a1c.team.picaditos_ma;

public class ListTeam {

    private String name;
    private String captain_name;
    private String score;

    public ListTeam(String name, String captain_name, String score) {
        this.name = name;
        this.captain_name = captain_name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaptain_name() {
        return captain_name;
    }

    public void setCaptain_name(String captain_name) {
        this.captain_name = captain_name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

   }


