package tg.licorne.entraideagro.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.adapter.CommentairesAdapter;
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.config.VolleySingleton;
import tg.licorne.entraideagro.helper.DriverItemDecoration;
import tg.licorne.entraideagro.model.Activites;
import tg.licorne.entraideagro.model.Commentaires;

/**
 * Created by Admin on 18/02/2018.
 */

public class CommentairesActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout linearLayoutCommentaire;
    private EditText editTextMessage;
    private Button buttonSend;

    private List<Commentaires> commentairesList = new ArrayList<>();
    private CommentairesAdapter commentairesAdapter;
    private Commentaires commentaires;
    private RecyclerView recyclerView;

    private String token;
    private int id_user;
    private static int id_article;

    String  ip = "192.168.43.123";
    String port = "80";
//    String  ip = "10.0.2.2";
//    String port = "8000";

    String urlDetail= ServerConfig.url_sever+"details";
    String urlPostCommentaire= ServerConfig.url_sever +"post_commentaire";
    String urlGetCommentaire= ServerConfig.url_sever +"commentaires";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentaire_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.idToolbarCommentaire);
        toolbar.setTitle("Commentaires");
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intentExtrat = getIntent();
        if (intentExtrat != null){
            token = intentExtrat.getStringExtra("TOKEN");
            id_user = intentExtrat.getIntExtra("ID_USER", 0);
            id_article = intentExtrat.getIntExtra("ID_ARTICLE", 0);
        }
        Toast.makeText(this, String.valueOf(id_user), Toast.LENGTH_SHORT).show();

        initView();
        initListner();

        //getCommentaire();
    }

    private void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.idRecycleViewCommentaire);
        editTextMessage = findViewById(R.id.idEditTextMessage);
        buttonSend = findViewById(R.id.sendButton);
    }

    private void initListner(){
        buttonSend.setOnClickListener(this);
    }

    private void sendCommentaire(){
        final String contenu = editTextMessage.getText().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringMessage = new StringRequest(Request.Method.POST, urlPostCommentaire,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        Log.i("response", response.toString());

                        try {
                            //converting response to json object
                            if (response != null){
                                JSONObject obj = new JSONObject(response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("error", error.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("contenu", contenu);
                params.put("id_article", String.valueOf(id_article));
                params.put("id_user", String.valueOf(id_user));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };
        //AppController.getInstance().addToRequestQueue(stringService, tag);
        requestQueue.add(stringMessage);
        editTextMessage.setText("");
    }

    private void getCommentaire(){

        commentairesList.clear();

        urlGetCommentaire = ServerConfig.url_sever +"commentaires/"+id_article;

        StringRequest stringDetailUser = new StringRequest(Request.Method.GET, urlGetCommentaire,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        Log.i("response", response.toString());

                        try {
                            //converting response to json object
                            if (response != null){
                                JSONObject obj = new JSONObject(response);
                                JSONArray resSucces = obj.getJSONArray("success");
                                //String res = obj.getString("success");
                                //Log.i("success", resSucces.toString());
                                for (int i=0; i<resSucces.length(); i++) {
                                    JSONObject proJson =resSucces.getJSONObject(i);
                                    //Log.i("sec", proJson.toString());
                                    String date = proJson.getString("created_at").substring(0,10)+"|"
                                            +proJson.getString("created_at").substring(11,16);
                                    commentaires = new Commentaires(
                                            proJson.getInt("id"),
                                            proJson.getString("username"),
                                            date,
                                            ServerConfig.url_file+"/uploads/avatars/"+proJson.getString("avatar"),
                                            proJson.getString("contenu")
                                    );
                                    commentairesList.add(commentaires);
                                }

                                commentairesAdapter = new CommentairesAdapter(commentairesList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.addItemDecoration(new DriverItemDecoration(CommentairesActivity.this, DriverItemDecoration.VERTICAL_LIST, 63));
                                recyclerView.setAdapter(commentairesAdapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(CommentairesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("error", error.getMessage().toString());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                // headers.put("Content-Type", "application/json");
                String auth= "Bearer "+ token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringDetailUser);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sendButton:
                //sendCommentaire();
                //getCommentaire();
                break;
        }
    }
}
