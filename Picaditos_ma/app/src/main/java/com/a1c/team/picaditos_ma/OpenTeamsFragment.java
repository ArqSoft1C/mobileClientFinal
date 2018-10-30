package com.a1c.team.picaditos_ma;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import static android.content.ContentValues.TAG;


public class OpenTeamsFragment extends Fragment {

    private ListView open_teams_list;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_open_teams, container, false);

        hideKeyboard(getActivity());

        open_teams_list = (ListView) v.findViewById(R.id.open_teams_list);

        Toast toast;

        if (MainActivity.dataProvider.getOpenTeams().size() > 0) {
            toast = Toast.makeText(getActivity(), "Pulsa sobre un equipo para unirte", Toast.LENGTH_LONG);
        } else {
            toast = Toast.makeText(getActivity(), "En este momento no hay equipos abiertos!", Toast.LENGTH_LONG);
        }
        toast.show();

        open_teams_list.setAdapter(new ListAdapter(getActivity(), R.layout.element_in_teams_list, MainActivity.dataProvider.getOpenTeams()) {
            @Override
            public void onEntry(Object entry, View view) {
                Team openTeam = (Team) entry;
                String bottomText = "Miembros actuales:\n" + ((Team) entry).getMembers();

                if (entry != null) {
                    TextView top_text = (TextView) view.findViewById(R.id.top_text);
                    if (top_text != null)
                        top_text.setText(((Team) entry).getName());

                    TextView bottom_text = (TextView) view.findViewById(R.id.bottom_text);
                    if (bottom_text != null)
                        bottom_text.setText(bottomText);

                    ImageView image = (ImageView) view.findViewById(R.id.image);
                    if (image != null)
                        image.setImageResource(R.drawable.ball);
                }
            }
        });

        open_teams_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> neighbour, View view, int pos, long id) {
                Team selected_open_team = (Team) neighbour.getItemAtPosition(pos);
                showMenu(view, selected_open_team.getId(), selected_open_team.getName(), open_teams_list.getAdapter());

            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void showMenu(View view, final String openTeam_id, final String opeTeam_name, final Adapter adapter) {

        PopupMenu menu = new PopupMenu(getActivity(), view);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.join_team:
                        joinTeam(openTeam_id, opeTeam_name, adapter);


                }
                return true;
            }
        });
        menu.inflate(R.menu.menu_join_team);
        menu.show();
    }


    protected boolean joinTeam(String team_id, final String team_name, Adapter adapter) {
        MyApolloClient.getMyApolloClient().mutate(
                AddPlayerToTeamMutation.builder()
                        .id(team_id)
                        .player_name(LoginActivity.user.getUsername())
                        .build())
                .enqueue(new ApolloCall.Callback<AddPlayerToTeamMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<AddPlayerToTeamMutation.Data> response) {//
                        if (response.data() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getActivity(), "Te has unido al equipo " + team_name + "!", Toast.LENGTH_SHORT).show();

                                }
                            });
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getActivity(), "Ha ocurrido un error. Intenta de nuevo.", Toast.LENGTH_SHORT).show();
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


