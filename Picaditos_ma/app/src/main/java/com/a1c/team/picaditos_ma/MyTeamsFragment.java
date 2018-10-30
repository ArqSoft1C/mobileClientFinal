package com.a1c.team.picaditos_ma;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

import static android.content.ContentValues.TAG;


public class MyTeamsFragment extends Fragment {

    private ListView teams_list;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_teams, container, false);
        hideKeyboard(getActivity());
        teams_list = (ListView) v.findViewById(R.id.teams_list);

        Toast toast;
        if(!MainActivity.auxiliarData.isChoosingTeam()){
            toast = Toast.makeText(getActivity(), "Pulsa sobre un equipo para consultar su capitán", Toast.LENGTH_LONG);
        }else{
            toast = Toast.makeText(getActivity(), "Pulsa sobre un equipo para unirlo al partido", Toast.LENGTH_LONG);
        }
        toast.show();

        teams_list.setAdapter(new ListAdapter(getActivity(), R.layout.element_in_teams_list, LoginActivity.user.getTeamsOfUser()) {
            @Override
            public void onEntry(Object entrada, View view) {
                if (entrada != null) {
                    TextView top_text = (TextView) view.findViewById(R.id.top_text);
                    if (top_text != null)
                        top_text.setText(((Team) entrada).getName());

                    TextView bottom_text = (TextView) view.findViewById(R.id.bottom_text);
                    if (bottom_text != null)
                        bottom_text.setText(((Team) entrada).getMembers());

                    ImageView image = (ImageView) view.findViewById(R.id.image);
                    if (image != null)
                        image.setImageResource(R.drawable.ball);
                }
            }
        });


        teams_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> neighbour, View view, int pos, long id) {
                Team selected_team = (Team) neighbour.getItemAtPosition(pos);
                CharSequence text="";
                String team_away = selected_team.getId();
                if(MainActivity.auxiliarData.isChoosingTeam()){
                    joinMatch(team_away);
                }else {
                    if (selected_team.getCaptain_name().equals(LoginActivity.user.getUsername())) {
                        text = "Eres el capitán de este equipo";
                    } else {
                        text = "El capitán del equipo es: " + selected_team.getCaptain_name();
                    }
                    Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    protected boolean joinMatch(String team_away) {
        MyApolloClient.getMyApolloClient().mutate(
                UpdateMatchMutation.builder()
                        .id(MainActivity.auxiliarData.getMatchId())
                        .team_away_id(team_away)
                        .build())
                        .enqueue(new ApolloCall.Callback<UpdateMatchMutation.Data>() {
                            @Override
                            public void onResponse(@Nonnull Response<UpdateMatchMutation.Data> response) {//
                                if (response.data() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getActivity(), "Tu equipo se ha unido al partido!", Toast.LENGTH_SHORT).show();
                                            MainActivity.auxiliarData.setChoosingTeam(false);

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

