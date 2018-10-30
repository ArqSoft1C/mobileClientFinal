package com.a1c.team.picaditos_ma;

public class Team {

    private String name;
    private String id;
    private String captain_name;
    private String members;

    public Team(String id, String name, String captain_name, String members) {
        this.id = id;
        this.name = name;
        this.captain_name = captain_name;
        this.members = members;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", captain_name='" + captain_name + '\'' +
                ", members='" + members + '\'' +
                '}';
    }
}


