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

import static android.content.ContentValues.TAG;


public class MyMatchesFragment extends Fragment { 
    
    private ListView matches_list;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_matches, container, false);

        hideKeyboard(getActivity());

        matches_list = (ListView) v.findViewById(R.id.matches_list);


        matches_list.setAdapter(new ListAdapter(getActivity(), R.layout.element_in_matches_list, LoginActivity.user.getMatchesOfUser()) {
            @Override
            public void onEntry(Object entry, View view) {
                String topText= "";
                String info ="";

                Match match  = (Match)entry;
                String date = match.getDate();

                int index = date.indexOf('T');
                String date_time = date.substring(0,index);
                String date_hour = date.substring(index+1,index+6);

                String date_formmated = "Fecha: "+date_time+"\nHora: "+date_hour;

                if(match.getTeamAway()!=null){
                    topText+=match.getTeamHome()+" vs. "+match.getTeamAway();
                }else{
                    topText+=match.getTeamHome()+"\n(Partido Abierto)";
                }

                if(match.isPlayed()==true){
                    info+="Jugado\n"+date_formmated+"\n"+"Resultado:\n"+match.getScore_teamHome()+
                            "       -       "+match.getScore_teamAway();
                }else {
                    info += date_formmated;
                }

                if (entry != null) {
                    TextView top_text = (TextView) view.findViewById(R.id.top_text);
                    if (top_text != null)
                        top_text.setText(topText);

                    TextView bottom_text = (TextView) view.findViewById(R.id.bottom_text);
                    if (bottom_text != null)
                        bottom_text.setText(info);

                    ImageView image = (ImageView) view.findViewById(R.id.image);
                    if (image != null)
                        image.setImageResource(R.drawable.match_icon);
                }
            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

