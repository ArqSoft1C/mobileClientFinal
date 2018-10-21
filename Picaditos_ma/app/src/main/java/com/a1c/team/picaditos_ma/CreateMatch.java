package com.a1c.team.picaditos_ma;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Nonnull;

public class CreateMatch extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    int year, month, day, hour, minute;
    int fYear, fMonth, fDay, fHour, fMinute;

 //cambiar tipo de dato si va a mostrar algo mas que string

    ArrayList<String> equipos1 = new ArrayList<String>();
    ArrayList<String> equipos2 = new ArrayList<String>();
    ArrayList<String> canchas = new ArrayList<String>();

    String fechayhora;
    int team1;
    int team2;
    String aux, name;


    Spinner teamSelect;
    Spinner teamSelect2;
    TextView dateTime;
    Button setDateTime;
    Spinner courtSelect;
    Button create;

    int courtID;
    int teamID;
    int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_match);

        teamSelect = (Spinner) findViewById(R.id.s_selectTeam);
        teamSelect2 = (Spinner) findViewById(R.id.s_selectTeam2);
        dateTime = (TextView) findViewById(R.id.t_datetime);
        setDateTime = (Button) findViewById(R.id.b_setDate);
        courtSelect = (Spinner) findViewById(R.id.s_selectCourt);
        create = (Button) findViewById(R.id.b_create);
        fechayhora = "";
        team1 = -1;
        team2 = -1;
        userid = 1; //cambiar por el valor recibido de otras actividades (login) o sqlite
        name = "john";
        aux="";

        setDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c  = Calendar.getInstance();
                year=c.get(Calendar.YEAR);
                month=c.get(Calendar.MONTH);
                day=c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateMatch.this, CreateMatch.this, year, month, day);
                datePickerDialog.show();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()){
                    saveMatch();
                }else {
                    Toast.makeText(CreateMatch.this, "Ingrese todos los datos", Toast.LENGTH_SHORT).show();
                }
                }

        });

    }

    public void saveMatch() {
        //mutation createMatch
        //equipolocal team1
        //equipoenemigo team2
        //fechayhora fechayhora
        //cancha courtID
//        onresponse
//          id(response.data() !=null){
//                mostrar toast diciendo que se ha creado el partido
//                llamar la actividad a mostart despues de crear un partido
//    }

//        onfailure
//                toast diciendo que ocurrio un error, intente de nuevo
//
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        fYear = year;
        fMonth = month;
        fDay = dayOfMonth;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateMatch.this, CreateMatch.this, hour, minute, true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        fHour = hourOfDay;
        fMinute = minute;

        dateTime.setText(fYear + "-" + fMonth + "-" + fDay + " " + fHour + ":" + fMinute + ":00");
        fechayhora = fYear + "-" + fMonth + "-" + fDay + " " + fHour + ":" + fMinute + ":00";
    }


    private void getTeamsByUser(){

        MyApolloClient.getMyApolloClient().query(
                TeamByPlayerQuery.builder()
                .player_name(name).build())
        .enqueue(new ApolloCall.Callback<TeamByPlayerQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<TeamByPlayerQuery.Data> response) {
                if (response.data()!=null) {
                    for (int i = 0; i < response.data().team().size; i++) {
                        aux = (response.data().team().get(i).name());
                        equipos1.add(aux);
                    }
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });
                 }


//        onresponse query teamsbyplayer
//                if(response.data()!=null){
//                    for int i=0;i<response.data().team().size();i++){
//                        aux = (response.data().team().get(i).name());
//                        equipos1.add(aux);
//                    }
//                }
    }

    private void getTeams(){
//        onresponse allteams
//        if(response.data()!=null){
//            for int i=0;i<response.data().team().size();i++){
//                aux = (response.data().team().get(i).name());
//                equipos2.add(aux);
//            }
//        }
    }

    public void spinnerSelectTeam1(){
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(CreateMatch.this, android.R.layout.simple_spinner_item, equipos1 );
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CreateMatch.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                teamSelect.setAdapter(adapter1);
            }
        });

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CreateMatch.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                teamSelect.setAdapter(adapter1);
            }
        });

        courtID = -1;
        teamSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                team1 = position;
                CreateMatch.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(CreateMatch.this, "selected court: "+ canchas.get(courtID), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }


        });
    }

    public void spinnerSelectTeam2(){
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(CreateMatch.this, android.R.layout.simple_spinner_item, equipos2 );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CreateMatch.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                teamSelect2.setAdapter(adapter2);
            }
        });

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CreateMatch.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                teamSelect2.setAdapter(adapter2);
            }
        });

        team2 = -1;
        teamSelect2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                team2 = position;
                CreateMatch.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(CreateMatch.this, "selected court: "+ canchas.get(courtID), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }


        });
    }

    public void spinnerSelectCourt(){
        final ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(CreateMatch.this, android.R.layout.simple_spinner_item, canchas );
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CreateMatch.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                courtSelect.setAdapter(adapter3);
            }
        });

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CreateMatch.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                courtSelect.setAdapter(adapter3);
            }
        });

        courtID = -1;
        courtSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                courtID = position;
                //court = canchas.get() // query y tomar el id de la cancha en pa posicion courtID
                CreateMatch.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(CreateMatch.this, "selected court: "+ canchas.get(courtID), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }


        });
    }




    public boolean validateFields(){
        if (courtID == -1){
            return false;
        }
        if (teamID == -1){
            return false;
        }
        if (fechayhora.equals("")){
            return false;
        }
        if (team1==-1){
            return false;
        }
        if (team2==-1){
            return  false;
        }

        return true;
    }

//    public void getCourts(){ //lo llama en el oncreate
//        MyApolloClient.getMyApolloClient().b
//
//                //onresponse
                //Log.d("=============================" + response.data())
//            //for
//                //aux = data[i].title.toString() +
//                //courtList.add(aux); //instanciar lista

//    }


}
