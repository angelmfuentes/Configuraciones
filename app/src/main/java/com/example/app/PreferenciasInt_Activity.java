package com.example.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PreferenciasInt_Activity extends ActionBarActivity {
    protected TextView txtV1;
    protected TextView txtV2;
    protected TextView txtV3;
    protected TextView txtV4;
    String NOMBRE = "Datos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias_int_);

        txtV1 = (TextView) findViewById(R.id.txtV1);
        txtV2 = (TextView) findViewById(R.id.txtV2);
        txtV3 = (TextView) findViewById(R.id.txtV3);
        txtV4 = (TextView) findViewById(R.id.txtV4);

        SharedPreferences sharedPreferences;
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        boolean opcion1;
        opcion1 = sharedPreferences.getBoolean("opcion1", false);

        leerMemoriaInterna ();
    }

    public void leerMemoriaInterna () {
        try {
            String textoMemoria;
            FileInputStream fileInputStream =
                    openFileInput(NOMBRE);

            BufferedReader bReader = new BufferedReader(
                    new InputStreamReader(fileInputStream, "UTF-8"), 8);

            StringBuilder sBuilder = new StringBuilder();

            String line = null;
            while ((line = bReader.readLine()) != null) {
                sBuilder.append(line);
            }

            try { JSONObject respuesta = new JSONObject(sBuilder.toString());
                txtV1.setText(respuesta.getString("telefono"));
                txtV2.setText(respuesta.getString("nombre"));
                txtV3.setText(respuesta.getString("hora"));
                txtV4.setText(respuesta.getString("activo"));
            } catch (JSONException e) {}
         } catch (FileNotFoundException e) { obtenerTextoInternet();
         } catch (IOException e) {
         }
    }

    private void obtenerTextoInternet() {
        if(isNetworkAvailable()){
            GetAPI getAPI = new GetAPI();
            getAPI.execute();
        }else{
            //Toast.makeText(this, "Hola", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        boolean isAvailable = false;

        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        else{
            Toast.makeText(this, "Sin Conexión", Toast.LENGTH_LONG);
        }

        return isAvailable;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.preferencias_int_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetAPI extends AsyncTask<Object, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Object... objects) {

            int responseCode = -1;
            String resultado = "";
            JSONObject jsonResponse = null;

            try {
                URL apiURL = new URL(
                        "http://continentalrescueafrica.com/2013/test.php");

                HttpURLConnection httpConnection = (HttpURLConnection)
                        apiURL.openConnection();
                httpConnection.connect();
                responseCode = httpConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpConnection.getInputStream();
                    BufferedReader bReader = new BufferedReader(
                            new InputStreamReader(inputStream, "UTF-8"), 8);

                    StringBuilder sBuilder = new StringBuilder();

                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        sBuilder.append(line + "\n");
                    }
                    inputStream.close();
                    resultado = sBuilder.toString();

                    try{
                        FileOutputStream fileOutputStream =
                        openFileOutput(NOMBRE, Context.MODE_PRIVATE);
                        fileOutputStream.write(resultado.getBytes());
                        fileOutputStream.close();
                    }
                    catch (FileNotFoundException e) {}
                    catch (IOException e) {}

                    System.out.println(resultado);
                    jsonResponse = new JSONObject(resultado);
                }
            } catch (JSONException e) {
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            } catch (Exception e) {
            }

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(JSONObject respuesta) {
            try {
                //JSONObject jsonObject = respuesta.getJSONObject;
                txtV1.setText(respuesta.getString("telefono"));
                txtV2.setText(respuesta.getString("nombre"));
                txtV3.setText(respuesta.getString("hora"));
                txtV4.setText(respuesta.getString("activo"));
            } catch (JSONException e) {
            }
        }
    }
}