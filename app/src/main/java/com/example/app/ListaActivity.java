package com.example.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;

public class ListaActivity extends ActionBarActivity {
    public static final String  TAG =
            ListaActivity.class.getSimpleName();
    ListView lista;
    JSONArray datosServidor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        lista = (ListView)findViewById(R.id.listView);

        GetAPI getAPI = new GetAPI();
        getAPI.execute();
    }

    private class GetAPI extends AsyncTask<Object, Void, JSONArray> {
        @Override
        protected void onPreExecute(){
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

            enlistarDatos(respuesta);
        }
    }

    private void enlistarDatos(JSONArray datos) {
        if (datos == null){
            Toast.makeText(this, "Error en el servidor",
                    Toast.LENGTH_LONG).show();
        }
        else{
            datosServidor = datos;
            ArrayList<HashMap<String,String>> valores =
                    new ArrayList<HashMap<String, String>>();

            try{
                for (int i =0; i< datos.length(); i++){
                    JSONObject valor = datos.getJSONObject(i);

                    HashMap<String, String> hashValor =
                            new HashMap<String, String>();

                    hashValor.put("id", valor.getString("id"));
                    hashValor.put("nombre",valor.getString("nombre"));

                    valores.add(hashValor);

                }
                String[] llaves = {"id","nombre"};
                int[] ids = {android.R.id.text2,android.R.id.text1};

                SimpleAdapter adaptador = new SimpleAdapter(this, valores,
                        android.R.layout.simple_list_item_2,
                        llaves, ids);

                lista.setAdapter(adaptador);
                /*lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view, int i, long l) {
                        mostrarAlerta(i);

                    }
                });*/

            }
            catch (JSONException e){}
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lista, menu);
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
