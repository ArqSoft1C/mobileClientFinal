package com.a1c.team.picaditos_ma;

public class Match {

    private int id;
    private String teamHome;
    private String teamAway;
    private String date;
    private boolean played;

    public Match(int id, String teamHome, String teamAway, String date, boolean played, int score_teamHome, int score_teamAway, int courtId) {
        this.id = id;
        this.teamHome = teamHome;
        this.teamAway = teamAway;
        this.date = date;
        this.played = played;
        this.score_teamHome = score_teamHome;
        this.score_teamAway = score_teamAway;
        this.courtId = courtId;
    }

    private int score_teamHome;

    public String getTeamHome() {
        return teamHome;
    }

    public void setTeamHome(String teamHome) {
        this.teamHome = teamHome;
    }

    public String getTeamAway() {
        return teamAway;
    }

    public void setTeamAway(String teamAway) {
        this.teamAway = teamAway;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public int getScore_teamHome() {
        return score_teamHome;
    }

    public void setScore_teamHome(int score_teamHome) {
        this.score_teamHome = score_teamHome;
    }

    public int getScore_teamAway() {
        return score_teamAway;
    }

    public void setScore_teamAway(int score_teamAway) {
        this.score_teamAway = score_teamAway;
    }

    public int getCourtId() {
        return courtId;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }

    private int score_teamAway;

    @Override
    public String toString() {
        return "Match{" +
                "teamHome='" + teamHome + '\'' +
                ", teamAway='" + teamAway + '\'' +
                ", date='" + date + '\'' +
                ", played=" + played +
                ", score_teamHome='" + score_teamHome + '\'' +
                ", score_teamAway='" + score_teamAway + '\'' +
                ", courtId='" + courtId + '\'' +
                '}';
    }

    private int courtId;

}
