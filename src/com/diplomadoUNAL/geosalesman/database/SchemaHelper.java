package com.diplomadoUNAL.geosalesman.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SchemaHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "geosalesman_data.db";
	// Toggle this number for updating tables and database
	private static final int DATABASE_VERSION = 1;

	SchemaHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		final String pk = " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";
		final String textNotNull = " TEXT NOT NULL";
		final String text = " TEXT";
		final String integerNotNull = " INTEGER NOT NULL";

		// Create ClientTable
		db.execSQL("CREATE TABLE " + ClientTable.TABLE_NAME + " ("
				+ ClientTable.ID + pk + "," + ClientTable.NAME + textNotNull
				+ "," + ClientTable.PHONE_NUMBER + integerNotNull + ","
				+ ClientTable.ADDRESS + textNotNull + ","
				+ ClientTable.CONTACT_NAME + textNotNull + ");");

		// Create QuestionTable
		db.execSQL("CREATE TABLE " + QuestionTable.TABLE_NAME + " ("
				+ QuestionTable.ID + pk + "," + QuestionTable.QUESTION
				+ textNotNull + "," + QuestionTable.QUESTION_DESCRIPTION
				+ textNotNull + "," + QuestionTable.QUESTION_TYPE + textNotNull
				+ "," + QuestionTable.ANSWER_OPTIONS + textNotNull + ");");

		// Create ReportTable
		db.execSQL("CREATE TABLE " + ReportTable.TABLE_NAME + " ("
				+ ReportTable.ID + pk + "," + ReportTable.CREATION_DATE
				+ textNotNull + "," + ReportTable.SENT_DATE + text + ","
				+ ReportTable.LOCATION + textNotNull + ","
				+ ReportTable.COMMENTS + textNotNull + ","
				+ ReportTable.CLIENT_ID + integerNotNull + ");");

		// Create ReportTemplateTable
		db.execSQL("CREATE TABLE " + ReportTemplateTable.TABLE_NAME + " ("
				+ ReportTemplateTable.ID + pk + ","
				+ ReportTemplateTable.NAME + textNotNull + ","
				+ ReportTemplateTable.DESCRIPTION + textNotNull + ","
				+ ReportTemplateTable.QUESTION_ID + integerNotNull + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("LOG_TAG", "Upgrading database from version " + oldVersion
				+ " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + ClientTable.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + ReportTable.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + ReportTemplateTable.TABLE_NAME);
		onCreate(db);
	}

	// Wrapper method for adding a client
	public long addClient(String clientName, int phoneNumber, String address,
			String contactName) {
		ContentValues cv = new ContentValues();
		cv.put(ClientTable.NAME, clientName);
		cv.put(ClientTable.PHONE_NUMBER, phoneNumber);
		cv.put(ClientTable.ADDRESS, address);
		cv.put(ClientTable.CONTACT_NAME, contactName);

		SQLiteDatabase database = getWritableDatabase();
		long result = database.insert(ClientTable.TABLE_NAME, ClientTable.NAME,
				cv);
		return result;
	}

	// Wrapper method for adding a question
	public long addQuestion(String question, String questionDescription,
			String questionType, String answerOptions) {
		ContentValues cv = new ContentValues();
		cv.put(QuestionTable.QUESTION, question);
		cv.put(QuestionTable.QUESTION_DESCRIPTION, questionDescription);
		cv.put(QuestionTable.QUESTION_TYPE, questionType);
		cv.put(QuestionTable.ANSWER_OPTIONS, answerOptions);

		SQLiteDatabase database = getWritableDatabase();
		long result = database.insert(QuestionTable.TABLE_NAME,
				QuestionTable.QUESTION, cv);
		return result;
	}

	// Wrapper method for adding a Report Template
	public long addReportTemplate(int questionId,String name,String description) {
		ContentValues cv = new ContentValues();
		cv.put(ReportTemplateTable.NAME, name);
		cv.put(ReportTemplateTable.DESCRIPTION,description);
		cv.put(ReportTemplateTable.QUESTION_ID, questionId);

		SQLiteDatabase database = getWritableDatabase();
		long result = database.insert(ReportTemplateTable.TABLE_NAME,
				ReportTemplateTable.QUESTION_ID, cv);
		return result;
	}

	public long addReportTable(String creationDate, String sentDate,
			String location, String comments, int clientId) {
		ContentValues cv = new ContentValues();
		cv.put(ReportTable.CREATION_DATE, creationDate);
		cv.put(ReportTable.SENT_DATE, sentDate);
		cv.put(ReportTable.LOCATION, location);
		cv.put(ReportTable.COMMENTS, comments);
		cv.put(ReportTable.CLIENT_ID, clientId);

		SQLiteDatabase database = getWritableDatabase();
		long result = database.insert(ReportTable.TABLE_NAME,
				ReportTable.CREATION_DATE, cv);
		return result;
	}
}
