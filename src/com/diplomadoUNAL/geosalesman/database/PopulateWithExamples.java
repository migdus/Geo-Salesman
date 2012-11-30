package com.diplomadoUNAL.geosalesman.database;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.content.Context;
import au.com.bytecode.opencsv.CSVReader;

public class PopulateWithExamples {
	public static void populateDatabase(Context context) {
		SchemaHelper schemaHelper = new SchemaHelper(context);
		schemaHelper.onUpgrade(schemaHelper.getWritableDatabase(), 1, 1);
		String[] files = { "clients_example.csv", "questions_example.csv" };
		for (int i = 0; i < files.length; i++) {
			String fileName = files[i];
			try {

				InputStream is = context.getAssets().open(fileName);
				InputStreamReader csvStreamReader = new InputStreamReader(is,"UTF-8");
				CSVReader csvReader = new CSVReader(csvStreamReader);
				String[] line;

				String[] header = csvReader.readNext();

				while ((line = csvReader.readNext()) != null) {
					HashMap<String, String> row = new HashMap<String, String>();
					for (int j = 0; j < line.length; j++) {
						row.put(header[j], line[j]);
					}
					if (fileName.equals("clients_example.csv")) {
						schemaHelper.addClient(
										row.get(ClientTable.NAME),
										Long.parseLong(row
														.get(ClientTable.PHONE_NUMBER)),
										row.get(ClientTable.ADDRESS),
										row.get(ClientTable.CONTACT_NAME));
					} else if (fileName.equals("questions_example.csv")) {
						schemaHelper.addQuestion(
										row.get(QuestionTable.QUESTION_TITLE),
										row.get(QuestionTable.QUESTION),
										row.get(QuestionTable.QUESTION_DESCRIPTION),
										row.get(QuestionTable.QUESTION_TYPE),
										row.get(QuestionTable.ANSWER_OPTIONS));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
