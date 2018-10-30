package com.a1c.team.picaditos_ma;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Session extends Application {


    public static String email;
    public static String username;
    public static String client;
    public static String token;

    public static String getClient() {
        return client;
    }

    public static void setClient(String client) {
        Session.client = client;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Session.token = token;
    }

    public static Set<String> getNamesOfTeams() {
        return namesOfTeams;
    }

    public static void setNamesOfTeams(Set<String> namesOfTeams) {
        Session.namesOfTeams = namesOfTeams;
    }

    public static String names;
    public static String last_names;
    public static String phone;
    public static String picture;
    public static ArrayList<Team> teamsOfUser;
    public static ArrayList<Match> matchesOfUser;
    public static HashSet<String> idsOfTeams;
    public static Set<String> namesOfTeams;

    public static HashSet<String> getIdsOfTeams() {
        return idsOfTeams;
    }

    public static void setIdsOfTeams(HashSet<String> idsOfTeams) {
        Session.idsOfTeams = idsOfTeams;
    }

    public void inititializeAll(){
        this.setTeamsOfUser(new ArrayList<Team>());
        this.setMessagesOfUser(new ArrayList<Message>());
        this.setMatchesOfUser(new ArrayList<Match>());
        this.setIdsOfTeams(new HashSet<String>());
        this.setNamesOfTeams(new HashSet<String>());
    }

    public static ArrayList<Message> getMessagesOfUser() {
        return messagesOfUser;
    }

    public void setMessagesOfUser(ArrayList<Message> messagesOfUser) {
        this.messagesOfUser = messagesOfUser;
    }

    public static ArrayList<Message>messagesOfUser;


    private static boolean updatedTeams;

    public static boolean isUpdatedTeams() {
        return updatedTeams;
    }

    public static void setUpdatedTeams(boolean updatedTeams) {
        Session.updatedTeams = updatedTeams;
    }

    public static ArrayList<Match> getMatchesOfUser() {
        return matchesOfUser;
    }

    public  void setMatchesOfUser(ArrayList<Match> matchesOfUser) {
        this.matchesOfUser = matchesOfUser;
    }

    public static String getTemp_name() {
        return temp_name;
    }

    public static void setTemp_name(String temp_name) {
        Session.temp_name = temp_name;
    }

    public static String temp_name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLast_names() {
        return last_names;
    }

    public void setLast_names(String last_names) {
        this.last_names = last_names;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public static ArrayList<Team> getTeamsOfUser() {
        return teamsOfUser;
    }

    public void setTeamsOfUser(ArrayList<Team> teamsOfUser) {
        this.teamsOfUser = teamsOfUser;
    }

    public void addteam(Team team){
        this.teamsOfUser.add(team);
    }
    public void addMessage(Message message){
        this.messagesOfUser.add(message);
    }
    public void addTeamId(String team_id){
        this.idsOfTeams.add(team_id);
    }

    public void addMatchofUser(Match match){
        this.matchesOfUser.add(match);
    }

    public void addNameOfTeam(String teamName){
        this.namesOfTeams.add(teamName);
    }

    public String getIdOfTeamByName(String team_name){
        String id = "";
        for(int i=0;i<this.teamsOfUser.size();i++){
            if(this.teamsOfUser.get(i).getName().equals(team_name)){
                id= teamsOfUser.get(i).getId();
            }

        }
        return id;
    }

}
