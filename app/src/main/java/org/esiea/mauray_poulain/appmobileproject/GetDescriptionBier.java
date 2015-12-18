package org.esiea.mauray_poulain.appmobileproject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Maxime on 02/12/2015.
 */
public class GetDescriptionBier extends IntentService {

    private static final String Description_Bear = "org.esiea.mauray_poulain.appmobileproject.GetDescriptionBier";
    private static final String TAG = GetDescriptionBier.class.getSimpleName();
    public static int id_position;

    public static void startAction(Context context, int position){
        id_position = position;
        Intent in = new Intent(context,GetDescriptionBier.class);
        in.setAction(Description_Bear);
        context.startService(in);
        Log.d(TAG,"START GETDESCRIPTIONBIER");
    }

    public GetDescriptionBier(){
        super("GetDescriptionBier");
        Log.d(TAG,"START GETDESCRIPTIONBIER CONSTRUCTOR");
    }

    protected void onHandleIntentBier(Intent intent){
        Log.d(TAG,"OKOK");
        if(intent != null){
            final String act = intent.getAction();
            if(Description_Bear.equals(act)){
                Log.d(TAG, "OKOKOK");
                handleBierAction();

            }
        }
    }

    private void copyInputStreamJSONToFile(InputStream inputStream, File file){
        Log.d(TAG,"COPYINPUT");
        try {
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int longueur;
            while ((longueur = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, longueur);
            }
            outputStream.close();
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void notification_test(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.drawable.ic_notif).setContentTitle("Fin téléchargement Description").setContentText("Le fichier JSON a été téléchargé");
        NotificationManager notifman = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notifman.notify(10, builder.build());

    }

    private void handleBierAction(){
        Log.d(TAG,"HANDLEBIERACTION");
        URL url = null;
        try {
            url = new URL("http://binouze.fabrigli.fr/bieres/"+id_position+".json");
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("GET");
            connect.connect();
            notification_test();
            if(HttpURLConnection.HTTP_OK == connect.getResponseCode()){
                copyInputStreamJSONToFile(connect.getInputStream(), new File(getCacheDir(), id_position+".json"));
                Log.d(TAG, "BIERES JSON OK");
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ThirdActivity.BIER_UPDATE));
            }

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "HANDLEINTENT");
        onHandleIntentBier(intent);

    }
}
