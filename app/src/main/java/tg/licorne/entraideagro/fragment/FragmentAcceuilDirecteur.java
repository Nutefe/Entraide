package tg.licorne.entraideagro.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.config.VolleySingleton;
import tg.licorne.entraideagro.controllers.DirecteurDocActivity;
import tg.licorne.entraideagro.controllers.DocumentActivity;
import tg.licorne.entraideagro.controllers.RapportInvalideActivity;
import tg.licorne.entraideagro.controllers.RapportInvalideDirecteurActivity;
import tg.licorne.entraideagro.controllers.RapportValideActivity;
import tg.licorne.entraideagro.controllers.RapportValideDirecteurActivity;
import tg.licorne.entraideagro.helper.CircleTransform;

public class FragmentAcceuilDirecteur extends Fragment implements View.OnClickListener{
    private LinearLayout linearLayoutDoc, linearLayoutRapportValide, linearLayoutRapportInvalide;
    private TextView textViewMeteo, textViewHeur;
    private TextView textViewNom, textViewAdresse, textViewNumero, textViewEquipe, textViewTmp;
    private ImageView imageViewAvatar;

    private String meteoUrl = "http://api.openweathermap.org/data/2.5/weather?lat="+5.4138+"&lon="+-0.7551+"&appid=7d48d39d2e29303ba0874767db04c86a";
    private String meteoUrl1 = "http://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22";
    private double longitude, latitude;

    private int id_user_detail;
    private String token, type_compte;

