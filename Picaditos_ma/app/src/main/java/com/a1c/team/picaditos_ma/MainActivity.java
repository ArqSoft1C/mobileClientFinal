package com.a1c.team.picaditos_ma;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import static android.content.ContentValues.TAG;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static DataProvider dataProvider = new DataProvider(new ArrayList<User>(), new ArrayList<Team>(), new ArrayList<Match>(), new ArrayList<Team>(), new ArrayList<Match>(), new ArrayList<Match>(), new ArrayList<Court>(),new HashSet<String>());
    public static AuxiliarData auxiliarData = new AuxiliarData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getData();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_container, new MyProfileFragment()).commit();
    }


    public static void getData() {
        LoginActivity.user.inititializeAll();
        clearAuxiliarData();
        clearDataProvider();
        getUserMessages();
        getCourts();
        getUserTeams();
        while (LoginActivity.user.getTeamsOfUser().size() == 0) {
            continue;
        }
        getMatchesIds();
        while (dataProvider.getMatchesIds().size() == 0) {
            continue;
        }
        getMatches();
        while (dataProvider.getMatches().size() == 0) {
            continue;
        }
        getMatchesOfUser();
        getOpenMatches();
        getOpenTeams();
        while (!dataProvider.isOpenTeamsModified()) {
            continue;
        }

    }


    public static void clearDataProvider() {
        dataProvider.initializeAll();
    }

    public static void clearAuxiliarData() {
        auxiliarData.initializeAll();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();
        getData();

        if (id == R.id.nav_my_teams) {
            fragmentManager.beginTransaction().replace(R.id.main_container, new MyTeamsFragment()).commit();
        } else if (id == R.id.nav_create_team) {
            fragmentManager.beginTransaction().replace(R.id.main_container, new CreateTeamFragment()).commit();
        } else if (id == R.id.nav_my_matches) {
            fragmentManager.beginTransaction().replace(R.id.main_container, new MyMatchesFragment()).commit();
        } else if (id == R.id.nav_request_match) {
            fragmentManager.beginTransaction().replace(R.id.main_container, new RequestMatchFragment()).commit();
        } else if (id == R.id.nav_courts) {
            fragmentManager.beginTransaction().replace(R.id.main_container, new CourtsFragment()).commit();
        } else if (id == R.id.nav_inbox) {
            fragmentManager.beginTransaction().replace(R.id.main_container, new InboxFragment()).commit();
        } else if (id == R.id.nav_send_message) {
            fragmentManager.beginTransaction().replace(R.id.main_container, new SendMessageFragment()).commit();
        } else if (id == R.id.nav_my_profile) {
            fragmentManager.beginTransaction().replace(R.id.main_container, new MyProfileFragment()).commit();
        } else if (id == R.id.nav_create_match) {
            fragmentManager.beginTransaction().replace(R.id.main_container, new CreateMatchFragment()).commit();
        } else if (id == R.id.nav_open_teams) {
            fragmentManager.beginTransaction().replace(R.id.main_container, new OpenTeamsFragment(),"OPEN_TEAMS").commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void getUserTeams() {
        final ArrayList<Team> playerTeams = new ArrayList<>();

        final TeamByPlayerQuery teamsByPlayer = TeamByPlayerQuery.builder()
                .player_name(LoginActivity.user.getUsername())
                .build();

        ApolloCall<TeamByPlayerQuery.Data> teamsCall = MyApolloClient.getMyApolloClient()
                .query(teamsByPlayer);

        teamsCall.enqueue(new ApolloCall.Callback<TeamByPlayerQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<TeamByPlayerQuery.Data> response) {
                TeamByPlayerQuery.Data teams = response.data();

                for (int i = 0; i < teams.teamByPlayer.size(); i++) {
                    String squad = "";
                    TeamByPlayerQuery.TeamByPlayer team = teams.teamByPlayer.get(i);
                    for (int j = 0; j < team.squad.size(); j++) {
                        if (j != team.squad.size() - 1) {
                            squad += team.squad.get(j) + ", ";
                        } else {
                            squad += team.squad.get(j);
                        }
                    }
                    Log.d(TAG, "----------------------EQUIPOS HASTA AHORA :" + playerTeams.toString());
                    LoginActivity.user.addteam(new Team(team.id, team.name, team.captain, squad));
                    LoginActivity.user.addTeamId(team.id);
                    LoginActivity.user.addNameOfTeam(team.name);

                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }


    public static void getUserMessages() {
        final ArrayList<Message> playerMessages = new ArrayList<>();

        final MessagesByUserQuery userMessages = MessagesByUserQuery.builder()
                .username(LoginActivity.user.getUsername())
                .build();

        ApolloCall<MessagesByUserQuery.Data> teamsCall = MyApolloClient.getMyApolloClient()
                .query(userMessages);

        teamsCall.enqueue(new ApolloCall.Callback<MessagesByUserQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<MessagesByUserQuery.Data> response) {
                MessagesByUserQuery.Data messages = response.data();

                for (int i = 0; i < messages.messageByUser.size(); i++) {
                    MessagesByUserQuery.MessageByUser message = messages.messageByUser.get(i);
                    LoginActivity.user.addMessage(new Message(message.user1, message.user2, message.subject, message.content));

                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }

    public static void getCourts() {
        final ArrayList<Message> courts = new ArrayList<>();

        final GetAllCourtsQuery allCourts = GetAllCourtsQuery.builder()
                .build();

        ApolloCall<GetAllCourtsQuery.Data> courtsCall = MyApolloClient.getMyApolloClient()
                .query(allCourts);

        courtsCall.enqueue(new ApolloCall.Callback<GetAllCourtsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<GetAllCourtsQuery.Data> response) {
                GetAllCourtsQuery.Data courts = response.data();

                for (int i = 0; i < courts.allCourts.size(); i++) {
                    GetAllCourtsQuery.AllCourt court = courts.allCourts.get(i);
                    dataProvider.addCourt(new Court(court.id, court.name, court.latitude, court.longitude, court.address, court.price_hour));
                    dataProvider.addIdOfCourt(court.id);
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }

    public static void getMatchesIds() {
        final ArrayList<Match> matches_ids = new ArrayList<>();

        final GetAllMatchesQuery allMatches = GetAllMatchesQuery.builder()
                .build();

        ApolloCall<GetAllMatchesQuery.Data> matchesCall = MyApolloClient.getMyApolloClient()
                .query(allMatches);

        matchesCall.enqueue(new ApolloCall.Callback<GetAllMatchesQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<GetAllMatchesQuery.Data> response) {
                GetAllMatchesQuery.Data matches = response.data();

                for (int i = 0; i < matches.allMatches.size(); i++) {
                    GetAllMatchesQuery.AllMatch match = matches.allMatches.get(i);
                    matches_ids.add(new Match(match.id, match.team_home_id, match.team_away_id, match.date, match.played, match.score_home, match.score_away, match.court_id));

                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });

        dataProvider.setMatchesIds(matches_ids);
    }

    public static void getOpenMatches() {

        ArrayList<Match> openMatches = new ArrayList<>();

        for (int i = 0; i < dataProvider.getMatches().size(); i++) {
            if (dataProvider.getMatches().get(i).getTeamAway() == null) {
                openMatches.add(dataProvider.getMatches().get(i));
            }
        }

        dataProvider.setOpenMatches(openMatches);

    }

    public static void getMatches() {
        ArrayList<Match> matches = dataProvider.getMatchesIds();
        ArrayList<Match> matches_change = new ArrayList<>();

        for (int i = 0; i < matches.size(); i++) {
            String team_home = "";
            String team_away = null;
            for (int j = 0; j < 2; j++) {
                dataProvider.setTemp_name(null);
                if (j == 0) {
                    setMatch(matches.get(i).getTeamHome());
                    while (dataProvider.getTemp_name() == null) {

                    }
                    team_home = dataProvider.getTemp_name();

                } else {
                    if (matches.get(i).getTeamAway() != null) {
                        setMatch(matches.get(i).getTeamAway());
                        while (dataProvider.getTemp_name() == null) {

                        }
                        team_away = dataProvider.getTemp_name();
                    }
                }

            }
            matches_change.add(new Match(matches.get(i).getId(), team_home, team_away, matches.get(i).getDate(), matches.get(i).isPlayed(),
                    matches.get(i).getScore_teamHome(), matches.get(i).getScore_teamAway(), matches.get(i).getCourtId()));


        }
        dataProvider.setMatches(matches_change);
    }

    public static void setMatch(String id) {
        final String name = "";

        final TeamByIdQuery teams = TeamByIdQuery.builder()
                .id(id)
                .build();

        ApolloCall<TeamByIdQuery.Data> teamsCall = MyApolloClient.getMyApolloClient()
                .query(teams);

        teamsCall.enqueue(new ApolloCall.Callback<TeamByIdQuery.Data>() {

            @Override
            public void onResponse(@Nonnull Response<TeamByIdQuery.Data> response) {
                dataProvider.setTemp_name(response.data().teamById.name);
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });

    }

    public static void getMatchesOfUser() {

        for (int i = 0; i < dataProvider.getMatches().size(); i++) {
            Match match = dataProvider.getMatchesIds().get(i);
            Log.d(TAG, "---EQUIPO: " + match.getTeamHome());
            if ((LoginActivity.user.getIdsOfTeams().contains(match.getTeamHome())) || LoginActivity.user.getIdsOfTeams().contains(match.getTeamAway())) {
                LoginActivity.user.addMatchofUser(dataProvider.getMatches().get(i));
            }

        }
    }

    public static void getOpenTeams() {
        final ArrayList<Team> openTeams = new ArrayList<>();

        final GetOpenTeamsQuery openTeamsQuery = GetOpenTeamsQuery.builder()
                .build();

        ApolloCall<GetOpenTeamsQuery.Data> openTeamsCall = MyApolloClient.getMyApolloClient()
                .query(openTeamsQuery);

        openTeamsCall.enqueue(new ApolloCall.Callback<GetOpenTeamsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<GetOpenTeamsQuery.Data> response) {
                GetOpenTeamsQuery.Data open_teams = response.data();

                for (int i = 0; i < open_teams.openTeams.size(); i++) {
                    String squad = "";
                    GetOpenTeamsQuery.OpenTeam open_team = open_teams.openTeams.get(i);
                    for (int j = 0; j < open_team.squad.size(); j++) {
                        if (j != open_team.squad.size() - 1) {
                            squad += open_team.squad.get(j) + ", ";
                        } else {
                            squad += open_team.squad.get(j);
                        }
                    }

                    Set openTeamsSet = LoginActivity.user.getNamesOfTeams();

                    if (!openTeamsSet.contains(open_team.name)) {
                        dataProvider.addOpenTeam(new Team(open_team.id, open_team.name, open_team.captain, squad));
                    }

                }

                dataProvider.setOpenTeamsModified(true);
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
    }

   public boolean logOut(){

        MyApolloClient.getMyApolloClient().mutate(
                LogOutMutation.builder()
                        .client(LoginActivity.user.getClient())
                        .uid(LoginActivity.user.getEmail())
                        .token(LoginActivity.user.getToken())
                        .build())
                .enqueue(new ApolloCall.Callback<LogOutMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<LogOutMutation.Data> response) {
                        if (response.errors()!= null){
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);

                        }else{
                            Log.d(TAG, "Ha fallado el intento de cierre de sesión.");

                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.d(TAG, "OnFailure: ==============================" + e);
                    }
                });
        return true;



    }


}