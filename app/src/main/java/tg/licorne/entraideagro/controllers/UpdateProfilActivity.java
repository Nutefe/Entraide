package tg.licorne.entraideagro.controllers;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import tg.licorne.entraideagro.Files.FileUtil;
import tg.licorne.entraideagro.R;
import tg.licorne.entraideagro.config.ServerConfig;
import tg.licorne.entraideagro.config.VolleySingleton;

public class UpdateProfilActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView imageView;
    private TextInputEditText textInputEditTextUsename, textInputEditTextPass;
    private AppCompatButton appCompatButton;

    private int id_user_detail, id_role;
    private String utilisateur;
    private String type_compte;
    private static String token;

    private static final int PICK_IMAGE_MULTIPLE = 100;

    String urlDetail= ServerConfig.url_sever +"details";
    String urlUserConnect= ServerConfig.url_sever +"user_connect_info";
    private ArrayList<String> imagesEncodedList;
    private ArrayList<Uri> mArrayUri = new ArrayList<>();
    private String imageEncoded;
    int permissionCheck;

    String urlUpdatePass = ServerConfig.url_sever +"profil";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profil);

        Toolbar toolbar = findViewById(R.id.idToolbarUpdateProfil);
        toolbar.setLogo(getResources().getDrawable(R.mipmap.ic_entraide_round));
        toolbar.setTitle("Modifier Profil");
        setSupportActionBar(toolbar);

        Intent intentExtrat = getIntent();
        if (intentExtrat != null){
            token = intentExtrat.getStringExtra("TOKEN");
        }

        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Permission","Permission not available requesting permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
        }  else {
            Log.i("Permission","Permission has already granted");
        }

        initView();
        initObjet();

        detail();
    }

    private void initView(){
        textInputEditTextUsename = findViewById(R.id.textInputEditTextUsernae);
        textInputEditTextPass = findViewById(R.id.textInputEditTextPass);
        appCompatButton = findViewById(R.id.idAppCompatButtonUpdate);
        imageView = findViewById(R.id.idProfilPicture);
    }

    private void initObjet(){
        imageView.setOnClickListener(this);
        appCompatButton.setOnClickListener(this);
    }

    public void browseClick() {
        Intent intentFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intentFile.addCategory(Intent.CATEGORY_OPENABLE);
        intentFile.setAction(Intent.ACTION_GET_CONTENT);
        intentFile.setType("image/*");
        startActivityForResult(intentFile, PICK_IMAGE_MULTIPLE);
    }

    private Bitmap getBitmap(Uri uri){
        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        try {
            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
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

    private void userConnect(){
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

                                String username = "", image_path = "", ava = "";

                                for (int i=0; i<resSucces.length(); i++) {
                                    JSONObject proJson =resSucces.getJSONObject(i);
                                    username = proJson.getString("username");
                                    utilisateur = proJson.getString("username");
                                    image_path = ServerConfig.url_file+"/"+proJson.getString("chemin")+""
                                            +proJson.getString("avatar");
                                    ava = proJson.getString("avatar");
                                }

                                textInputEditTextUsename.setText(username);
                                //Toast.makeText(UpdateProfilActivity.this, ava.toString(), Toast.LENGTH_SHORT).show();
                                if (!ava.isEmpty()){
                                    Glide.with(UpdateProfilActivity.this)
                                            .load(image_path)
                                            .asBitmap()
                                            .into(imageView);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PICK_IMAGE_MULTIPLE:
                if (resultCode == Activity.RESULT_OK){
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    imagesEncodedList = new ArrayList<>();
                    mArrayUri.clear();
                    Uri uriFileTake = null;
                    if (data!= null){
                        uriFileTake= data.getData();
                        //Log.i("path", FileUtil.getPath(UpdateProfilActivity.this, uriFileTake));
                        mArrayUri.add(uriFileTake);
                        Cursor cursor =getContentResolver().query(uriFileTake, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageEncoded = cursor.getString(columnIndex);
                        imageView.setImageBitmap(getBitmap(uriFileTake));
                        cursor.close();
                    } else {
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
        @POST("GEST_FARM/public/api/avatar")
        Call<UpdateProfilActivity.MyResponse> uploadFile(
                @Part("current_user")RequestBody current_user,
                @Part("id")RequestBody id,
                @Part List<MultipartBody.Part> chg_avatar
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
                    .baseUrl(UpdateProfilActivity.AppConf.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    // la methode d'envoie de rapport avec fichier
    private void uploadFile(){
        final String user = textInputEditTextUsename.getText().toString();
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (Uri p : mArrayUri){
            //Log.i("uri",  p.toString());
            //Log.i("uri and path",  p.getPath().toString());
            Log.i("path", FileUtil.getPath(this, p));
            File file1 = new File(FileUtil.getPath(this, p));
            //File file1 = new File(p.getPath());
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

        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), user);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(id_user_detail));

        UpdateProfilActivity.ApiConfig getResponse = UpdateProfilActivity.AppConf.getRetrofit().create(UpdateProfilActivity.ApiConfig.class);
        Call<UpdateProfilActivity.MyResponse> call = getResponse.uploadFile(username, id, parts);
        call.enqueue(new Callback<UpdateProfilActivity.MyResponse>() {

            @Override
            public void onResponse(Call<UpdateProfilActivity.MyResponse> call, retrofit2.Response<UpdateProfilActivity.MyResponse> response) {
                if (response.body()!= null){
                    Log.i("upload", response.message());
                    Log.i("response", response.body().toString());
                    progressDialog.hide();
                } else {
                    Log.i("error ", response.message());
                    progressDialog.hide();
                }
            }

            @Override
            public void onFailure(Call<UpdateProfilActivity.MyResponse> call, Throwable t) {
                Log.i("Error_upload ", t.getMessage());
                progressDialog.hide();
            }
        });
        parts.clear();
        mArrayUri.clear();
        //textInputEditTextUsename.setText("");
    }

    private void updatePassword(){
        final String username = textInputEditTextUsename.getText().toString();
        final String pass = textInputEditTextPass.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Envoi en cours ...");
        progressDialog.show();

        StringRequest stringMessage = new StringRequest(Request.Method.POST, urlUpdatePass,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        Log.i("response", response.toString());

                        try {
                            //converting response to json object
                            if (response != null){
                                JSONObject obj = new JSONObject(response);
                                progressDialog.hide();
                                Toast.makeText(UpdateProfilActivity.this, "Envoyer avec succes", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.hide();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", pass);
                params.put("id", String.valueOf(id_user_detail));
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
        textInputEditTextPass.setText("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.idProfilPicture:
                browseClick();
                break;
            case R.id.idAppCompatButtonUpdate:
                if (mArrayUri.isEmpty()) {
                    updatePassword();
                } else {
                    uploadFile();
                }
                break;
        }
    }
}
