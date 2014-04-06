package com.example.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class PreferenciasActivity extends ActionBarActivity {
    ToggleButton toggleButton;
    Button button;
    Button button2;
    EditText editText;
    CheckBox checkBox;
    String NOMBRE = "hola_mundo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        toggleButton = (ToggleButton)findViewById(R.id.togPref);
        button = (Button)findViewById(R.id.btnPref);
        button2 = (Button)findViewById(R.id.btnInterno);
        editText = (EditText)findViewById(R.id.edPref);
        checkBox = (CheckBox)findViewById(R.id.chkPref);

        SharedPreferences sharedPreferences;
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        boolean opcion1;
        boolean opcion2;
        //String opcion3;

        opcion1 = sharedPreferences.getBoolean("opcion1", false);
        opcion2 = sharedPreferences.getBoolean("opcion2", true);
        //opcion3 = sharedPreferences.getString("opcion3", "hola");

        toggleButton.setChecked(opcion1);
        checkBox.setChecked(opcion2);
        //editText.setText(opcion3);
        leerMemoriaInterna ();

    }

    public void guardarPreferencias (View v){

        SharedPreferences sharedPreferences;
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("opcion1", toggleButton.isChecked());
        editor.putBoolean("opcion2", checkBox.isChecked());
        //editor.putString("opcion3", editText.getText().toString());

        editor.commit();

    }

    public void guardarMemoriaInterna(View v){
        try{
            String texto = editText.getText().toString();

            FileOutputStream fileOutputStream =
                    openFileOutput(NOMBRE, Context.MODE_PRIVATE);

            //MODE_APPEND, MODE_WORLD_READABLE, and MODE_WORLD_WRITEABLE

            fileOutputStream.write(texto.getBytes());
            fileOutputStream.close();
        }
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
    }

    public void leerMemoriaInterna (){
        try{
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

            textoMemoria = sBuilder.toString();

            editText.setText(textoMemoria);

        }
        catch (FileNotFoundException e){}
        catch (IOException e){}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.preferencias, menu);
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

    /**
     * A placeholder fragment containing a simple view.*/
}
