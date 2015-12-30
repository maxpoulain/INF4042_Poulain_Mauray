package org.esiea.mauray_poulain.appmobileproject;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

/**
 * Created by Maxime on 28/12/2015.
 */
public class ThirdActivityBis extends AppCompatActivity{
    private static final String TAG = ThirdActivityBis.class.getSimpleName();
    public int id_position;
    JSONObject tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thirdbis);
        Log.d(TAG, "Troisième activité BIS");

        Bundle extras = getIntent().getExtras();
        id_position = extras.getInt("position")+1;
        GetBierServices.startActionBier(this, id_position);
        tab = getBiersFromOtherFile();
        Affichage();

    }

    public void Affichage(){
        ImageView image = (ImageView) findViewById(R.id.rv_bier_description_thumb);
        TextView name = (TextView) findViewById(R.id.rv_bier_description_name);
        TextView description = (TextView) findViewById((R.id.rv_bier_description_description));
        TextView category = (TextView) findViewById((R.id.rv_bier_description_category));
        TextView note = (TextView) findViewById((R.id.rv_bier_description_note));
        TextView buveur = (TextView) findViewById((R.id.rv_bier_description_buveur));
        TextView pays = (TextView) findViewById(R.id.rv_bier_description_pays);

        try {


            image.setImageResource(R.drawable.ic_notif);
            image.setTag(tab.getInt("id"));
            new DownloadImageTask().execute(tab.getInt("id"), image);

            String name_str =  tab.getString("name");
            String description_str = tab.getString("description");
            String category_str = tab.getString("category");
            String note_str = tab.getString("note");
            String buveur_str = tab.getString("buveur");
            JSONObject pays_str = tab.getJSONObject("country");
            String pays_name = pays_str.getString("name");
            name.setText(name_str);
            if(category_str.contains("null")){
                if(Locale.getDefault().getLanguage().contains("fr")){
                    category_str = "Pas encore de catégorie";
                }
                if(Locale.getDefault().getLanguage().contains("en")) {
                    category_str = "No category yet";
                }
            }
           Log.d(TAG, "LANGUE : "+Locale.getDefault().getLanguage());

            category.setText(category_str);
            description.setText("Description : \n" + description_str);
            if(note_str.contains("null")){
                if(Locale.getDefault().getLanguage().contains("fr")){
                    note_str = "Pas encore de note";
                }
                if(Locale.getDefault().getLanguage().contains("en")) {
                    note_str = "No note yet";
                }

            }
            note.setText("Note : "+note_str);
            if(Locale.getDefault().getLanguage().contains("fr")){
                buveur.setText("Testée par "+buveur_str);
                pays.setText("Pays d'origine : "+pays_name);
            }
            if(Locale.getDefault().getLanguage().contains("en")) {
                buveur.setText("Tested by "+buveur_str);
                pays.setText("Country of origin : "+pays_name);
            }






        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    public JSONObject getBiersFromOtherFile(){
        try {
            InputStream is3 = new FileInputStream(getCacheDir()+"/"+id_position+".json");
            byte[] buffer3 = new byte[is3.available()];
            is3.read(buffer3);
            is3.close();
            return new JSONObject(new String(buffer3, "UTF-8"));
        }catch (IOException e){
            e.printStackTrace();
            return new JSONObject();
        }catch (JSONException e){
            e.printStackTrace();
            return new JSONObject();
        }
    }


    public void notification_test(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_notif).setContentTitle("Fin téléchargement Description").setContentText("Le fichier JSON a été téléchargé");
        NotificationManager notifman = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notifman.notify(10, builder.build());

    }




    private class DownloadImageTask extends AsyncTask<Object, Integer,Void> {

            int position = 0;
            ImageView thumb=null;

            @Override
            protected Void doInBackground(Object... params) {
                position =(int) params[0];
                thumb = (ImageView) params[1];
                try {

                    if(((File)new File(getCacheDir().getAbsolutePath() + "/image" + position)).exists())
                        return null;

                    Log.d(TAG, "Task :"+position);
                    JSONObject object = GetBierServices.handleBierActionThumb(getApplicationContext(),position);
                    JSONObject obj = object.getJSONObject("image");
                    JSONObject image = obj.getJSONObject("image");
                    String uri =  image.getString("url");
                    Log.d(TAG, "URI IMAGE:"+uri);
                    URL urlfin = new URL("http://binouze.fabrigli.fr"+uri);
                    GetBierServices.handleBierActionDownloadImage(getApplicationContext(), urlfin, position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Void result){
                Uri uri = Uri.parse(getCacheDir().getAbsolutePath()+"/image"+position);
                Log.d(TAG, "URI: "+uri);
                Log.d(TAG, "onPost: " + (new File(getCacheDir().getAbsolutePath() + "/image" + position).getAbsolutePath()));
                if(((int)thumb.getTag()) == position ) {
                    thumb.setImageURI(uri);
                }
            }

        }
}
