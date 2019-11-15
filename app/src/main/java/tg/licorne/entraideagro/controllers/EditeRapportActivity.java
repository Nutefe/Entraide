package tg.licorne.entraideagro.controllers;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import tg.licorne.entraideagro.BuildConfig;
import tg.licorne.entraideagro.Files.FileUtil;
import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.config.VolleySingleton;
import tg.licorne.entraideagro.model.Activites;
import tg.licorne.entraideagro.model.Agents;
import tg.licorne.entraideagro.model.TypeAgent;

import static tg.licorne.entraideagro.Files.FileUtil.getPath;

/**
 * Created by Admin on 21/02/2018.
 */

public class EditeRapportActivity extends AppCompatActivity {

    private File file;
    private Uri uri, photoURI, videoURI;

    private static final int REQUEST_VIDEO_CAPTURE = 6384;
    private static final int PICK_IMAGE_MULTIPLE = 1;
    private static final String TAG = "EditeRapportActivity";

    private String fileType="", path, imageEncoded, mCurrentVideoPath, mCurrentPhotoPath;

    private static final int TAKE_PICTURE = 10;

    int permisCamera, permissionCheck;

    List<String> imagesEncodedList;

    List<TypeAgent> agentList = new ArrayList<>();
    List<String> agents =  new ArrayList<>();
    private  Activites activites;
    private Agents agent;


    private Spinner spinnerTypeAgent, spinnerAgent, spinnerActiviter;
    private View viewL;
    private LinearLayout linearLayout;
    private EditText editTextObjet, editTextContenu;

    private static int id_type_agent = 0, id_agent = 0, id_activite = 0;

    String urlDetail= ServerConfig.url_sever +"details";
    String urlAgent = ServerConfig.url_sever +"reciever";
    String urlActiviter = ServerConfig.url_sever +"select_activite";
    String urlSendRapport = ServerConfig.url_sever +"rapport";

    private static int id_user_detail = 0, zone_id =0;
    private static String token, type_compte;