    String urlDetail= ServerConfig.url_sever +"details";
    String urlUserConnect= ServerConfig.url_sever +"user_connect_info";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coordinateur_acceuil, container, false);
        Bundle bundle = getArguments();
        if (bundle!=null){
            if (bundle.containsKey("TOKEN")){
                token = bundle.getString("TOKEN");
            }
        }

        initView(view);
        initListener();

        if (checkNetworkConnexion()){
            detail();
            date();
            getCountMeteo();
        }

        return view;
    }

    private void initView(View view){
        linearLayoutDoc = view.findViewById(R.id.idLinearLayoutDocCoor);
        linearLayoutRapportValide = view.findViewById(R.id.idLinearLayoutRapportValideCoor);
        linearLayoutRapportInvalide = view.findViewById(R.id.idLinearLayoutRapportInvalideCoor);

        textViewMeteo = view.findViewById(R.id.idTextViewDateCoordinateur);
        textViewHeur = view.findViewById(R.id.idTextViewHeureCoordinateur);

        textViewNom = view.findViewById(R.id.idTextViewNomCoordinateur);
        textViewAdresse = view.findViewById(R.id.idTextViewAdresseCoordinateur);
        textViewNumero = view.findViewById(R.id.idTextViewNumeroCoordinateur);
        textViewTmp = view.findViewById(R.id.idTextViewDateTmpCoordinateur);

        imageViewAvatar = view.findViewById(R.id.idImageViewAcceuilCoorAvatar);
    }

    private void initListener(){
        linearLayoutDoc.setOnClickListener(this);
        linearLayoutRapportInvalide.setOnClickListener(this);
        linearLayoutRapportValide.setOnClickListener(this);
    }

    private void date(){
        Date dateInstance = new Date();
        int year = dateInstance.getYear()+1900;//Returns:the year represented by this date, minus 1900.
        int date = dateInstance.getDate();
        int month = dateInstance.getMonth();
        int day = dateInstance.getDay();
        int hours = dateInstance.getHours();
        int min = dateInstance.getMinutes();
        int sec = dateInstance.getSeconds();

        String dayOfWeek = "";
        String monthOfWeek = "";
        switch(day){
            case 0:
                dayOfWeek = "Dimanche";
                break;
            case 1:
                dayOfWeek = "Lundi";
                break;
            case 2:
                dayOfWeek = "Mardi";
                break;
            case 3:
                dayOfWeek = "Mercredi";
                break;
            case 4:
                dayOfWeek = "Jeudi";
                break;
            case 5:
                dayOfWeek = "Vendredi";
                break;
            case 6:
                dayOfWeek = "Samedi";
                break;
        }
        switch(month){
            case 0:
                monthOfWeek = "Janvier";
                break;
            case 1:
                monthOfWeek = "Fevrier";
                break;
            case 2:
                monthOfWeek = "Mars";
                break;
            case 3:
                monthOfWeek = "Avril";
                break;
            case 4:
                monthOfWeek = "Mai";
                break;
            case 5:
                monthOfWeek = "Juin";
                break;
            case 6:
                monthOfWeek = "Juillet";
                break;
            case 7:
                monthOfWeek = "Août";
                break;
            case 8:
                monthOfWeek = "Septembre";
                break;
            case 9:
                monthOfWeek = "Octobre";
                break;
            case 10:
                monthOfWeek = "Novembre";
                break;
            case 11:
                monthOfWeek = "Décembre";
                break;
        }

        textViewHeur.setText(hours +":"+min);
        textViewMeteo.setText(dayOfWeek+", "+date+" "+monthOfWeek +"|");
    }

    private void getCountMeteo(){

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, meteoUrl,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        Log.i("response", response.toString());
                        //progressDialog.hide();

                        try {
                            //converting response to json object
                            if (response != null){
                                JSONObject obj = new JSONObject(response);
                                if (obj != null) {
                                    JSONObject temp = obj.getJSONObject("main");
                                    Log.i("main", temp.toString());
                                    //textViewNbrMessage.setText(obj.getString("success"));
                                    double degre = temp.getDouble("temp")-273.5;
                                    DecimalFormat f = new DecimalFormat();
                                    f.setMaximumFractionDigits(2);
                                    //double p = Math.pow(degre,2);
                                    String dt;
                                    //textViewHeur.setText(hours +":"+min);
                                    textViewTmp.setText(f.format(degre)+"°C");

                                } else {
                                    Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                        //Toast.makeText(getContext(), "erreur de connection au serveur", Toast.LENGTH_SHORT).show();
                        //progressDialog.hide();
                        Log.i("error", error.getMessage().toString());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Authorization", "Bearer "+ token);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void detail(){

        StringRequest stringDetailUser = new StringRequest(Request.Method.GET, urlDetail,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        //Log.i("response", response.toString());

                        try {
                            //converting response to json object
                            if (response != null){
                                JSONObject obj = new JSONObject(response);
                                JSONObject resSucces = obj.getJSONObject("success");
                                Log.i("success", resSucces.toString());
                                id_user_detail = resSucces.getInt("id");
                                type_compte = resSucces.getString("type_compte");
                                Log.i("id", String.valueOf(id_user_detail));

                                /*String nom = "", prenom = "", adresse = "", numero = "", equipe ="", avatar = "";

                                    nom = resSucces.getString("username");
                                    prenom = resSucces.getString("prenom");
                                    adresse = resSucces.getString("adresse");
                                    numero = resSucces.getString("telephone");
                                    avatar = ServerConfig.url_file+"/"+
                                            resSucces.getString("chemin")+""+
                                            resSucces.getString("avatar");


                                textViewNom.setText(nom +" "+prenom);
                                textViewAdresse.setText(adresse);
                                textViewNumero.setText(numero);
                                if (getContext()!=null){
                                    Glide.with(getContext())
                                            .load(avatar)
                                            .asBitmap()
                                            .transform(new CircleTransform(getContext()))
                                            .into(imageViewAvatar);
                                }*/

                                userConnect();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringDetailUser);
    }

    private void userConnect(){
        String  nom, adresse, numero, equipe;

        urlUserConnect = ServerConfig.url_sever +"user_connect_info/"+id_user_detail+"/"+type_compte;

        StringRequest stringDetailUser = new StringRequest(Request.Method.GET, urlUserConnect,
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
                                Log.i("testssss", obj.toString());

                                String nom = "", prenom = "", adresse = "", numero = "", equipe ="", avatar = "";

                                for (int i=0; i<resSucces.length(); i++) {
                                    JSONObject proJson =resSucces.getJSONObject(i);
                                    nom = proJson.getString("nom");
                                    prenom = proJson.getString("prenom");
                                    adresse = proJson.getString("adresse");
                                    numero = proJson.getString("telephone");
                                    avatar = ServerConfig.url_file+"/"+
                                            proJson.getString("chemin")+""+
                                            proJson.getString("avatar");
                                }

                                textViewNom.setText(nom +" "+prenom);
                                textViewAdresse.setText(adresse);
                                textViewNumero.setText(numero);
                                if (getContext()!=null){
                                    Glide.with(getContext())
                                            .load(avatar)
                                            .asBitmap()
                                            .transform(new CircleTransform(getContext()))
                                            .into(imageViewAvatar);
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
                        //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringDetailUser);
    }

    private void tempo(){
        long period = 60*10000;
        long debut = 0;
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                getCountMeteo();
            }
        };
        timer.schedule(timerTask, debut, period);
    }

    public boolean checkNetworkConnexion() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            /*final android.net.NetworkInfo wifi = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            final android.net.NetworkInfo mobile = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);*/

            return (networkInfo != null && networkInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.idLinearLayoutDocCoor:
                Intent intentDoc = new Intent(getContext(), DirecteurDocActivity.class);
                intentDoc.putExtra("TOKEN", token);
                startActivity(intentDoc);
                break;
            case R.id.idLinearLayoutRapportValideCoor:
                Intent intentRapportValide = new Intent(getContext(), RapportValideDirecteurActivity.class);
                intentRapportValide.putExtra("ID_USER", id_user_detail);
                intentRapportValide.putExtra("TOKEN", token);
                startActivity(intentRapportValide);
                break;
            case R.id.idLinearLayoutRapportInvalideCoor:
                Intent intentRapportRecu = new Intent(getContext(), RapportInvalideDirecteurActivity.class);
                intentRapportRecu.putExtra("ID_USER", id_user_detail);
                intentRapportRecu.putExtra("TOKEN", token);
                startActivity(intentRapportRecu);
                break;
        }
    }
}
