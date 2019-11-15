package tg.licorne.entraideagro.controllers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.config.VolleySingleton;
import tg.licorne.entraideagro.helper.CircleTransform;
import tg.licorne.entraideagro.helper.SessionManager;
import tg.licorne.entraideagro.model.Login;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private AppCompatButton appCompatButton;
    private TextInputEditText textInputEditTextUsername, textInputEditTextPassword;
    private AppCompatImageView imageViewLogo;

    String url= ServerConfig.url_sever +"login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        initView();
        initListener();

        if (SessionManager.getInstance(this).isLoggedIn()) {
            finish();
            //startActivity(new Intent(this, CommandeActivity.class));
            Login loginSession = SessionManager.getInstance(this).getUser();
            int role_id = loginSession.getId_role();
            if (role_id == 6){
                Intent intent = new Intent(LoginActivity.this, DashboardAgentActivity.class);
                intent.putExtra("TOKEN", loginSession.getToken());
                intent.putExtra("ID_ROLE", loginSession.getId_role());
                startActivity(intent);
            } else if (role_id == 7){
                Intent intent = new Intent(LoginActivity.this, DashboardPromoteurActivity.class);
                intent.putExtra("TOKEN", loginSession.getToken());
                intent.putExtra("ID_ROLE", loginSession.getId_role());
                startActivity(intent);
            } else if (role_id == 3 || role_id == 4){
                Intent intent = new Intent(LoginActivity.this, DashboardCoordinateurActivity.class);
                intent.putExtra("TOKEN", loginSession.getToken());
                intent.putExtra("ID_ROLE", loginSession.getId_role());
                startActivity(intent);
            } else if (role_id == 5){
                Intent intent = new Intent(LoginActivity.this, DashboardSuivieActivity.class);
                intent.putExtra("TOKEN", loginSession.getToken());
                intent.putExtra("ID_ROLE", loginSession.getId_role());
                startActivity(intent);
            } else if (role_id == 1){
                Intent intent = new Intent(LoginActivity.this, DashboardDirecteur.class);
                intent.putExtra("TOKEN", loginSession.getToken());
                intent.putExtra("ID_ROLE", loginSession.getId_role());
                startActivity(intent);
            } else if (role_id == 2){
                finish();
                Intent intent = new Intent(LoginActivity.this, DashboardPartenaireActivity.class);
                intent.putExtra("TOKEN", loginSession.getToken());
                intent.putExtra("ID_ROLE", loginSession.getId_role());
                startActivity(intent);
            }  else {
                Toast.makeText(LoginActivity.this, "Vous n'ête pas autoriser", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("WrongViewCast")
    private void initView(){
        appCompatButton = (AppCompatButton) findViewById(R.id.idAppCompatButtonLogin);
        textInputEditTextUsername = (TextInputEditText) findViewById(R.id.textInputEditTextUsername);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        imageViewLogo = (AppCompatImageView) findViewById(R.id.idAppCompatImageViewLogo);
    }

    private void initListener(){
        appCompatButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.idAppCompatButtonLogin:
                //login();
                userLogin();
                break;
        }
    }

    private void actualiserEdite(){
        textInputEditTextUsername.setText("");
        textInputEditTextPassword.setText("");
    }

    private void userLogin() {
        //first getting the values
        final String username = textInputEditTextUsername.getText().toString();
        final String password = textInputEditTextPassword.getText().toString();

        //Toast.makeText(context, url, Toast.LENGTH_LONG).show();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            textInputEditTextUsername.setError("Entrer le nom d'utilisateur");
            textInputEditTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            textInputEditTextPassword.setError("Entrer le mot de passe");
            textInputEditTextPassword.requestFocus();
            return;
        }

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        Log.i("response", response.toString());

                        try {
                            //converting response to json object
                            if (response != null){
                                JSONObject obj = new JSONObject(response);

                                //Log.i("success", obj.toString());

                                //if no error in response
                                if (obj != null) {
                                    JSONObject  successJson = obj.getJSONObject("success");
                                    //Log.i("success", successJson.toString());
                                    //creating a new user object
                                    String token = successJson.getString("token");
                                    //Log.i("token", token.toString());
                                    int id_role = obj.getInt("role");
                                    //Log.i("role", String.valueOf(id_role));

                                    Login login = new Login(token, id_role);

                                    SessionManager.getInstance(getApplicationContext()).userLogin(login);

                                    if (id_role == 6){
                                        finish();
                                        Intent intent = new Intent(LoginActivity.this, DashboardAgentActivity.class);
                                        intent.putExtra("TOKEN", token);
                                        intent.putExtra("ID_ROLE",  id_role);
                                        startActivity(intent);
                                    } else if (id_role == 7){
                                        finish();
                                        Intent intent = new Intent(LoginActivity.this, DashboardPromoteurActivity.class);
                                        intent.putExtra("TOKEN", token);
                                        intent.putExtra("ID_ROLE",  id_role);
                                        startActivity(intent);
                                    } else if (id_role == 3 || id_role == 4){
                                        finish();
                                        Intent intent = new Intent(LoginActivity.this, DashboardCoordinateurActivity.class);
                                        intent.putExtra("TOKEN", token);
                                        intent.putExtra("ID_ROLE",  id_role);
                                        startActivity(intent);
                                    } else if (id_role == 5){
                                        finish();
                                        Intent intent = new Intent(LoginActivity.this, DashboardSuivieActivity.class);
                                        intent.putExtra("TOKEN", token);
                                        intent.putExtra("ID_ROLE",  id_role);
                                        startActivity(intent);
                                    }  else if (id_role == 1){
                                        finish();
                                        Intent intent = new Intent(LoginActivity.this, DashboardDirecteur.class);
                                        intent.putExtra("TOKEN", token);
                                        intent.putExtra("ID_ROLE",  id_role);
                                        startActivity(intent);
                                    } else if (id_role == 2){
                                        finish();
                                        Intent intent = new Intent(LoginActivity.this, DashboardPartenaireActivity.class);
                                        intent.putExtra("TOKEN", token);
                                        intent.putExtra("ID_ROLE",  id_role);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Vous n'ête pas autoriser", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(LoginActivity.this, "error de connexion", Toast.LENGTH_SHORT).show();
                        Log.i("error", error.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
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
    }
}
