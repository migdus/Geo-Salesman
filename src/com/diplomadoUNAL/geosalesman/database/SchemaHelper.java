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
		final String pk =" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";
		final String textNotNull = " TEXT  NOT NULL";
		final String integerNotNull = " INTEGER NOT NULL";
        // Create ClientTable
    	db.execSQL("CREATE TABLE "+ ClientTable.TABLE_NAME + " ("
    			+ ClientTable.ID + pk+ ","
    			+ ClientTable.NAME + textNotNull + ","
    			+ ClientTable.PHONE_NUMBER + integerNotNull + ","
    			+ ClientTable.ADDRESS + textNotNull + ","
    			+ ClientTable.ADDRESS + textNotNull+");");
    			
    	//Create QuestionTable
    	db.execSQL("CREATE TABLE " + QuestionTable.TABLE_NAME + " ("
    			+ QuestionTable.ID + pk + ","
    			+ QuestionTable.QUESTION + textNotNull+ ","
    			+ QuestionTable.QUESTION_DESCRIPTION + textNotNull+ ","
    			+ QuestionTable.QUESTION_TYPE + textNotNull+ ","
    			+ QuestionTable.ANSWER_OPTIONS + textNotNull+");");	
    	
    	//Create ReportTable
    	
    	//Create ReportTemplateTable
}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
}