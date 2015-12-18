package org.esiea.mauray_poulain.appmobileproject;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class ThirdActivity extends Activity {

    private RecyclerView rv3;
    private BiersAdapterTest adapter3;
    private static final String TAG = ThirdActivity.class.getSimpleName();
    public int id_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        Log.d(TAG, "Troisième activité");

        Bundle extras = getIntent().getExtras();
        id_position = extras.getInt("position")+1;
        IntentFilter intentFilter = new IntentFilter(BIER_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BierUpdateTest(), intentFilter);
        GetBierServices.startActionBier(this,id_position);
        rv3 = (RecyclerView)findViewById(R.id.rv_bier);
        rv3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        JSONObject tab = getBiersFromOtherFile();
        adapter3 = new BiersAdapterTest(tab);
        rv3.setAdapter(adapter3);
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

    public static final String BIER_UPDATE="com.octip.cours.inf4042_11.BIER_UPDATE";
    public class BierUpdateTest extends BroadcastReceiver {



        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "OK3:" + getIntent().getAction());
            notification_test();
            adapter3.setNewBiere();
            Log.d(TAG, "setNewBiere3 OK");

        }
    }

    private class BiersAdapterTest extends RecyclerView.Adapter<BiersAdapterTest.BierHolderTest>{

        public JSONObject bier;

        public class BierHolderTest extends RecyclerView.ViewHolder{
            public TextView name;
            public TextView category;
            public TextView description;
            public TextView note;
            public TextView buveur;
            public TextView pays;
            //public ImageView thumb;
            public BierHolderTest(View v){
                super(v);
                name = (TextView) itemView.findViewById(R.id.rv_bier_description_name);
                description = (TextView) itemView.findViewById((R.id.rv_bier_description_description));
                category = (TextView) itemView.findViewById((R.id.rv_bier_description_category));
                note = (TextView) itemView.findViewById((R.id.rv_bier_description_note));
                buveur = (TextView) itemView.findViewById((R.id.rv_bier_description_buveur));
                pays = (TextView) itemView.findViewById(R.id.rv_bier_description_pays);
                //thumb = (ImageView) itemView.findViewById(R.id.rv_bier_description_thumb);
            }
        }

        public BiersAdapterTest (JSONObject jsonArray){
            bier = jsonArray;

        }


        @Override
        public BierHolderTest onCreateViewHolder(ViewGroup parent, int viewType) {
            View v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_bier_description, parent, false);
            BierHolderTest bh = new BierHolderTest(v);
            return bh;
        }

        @Override
        public void onBindViewHolder(BierHolderTest holder, int position) {
            try {
                String nmbier =  bier.getString("name");
                String descript = bier.getString("description");
                String category = bier.getString("category");
                String note = bier.getString("note");
                String buveur = bier.getString("buveur");
                JSONObject pays = bier.getJSONObject("country");
                String pays_name = pays.getString("name");
                holder.name.setText(nmbier);
                holder.category.setText(category);
                holder.description.setText(descript);
                if(note.contains("null")){
                    note = "Pas de note";
                }
                holder.note.setText(note);
                holder.buveur.setText("Par "+buveur);
                holder.pays.setText(pays_name);

                /*for(int i = id_position;i<(id_position+10);i++){
                    GetDescriptionBier.startAction(getApplicationContext(),i);
                }*/



            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            //return bier.length();
            return 1;
        }

        public void setNewBiere(){
            this.bier = getBiersFromOtherFile();
            Log.d(TAG, "Length :"+bier.length());
            notifyDataSetChanged();
        }

    }

}