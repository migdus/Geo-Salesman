package com.diplomadoUNAL.geosalesman.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SchemaHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "geosalesman_data.db";
	// TOGGLE THIS NUMBER FOR UPDATING TABLES AND DATABASE
	private static final int DATABASE_VERSION = 1;

	SchemaHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
    public void onCreate(SQLiteDatabase db) {
        // Create ClientTable
    	db.execSQL("CREATE TABLE ");
    	//Create QuestionTable
    	db.execSQL("CREATE TABLE " + QuestionTable.TABLE_NAME + " ("
    			+ QuestionTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
    			+ QuestionTable.QUESTION + " TEXT  NOT NULL,"
    			+ QuestionTable.QUESTION_DESCRIPTION + " TEXT  NOT NULL,"
    			+ QuestionTable.QUESTION_TYPE + " TEXT  NOT NULL, "
    			+ QuestionTable.ANSWER_OPTIONS + " TEXT  NOT NULL);");	
    	//Create ReportTable
    	//Create ReportTemplateTable
}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}