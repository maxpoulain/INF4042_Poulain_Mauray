package org.esiea.mauray_poulain.appmobileproject;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetBierDescription extends IntentService {

    private static final String get_description_bier = "org.esiea.mauray_poulain.appmobileproject.action.get_description_bier";
    private static final String TAG = GetBierDescription.class.getSimpleName();
    public static int id_position;

    public static void startAction(Context context, int position) {
        id_position=position+1;
        Intent intent = new Intent(context, GetBierDescription.class);
        intent.setAction(get_description_bier);

        context.startService(intent);

    }

    public GetBierDescription() {
        super("GetBierDescription");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (get_description_bier.equals(action)) {
                handleActionBiers();
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ThirdActivity.BIER_UPDATE));
            }
        }
    }


    private void copyInputStreamToFile(InputStream in, File file){
        try{
            Log.d(TAG, "CopyInputStream");
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void handleActionBiers() {

        Log.d(TAG, "Thread service name:"+ Thread.currentThread().getName());
        URL url = null;
        try{
            //url = new URL("http://binouze.fabrigli.fr/bieres/"+id_position+".json");
            url = new URL("http://binouze.fabrigli.fr/bieres.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                String test = ""+id_position;
                //copyInputStreamToFile(conn.getInputStream(),new File(getCacheDir(),"bieres"+"/"+id_position+".json"));
                copyInputStreamToFile(conn.getInputStream(),new File(getCacheDir(),"biere.json"));
                Log.d(TAG, "BIERES JSON OK");
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
