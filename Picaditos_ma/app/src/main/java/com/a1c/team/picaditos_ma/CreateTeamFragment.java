package com.a1c.team.picaditos_ma;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;


public class CreateTeamFragment extends Fragment {

    private EditText team_name;
    private EditText team_captain;
    private EditText team_members;
    private CreateTeamFragment.CreateTeamTask createTeamTask = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_team, container, false);
        team_name = (EditText) v.findViewById(R.id.team_name);
        team_captain = (EditText) v.findViewById(R.id.team_captain);
        team_members = (EditText) v.findViewById(R.id.team_members);

        Button createTeamButton = (Button) v.findViewById(R.id.create_team);
        createTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyboard(getActivity());
                attemptCreateTeam();
                team_name.getText().clear();
                team_captain.getText().clear();
                team_members.getText().clear();
            }
        });
        // Inflate the layout for this fragment
        return v;
    }


    private void attemptCreateTeam() {
        if (createTeamTask != null) {
            return;
        }

        // Reset errors.
        team_name.setError(null);
        team_captain.setError(null);
        team_members.setError(null);

        // Store values at the time of the login attempt.
        String name = team_name.getText().toString();
        String captain = team_captain.getText().toString();
        String members = team_members.getText().toString();
        List<String> membersAsList = new LinkedList<>();

        members = members.replace(" ", "");
        String[] members_splitted = members.split(",");

        for (int i = 0; i < members_splitted.length; i++) {
            membersAsList.add(members_splitted[i]);
        }

        boolean cancel = false;
        View focusView = null;

        if (cancel) {
            focusView.requestFocus();
        } else {
            createTeamTask = new CreateTeamTask(name, captain, membersAsList);
            createTeamTask.execute((Void) null);
        }
    }

    public class CreateTeamTask extends AsyncTask<Void, Void, Boolean> {
        Boolean status = false;
        Boolean ldap = false;
        private final String team_name;
        private final String team_captain;
        private final List<String> team_members;
        final String TAG = "CreateTeamFragment";

        CreateTeamTask(String teamName, String teamCaptain, List<String> teamMembers) {
            team_name = teamName;
            team_captain = teamCaptain;
            team_members = teamMembers;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            createTeam();
            status = true;
            return status;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            createTeamTask = null;
            Log.d(TAG, "postExecute : ");
        }

        @Override
        protected void onCancelled() {
            createTeamTask = null;
            Log.d(TAG, "onCancelled: ");
        }

        protected boolean createTeam() {
            MyApolloClient.getMyApolloClient().mutate(
                    CreateTeamMutation.builder()
                            .name(team_name)
                            .captain(team_captain)
                            .sport("futbol")
                            .squad(team_members)
                            .build())
                    .enqueue(new ApolloCall.Callback<CreateTeamMutation.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<CreateTeamMutation.Data> response) {
//                            Log.d(TAG, "answer: "+response.data().auth().answer());
                            if (response.data() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getActivity(), "El equipo ha sido creado!", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getActivity(), "El equipo no ha sido creado! Intente de nuevo.", Toast.LENGTH_SHORT).show();
                                    }
                                });
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
