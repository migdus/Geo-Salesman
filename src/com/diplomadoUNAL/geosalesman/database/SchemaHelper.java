package com.diplomadoUNAL.geosalesman.database;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class SchemaHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "geosalesman_data.sqlite";

	public String getDatabaseName() {
		return DATABASE_NAME;
	}

	// Toggle this number for updating tables and database
	private static final int DATABASE_VERSION = 1;

	public SchemaHelper(Context context) {
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
						+ ClientTable.ID + pk + "," + ClientTable.NAME
						+ textNotNull + "," + ClientTable.PHONE_NUMBER
						+ integerNotNull + "," + ClientTable.ADDRESS
						+ textNotNull + "," + ClientTable.CONTACT_NAME
						+ textNotNull + ");");

		// Create QuestionTable
		db.execSQL("CREATE TABLE " + QuestionTable.TABLE_NAME + " ("
						+ QuestionTable.ID + pk + "," + QuestionTable.QUESTION
						+ textNotNull + "," + QuestionTable.QUESTION_TITLE
						+ textNotNull + ","
						+ QuestionTable.QUESTION_DESCRIPTION + textNotNull
						+ "," + QuestionTable.QUESTION_TYPE + textNotNull + ","
						+ QuestionTable.ANSWER_OPTIONS + textNotNull + ");");

		// Create ReportTable
		db.execSQL("CREATE TABLE " + ReportTable.TABLE_NAME + " ("
						+ ReportTable.ID + pk + "," + ReportTable.CREATION_DATE
						+ textNotNull + "," + ReportTable.SENT_DATE + text
						+ "," + ReportTable.LOCATION + textNotNull + ","
						+ ReportTable.COMMENTS + textNotNull + ","
						+ ReportTable.CLIENT_ID + integerNotNull + ");");

		// Create ReportTemplateTable
		db.execSQL("CREATE TABLE " + ReportTemplateTable.TABLE_NAME + " ("
						+ ReportTemplateTable.ID + pk + ","
						+ ReportTemplateTable.NAME + textNotNull + ","
						+ ReportTemplateTable.DESCRIPTION + textNotNull + ","
						+ ReportTemplateTable.QUESTION_ID + integerNotNull
						+ ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("LOG_TAG", "Upgrading database from version " + oldVersion
						+ " to " + newVersion
						+ ", which will destroy all old data");
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

	public int updateClient(String clientID, String clientName,
					int phoneNumber, String address, String contactName) {

		ContentValues cv = new ContentValues();
		cv.put(ClientTable.ADDRESS, address);
		cv.put(ClientTable.CONTACT_NAME, contactName);
		cv.put(ClientTable.NAME, clientName);
		cv.put(ClientTable.PHONE_NUMBER, phoneNumber);

		SQLiteDatabase database = getWritableDatabase();
		int result = database.update(ClientTable.TABLE_NAME, cv, ClientTable.ID
						+ "=?", new String[] { clientID });
		return result;
	}

	// Wrapper method for adding a question
	public long addQuestion(String questionTitle, String question,
					String questionDescription, String questionType,
					String answerOptions) {
		ContentValues cv = new ContentValues();
		cv.put(QuestionTable.QUESTION_TITLE, questionTitle);
		cv.put(QuestionTable.QUESTION, question);
		cv.put(QuestionTable.QUESTION_DESCRIPTION, questionDescription);
		cv.put(QuestionTable.QUESTION_TYPE, questionType);
		cv.put(QuestionTable.ANSWER_OPTIONS, answerOptions);

		SQLiteDatabase database = getWritableDatabase();
		long result = database.insert(QuestionTable.TABLE_NAME,
						QuestionTable.QUESTION, cv);
		return result;
	}

	// Wrapper method for upgrading a question

	public int updateQuestion(String questionID, String questionTitle,
					String questionDescription, String question,
					String questionType, String answerOptions) {

		ContentValues cv = new ContentValues();
		cv.put(QuestionTable.QUESTION_TITLE, questionTitle);
		cv.put(QuestionTable.QUESTION_DESCRIPTION, questionDescription);
		cv.put(QuestionTable.QUESTION, question);
		cv.put(QuestionTable.QUESTION_TYPE, questionType);
		cv.put(QuestionTable.ANSWER_OPTIONS, answerOptions);

		SQLiteDatabase database = getWritableDatabase();
		int result = database.update(QuestionTable.TABLE_NAME, cv,
						QuestionTable.ID + "=?", new String[] { questionID });
		return result;
	}

	/**
	 * Wrapper method for deleting a question
	 * 
	 * @param questionID
	 * @return The number of deleted columns. Returns <code>-3</code> if can not delete a question because a dependency
	 *         with other table.
	 */
	public int deleteQuestion(String questionID) {
		// Check first if this question is not included in some report template row. If it is, then can not delete the
		String query = SQLiteQueryBuilder.buildQueryString(false,
						ReportTemplateTable.TABLE_NAME,
						new String[] { ReportTemplateTable.QUESTION_ID },
						ReportTemplateTable.QUESTION_ID + " = "
										+ questionID, null, null, null,
						"1");
		SQLiteDatabase database = getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		if (cursor.getCount() == 0) {
			int result = database.delete(QuestionTable.TABLE_NAME,
							QuestionTable.ID + "=?",
							new String[] { questionID });
			return result;
		} else {
			return -3;
		}
	}

	// Wrapper method for adding a Report Template
	public long addReportTemplate(int questionId, String name,
					String description) {
		ContentValues cv = new ContentValues();
		cv.put(ReportTemplateTable.NAME, name);
		cv.put(ReportTemplateTable.DESCRIPTION, description);
		cv.put(ReportTemplateTable.QUESTION_ID, questionId);

		SQLiteDatabase database = getWritableDatabase();
		long result = database.insert(ReportTemplateTable.TABLE_NAME,
						ReportTemplateTable.QUESTION_ID, cv);
		return result;
	}

	// Wrapper method for adding a Report
	public long addReport(String creationDate, String sentDate,
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

	/**
	 * 
	 * @return An <code>Arraylist</code>, where each element is a <code>HashMap</code> element with a
	 *         <code>String</code> key that can be: <code>QuestionTable.ID</code>,
	 *         <code>QuestionTable.QUESTION_TITLE</code> or <code>QuestionTable.QUESTION_DESCRIPTION</code>. The value
	 *         is the stored information for each one of these elements.
	 */
	public ArrayList<HashMap<String, String>> getQuestionsTitlesAndDescriptions() {
		String[] columns = new String[] { QuestionTable.ID,
				QuestionTable.QUESTION_TITLE,
				QuestionTable.QUESTION_DESCRIPTION };
		String query = SQLiteQueryBuilder.buildQueryString(false,
						QuestionTable.TABLE_NAME, columns, null, null, null,
						null, null);
		SQLiteDatabase database = getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		while (cursor.moveToNext()) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put(QuestionTable.ID, cursor.getString(cursor
							.getColumnIndex(QuestionTable.ID)));
			hashMap.put(QuestionTable.QUESTION_TITLE, cursor.getString(cursor
							.getColumnIndex(QuestionTable.QUESTION_TITLE)));
			hashMap.put(QuestionTable.QUESTION_DESCRIPTION,
							cursor.getString(cursor
											.getColumnIndex(QuestionTable.QUESTION_DESCRIPTION)));
			result.add(hashMap);
		}
		return result;
	}

	public HashMap<String, String> getQuestion(int id) {

		String[] columns = new String[] { QuestionTable.QUESTION_TITLE,
				QuestionTable.QUESTION_DESCRIPTION, QuestionTable.QUESTION,
				QuestionTable.QUESTION_TYPE, QuestionTable.ANSWER_OPTIONS };
		String query = SQLiteQueryBuilder.buildQueryString(false,
						QuestionTable.TABLE_NAME, columns, QuestionTable.ID
										+ "=" + Integer.valueOf(id).toString(),
						null, null, null, null);
		SQLiteDatabase database = getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		HashMap<String, String> result = new HashMap<String, String>();
		while (cursor.moveToNext()) {

			result.put(QuestionTable.QUESTION_TITLE, cursor.getString(cursor
							.getColumnIndex(QuestionTable.QUESTION_TITLE)));
			result.put(QuestionTable.QUESTION, cursor.getString(cursor
							.getColumnIndex(QuestionTable.QUESTION)));
			result.put(QuestionTable.QUESTION_DESCRIPTION,
							cursor.getString(cursor
											.getColumnIndex(QuestionTable.QUESTION_DESCRIPTION)));
			result.put(QuestionTable.QUESTION_TYPE, cursor.getString(cursor
							.getColumnIndex(QuestionTable.QUESTION_TYPE)));
			result.put(QuestionTable.ANSWER_OPTIONS, cursor.getString(cursor
							.getColumnIndex(QuestionTable.ANSWER_OPTIONS)));
		}
		return result;
	}

	public HashMap<String, String> getClient(int id) {

		String[] columns = new String[] { ClientTable.ADDRESS,
				ClientTable.CONTACT_NAME, ClientTable.NAME,
				ClientTable.PHONE_NUMBER };
		String query = SQLiteQueryBuilder.buildQueryString(false,
						ClientTable.TABLE_NAME, columns, ClientTable.ID + "="
										+ Integer.valueOf(id).toString(), null,
						null, null, null);
		SQLiteDatabase database = getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		HashMap<String, String> result = new HashMap<String, String>();
		while (cursor.moveToNext()) {

			result.put(ClientTable.ADDRESS, cursor.getString(cursor
							.getColumnIndex(ClientTable.ADDRESS)));
			result.put(ClientTable.CONTACT_NAME, cursor.getString(cursor
							.getColumnIndex(ClientTable.CONTACT_NAME)));
			result.put(ClientTable.NAME, cursor.getString(cursor
							.getColumnIndex(ClientTable.NAME)));
			result.put(ClientTable.PHONE_NUMBER, cursor.getString(cursor
							.getColumnIndex(ClientTable.PHONE_NUMBER)));
		}
		return result;
	}

	/**
	 * 
	 * @return An <code>Arraylist</code>, where each element is a <code>HashMap</code> element with a
	 *         <code>String</code> key that can be: <code>ClientTable.ID</code>, <code>ClientTable.NAME</code> or
	 *         <code>ClientTable.CONTACT_NAME</code>. The value is the stored information for each one of these
	 *         elements.
	 */
	public ArrayList<HashMap<String, String>> getClientNameAndContactName() {
		String[] columns = new String[] { ClientTable.ID, ClientTable.ADDRESS,
				ClientTable.CONTACT_NAME, ClientTable.NAME,
				ClientTable.PHONE_NUMBER };
		String query = SQLiteQueryBuilder.buildQueryString(false,
						ClientTable.TABLE_NAME, columns, null, null, null,
						null, null);
		SQLiteDatabase database = getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		while (cursor.moveToNext()) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put(ClientTable.ID, cursor.getString(cursor
							.getColumnIndex(ClientTable.ID)));
			hashMap.put(ClientTable.NAME, cursor.getString(cursor
							.getColumnIndex(ClientTable.NAME)));
			hashMap.put(ClientTable.CONTACT_NAME, cursor.getString(cursor
							.getColumnIndex(ClientTable.CONTACT_NAME)));
			result.add(hashMap);
		}
		return result;
	}

	/**
	 * Wrapper method for deleting a client
	 * 
	 * @param clientID
	 * @return The number of deleted columns. Returns <code>-3</code> if can not delete a client because a dependency
	 *         with other table.
	 */
	public int deleteClient(String clientID) {
		// Check first if this client is not included in some report row. If it is, then can not delete the client
		String query = SQLiteQueryBuilder.buildQueryString(false,
						ReportTable.TABLE_NAME,
						new String[] { ReportTable.CLIENT_ID },
						ReportTable.CLIENT_ID + " = "
										+ clientID, null, null, null,
						"1");
		SQLiteDatabase database = getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		if (cursor.getCount() == 0) {
			int result = database.delete(ClientTable.TABLE_NAME,
							ClientTable.ID + "=?",
							new String[] { clientID });
			return result;
		} else {
			return -3;
		}
	}
}
