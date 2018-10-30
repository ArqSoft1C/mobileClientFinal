package com.a1c.team.picaditos_ma;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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


public class RequestMatchFragment extends Fragment {

    private ListView open_matches_list;
    private View v;
    Set set = LoginActivity.user.getNamesOfTeams();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_request_match, container, false);

        hideKeyboard(getActivity());

        open_matches_list = (ListView) v.findViewById(R.id.open_matches_list);

        Toast toast;

        if(MainActivity.dataProvider.getOpenMatches().size()>0){
            toast = Toast.makeText(getActivity(), "Pulsa sobre un partido para unirte con uno de tus equipos", Toast.LENGTH_LONG);
        }else{
            toast = Toast.makeText(getActivity(), "En este momento no hay partidos abiertos!", Toast.LENGTH_LONG);
        }
        toast.show();

        open_matches_list.setAdapter(new ListAdapter(getActivity(), R.layout.element_in_matches_list, MainActivity.dataProvider.getOpenMatches()) {
            @Override
            public void onEntry(Object entry, View view) {
                String topText= "";

                Match match  = (Match)entry;
                String date = match.getDate();

                int index = date.indexOf('T');
                String date_time = date.substring(0,index);
                String date_hour = date.substring(index+1,index+6);

                String date_formmated = "Fecha: "+date_time+"\nHora: "+date_hour;

                topText+="Abierto por:\n"+match.getTeamHome();
                

                if (entry != null) {
                    TextView top_text = (TextView) view.findViewById(R.id.top_text);
                    if (top_text != null)
                        top_text.setText(topText);

                    TextView bottom_text = (TextView) view.findViewById(R.id.bottom_text);
                    if (bottom_text != null)
                        bottom_text.setText(date_formmated);

                    ImageView image = (ImageView) view.findViewById(R.id.image);
                    if (image != null)
                        image.setImageResource(R.drawable.match_icon);
                }
            }
        });

        open_matches_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> neighbour, View view, int pos, long id) {
                Match selected_match = (Match) neighbour.getItemAtPosition(pos);
                CharSequence text ="";
                 if(set.contains(selected_match.getTeamHome())){
                    text ="Perteneces a este equipo. Espera a que otro equipo se una para jugar.";
                    Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);
                    toast.show();
                }else{
                     showMenu(view,selected_match.getId());
                 }
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void showMenu (View view, final int match_id)
    {

        PopupMenu menu = new PopupMenu (getActivity(), view);
        menu.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener ()
        {
            @Override
            public boolean onMenuItemClick (MenuItem item)
            {
                int id = item.getItemId();
                switch (id)
                {
                    case R.id.join_match:

                        MainActivity.auxiliarData.setChoosingTeam(true);
                        MainActivity.auxiliarData.setMatchId(match_id);
                        FragmentManager fm = getFragmentManager();
                        ConstraintLayout contentView = (ConstraintLayout) getActivity().findViewById(R.id.main_container);

                        fm.beginTransaction()
                                .replace(contentView.getId(), new MyTeamsFragment())
                                .addToBackStack(null)
                                .commit();
                }
                return true;
            }
        });
        menu.inflate (R.menu.menu_join_match);
        menu.show();
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


