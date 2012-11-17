package com.diplomadoUNAL.geosalesman;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener implements LocationListener {
    
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		location.getLatitude();

		location.getLongitude();
		
		//String coordenadas = "Mis coordenadas son: " + "Latitud = " + location.getLatitude() + "Longitud = " + location.getLongitude();
		//Toast.makeText( getApplicationContext(),coordenadas,Toast.LENGTH_LONG).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		//Toast.makeText( getApplicationContext(),"Gps Desactivado",Toast.LENGTH_SHORT ).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		//Toast.makeText( getApplicationContext(),"Gps Activo",Toast.LENGTH_SHORT ).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}

