package tg.licorne.entraideagro.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tg.licorne.entraideagro.Files.FileUtil;
import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.config.VolleySingleton;
import tg.licorne.entraideagro.controllers.EditeRapportActivity;
import tg.licorne.entraideagro.controllers.UpdateProfilActivity;
import tg.licorne.entraideagro.helper.SessionManager;

/**
 * Created by Admin on 09/04/2018.
 */

public class FragmentPromoteurProfile extends Fragment {

    private TextView textViewNom, textViewAdresse, textViewNumero;
    private TextView textViewPrenom;
    private FloatingActionButton actionButton;

    private ImageView imageView;

    private LinearLayout linearLayoutDeconnect;

    private int id_user_detail, id_role;
    private String token, type_compte;

    private static final int PICK_IMAGE_MULTIPLE = 100;

    String urlDetail= ServerConfig.url_sever +"details";
    String urlUserConnect= ServerConfig.url_sever +"user_connect_info";
    private ArrayList<String> imagesEncodedList;
    private ArrayList<Uri> mArrayUri;
    private String imageEncoded;

    public static FragmentPromoteurProfile newInstance(){
        FragmentPromoteurProfile fragment = new FragmentPromoteurProfile();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_coor, container, false);
        Bundle bundle = getArguments();

        if (bundle!=null){
            if (bundle.containsKey("TOKEN") || bundle.containsKey("ID_ROLE")){
                token = bundle.getString("TOKEN");
                id_role = bundle.getInt("ID_ROLE");
            }
        }

        Log.i("role", String.valueOf(id_role));
        initView(view);

        if (checkNetworkConnexion()){
            detail();
        }

        linearLayoutDeconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deconect();
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

        linearLayoutDeconnect = view.findViewById(R.id.lineareLayouteDeconnecterCoor);
        actionButton = view.findViewById(R.id.updateProfil);
        imageView = view.findViewById(R.id.user_profile_photo);
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
                                Log.i("success", resSucces.toString());

                                String nom = "", prenom = "", adresse = "", numero = "", image_path ="";
                                int gerant = 0, ouvrier = 0, respo_pv =0, respo_ma=0, respo_pa=0;

                                for (int i=0; i<resSucces.length(); i++) {
                                    JSONObject proJson =resSucces.getJSONObject(i);
                                    nom = proJson.getString("nom");
                                    prenom = proJson.getString("prenom");
                                    adresse = proJson.getString("adresse");
                                    numero = proJson.getString("telephone");
                                    image_path = ServerConfig.url_file+"/"+proJson.getString("chemin")+""
                                            +proJson.getString("avatar");
                                }

                                textViewNom.setText(nom);
                                textViewPrenom.setText(prenom);
                                textViewAdresse.setText(adresse);
                                textViewNumero.setText(numero);
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
                        /*if (getContext() != null){
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }*/
                        Log.i("error", error.getMessage().toString());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
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
