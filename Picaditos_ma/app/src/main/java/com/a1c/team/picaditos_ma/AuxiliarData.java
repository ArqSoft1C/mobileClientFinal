package com.a1c.team.picaditos_ma;

public class AuxiliarData {

    private String temporal_message_username;
    private boolean choosingTeam;
    private boolean viewingCourt;
    private int courtId;
    private int matchId;

    public int getCourtId() {
        return courtId;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }

    public boolean isViewingCourt() {
        return viewingCourt;
    }

    public void setViewingCourt(boolean viewingCourt) {
        this.viewingCourt = viewingCourt;
    }

    public int getMatchId() {

        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public boolean isChoosingTeam() {
        return choosingTeam;
    }

    public void setChoosingTeam(boolean choosingTeam) {
        this.choosingTeam = choosingTeam;
    }

    public String getTemporal_message_username() {
        return temporal_message_username;
    }

    public void setTemporal_message_username(String temporal_message_username) {
        this.temporal_message_username = temporal_message_username;
    }

    public void initializeAll() {
        this.temporal_message_username = "";
        this.choosingTeam = false;
        this.viewingCourt = false;
        this.courtId = 0;
        this.matchId = 0;
    }
}
