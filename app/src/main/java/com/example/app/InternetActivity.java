package com.example.app;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class InternetActivity extends ActionBarActivity {

    public static final String  TAG = InternetActivity.class.getSimpleName();
    protected TextView textResultado;
    protected TextView textNombre;
    protected ProgressBar barra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);

        textResultado = (TextView)findViewById(R.id.textoMensaje);
        textNombre = (TextView)findViewById(R.id.txtNombre);
        barra = (ProgressBar)findViewById(R.id.progressBar);
        obtenerTextoInternet();
    }

    private void obtenerTextoInternet() {
        if(isNetworkAvailable()){
            GetAPI getAPI = new GetAPI();
            getAPI.execute();
        }else{
            //Toast.makeText(this, "Hola", Toast.LENGTH_LONG).show();
            mostrarAlerta();
        }
    }

    private void mostrarAlerta() {
        textNombre.setText("");
        textResultado.setText("");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_error));
        builder.setMessage(getString(R.string.title_error_mensaje));
        builder.setPositiveButton(android.R.string.ok,null);

        AlertDialog alertDialog =builder.create();
        alertDialog.show();
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

    private class GetAPI extends AsyncTask<Object, Void, JSONArray>{
        @Override
        protected void onPreExecute(){
            barra.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONArray doInBackground(Object... objects) {
            Log.d(TAG, "Response OK");

            int responseCode = -1;
            String resultado = "";
            JSONArray jsonResponse = null;

            try{
                URL apiURL =  new URL(
                        "http://continentalrescueafrica.com/2013/testJSON.php");

                HttpURLConnection httpConnection = (HttpURLConnection)
                        apiURL.openConnection();
                httpConnection.connect();
                responseCode = httpConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK){
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
                    Log.d(TAG, resultado);
                    jsonResponse = new JSONArray(resultado);

                }else{
                    Log.i(TAG, "Error en el HTTP " + responseCode);
                }
            }
            catch (JSONException e){}
            catch (MalformedURLException e){}
            catch (IOException e){}
            catch (Exception e){}

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(JSONArray respuesta) {
            try{
                barra.setVisibility(View.INVISIBLE);
                JSONObject jsonObject = respuesta.getJSONObject(0);
                textResultado.setText(jsonObject.getString("mensaje") );
                textNombre.setText( jsonObject.getString("nombre"));
            }
            catch (JSONException e){}
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.internet, menu);
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

}