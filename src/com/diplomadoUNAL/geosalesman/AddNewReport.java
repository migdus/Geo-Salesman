package com.diplomadoUNAL.geosalesman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.diplomadoUNAL.geosalesman.database.ClientTable;
import com.diplomadoUNAL.geosalesman.database.ReportTemplateTable;
import com.diplomadoUNAL.geosalesman.database.SchemaHelper;

@SuppressLint("SetJavaScriptEnabled")
public class AddNewReport extends Activity implements LocationListener {

	private static final int REQUEST_GET_ANSWERS = 154;
	private String answerQuestionsReport;
	Button sendButton;
	protected Location location;
	LocationManager locationManager;
	String provider;
	private Button locationButton;
	private HashMap<String, String> activityForm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityForm = new HashMap<String, String>();
		setContentView(R.layout.activity_add_new_report);

		// Get the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the location provider -> use
		// default
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		location = locationManager.getLastKnownLocation(provider);

		// Initialize the location fields
		if (location != null) {
			System.out.println("Provider " + provider + " has been selected.");
			onLocationChanged(location);

		} else {
			Toast.makeText(AddNewReport.this, "Error showing the location.",
							Toast.LENGTH_SHORT).show();
		}
		locationButton = (Button) findViewById(R.id.buttonSetLocation);
		locationButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(AddNewReport.this,
								com.diplomadoUNAL.geosalesman.Location.class);
				intent.putExtra("latitude",
								Double.toString(location.getLatitude()))
								.putExtra("longitude",
												Double.toString(location
																.getLongitude()));
				startActivity(intent);

			}
		});

		sendButton = (Button) findViewById(R.id.buttonSendReport);
		final Button answerQuestionsButton = (Button) findViewById(R.id.buttonAnswerQuestions);
		answerQuestionsButton.setEnabled(false);

		final Intent answerQuestionIntent = new Intent(this,
						AnswerQuestions.class);

		answerQuestionsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(answerQuestionIntent,
								REQUEST_GET_ANSWERS);
				if (answerQuestionsReport != null) {
					sendButton.setEnabled(true);
				}
			}
		});

		final EditText editTextComments = (EditText) findViewById(R.id.editTextComments);
		editTextComments.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					activityForm.put("editTextComments",
									(String) ((EditText) v).getText()
													.toString());
				}
			}
		});

		final EditText editTextReportTemplate = (EditText) findViewById(R.id.editTextReportTemplateName);
		editTextReportTemplate.setKeyListener(null);
		editTextReportTemplate.setCursorVisible(false);
		editTextReportTemplate
						.setOnFocusChangeListener(new OnFocusChangeListener() {

							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								if (hasFocus) {

									AlertDialog.Builder builder = new AlertDialog.Builder(
													AddNewReport.this);
									// get report names list
									SchemaHelper schemaHelper = new SchemaHelper(
													getApplicationContext());
									final ArrayList<HashMap<String, String>> reportTemplateInfo = schemaHelper
													.getReportTemplateNameAndDescription();
									ArrayList<String> reportTemplate = new ArrayList<String>();
									for (int i = 0; i < reportTemplateInfo
													.size(); i++) {
										reportTemplate.add(reportTemplateInfo
														.get(i)
														.get(ReportTemplateTable.NAME)
														+ "\n"
														+ reportTemplateInfo
																		.get(i)
																		.get(ReportTemplateTable.DESCRIPTION));
									}
									final CharSequence[] cs = reportTemplate
													.toArray(new CharSequence[reportTemplate
																	.size()]);
									builder.setTitle(getResources()
													.getString(R.string.select_report_template));
									builder.setSingleChoiceItems(
													cs,
													-1,
													new DialogInterface.OnClickListener() {

														public void onClick(
																		DialogInterface dialogInterface,
																		int item) {
															editTextReportTemplate
																			.setText(cs[item]);
															answerQuestionIntent
																			.putExtra(AnswerQuestions.ACTIVITY_MODE_DB_ITEM_ID,
																							reportTemplateInfo
																											.get(item)
																											.get(ReportTemplateTable.ID));
															answerQuestionsButton
																			.setEnabled(true);
															activityForm.put(
																			"editTextReportTemplate",
																			(String) cs[item]);
															dialogInterface.dismiss();
														}
													});
									builder.create().show();

								}

							}
						});

		final EditText editTextClient = (EditText) findViewById(R.id.editTextClient);
		editTextClient.setKeyListener(null);
		editTextReportTemplate.setCursorVisible(false);
		editTextClient.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

					AlertDialog.Builder builder = new AlertDialog.Builder(
									AddNewReport.this);
					// get client list
					SchemaHelper schemaHelper = new SchemaHelper(
									getApplicationContext());
					ArrayList<HashMap<String, String>> clientsInfo = schemaHelper
									.getClientNameAndContactName();
					ArrayList<String> clients = new ArrayList<String>();
					for (int i = 0; i < clientsInfo.size(); i++) {
						clients.add(clientsInfo.get(i).get(ClientTable.NAME)
										+ "\n"
										+ clientsInfo.get(i)
														.get(ClientTable.CONTACT_NAME));
					}
					final CharSequence[] cs = clients
									.toArray(new CharSequence[clients.size()]);
					builder.setTitle(getResources().getString(
									R.string.select_client));
					builder.setSingleChoiceItems(cs, -1,
									new DialogInterface.OnClickListener() {

										public void onClick(
														DialogInterface dialogInterface,
														int item) {
											editTextClient.setText(cs[item]);
											activityForm.put("editTextClient",
															(String) cs[item]);
											dialogInterface.dismiss();
										}
									});
					builder.create().show();

				}

			}
		});

		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String reportDetails = getResources().getString(
								R.string.report_details);
				reportDetails = reportDetails.replace("$report_comments",
								activityForm.get("editTextComments"));
				reportDetails = reportDetails.replace("$report_client",
								activityForm.get("editTextClient"));
				reportDetails = reportDetails.replace(
								"$report_template_name_and_description",
								activityForm.get("editTextReportTemplate"));

				String emailMessage = "<html><body><div align='left'><p>Reporte GeoSalesman</b>: "
								+ "</p><p>"
								+ reportDetails
								+ "</p><p>"
								+ "</p><p>"
								+ answerQuestionsReport
								+ "</p><p>"
								+ "</p><p> <a href="
								+ "https://maps.google.com/?q="
								+ location.getLatitude()
								+ ","
								+ location.getLongitude()
								+ ">Location</a></p></p><p><b>Thank you</b></div></body></html> ";

				Intent i = new Intent(Intent.ACTION_SEND);
				// i.setType("message/rfc822"); //para plain text
				i.setType("text/html");
				i.putExtra(Intent.EXTRA_EMAIL,
								new String[] { "example@gmail.com" });
				i.putExtra(Intent.EXTRA_SUBJECT, "Reporte de Visita");
				// i.putExtra(Intent.EXTRA_TEXT , "adjunto correo"); //para plain text
				i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(emailMessage));

				try {
					startActivity(Intent.createChooser(i, "Send mail..."));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(AddNewReport.this,
									"There are no email clients installed.",
									Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	@Override
	public void onLocationChanged(Location location) {

	}

	private void openMap() {

		String uri = String.format(Locale.ENGLISH, "geo:%f,%f",
						location.getLatitude(), location.getLongitude());
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		this.startActivity(intent);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
					Intent receivedData) {

		super.onActivityResult(requestCode, resultCode, receivedData);
		switch (requestCode) {

		case REQUEST_GET_ANSWERS:
			if (receivedData != null
							&& receivedData.hasExtra("questionReportAnswers")) {
				answerQuestionsReport = receivedData
								.getStringExtra("questionReportAnswers");
				sendButton.setEnabled(true);
			}
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_add_new_report, menu);
		return true;
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
