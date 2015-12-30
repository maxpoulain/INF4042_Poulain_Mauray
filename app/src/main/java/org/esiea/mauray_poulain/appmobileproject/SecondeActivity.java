package org.esiea.mauray_poulain.appmobileproject;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;



public class SecondeActivity extends Activity {

    public RecyclerView rv;
    public BiersAdapter adapter;
    private static final String TAG = SecondeActivity.class.getSimpleName();
    public int id_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seconde);
        Log.d(TAG, "deuxieme activité");

        IntentFilter intentFilter = new IntentFilter(BIERS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BierUpdate(), intentFilter);

        rv = (RecyclerView)findViewById(R.id.rv_biere);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .color(Color.LTGRAY)
                        .build());
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {

                    public void onItemClick(View view, int position) {
                        Log.d(TAG, "CLICK " + position);
                        IntentFonction2(position);


                    }
                })
        );
        adapter = new BiersAdapter(getBiersFromFile());
        rv.setAdapter(adapter);
    }


    public void notification_test(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.drawable.ic_notif).setContentTitle("Fin téléchargement").setContentText("Le fichier JSON a été téléchargé");
        NotificationManager notifman = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notifman.notify(10, builder.build());

    }

    public void IntentFonction2(int position){
        try {
            Intent intent = new Intent(this,ThirdActivityBis.class);
            intent.putExtra("position",position);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
            Toast toast = Toast.makeText(this,"Vous n'avez pas accès aux détails des bières !!",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public JSONArray getBiersFromFile(){
        try {
            InputStream is = new FileInputStream(getCacheDir()+"/"+"bieres.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, "UTF-8"));
        }catch (IOException e){
            e.printStackTrace();
            return new JSONArray();
        }catch (JSONException e){
            e.printStackTrace();
            return new JSONArray();
        }
    }


    public static final String BIERS_UPDATE="com.octip.cours.inf4042_11.BIERS_UPDATE";
    public class BierUpdate extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "OK:" + getIntent().getAction());
            notification_test();
            adapter.setNewBiere();
            Log.d(TAG, "setNewBiere OK");

        }
    }


    Bitmap bitmap;

    private class BiersAdapter extends RecyclerView.Adapter<BiersAdapter.BierHolder>{

        public  JSONArray biers;


        public class BierHolder extends RecyclerView.ViewHolder{
            public TextView name;
            public ImageView thumb;
            public BierHolder(View v){
                super(v);
                name = (TextView) itemView.findViewById(R.id.rv_bier_element_name);
                thumb = (ImageView) itemView.findViewById(R.id.rv_bier_element_image);

            }
        }

        public BiersAdapter (JSONArray jsonArray){
            biers = jsonArray;

        }


        @Override
        public BierHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_bier_element, parent, false);
            BierHolder bh = new BierHolder(v);
            return bh;
        }

        @Override
        public void onBindViewHolder(BierHolder holder, int position) {
            try {
                id_position = position;
                JSONObject obj = biers.getJSONObject(position);
                String nmbier =  obj.getString("name");
                holder.name.setText(nmbier);
                holder.thumb.setImageResource(R.drawable.ic_notif);
                holder.thumb.setTag(obj.getInt("id"));
                new DownloadThumbTask().execute(obj.getInt("id"),holder.thumb);

            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {

            int lng = biers.length();
            return lng;
        }

        public void setNewBiere(){
            this.biers = getBiersFromFile();
            Log.d(TAG, "Length :" + biers.length());
            notifyDataSetChanged();
        }

    }

    private class DownloadThumbTask extends AsyncTask<Object, Integer,Void> {

        int position = 0;
        ImageView thumb=null;

        @Override
        protected Void doInBackground(Object... params) {
            position =(int) params[0];
            thumb = (ImageView) params[1];
            try {

                if(((File)new File(getCacheDir().getAbsolutePath() + "/thumb" + position)).exists())
                    return null;

                Log.d(TAG, "Task :"+position);
                JSONObject object = GetBierServices.handleBierActionThumb(getApplicationContext(),position);
                JSONObject obj = object.getJSONObject("image");
                JSONObject image = obj.getJSONObject("image");
                JSONObject url = image.getJSONObject("thumb");
                String uri =  url.getString("url");
                URL urlfin = new URL("http://binouze.fabrigli.fr"+uri);
                GetBierServices.handleBierActionDownload(getApplicationContext(), urlfin, position);
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
            Uri uri = Uri.parse(getCacheDir().getAbsolutePath()+"/thumb"+position);
            Log.d(TAG, "URI: "+uri);
            Log.d(TAG, "onPost: " + (new File(getCacheDir().getAbsolutePath() + "/thumb" + position).getAbsolutePath()));
            if(((int)thumb.getTag()) == position ) {
                thumb.setImageURI(uri);
            }
        }

    }

}


