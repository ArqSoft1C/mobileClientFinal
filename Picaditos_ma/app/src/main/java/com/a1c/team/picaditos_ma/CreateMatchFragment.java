package com.a1c.team.picaditos_ma;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;


public class CreateMatchFragment extends Fragment {

    private AutoCompleteTextView team_home;
    private AutoCompleteTextView court_id;
    private ImageButton date;
    private ImageButton hour;
    private EditText date_selected;
    private EditText hour_selected;

    private String[] teamsOfUser = LoginActivity.user.getNamesOfTeams().toArray(new String[0]);
    private String[] courtsIds = MainActivity.dataProvider.getIdsOfCourts().toArray(new String[0]);

    private CreateMatchFragment.CreateMatchTask createMatchTask = null;
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    public final Calendar c = Calendar.getInstance();
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_create_match, container, false);

        date = (ImageButton) v.findViewById(R.id.obtain_date);
        hour = (ImageButton) v.findViewById(R.id.obtain_hour);

        date_selected = (EditText) v.findViewById(R.id.selected_date);
        hour_selected = (EditText) v.findViewById(R.id.selected_hour);


        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.obtain_date:
                        showDatePickerDialog();
                        break;
                }
                //do your operation here
                // this will be called whenever user click anywhere in Fragment
            }
        });

        hour.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.obtain_hour:
                        obtenerHora();
                        break;
                }
                //do your operation here
                // this will be called whenever user click anywhere in Fragment
            }
        });

        ArrayAdapter<String> team_home_adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, teamsOfUser);
        team_home = (AutoCompleteTextView) v.findViewById(R.id.match_home);
        team_home.setThreshold(1);
        team_home.setAdapter(team_home_adapter);

        ArrayAdapter<String> court_id_adapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.select_dialog_item, courtsIds);

        court_id = (AutoCompleteTextView) v.findViewById(R.id.match_court);
        court_id.setThreshold(1);
        court_id.setAdapter(court_id_adapter);

        Button createMatchButton = (Button) v.findViewById(R.id.create_match);
        createMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyboard(getActivity());
                attemptCreateMatch();

            }
        });

        // Inflate the layout for this fragment
        return v;
    }


    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = day + " / " + (month + 1) + " / " + year;
                date_selected.setText(selectedDate);
            }
        });
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private void obtenerHora() {
        TimePickerDialog recogerHora = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if (hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                hour_selected.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, false);

        recogerHora.show();
    }


    private void attemptCreateMatch() {
        if (createMatchTask != null) {
            return;
        }

        // Reset errors.
        team_home.setError(null);
        court_id.setError(null);
        date_selected.setError(null);
        hour_selected.setError(null);

        boolean cancel = false;
        View focusView = null;

        String teamHome = team_home.getText().toString();
        String courtId = court_id.getText().toString();
        String matchDate = date_selected.getText().toString();
        String matchHour = hour_selected.getText().toString();


        if (TextUtils.isEmpty(teamHome)) {
            team_home.setError("Campo requerido");
            focusView = date_selected;
            cancel = true;
        }
        if (!LoginActivity.user.getNamesOfTeams().contains(teamHome) && !TextUtils.isEmpty(teamHome)) {
            team_home.setError("No perteneces a este equipo");
            focusView = team_home;
            cancel = true;
        }
        if (TextUtils.isEmpty(courtId)) {
            court_id.setError("Campo requerido");
            focusView = date_selected;
            cancel = true;
        }
        if (!MainActivity.dataProvider.getIdsOfCourts().contains(courtId) && !TextUtils.isEmpty(courtId)) {
            court_id.setError("La cancha no existe");
            focusView = court_id;
            cancel = true;
        }
        if (TextUtils.isEmpty(matchDate)) {
            date_selected.setError("Selecciona una fecha para el partido");
            focusView = date_selected;
            cancel = true;
        }
        if (TextUtils.isEmpty(matchHour)) {
            hour_selected.setError("Selecciona una hora para el partido");
            focusView = hour_selected;
            cancel = true;
        }

        String final_date ="";

        if (cancel != true) {
            //modify date and hour format

            matchDate = matchDate.replace(" ", "");
            String[] date = matchDate.split("/");
            String[] hour = matchHour.split(" ");

            String year = date[2];
            String month = "";
            if (date[1].length() == 1) {
                month += "0";
            }
            month += date[1];
            String day = "";
            if (date[0].length() == 1) {
                day += "0";
            }
            day += date[0];

            final_date += year + "-" + month + "-" + day + "T" + hour[0] + ":00";
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            createMatchTask = new CreateMatchTask(teamHome, courtId, final_date);
            createMatchTask.execute((Void) null);
        }
    }

    public class CreateMatchTask extends AsyncTask<Void, Void, Boolean> {
        Boolean status = false;
        Boolean ldap = false;

        private final String team_home;
        private final String court_id;
        private final String match_date;


        public CreateMatchTask(String team_home, String court_id, String match_date) {
            this.team_home = team_home;
            this.court_id = court_id;
            this.match_date = match_date;
        }

        final String TAG = "CreateTeamFragment";


        @Override
        protected Boolean doInBackground(Void... params) {
            createMatch();
            status = true;
            return status;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            createMatchTask = null;
            Log.d(TAG, "postExecute : ");
        }

        @Override
        protected void onCancelled() {
            createMatchTask = null;
            Log.d(TAG, "onCancelled: ");
        }

        protected boolean createMatch() {
            MyApolloClient.getMyApolloClient().mutate(
                    CreateMatchMutation.builder()
                            .team_home_id(LoginActivity.user.getIdOfTeamByName(team_home))
                            .court_id(Integer.parseInt(court_id))
                            .played(false)
                            .score_away(0)
                            .score_home(0)
                            .date(match_date)
                            .build())
                    .enqueue(new ApolloCall.Callback<CreateMatchMutation.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<CreateMatchMutation.Data> response) {
//                            Log.d(TAG, "answer: "+response.data().auth().answer());
                            if (response.data() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getActivity(), "El partido ha sido creado!", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getActivity(), "El partido no ha sido creado! Intente de nuevo.", Toast.LENGTH_SHORT).show();
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
