package com.a1c.team.picaditos_ma;

import java.util.ArrayList;
import java.util.HashSet;

public class DataProvider {
    private ArrayList<User> users;
    private ArrayList<Team> teams;
    private ArrayList<Match> matches;
    private ArrayList<Team> openTeams;
    private ArrayList<Match> matchesIds;
    private static String temp_name;
    private boolean openTeamsModified;
    private ArrayList<Match> openMatches;
    private ArrayList<Court> courts;
    private HashSet<String> idsOfCourts;

    public DataProvider(ArrayList<User> users, ArrayList<Team> teams, ArrayList<Match> matches, ArrayList<Team> openTeams, ArrayList<Match> matchesIds, ArrayList<Match> openMatches, ArrayList<Court> courts, HashSet<String> idsOfCourts) {
        this.users = users;
        this.teams = teams;
        this.matches = matches;
        this.openTeams = openTeams;
        this.matchesIds = matchesIds;
        this.openMatches = openMatches;
        this.courts = courts;
        this.idsOfCourts = idsOfCourts;
    }

    public HashSet<String> getIdsOfCourts() {
        return idsOfCourts;
    }

    public void setIdsOfCourts(HashSet<String> idsOfCourts) {
        this.idsOfCourts = idsOfCourts;
    }

    public boolean isOpenTeamsModified() {
        return openTeamsModified;
    }

    public void setOpenTeamsModified(boolean openTeamsModified) {
        this.openTeamsModified = openTeamsModified;
    }

    public ArrayList<Match> getMatchesIds() {
        return matchesIds;
    }

    public void setMatchesIds(ArrayList<Match> matchesIds) {
        this.matchesIds = matchesIds;
    }

    public static String getTemp_name() {
        return temp_name;
    }

    public static void setTemp_name(String temp_name) {
        DataProvider.temp_name = temp_name;
    }

    public ArrayList<Match> getOpenMatches() {
        return openMatches;
    }

    public void setOpenMatches(ArrayList<Match> openMatches) {
        this.openMatches = openMatches;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Team> getOpenTeams() {
        return openTeams;
    }

    public void setOpenTeams(ArrayList<Team> openTeams) {
        this.openTeams = openTeams;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }

    public ArrayList<Court> getCourts() {
        return courts;
    }

    public void setCourts(ArrayList<Court> courts) {
        this.courts = courts;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void addTeam(Team team) {
        this.teams.add(team);
    }

    public void addMatch(Match match) {
        this.matches.add(match);
    }

    public void addCourt(Court court) {
        this.courts.add(court);
    }

    public void addOpenTeam(Team openTeam) {
        this.openTeams.add(openTeam);
    }

    public Court getCourtById(int courtId) {
        return this.courts.get(courtId - 1);
    }

    public void addIdOfCourt(int id){
        String idAsString = Integer.toString(id);
        this.idsOfCourts.add(idAsString);
    }

    public void initializeAll() {
        this.setUsers(new ArrayList<User>());
        this.setTeams(new ArrayList<Team>());
        this.setMatches(new ArrayList<Match>());
        this.setOpenMatches(new ArrayList<Match>());
        this.setMatchesIds(new ArrayList<Match>());
        this.setCourts(new ArrayList<Court>());
        this.setOpenTeams(new ArrayList<Team>());
        this.setIdsOfCourts(new HashSet<String>());
        this.setOpenTeamsModified(false);
        this.setTemp_name("");
    }
}
