package com.diplomadoUNAL.geosalesman;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.diplomadoUNAL.geosalesman.database.ClientTable;
import com.diplomadoUNAL.geosalesman.database.SchemaHelper;
import com.diplomadoUNAL.geosalesman.util.EditTextValidation;

public class AddNewClient extends Activity {
	// Received Intent
	Intent receivedIntent;

	// Form fields
	private EditText clientName;
	private EditText phoneNumber;
	private EditText address;
	private EditText contactName;

	// Validation flags
	private boolean clientNameValidationFlag;
	private boolean phoneNumberValidationFlag;
	private boolean addressValidationFlag;
	private boolean contactNameValidationFlag;

	private HashMap<Integer, String> validationTestNumbers = null;
	private HashMap<Integer, String> validationTestAlphaWithSpace = null;
	private HashMap<Integer, String> validationTestNumberWithSpaces = null;

	// Intent messages
	// Mode of this activity
	public final static String ADD_NEW_CLIENT_ACTIVITY_MODE = "com.diplomadoUNAL.geosalesman.ADD_NEW_CLIENT_ACTIVITY_MODE";
	public final static String ACTIVITY_MODE_READ_ONLY = "readOnly";
	public final static String ACTIVITY_MODE_ADD_NEW = "addNew";
	public final static String ACTIVITY_MODE_UPDATE = "update";
	public final static String ACTIVITY_MODE_DB_ITEM_ID = "itemId";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_client);

		receivedIntent = getIntent();

		// EditText definition and validation
		validationTestAlphaWithSpace = new HashMap<Integer, String>();
		validationTestAlphaWithSpace
						.put(EditTextValidation.ALPHABETHIC_VALIDATION,
										getResources().getString(
														R.string.editText_validation_error_alpha_only));
		validationTestAlphaWithSpace
						.put(EditTextValidation.CHARACTER_VALIDATION,
										getResources().getString(
														R.string.editText_validation_error_char_not_allowed));
		validationTestAlphaWithSpace
						.put(EditTextValidation.NO_PIPE_CHAR_VALIDATION,
										getResources().getString(
														R.string.editText_validation_error_pipe_char_not_allowed));
		validationTestNumberWithSpaces = new HashMap<Integer, String>();
		validationTestNumberWithSpaces
						.put(EditTextValidation.NUMBER_WITH_SPACES_VALIDATION,
										getResources().getString(
														R.string.editText_validation_error_numbers_and_spaces_only));
		validationTestNumberWithSpaces
						.put(EditTextValidation.CHARACTER_VALIDATION,
										getResources().getString(
														R.string.editText_validation_error_char_not_allowed));
		validationTestNumberWithSpaces
						.put(EditTextValidation.NO_PIPE_CHAR_VALIDATION,
										getResources().getString(
														R.string.editText_validation_error_pipe_char_not_allowed));

		OnFocusChangeListener onFocusChangeListenerAlphaWithSpace = new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					validateEditText((EditText) v, validationTestAlphaWithSpace);
				}
			}
		};
		OnFocusChangeListener onFocusChangeListenerNumberWithSpace = new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					validateEditText((EditText) v,
									validationTestNumberWithSpaces);
				}
			}
		};

		clientName = (EditText) AddNewClient.this
						.findViewById(R.id.client_name);
		clientName.setOnFocusChangeListener(onFocusChangeListenerAlphaWithSpace);
		phoneNumber = (EditText) AddNewClient.this
						.findViewById(R.id.phone_number);
		phoneNumber.setOnFocusChangeListener(onFocusChangeListenerNumberWithSpace);
		address = (EditText) AddNewClient.this.findViewById(R.id.address);
		address.setOnFocusChangeListener(onFocusChangeListenerAlphaWithSpace);
		contactName = (EditText) AddNewClient.this
						.findViewById(R.id.contact_name);
		contactName.setOnFocusChangeListener(onFocusChangeListenerAlphaWithSpace);

	}

	@Override
	public void onStart() {
		super.onStart();

		int dbId = -1;
		if (receivedIntent.getStringExtra(ADD_NEW_CLIENT_ACTIVITY_MODE)
						.equals(ACTIVITY_MODE_READ_ONLY)
						|| receivedIntent.getStringExtra(
										ADD_NEW_CLIENT_ACTIVITY_MODE).equals(
										ACTIVITY_MODE_UPDATE)) {
			dbId = Integer.parseInt(receivedIntent
							.getStringExtra(ACTIVITY_MODE_DB_ITEM_ID));
			// Load the previously stored data from the DB
			SchemaHelper schemaHelper = new SchemaHelper(
							getApplicationContext());
			HashMap<String, String> storedValues = schemaHelper.getClient(dbId);
			clientName.setText(storedValues.get(ClientTable.NAME));
			phoneNumber.setText(storedValues.get(ClientTable.PHONE_NUMBER));
			address.setText(storedValues.get(ClientTable.ADDRESS));
			contactName.setText(storedValues.get(ClientTable.CONTACT_NAME));
		}

		Button okButton = (Button) this.findViewById(R.id.ok_button);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SchemaHelper schemaHelper = new SchemaHelper(AddNewClient.this);

				// This will store in a string data according to the selected answer option
				String answerOptions = new String();

				validateEditText(clientName, validationTestAlphaWithSpace);
				validateEditText(phoneNumber, validationTestNumberWithSpaces);
				validateEditText(address, validationTestAlphaWithSpace);
				validateEditText(contactName, validationTestAlphaWithSpace);

				if (clientNameValidationFlag && phoneNumberValidationFlag
								&& addressValidationFlag
								&& contactNameValidationFlag) {
					// Get data from fields
					String clientNameData = clientName.getText().toString();
					String phoneNumberData = (phoneNumber).getText().toString();
					String addressData = address.getText().toString();
					String contactNameData = contactName.getText().toString();

					// Add New Question Activity Mode: add new
					if (receivedIntent
									.getStringExtra(AddNewClient.ADD_NEW_CLIENT_ACTIVITY_MODE)
									.equals(AddNewQuestion.ACTIVITY_MODE_ADD_NEW)) {
						// Save to database
						long dbResult = schemaHelper.addClient(clientNameData,
										Long.parseLong(phoneNumberData),
										addressData, contactNameData);
						if (dbResult < 0) {
							Toast.makeText(AddNewClient.this,
											getResources().getString(
															R.string.database_error_storing_data),
											Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(AddNewClient.this,
											getResources().getString(
															R.string.database_success_storing_data),
											Toast.LENGTH_LONG).show();
						}
						// Go to previous activity
						finish();
					} else if (receivedIntent.getStringExtra(
									AddNewClient.ADD_NEW_CLIENT_ACTIVITY_MODE).equals(AddNewQuestion.ACTIVITY_MODE_UPDATE)) {
						int result = schemaHelper.updateClient(receivedIntent.getStringExtra(AddNewClient.ACTIVITY_MODE_DB_ITEM_ID),
										clientNameData,Integer.parseInt(phoneNumberData),addressData,contactNameData);
						if (result > 0)
							Toast.makeText(AddNewClient.this,
											getResources().getString(
															R.string.database_success_updating_data),
											Toast.LENGTH_LONG).show();
						else
							Toast.makeText(AddNewClient.this,
											getResources().getString(
															R.string.database_error_storing_data),
											Toast.LENGTH_LONG).show();
						// Go to previous activity
						finish();
					}
				}
			}
		});
	}

	private void validateEditText(EditText editText,
					HashMap<Integer, String> validationTestNumbers) {

		String textToValidate = editText.getText().toString();
		String errorMessage = EditTextValidation.editTextValidation(
						textToValidate, validationTestNumbers);

		if (editText.equals(clientName))
			clientNameValidationFlag = errorMessage == null ? true : false;
		else if (editText.equals(phoneNumber))
			phoneNumberValidationFlag = errorMessage == null ? true : false;
		else if (editText.equals(address))
			addressValidationFlag = errorMessage == null ? true : false;
		else if (editText.equals(contactName))
			contactNameValidationFlag = errorMessage == null ? true : false;

		if (errorMessage != null) {
			editText.setError(errorMessage);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_add_new_client, menu);
		return true;
	}
}
