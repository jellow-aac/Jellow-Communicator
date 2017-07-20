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
import com.dsource.idc.jellow.Utility.SessionManager;
import com.dsource.idc.jellow.app.AppConfig;
import com.dsource.idc.jellow.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UserRegistrationActivity extends AppCompatActivity {
    final int LANG_ENGLISH = 0, GRID_3BY3 = 1;
    private Button bRegister;
    private EditText etName, etEmergencyContact, etEmailId;
    private SessionManager mSession;
    private String email, n, ec, formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        etName = (EditText) findViewById(R.id.etName);
        etEmergencyContact = (EditText) findViewById(R.id.etEmergencyContact);
        etEmailId= (EditText)findViewById(R.id.etEmailId);
        bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setAlpha(0.5f);
        bRegister.setEnabled(true);
        mSession = new SessionManager(this);

        etName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etName.getText().toString().isEmpty()){
                    bRegister.setAlpha(0.5f);
                    bRegister.setEnabled(false);
                }else{
                    bRegister.setAlpha(1f);
                    bRegister.setEnabled(true);
                }
            }
        });

        if (mSession.isUserLoggedIn()) {
            startActivity(new Intent(this, SplashActivity.class));
            finish();
        }

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n  = etName.getText().toString();
                ec  = etEmergencyContact.getText().toString();
                if (etName.getText().length()>0){
                    if (etEmergencyContact.getText().toString().trim().length() == 10) {
                        email = etEmailId.getText().toString().trim();
                        if (isValidEmail(email)){
                            mSession.setName(n);
                            mSession.setFather_no(ec);
                            mSession.setEmailId(email);
                            Calendar ca = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            formattedDate = df.format(ca.getTime());
                            new LongOperation().execute("");
                        }else
                            Toast.makeText(getApplicationContext(),getString(R.string.invalid_emailId), Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), getString(R.string.invalidContactNumber), Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(UserRegistrationActivity.this, getString(R.string.enterTheName), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void checkLogin(final String name, final String contact, final String mail, final String time) {
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        bRegister.setEnabled(false);
                        mSession.setUserLoggedIn(true);
                        mSession.setLanguage(LANG_ENGLISH);
                        mSession.setGridSize(GRID_3BY3);
                        startActivity(new Intent(UserRegistrationActivity.this, Intro.class));
                        finish();
                    } else {
                        bRegister.setEnabled(true);
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(UserRegistrationActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    bRegister.setEnabled(true);
                    e.printStackTrace();
                    Toast.makeText(UserRegistrationActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                bRegister.setEnabled(true);
                Toast.makeText(UserRegistrationActivity.this, getString(R.string.checkInternetConn) , Toast.LENGTH_LONG).show();
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
    }
}

