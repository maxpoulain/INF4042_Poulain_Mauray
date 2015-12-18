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

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GetBiersServices extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String get_all_biers = "org.esiea.mauray_poulain.appmobileproject.action.get_all_biers";
    private static final String TAG = GetBiersServices.class.getSimpleName();
    /*private static final String ACTION_BAZ = "org.esiea.mauray_poulain.projet_4a.action.BAZ";*/

    // TODO: Rename parameters
    /*private static final String EXTRA_PARAM1 = "org.esiea.mauray_poulain.projet_4a.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "org.esiea.mauray_poulain.projet_4a.extra.PARAM2";*/

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startAction(Context context) {
        Intent intent = new Intent(context, GetBiersServices.class);
        intent.setAction(get_all_biers);
       /* intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);*/
        context.startService(intent);

    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    /*public static void startActionBaz(Context context) {
        Intent intent = new Intent(context, GetBiersServices.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }*/

    public GetBiersServices() {
        super("GetBiersServices");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (get_all_biers.equals(action)) {
                handleActionBiers();
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(SecondeActivity.BIERS_UPDATE));
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void copyInputStreamToFile(InputStream in, File file){
        try{
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
        // TODO: Handle action Beer
        //throw new UnsupportedOperationException("Not yet implemented");
        Log.d(TAG, "Thread service name:"+ Thread.currentThread().getName());
        URL url = null;
        try{
            url = new URL("http://binouze.fabrigli.fr/bieres.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                copyInputStreamToFile(conn.getInputStream(),new File(getCacheDir(),"bieres.json"));
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
    /*private void handleActionBaz() {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }*/

}
