package tg.licorne.entraideagro.controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.config.VolleySingleton;
import tg.licorne.entraideagro.helper.SessionManager;
import tg.licorne.entraideagro.model.Login;

public class EntraideActivity extends AppCompatActivity {

    private TextInputEditText textInputEditTextNom, textInputEditTextPrenom, textInputEditTextAdresse,
            textInputEditTextTel, textInputEditTextEmail, textInputEditTextMessage;
    private AppCompatButton appCompatButtonSend;
    private RadioGroup radioGroup;
    private String sexe;
    String url= ServerConfig.url_sever +"entraide";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entraide_activity);

        initView();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGrou, int i) {
                RadioButton selecte = radioGroup.findViewById(i);
                boolean isSelect = selecte.isChecked();
                if (isSelect){
                    sexe = selecte.getText().toString();
                    //Toast.makeText(EntraideActivity.this, sexe, Toast.LENGTH_SHORT).show();
                }
            }
        });

        appCompatButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });
    }

    private void initView(){
        textInputEditTextNom = findViewById(R.id.textInputEditTextNom);
        textInputEditTextPrenom = findViewById(R.id.textInputEditTextPrenom);
        textInputEditTextAdresse = findViewById(R.id.textInputEditTextAdresse);
        textInputEditTextTel = findViewById(R.id.textInputEditTextTelephone);
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextMessage = findViewById(R.id.textInputEditTextMessage);

        radioGroup = findViewById(R.id.radioGroup);
        appCompatButtonSend = findViewById(R.id.idAppCompatButtonSendEntraide);

    }

    private void sendMail(){
        final String nom = textInputEditTextNom.getText().toString();
        final String prenom = textInputEditTextPrenom.getText().toString();
        final String adresse = textInputEditTextAdresse.getText().toString();
        final String tel = textInputEditTextTel.getText().toString();
        final String email = textInputEditTextEmail.getText().toString();
        final String message = textInputEditTextMessage.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(EntraideActivity.this);
        progressDialog.setMessage("Evoie en cour ...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        Log.i("response", response.toString());

                        try {
                            //converting response to json object
                            if (response != null){
                                JSONObject obj = new JSONObject(response);
                                String success = obj.getString("success");
                                progressDialog.hide();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error", error.getMessage().toString());
                        progressDialog.hide();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nom", nom);
                params.put("prenom", prenom);
                params.put("adresse", adresse);
                params.put("telephone", tel);
                params.put("email", email);
                params.put("message", message);
                params.put("sexe", sexe);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

        textInputEditTextNom.setText("");
        textInputEditTextPrenom.setText("");
        textInputEditTextAdresse.setText("");
        textInputEditTextTel.setText("");
        textInputEditTextEmail.setText("");
        textInputEditTextMessage.setText("");
    }

}
