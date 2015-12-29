package org.esiea.mauray_poulain.appmobileproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;


import java.io.IOException;
import java.util.Locale;
import java.util.Timer;

import pl.droidsonroids.gif.GifDrawable;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        Log.d(TAG, "LANGUAGE : " + Locale.getDefault().getDisplayLanguage());

        if(Locale.getDefault().getDisplayLanguage().contains("English")){
            toolbar.setTitle("Beer Palace");
            Log.d(TAG, "ENGLISH");
        }else if(Locale.getDefault().getDisplayLanguage().contains("fra")) {
            toolbar.setTitle("Palais de la Bière");
        }

        setSupportActionBar(toolbar);

        try {



        new Thread(new Runnable() {
            GifDrawable gifFromResource = new GifDrawable(getResources(), R.drawable.giphy);
            @Override
            public void run() {
                try{
                    gifFromResource.start();
                    synchronized(this){
                        wait(3560);
                    }
                    gifFromResource.stop();
                    IntentFonction();
                    GetBierServices.startActionBiers(getApplicationContext());
                    Log.d(TAG, "LANCEMENT OK");
                }catch (InterruptedException e){

                }
            }
        }).start();


        }catch (IOException e){

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_settings) {
            return true;
        }
        if (id == R.id.menu_search) {
            Toast toast = Toast.makeText(this,"Cette fonctionnalité est disponible dans la version payante de l'application",Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }
        if(id==R.id.menu_about){
            Toast toast = Toast.makeText(this,"Cette application a été codée par Maxime Mauray & Maxime Poulain",Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void actionbouton(View v){


        IntentFonction();
        GetBierServices.startActionBiers(this);
        Log.d(TAG, "LANCEMENT OK");

    }

    public void IntentFonction(){

        try {
            startActivity(new Intent(MainActivity.this,SecondeActivity.class));
        }catch (Exception e){
            Toast toast = Toast.makeText(this,"Vous n'avez pas accès au trésor des bières !!",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
