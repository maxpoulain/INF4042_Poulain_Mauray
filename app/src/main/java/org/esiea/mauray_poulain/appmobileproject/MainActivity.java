package org.esiea.mauray_poulain.appmobileproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.IOException;
import java.util.Locale;

import pl.droidsonroids.gif.GifDrawable;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {



        new Thread(new Runnable() {
            GifDrawable gifFromResource = new GifDrawable(getResources(), R.drawable.giphy);
            @Override
            public void run() {
                try{
                    gifFromResource.start();
                    synchronized(this){
                        wait(3570);
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

        ImageView fashionImg = (ImageView) findViewById(R.id.button);
        // set a onclick listener for when the button gets clicked
        fashionImg.setOnClickListener(new View.OnClickListener() {
            // Start new list activity
            public void onClick(View v) {
                IntentFonction();
                GetBierServices.startActionBiers(getApplicationContext());
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

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


    public void IntentFonction(){

        try {
            startActivity(new Intent(MainActivity.this,SecondeActivity.class));
        }catch (Exception e){
            Toast toast = Toast.makeText(this,"Vous n'avez pas accès au trésor des bières !!",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
