package com.a1c.team.picaditos_ma;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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


public class CourtsFragment extends Fragment {
    private ListView courts_list;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_courts, container, false);

        hideKeyboard(getActivity());

        courts_list = (ListView) v.findViewById(R.id.courts_list);

        courts_list.setAdapter(new ListAdapter(getActivity(), R.layout.element_in_courts_list, MainActivity.dataProvider.getCourts()) {
            @Override
            public void onEntry(Object entry, View view) {

                Court court = ((Court) entry);
                String name = court.getName();
                String info = "Id: " + court.getId() + "\nDirección: " + court.getAddress() + "\nPrecio (hora): $" + court.getPrice_hour();

                if (entry != null) {
                    TextView top_text = (TextView) view.findViewById(R.id.top_text);
                    if (top_text != null)
                        top_text.setText(name);

                    TextView bottom_text = (TextView) view.findViewById(R.id.bottom_text);
                    if (bottom_text != null)
                        bottom_text.setText(info);

                    ImageView image = (ImageView) view.findViewById(R.id.image);
                    if (image != null)
                        image.setImageResource(R.drawable.court_icon);
                }
            }
        });

        courts_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> neighbour, View view, int pos, long id) {
                Court selected_court = (Court) neighbour.getItemAtPosition(pos);
                showMenu(view, selected_court.getId());

            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showMenu(View view, final int court_id) {

        PopupMenu menu = new PopupMenu(getActivity(), view);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.view_court_in_map:

                        MainActivity.auxiliarData.setViewingCourt(true);
                        MainActivity.auxiliarData.setCourtId(court_id);
                        FragmentManager fm = getFragmentManager();
                        ConstraintLayout contentView = (ConstraintLayout) getActivity().findViewById(R.id.main_container);
                        fm.beginTransaction()
                                .replace(contentView.getId(), new CourtsInMapFragment())
                                .addToBackStack(null)
                                .commit();
                }
                return true;
            }
        });
        menu.inflate(R.menu.menu_view_court_in_map);
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