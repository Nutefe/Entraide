package tg.licorne.entraideagro.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.config.VolleySingleton;
import tg.licorne.entraideagro.controllers.ActivitesActivity;
import tg.licorne.entraideagro.controllers.DashboardAgentActivity;
import tg.licorne.entraideagro.controllers.RapportInvalideActivity;
import tg.licorne.entraideagro.controllers.RapportValideActivity;
import tg.licorne.entraideagro.helper.CircleTransform;


/**
 * Created by Admin on 09/04/2018.
 */

public class FragmentAgentAcceuil extends Fragment implements View.OnClickListener{
    private LinearLayout linearLayoutActivite, linearLayoutRapportValide, linearLayoutRapportInvalide;
    private TextView textViewMeteo, textViewHeur;
    private TextView textViewNom, textViewAdresse, textViewNumero, textViewEquipe, textViewTmp;
    private ImageView imageViewAvatar;

    private String meteoUrl = "http://api.openweathermap.org/data/2.5/weather?lat="+5.4138+"&lon="+-0.7551+"&appid=7d48d39d2e29303ba0874767db04c86a";
    private String meteoUrl1 = "http://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22";
    private double longitude, latitude;

    private static int id_user_detail;
    private String token, type_compte;

    String urlDetail= ServerConfig.url_sever +"details";
    String urlUserConnect= ServerConfig.url_sever +"user_connect_info";


    public static FragmentAgentAcceuil newInstance(){
        FragmentAgentAcceuil fragment = new FragmentAgentAcceuil();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agent_acceuil, container, false);

        Bundle bundle = getArguments();

        if (bundle!=null){
            if (bundle.containsKey("TOKEN") || bundle.containsKey("ID_ROLE")){
                token = bundle.getString("TOKEN");
            }
        }

        initView(view);
        initListener();

        if (checkNetworkConnexion()){
            detail();
            date();
        }

        return view;
    }

    private void initView(View view){
        linearLayoutActivite = (LinearLayout) view.findViewById(R.id.idLinearLayoutActiviteAcceuil);
        linearLayoutRapportValide = (LinearLayout) view.findViewById(R.id.idLinearLayoutRapportValideAcceuil);
        linearLayoutRapportInvalide = (LinearLayout) view.findViewById(R.id.idLinearLayoutRapportInvalideAcceuil);

        textViewMeteo = (TextView) view.findViewById(R.id.idTextViewDateMeteoAcceuil);
        textViewHeur = (TextView) view.findViewById(R.id.idTextViewHeurAcceuil);
        textViewNom = view.findViewById(R.id.idTextViewNomUser);
        textViewAdresse = view.findViewById(R.id.idTextViewAdresseUser);
        textViewNumero = view.findViewById(R.id.idTextViewNumeroUser);
        textViewEquipe = view.findViewById(R.id.idTextViewEquipeUser);
        textViewTmp = view.findViewById(R.id.idTextViewDateMeteoTmpAcceuil);
        imageViewAvatar = view.findViewById(R.id.idImageViewDashboardAvatar);
    }

    private void initListener(){
        linearLayoutActivite.setOnClickListener(this);
        linearLayoutRapportValide.setOnClickListener(this);
        linearLayoutRapportInvalide.setOnClickListener(this);
    }

    private void date(){
        //String tmp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        //Toast.makeText(getContext(), tmp, Toast.LENGTH_SHORT).show();

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
                                    Date cal = (Date) Calendar.getInstance().getTime();
                                    dt = cal.toLocaleString();
                                    final  Calendar c = Calendar.getInstance();
                                    int hour = c.get(Calendar.HOUR_OF_DAY);
                                    int min = c.get(Calendar.MINUTE);
                                    textViewHeur.setText(String.valueOf(hour)+":"+String.valueOf(min));
                                    textViewTmp.setText(dt+" | "+f.format(degre)+"°C");

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

                        Log.i("response", response.toString());

                        try {
                            //converting response to json object
                            if (response != null){
                                JSONObject obj = new JSONObject(response);
                                JSONObject resSucces = obj.getJSONObject("success");
                                Log.i("success", resSucces.toString());
                                id_user_detail = resSucces.getInt("id");
                                type_compte = resSucces.getString("type_compte");
                                Log.i("id", String.valueOf(id_user_detail));

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
                        /*if (getContext()!= null){
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }*/
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
                                Log.i("success", resSucces.toString());

                                String nom = "", prenom = "", adresse = "", numero = "", equipe="", avatar="";

                                for (int i=0; i<resSucces.length(); i++) {
                                    JSONObject proJson =resSucces.getJSONObject(i);
                                    nom = proJson.getString("nom");
                                    prenom = proJson.getString("prenom");
                                    adresse = proJson.getString("adresse");
                                    numero = proJson.getString("telephone");
                                    equipe = proJson.getString("nom_equipe");
                                    avatar = ServerConfig.url_file +"/"+
                                            proJson.getString("chemin")+""+
                                            proJson.getString("avatar");
                                    Log.i("success", nom.toString());
                                }

                                textViewNom.setText(nom +" "+prenom);
                                textViewAdresse.setText(adresse);
                                textViewNumero.setText(numero);
                                textViewEquipe.setText(equipe);
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
                        /*if (getContext()!= null){
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }*/
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
                //getCountMeteo();
            }
        };
        timer.schedule(timerTask, debut, period);
    }

    public boolean checkNetworkConnexion(){
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            /*final android.net.NetworkInfo wifi = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            final android.net.NetworkInfo mobile = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);*/

            return (networkInfo != null && networkInfo.isConnected());
        } catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.idLinearLayoutActiviteAcceuil:
                Intent intentActivite = new Intent(getContext(), ActivitesActivity.class);
                intentActivite.putExtra("ID_USER", id_user_detail);
                intentActivite.putExtra("TOKEN", token);
                startActivity(intentActivite);
                break;
            case R.id.idLinearLayoutRapportValideAcceuil:
                Intent intentRapportValide = new Intent(getContext(), RapportValideActivity.class);
                intentRapportValide.putExtra("ID_USER", id_user_detail);
                intentRapportValide.putExtra("TOKEN", token);
                startActivity(intentRapportValide);
                break;
            case R.id.idLinearLayoutRapportInvalideAcceuil:
                Intent intentRapportRecu = new Intent(getContext(), RapportInvalideActivity.class);
                intentRapportRecu.putExtra("ID_USER", id_user_detail);
                intentRapportRecu.putExtra("TOKEN", token);
                startActivity(intentRapportRecu);
                break;
        }
    }
}
