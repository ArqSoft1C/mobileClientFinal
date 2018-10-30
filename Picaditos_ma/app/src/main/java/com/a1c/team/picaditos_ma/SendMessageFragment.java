package com.a1c.team.picaditos_ma;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;


public class SendMessageFragment extends Fragment {

    private EditText message_username;
    private EditText message_content;
    private SendMessageFragment.CreateMessageTask createMessageTask = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_send_message, container, false);
        hideKeyboard(getActivity());
        // Inflate the layout for this fragment

        message_username = (EditText) v.findViewById(R.id.message_username);
        message_content= (EditText) v.findViewById(R.id.message_content);

        if(!MainActivity.auxiliarData.getTemporal_message_username().equals("")){
            message_username = v.findViewById(R.id.message_username);
            message_username.setText(MainActivity.auxiliarData.getTemporal_message_username());
        }

        Button createMessageButton = (Button) v.findViewById(R.id.create_message);
        createMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyboard(getActivity());
                attemptCreateMessage();
                message_username.getText().clear();
                message_content.getText().clear();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }


    private void attemptCreateMessage() {
        if (createMessageTask != null) {
            return;
        }

        // Reset errors.
        message_username.setError(null);
        message_content.setError(null);

        // Store values at the time of the message creation
        String user_name = message_username.getText().toString();
        String content = message_content.getText().toString();


        boolean cancel = false;
        View focusView = null;


        if (cancel) {
            focusView.requestFocus();
        } else {
            createMessageTask = new CreateMessageTask(user_name, content);
            createMessageTask.execute((Void) null);
        }
    }

    public class CreateMessageTask extends AsyncTask<Void, Void, Boolean> {
        Boolean status = false;
        Boolean ldap = false;
        private final String message_username;
        private final String message_content;
        private final  String message_sender = LoginActivity.user.getUsername();
        private final String message_subject = "Sin Asunto";

        final String TAG = "CreateMessageFragment";

        CreateMessageTask(String username, String content) {
            message_username = username;
            message_content=content;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            createMessage();
            status = true;
            return status;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            createMessageTask = null;
            Log.d(TAG, "postExecute : ");
        }

        @Override
        protected void onCancelled() {
            createMessageTask = null;
            Log.d(TAG, "onCancelled: ");
        }

        protected boolean createMessage() {
            MyApolloClient.getMyApolloClient().mutate(
                    MutationCreateMessageMutation.builder()
                            .user1(message_sender)
                            .user2(message_username)
                            .subject(message_subject)
                            .content(message_content)
                            .build())
                    .enqueue(new ApolloCall.Callback<MutationCreateMessageMutation.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<MutationCreateMessageMutation.Data> response) {
//                            Log.d(TAG, "answer: "+response.data().auth().answer());
                            if (response.data() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getActivity(), "El mensajea ha sido enviado exitosamente!", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getActivity(), "El mensaje no ha podido enviarse! Intente de nuevo.", Toast.LENGTH_SHORT).show();
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
