package com.a1c.team.picaditos_ma;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;


public class MyProfileFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);
        hideKeyboard(getActivity());

        TextView name_of_user = (TextView) v.findViewById(R.id.name);
        name_of_user.setText(LoginActivity.user.getNames());
        TextView user_name = (TextView) v.findViewById(R.id.user_name);
        user_name.setText(LoginActivity.user.getUsername());

        // Inflate the layout for this fragment
        return v;
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
