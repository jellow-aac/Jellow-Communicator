package com.dsource.idc.jellow;

/**
 * Created by user on 5/25/2016.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dsource.idc.jellow.app.AppConfig;
import com.dsource.idc.jellow.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SharedPreferences extends AppCompatActivity {

    Button bRegister;
    EditText etName, etEmergencyContact, etEmailId;
    private SessionManager session;
    String email, n, ec, formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.format);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        etName = (EditText) findViewById(R.id.etName);
        etEmergencyContact = (EditText) findViewById(R.id.etEmergencyContact);
        etEmailId= (EditText)findViewById(R.id.etEmailId);
        bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setAlpha(0.5f);
        //register
        bRegister.setEnabled(true);
        session = new SessionManager(getApplicationContext());

// onClick of button perform this simplest code.

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etName.getText().toString().isEmpty()){
                    bRegister.setAlpha(0.5f);
                    bRegister.setEnabled(false);
                }
                else
                {bRegister.setAlpha(1f);
                    bRegister.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
             Intent i = new Intent(this, Intro.class);
            //current
            //Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                n  = etName.getText().toString();
                ec  = etEmergencyContact.getText().toString();

                if (etName.getText().length()>0){
                    email = etEmailId.getText().toString().trim();
                    if (isValidEmail(email))
                    {
                        session.setName(n);
                        session.setFather_no(ec);
                        session.setEmailId(email);
                        //session.setLogin(true);
                        Calendar ca = Calendar.getInstance();
                        System.out.println("Current time =&gt; "+ca.getTime());

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        formattedDate = df.format(ca.getTime());
                        new LongOperation().execute("");
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(SharedPreferences.this, "Please Enter the Name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void checkLogin(final String name, final String contact, final String mail, final String time) {
        // Tag used to cancel the request

        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        //register
                        bRegister.setEnabled(false);
                        session.setLogin(true);
                        Intent intent = new Intent(SharedPreferences.this, Intro.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        //register
                        bRegister.setEnabled(true);
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    //register
                    bRegister.setEnabled(true);
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Login Error: " + error.getMessage());
                //register
                bRegister.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Please check internet connection and try again" , Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("contact", contact);
                params.put("mail", mail);
                params.put("time", time);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {

                checkLogin(n, ec, email, formattedDate);
            } catch (Exception e) {
                Thread.interrupted();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}

