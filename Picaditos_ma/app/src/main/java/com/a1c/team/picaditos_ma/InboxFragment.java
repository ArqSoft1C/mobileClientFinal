package com.a1c.team.picaditos_ma;

import android.app.Activity;
import android.graphics.Color;
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
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class InboxFragment extends Fragment {
    private ListView messages_list;
    private View v;
    private final static String RECEIVER = "De: ";
    private final static String SENDER = "Para: ";
    private final static String SENT = "ENVIADO";
    private final static String RECEIVED = "RECIBIDO";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_inbox, container, false);

        hideKeyboard(getActivity());

        messages_list = (ListView) v.findViewById(R.id.messages_list);

        Toast toast = Toast.makeText(getActivity(), "Pulsa sobre un mensaje recibido para responderlo", Toast.LENGTH_LONG);
        toast.show();

        messages_list.setAdapter(new ListAdapter(getActivity(), R.layout.element_in_messages_list, LoginActivity.user.getMessagesOfUser()) {
            @Override
            public void onEntry(Object entry, View view) {

                String origin = "";
                String type = "";
                Message message = (Message) entry;
                if (message.getReceiver().equals(LoginActivity.user.getUsername())) {
                    origin = RECEIVER + message.getSender();
                    type = RECEIVED;
                } else {
                    origin = SENDER + message.getReceiver();
                    type = SENT;
                }

                if (entry != null) {
                    TextView top_text = (TextView) view.findViewById(R.id.top_text);
                    if (top_text != null)
                        top_text.setText(origin);

                    TextView bottom_text = (TextView) view.findViewById(R.id.bottom_text);
                    if (bottom_text != null)
                        bottom_text.setText(message.getContent());

                    TextView type_text = (TextView) view.findViewById(R.id.type);
                    if (type_text != null)
                        if (type.equals(RECEIVED)) {
                            type_text.setTextColor(Color.rgb(41, 163, 41));
                        } else {
                            type_text.setTextColor(Color.rgb(153, 0, 0));
                        }
                    type_text.setText(type);


                }
            }
        });

        messages_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> neighbour, View view, int pos, long id) {
                Message selected_message = (Message) neighbour.getItemAtPosition(pos);

                if (selected_message.getReceiver().equals(LoginActivity.user.getUsername())) {
                    showMenu(view, selected_message.getSender());
                }
            }
        });
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showMenu(View view, final String username) {

        PopupMenu menu = new PopupMenu(getActivity(), view);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.answer_message:

                        MainActivity.auxiliarData.setTemporal_message_username(username);
                        FragmentManager fm = getFragmentManager();
                        ConstraintLayout contentView = (ConstraintLayout) getActivity().findViewById(R.id.main_container);
                        fm.beginTransaction()
                                .replace(contentView.getId(), new SendMessageFragment())
                                .addToBackStack(null)
                                .commit();
                }
                return true;
            }
        });
        menu.inflate(R.menu.menu_message);
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
