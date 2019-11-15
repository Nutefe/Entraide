package tg.licorne.entraideagro.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.config.VolleySingleton;
import tg.licorne.entraideagro.controllers.UpdateProfilActivity;
import tg.licorne.entraideagro.helper.SessionManager;

/**
 * Created by Admin on 03/05/2018.
 */

public class FragmentAgentProfile extends Fragment {
    private TextView textViewNom, textViewAdresse, textViewNumero, textViewEquipe, textViewTypeAgent;
    private TextView textViewPrenom, textViewFerme, textViewTitreEquipe, textViewTitreTypeAgent, textViewTitreFerme;

    private LinearLayout linearLayoutDeconnect;
    private FloatingActionButton actionButton;

    private ImageView imageView;

    private int id_user_detail, id_role;
    private String token, type_compte;

    String urlDetail= ServerConfig.url_sever +"details";
    String urlUserConnect= ServerConfig.url_sever +"user_connect_info";

    public static FragmentAgentProfile newInstance(){
        FragmentAgentProfile fragment = new FragmentAgentProfile();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        Bundle bundle = getArguments();

        if (bundle!=null){
            if (bundle.containsKey("TOKEN") || bundle.containsKey("ID_ROLE")){
                token = bundle.getString("TOKEN");
                id_role = bundle.getInt("ID_ROLE");
            }
        }

        Log.i("role", String.valueOf(id_role));
        initView(view);
        if (id_role == 7){
            textViewFerme.setVisibility(View.GONE);
            textViewEquipe.setVisibility(View.GONE);
            textViewTypeAgent.setVisibility(View.GONE);
            textViewTitreEquipe.setVisibility(View.GONE);
            textViewTitreTypeAgent.setVisibility(View.GONE);
            textViewTitreFerme.setVisibility(View.GONE);
        }

        if (checkNetworkConnexion()){
            detail();
        }

        linearLayoutDeconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deconect();
                //getActivity().finish();
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentUpdate = new Intent(getContext(), UpdateProfilActivity.class);
                intentUpdate.putExtra("TOKEN", token);
                startActivity(intentUpdate);
            }
        });

        return view;
    }

    private void deconect(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Deconnecter");
        builder.setMessage("Voulez vous vraiment vous deconnecter");
        builder.setPositiveButton(
                "Oui",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SessionManager.getInstance(getContext()).logout();
                        getActivity().finish();
                    }
                }
        );

        builder.setNegativeButton(
                "Annuler",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }
        );
        builder.create();
        builder.show();
    }

    private void initView(View view){
        textViewNom = view.findViewById(R.id.idTextViewNomProfile);
        textViewPrenom = view.findViewById(R.id.idTextViewPrenomProfile);
        textViewAdresse = view.findViewById(R.id.idTextViewAdresseProfile);
        textViewNumero = view.findViewById(R.id.idTextViewNumeroProfile);
        textViewEquipe = view.findViewById(R.id.idTextViewEquipeProfile);
        textViewTypeAgent = view.findViewById(R.id.idTextViewTypeAgentProfile);

        textViewTitreEquipe = view.findViewById(R.id.idTitreEquipeProfile);
        textViewTitreTypeAgent = view.findViewById(R.id.idTitreTypeAgentProfile);

        linearLayoutDeconnect = view.findViewById(R.id.lineareLayouteDeconnecter);
        actionButton = view.findViewById(R.id.idAgentUpdate);

        imageView = view.findViewById(R.id.user_profile_photo);
    }

    private void detail(){
        //final List<Projet> all_projet = new ArrayList<Projet>();

        //RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

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
                        if (getContext()!=null){
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
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

        urlUserConnect = ServerConfig.url_sever+"user_connect_info/"+id_user_detail+"/"+type_compte;

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

                                String nom = "", prenom = "", adresse = "", numero = "", equipe="", image_path ="";
                                int gerant = 0, ouvrier = 0, respo_pv =0, respo_ma=0, respo_pa=0;

                                for (int i=0; i<resSucces.length(); i++) {
                                    JSONObject proJson =resSucces.getJSONObject(i);
                                    nom = proJson.getString("nom");
                                    prenom = proJson.getString("prenom");
                                    adresse = proJson.getString("adresse");
                                    numero = proJson.getString("telephone");
                                    gerant = proJson.getInt("gerant");
                                    ouvrier = proJson.getInt("ouvrier");
                                    respo_pv = proJson.getInt("respo_pv");
                                    respo_ma = proJson.getInt("respo_ma");
                                    respo_pa = proJson.getInt("respo_pa");
                                    equipe = proJson.getString("nom_equipe");
                                    image_path = ServerConfig.url_file+"/"+proJson.getString("chemin")+""
                                            +proJson.getString("avatar");
                                }

                                textViewNom.setText(nom);
                                textViewPrenom.setText(prenom);
                                textViewAdresse.setText(adresse);
                                textViewNumero.setText(numero);
                                if (gerant == 1){
                                    textViewTypeAgent.setText("gerant");
                                } else if (ouvrier == 1){
                                    textViewTypeAgent.setText("ouvier");
                                } else if (respo_ma == 1){
                                    textViewTypeAgent.setText("respo_ma");
                                }else if (respo_pv == 1){
                                    textViewTypeAgent.setText("respo_pv");
                                }else if (respo_pa == 1){
                                    textViewTypeAgent.setText("respo_pa");
                                }
                                textViewEquipe.setText(equipe);
                                Glide.with(getContext())
                                        .load(image_path)
                                        .asBitmap()
                                        .into(imageView);

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
}
