package com.diplomadoUNAL.geosalesman;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AddNewReportTemplate extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_report_template);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_new_report_template, menu);
        return true;
    }
}
