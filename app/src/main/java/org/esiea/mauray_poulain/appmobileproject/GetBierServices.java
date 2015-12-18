package org.esiea.mauray_poulain.appmobileproject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GetBierServices extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS

    private static final String get_all_biers = "org.esiea.mauray_poulain.appmobileproject.action.get_all_biers";
    private static final String Description_Bear = "org.esiea.mauray_poulain.appmobileproject.GetBierServices";
    private static final String TAG = GetBierServices.class.getSimpleName();
    public static int id_position;


    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBiers(Context context) {
        Intent intent = new Intent(context, GetBierServices.class);
        intent.setAction(get_all_biers);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBier(Context context, int position) {
        id_position = position;
        Intent in = new Intent(context,GetBierServices.class);
        in.setAction(Description_Bear);
        context.startService(in);
        Log.d(TAG, "START GETDESCRIPTIONBIER");
    }

    public GetBierServices() {
        super("GetBierServices");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (get_all_biers.equals(action)) {
                //handleActionFoo(param1, param2);
                handleActionBiers(this);
            } else if (Description_Bear.equals(action)) {
                //handleActionBaz(param1, param2);
                handleBierAction(this);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    public static void handleActionBiers(Context context) {
        Log.d(TAG, "Thread service name:"+ Thread.currentThread().getName());
        URL url = null;
        try{
            url = new URL("http://binouze.fabrigli.fr/bieres.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                copyInputStreamToFile(conn.getInputStream(),new File(context.getCacheDir(),"bieres.json"));
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(SecondeActivity.BIERS_UPDATE));
                Log.d(TAG, "BIERES JSON OK");
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    public static void handleBierAction(Context context) {
        Log.d(TAG,"HANDLEBIERACTION");
        URL url = null;
        try {
            url = new URL("http://binouze.fabrigli.fr/bieres/"+id_position+".json");
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("GET");
            connect.connect();
            if(HttpURLConnection.HTTP_OK == connect.getResponseCode()){
                copyInputStreamToFile(connect.getInputStream(), new File(context.getCacheDir(), id_position + ".json"));
                Log.d(TAG, "BIERES JSON OK");
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ThirdActivity.BIER_UPDATE));
            }

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static JSONObject handleBierActionThumb(Context context, int id_position) {
        Log.d(TAG,"THUMB");
        URL url = null;
        try {
            url = new URL("http://binouze.fabrigli.fr/bieres/"+id_position+".json");
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("GET");
            connect.connect();
            if(HttpURLConnection.HTTP_OK == connect.getResponseCode()){
                copyInputStreamToFile(connect.getInputStream(), new File(context.getCacheDir(), id_position + ".json"));
                Log.d(TAG, "BIERES Thumb OK");
                try {
                    InputStream is3 = new FileInputStream(context.getCacheDir()+"/"+id_position+".json");
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
            }else{
                Log.d(TAG, "code :"+connect.getResponseCode());
            }

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    public static void handleBierActionDownload(Context context, URL uri, int id) {
        Log.d(TAG,"Download");
        try {
            HttpURLConnection connect = (HttpURLConnection) uri.openConnection();
            connect.setRequestMethod("GET");
            connect.connect();
            if(HttpURLConnection.HTTP_OK == connect.getResponseCode()){
                copyInputStreamToFile(connect.getInputStream(), new File(context.getCacheDir(), "thumb" + id));
                Log.d(TAG, "Download Thumb OK" + (new File(context.getCacheDir(), "thumb" + id)).getAbsolutePath());
            }

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void notification_test(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.drawable.ic_notif).setContentTitle("Fin téléchargement Description").setContentText("Le fichier JSON a été téléchargé");
        NotificationManager notifman = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notifman.notify(10, builder.build());

    }

    private static void copyInputStreamToFile(InputStream inputStream, File file){
        Log.d(TAG, "COPYINPUT");
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

}
