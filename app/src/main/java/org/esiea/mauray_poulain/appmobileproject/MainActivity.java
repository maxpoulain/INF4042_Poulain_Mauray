package org.esiea.mauray_poulain.appmobileproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    public DatePickerDialog dpd;
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

       /* final TextView tv_hw = (TextView) findViewById(R.id.tv_hello_world);
        Button btn_hw = (Button) findViewById(R.id.btn_hello_world);
        String now = DateUtils.formatDateTime(getApplicationContext(), (new Date()).getTime(), DateFormat.FULL);
        //getString(R.string.hello);
        tv_hw.setText(now);
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int day) {
                tv_hw.setText(day + "-" + month + "-" + year);
            }
        }, mYear, mMonth, mDay);*/


        setSupportActionBar(toolbar);

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
        if (id == R.id.action_settings) {
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