    ArrayList<Uri> mArrayUri = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edite_rapport_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.idToolbarEditeRapport);
        toolbar.setLogo(getResources().getDrawable(R.mipmap.ic_entraide_round));
        toolbar.setTitle("Envoyer rapport");
        setSupportActionBar(toolbar);

        Intent intentExtrat = getIntent();
        if (intentExtrat != null){
            token = intentExtrat.getStringExtra("TOKEN");
        }

        // ajouter les permission de camera et de lecture sur le stockage
        permisCamera = ActivityCompat.checkSelfPermission(EditeRapportActivity.this, Manifest.permission.CAMERA);
        permissionCheck = ContextCompat.checkSelfPermission(EditeRapportActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"Permission not available requesting permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        } else {
            Log.d(TAG,"Permission has already granted");
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"Permission not available requesting permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
        }  else {
            Log.d(TAG,"Permission has already granted");
        }

        initView();
        loadSpinnerTypeAgent();
        detail();
        spinnerAgent.setVisibility(View.GONE);
        viewL.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
    }

    // la methode pour initialiser les vues
    private void initView(){
        spinnerTypeAgent = findViewById(R.id.idSpinnerTypeAgent);
        spinnerAgent = findViewById(R.id.idSpinnerAgent);
        spinnerActiviter = findViewById(R.id.idSpinnerActiviter);
        viewL = findViewById(R.id.idViewLineSpinner2);
        linearLayout = findViewById(R.id.idLineareLayouteAgent);
        editTextObjet = findViewById(R.id.idEditTextObjet);
        editTextContenu = findViewById(R.id.idEditTextRapport);
    }

    //
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
                                zone_id = resSucces.getInt("zone_id");
                                Log.i("id", String.valueOf(id_user_detail));

                                loadSpinnerActivite();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(EditeRapportActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    // la methode de chargement du type agent dans le select
    private void loadSpinnerTypeAgent(){
        agentList.add(new TypeAgent(0, "Selectionner le type agent"));
        agentList.add(new TypeAgent(1, "Tout"));
        agentList.add(new TypeAgent(2, "Directeur général"));
        agentList.add(new TypeAgent(3, "Coordinateur National"));
        agentList.add(new TypeAgent(4, "Coordinateur Régional"));
        agents.add("Selectionner le type agent");
        agents.add("Tout");
        agents.add("Directeur général");
        agents.add("Coordinateur National");
        agents.add("Coordinateur Régional");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditeRapportActivity.this, android.R.layout.simple_spinner_item, agents);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeAgent.setAdapter(arrayAdapter);
        spinnerTypeAgent.setClickable(true);
        spinnerTypeAgent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                id_type_agent = agentList.get(i).getId();
                if (id_type_agent!=0 && id_type_agent!=1){
                    spinnerAgent.setVisibility(View.VISIBLE);
                    viewL.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                    loadSpinnerAgent();
                } else {
                    spinnerAgent.setVisibility(View.GONE);
                    viewL.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // la methode de chargement de l'agent dans le select
    private void loadSpinnerAgent(){
        final List<String> agents = new ArrayList<String>();
        final List<Agents> agentList = new ArrayList<Agents>();
        urlAgent = ServerConfig.url_sever +"reciever/"+id_type_agent;
        //Toast.makeText(EditeRapportActivity.this, String.valueOf(id_type_agent), Toast.LENGTH_SHORT).show();
        StringRequest stringMessage = new StringRequest(Request.Method.GET, urlAgent,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        Log.i("response", response.toString());

                        try {
                            //converting response to json object
                            if (response != null){
                                JSONObject obj = new JSONObject(response);
                                JSONArray res = obj.getJSONArray("success");
                                for (int i=0; i<res.length(); i++) {
                                    //if no error in response
                                    JSONObject proJson =res.getJSONObject(i);

                                    agent = new Agents(
                                            proJson.getInt("id"),
                                            proJson.getString("username")
                                    );
                                    int id_user = proJson.getInt("id");
                                    String nom = proJson.getString("username");
                                    agents.add(nom);
                                    agentList.add(agent);
                                }
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditeRapportActivity.this, android.R.layout.simple_spinner_item, agents);
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerAgent.setAdapter(arrayAdapter);
                                spinnerAgent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        id_agent = agentList.get(i).getId();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                        Log.i("error", error.getMessage().toString());
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                String auth= "Bearer "+ token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringMessage);
    }

    // la methode de chargement des activiter lier à une ferme
    private void loadSpinnerActivite(){
        final List<String> activite = new ArrayList<String>();
        final List<Activites> activiteList = new ArrayList<Activites>();
        urlActiviter = ServerConfig.url_sever +"select_activite/"+id_user_detail;
        //Toast.makeText(EditeRapportActivity.this, String.valueOf(id_type_agent), Toast.LENGTH_SHORT).show();
        StringRequest stringMessage = new StringRequest(Request.Method.GET, urlActiviter,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.i("response", response.toString());
                        try {
                            //converting response to json object
                            if (response != null){
                                JSONObject obj = new JSONObject(response);
                                JSONArray res = obj.getJSONArray("success");
                                for (int i=0; i<res.length(); i++) {
                                    //if no error in response
                                    JSONObject proJson =res.getJSONObject(i);

                                    activites = new Activites(
                                            proJson.getInt("id"),
                                            proJson.getString("nom_activite")
                                    );
                                    int id_user = proJson.getInt("id");
                                    String nom = proJson.getString("nom_activite");
                                    activite.add(nom);
                                    activiteList.add(activites);
                                }
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditeRapportActivity.this, android.R.layout.simple_spinner_item, activite);
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerActiviter.setAdapter(arrayAdapter);
                                spinnerActiviter.setClickable(true);
                                spinnerActiviter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        id_activite = activiteList.get(i).getId();
                                        //Toast.makeText(EditeRapportActivity.this, String.valueOf(id_activite), Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                        Log.i("error", error.getMessage().toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                String auth= "Bearer "+ token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringMessage);
    }

    // la methode d'envoie de rapport sans les fichiers
    private void sendRapport(){
        final String contenu = editTextContenu.getText().toString();
        final String objet = editTextObjet.getText().toString();

        if (TextUtils.isEmpty(contenu)) {
            editTextContenu.setError("Entrer le rapport");
            editTextContenu.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(objet)) {
            editTextObjet.setError("Entrer l'objet du rapport");
            editTextObjet.requestFocus();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Envoi en cours ...");
        progressDialog.show();

        StringRequest stringMessage = new StringRequest(Request.Method.POST, urlSendRapport,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        Log.i("response", response.toString());

                        try {
                            //converting response to json object
                            if (response != null){
                                JSONObject obj = new JSONObject(response);
                                progressDialog.hide();
                                Toast.makeText(EditeRapportActivity.this, "Rapport envoyé avec success", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Log.i("error", error.getMessage().toString());
                        Toast.makeText(getApplicationContext(), "erreur vérifier votre connexion", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("objet", objet);
                params.put("contenu", contenu);
                params.put("activite_id", String.valueOf(id_activite));
                params.put("sender_id", String.valueOf(id_user_detail));
                params.put("zone_id", String.valueOf(zone_id));
                params.put("reciever_id", String.valueOf(id_agent));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                String auth= "Bearer "+ token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringMessage);
        editTextObjet.setText("");
        editTextContenu.setText("");
    }

    //menu contextuel
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contextual_menu, menu);
        return true;
    }

    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        String title = (String) item.getTitle();
        switch (item.getItemId()){
            case R.id.idItemPhoto:
                try {
                    takePhoto();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.idItemPiece:
                browseClick();
                break;
            case R.id.idItemVideo:
                try {
                    camera();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.idItemSend:
                if (file != null){
                    sendRapport();
                } else {
                    uploadFile();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //la methode de creation de fichier image
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    // la methode de creation de fichier video
    private File createVideoFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String videoFileName = "MP4_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File video = File.createTempFile(
                videoFileName,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentVideoPath = video.getAbsolutePath();
        return video;
    }

   // @RequiresApi(api = Build.VERSION_CODES.M)
    private void camera() throws IOException {
            invokeVideoCamera();
    }

    // la methode pour faire apelle à la camera
    private void invokeVideoCamera() throws IOException {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            File videoFile = null;
            try {
                videoFile = createVideoFile();
            } catch (IOException ex) {
            }
            if (videoFile != null) {
                videoURI = FileProvider.getUriForFile(EditeRapportActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        createVideoFile());

                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
        }
    }

    // la methode pour selectionner un
    public void browseClick() {
        Intent intentFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intentFile.addCategory(Intent.CATEGORY_OPENABLE);
        intentFile.setAction(Intent.ACTION_GET_CONTENT);
        intentFile.setType("*/*");
        startActivityForResult(intentFile, PICK_IMAGE_MULTIPLE);
    }

    public void takePhoto() throws IOException {
                invokeCamera();
    }

    private void invokeCamera() throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                //photoFile.createNewFile();
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(EditeRapportActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, TAKE_PICTURE);
            }
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                try {
                    invokeCamera();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == CAMERA_PERMISSION_REQUEST_VIDEO){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                try {
                    invokeVideoCamera();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    mArrayUri.clear();
                    Uri selectedImage = Uri.parse(mCurrentPhotoPath);
                    path = selectedImage.getPath();
                    Log.i("path", path);
                    Toast.makeText(this, "success take picture", Toast.LENGTH_SHORT).show();
                    mArrayUri.add(selectedImage);
                    try {
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to take picture", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
                break;
            case REQUEST_VIDEO_CAPTURE:
                if (resultCode == RESULT_OK){
                        mArrayUri.clear();
                        uri = Uri.parse(mCurrentVideoPath);
                        path = uri.getPath();
                        Log.i("absolut", mCurrentPhotoPath);
                        Toast.makeText(this, "success record video", Toast.LENGTH_SHORT).show();
                        mArrayUri.add(uri);
                        try{

                        } catch (Exception e){
                            Toast.makeText(EditeRapportActivity.this, "failed to record video", Toast.LENGTH_SHORT).show();
                            Log.i("error", e.getMessage().toString());
                        }
                }
                break;
            case PICK_IMAGE_MULTIPLE:
                    if (resultCode == Activity.RESULT_OK){
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        imagesEncodedList = new ArrayList<>();
                        mArrayUri.clear();
                        Uri uriFileTake = null;
                        if (data!= null){
                            uriFileTake= data.getData();
                            Log.i("path", FileUtil.getPath(EditeRapportActivity.this, uriFileTake));
                            mArrayUri.add(uriFileTake);
                            Cursor cursor =getContentResolver().query(uriFileTake, filePathColumn, null, null, null);
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded = cursor.getString(columnIndex);
                            cursor.close();
                        } else {
                            /*if (data.getClipData() != null){
                                ClipData mClipData = data.getClipData();
                                mArrayUri.clear();
                                for (int i = 0; i<mClipData.getItemCount(); i++){
                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri uri = item.getUri();
                                    mArrayUri.add(uri);
                                    Toast.makeText(EditeRapportActivity.this, path.toString(), Toast.LENGTH_SHORT).show();
                                    Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                                    cursor.moveToFirst();
                                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                    imageEncoded  = cursor.getString(columnIndex);
                                    imagesEncodedList.add(imageEncoded);
                                    cursor.close();
                                }
                            }*/
                        }
                    } else {
                        Toast.makeText(this, "You haven't picked Image",
                                Toast.LENGTH_LONG).show();
                    }
                break;
        }
    }

    public class MyResponse {
        boolean error;
        String message;
    }

    // interface d'initialisation des élément a envoyer dans la base
    public interface ApiConfig{
        @Multipart
        @POST("GEST_FARM/public/api/rapport")
        Call<MyResponse> uploadFile(
                @Part("objet") RequestBody objet,
                @Part("contenu")RequestBody contenu,
                @Part("activite_id")RequestBody activite_id,
                @Part("sender_id")RequestBody sender_id,
                @Part("zone_id")RequestBody zone_id,
                @Part("reciever_id")RequestBody reciever_id,
                @Part List<MultipartBody.Part> fichiers
        );
    }

    // la class de configuration pour avoyé un rapport avec fichier
    public static class AppConf{
        // déclaration de l'url d'envoie de rapport
        private static String BASE_URL = ServerConfig.url_port_ip;

        static OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.MINUTES)
                .writeTimeout(60, TimeUnit.MINUTES)
                .addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request requestNew = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer "+ token)
                        .addHeader("Accept", "application/json")
                        .build();
                return chain.proceed(requestNew);
            }
        }).build();


        public static Retrofit getRetrofit(){
            return new Retrofit.Builder()
                    .client(client)
                    .baseUrl(AppConf.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    // la methode d'envoie de rapport avec fichier
    private void uploadFile(){
        final String contenu = editTextContenu.getText().toString();
        final String objet = editTextObjet.getText().toString();
        if (TextUtils.isEmpty(contenu)) {
            editTextContenu.setError("Entrer le rapport");
            editTextContenu.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(objet)) {
            editTextObjet.setError("Entrer l'objet du rapport");
            editTextObjet.requestFocus();
            return;
        }
        //Log.i("uir array", mArrayUri.toString());
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (Uri p : mArrayUri){
            //Log.i("uri",  p.toString());
            //Log.i("uri and path",  p.getPath().toString());
            //Log.i("path", FileUtil.getPath(this, p));
            File file1 = new File(FileUtil.getPath(this, p));
            //File file1 = new File(p.getPath());
            file = file1;
            if (file1.exists()){
                Log.i("file", "file existe");
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse("fichiers[]"), file1);
            MultipartBody.Part fileUpload = MultipartBody.Part.createFormData("fichiers[]", file1.getName(), requestBody);
            parts.add(fileUpload);
        }

        Log.i("part", parts.toString());
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Envoi en cours ...");
        progressDialog.show();

        int zon = zone_id;
        RequestBody contenuRap = RequestBody.create(MediaType.parse("text/plain"), contenu);
        RequestBody objetRap = RequestBody.create(MediaType.parse("text/plain"), objet);
        RequestBody activite_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(id_activite));
        RequestBody sender_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(id_user_detail));
        RequestBody zone_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(zon));
        RequestBody reciever_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(id_agent));

        ApiConfig getResponse = AppConf.getRetrofit().create(ApiConfig.class);
        Call<MyResponse> call = getResponse.uploadFile( objetRap, contenuRap, activite_id, sender_id, zone_id, reciever_id, parts);
        call.enqueue(new Callback<MyResponse>() {

            @Override
            public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {
                if (response.body()!= null){
                    Log.i("upload", response.message());
                    progressDialog.hide();
                    Toast.makeText(EditeRapportActivity.this, "Rapport envoyé avec success", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("error ", response.message());
                    progressDialog.hide();
                    Toast.makeText(EditeRapportActivity.this, "Erreur vérifier votre connexion", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.i("Error_upload ", t.getMessage());
                progressDialog.hide();
                Toast.makeText(EditeRapportActivity.this, "Echec d'envoie", Toast.LENGTH_SHORT).show();
            }
        });
        fileType = "";
        file =null;
        parts.clear();
        mArrayUri.clear();
        editTextObjet.setText("");
        editTextContenu.setText("");
    }
}
