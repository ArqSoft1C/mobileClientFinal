package com.a1c.team.picaditos_ma;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class MyProfileFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);

        TextView name_of_user = (TextView) v.findViewById(R.id.name);
        name_of_user.setText(LoginActivity.user.getNames());
        TextView user_name = (TextView) v.findViewById(R.id.user_name);
        user_name.setText(LoginActivity.user.getUsername());

        // Inflate the layout for this fragment
        return v;
    }
}
