package com.diplomadoUNAL.geosalesman;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CRUDObject extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crudobject);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_crudobject, menu);
        return true;
    }
}
