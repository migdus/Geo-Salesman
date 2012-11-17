package com.diplomadoUNAL.geosalesman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.diplomadoUNAL.geosalesman.database.QuestionTable;
import com.diplomadoUNAL.geosalesman.database.SchemaHelper;

public class AnswerQuestions extends Activity {
	public static String ACTIVITY_MODE_DB_ITEM_ID = "com.diplomadoUNAL.geosalesman.reportTemplateId";

	private Intent mReceivedIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer_questions);

		mReceivedIntent = getIntent();

		StringBuilder stringBuilder = new StringBuilder();

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

		// get the report template id from database
		int reportTemplateId = Integer.parseInt(mReceivedIntent
						.getStringExtra(ACTIVITY_MODE_DB_ITEM_ID));

		// get questions from report template id
		SchemaHelper schemaHelper = new SchemaHelper(this);

		ArrayList<String> questionIdList = schemaHelper
						.getReportTemplateQuestionIDByReportTemplateID(reportTemplateId);

		final ArrayList<RadioButton> radioButtonList = new ArrayList<RadioButton>();
		final ArrayList<SeekBar> seekBarList = new ArrayList<SeekBar>();
		final ArrayList<EditText> editTextList = new ArrayList<EditText>();

		for (String questionId : questionIdList) {
			final HashMap<String, String> questionDetails = schemaHelper
							.getQuestion(Integer.parseInt(questionId));
			// setup the title
			TextView title = new TextView(this);
			title.setText(questionDetails.get(QuestionTable.QUESTION_TITLE));
			title.setTextAppearance(this, android.R.style.TextAppearance_Large);
			linearLayout.addView(title);

			// setup the description
			TextView description = new TextView(this);
			description.setText(questionDetails
							.get(QuestionTable.QUESTION_DESCRIPTION));
			description.setTextAppearance(this,
							android.R.style.TextAppearance_Medium);
			linearLayout.addView(description);

			// setup the question
			TextView question = new TextView(this);
			question.setText(questionDetails.get(QuestionTable.QUESTION));
			question.setTextAppearance(this,
							android.R.style.TextAppearance_Medium);
			linearLayout.addView(question);

			if (questionDetails.get(QuestionTable.QUESTION_TYPE).equals(
							AddNewQuestion.QUESTION_TYPE_RANGE)) {
				// create a horizontal lineal layout for each one of the elements
				LinearLayout horizontalLinearLayout = new LinearLayout(this);
				horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

				linearLayout.addView(horizontalLinearLayout);

				// TextView for showing the results of the slider
				final TextView textView = new TextView(this);
				textView.setTextAppearance(this,
								android.R.style.TextAppearance_Medium);
				textView.setGravity(Gravity.CENTER);

				SeekBar seekBar = new SeekBar(this);

				final String[] answerOptions = questionDetails.get(
								QuestionTable.ANSWER_OPTIONS).split("-");

				String[] respuestas = new String[4];
				respuestas[0] = questionDetails
								.get(QuestionTable.QUESTION_TITLE);
				respuestas[1] = questionDetails
								.get(QuestionTable.QUESTION_DESCRIPTION);
				respuestas[2] = questionDetails.get(QuestionTable.QUESTION);
				respuestas[3] = answerOptions[0];
				seekBar.setTag(respuestas);

				seekBar.setMax(Integer.parseInt(answerOptions[1])
								- Integer.parseInt(answerOptions[0]));
				textView.setText(answerOptions[0]);

				seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
									int progress, boolean fromUser) {
						String selectedValue = Integer.toString(progress
										+ Integer.parseInt(answerOptions[0]));
						textView.setText(selectedValue);
						String[] respuestas = new String[4];
						respuestas[0] = questionDetails
										.get(QuestionTable.QUESTION_TITLE);
						respuestas[1] = questionDetails
										.get(QuestionTable.QUESTION_DESCRIPTION);
						respuestas[2] = questionDetails
										.get(QuestionTable.QUESTION);
						respuestas[3] = selectedValue;
						seekBar.setTag(respuestas);

					}
				});
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);
				layoutParams.weight = 1;
				horizontalLinearLayout.addView(seekBar, layoutParams);
				seekBarList.add(seekBar);
				layoutParams = new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);
				layoutParams.weight = 4;
				horizontalLinearLayout.addView(textView, layoutParams);

			} else if (questionDetails.get(QuestionTable.QUESTION_TYPE).equals(
							AddNewQuestion.QUESTION_TYPE_YES_NO)) {

				List<String> options = Arrays.asList(
								getResources().getString(R.string.Yes),
								getResources().getString(R.string.No));
				final List<RadioButton> radioButtons = new ArrayList<RadioButton>();

				for (String option : options) {
					// create a horizontal lineal layout for each one of the options
					LinearLayout horizontalLinearLayout = new LinearLayout(this);
					horizontalLinearLayout
									.setOrientation(LinearLayout.HORIZONTAL);

					linearLayout.addView(horizontalLinearLayout);

					// add a text for the option
					TextView textView = new TextView(this);
					textView.setText(option);
					textView.setTextAppearance(this,
									android.R.style.TextAppearance_Medium);
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.WRAP_CONTENT);
					layoutParams.weight = 1;
					horizontalLinearLayout.addView(textView, layoutParams);

					// add the radio button
					RadioButton radioButton = new RadioButton(this);
					radioButtonList.add(radioButton);
					
					String[] respuestas = new String[4];
					respuestas[0] = questionDetails
									.get(QuestionTable.QUESTION_TITLE);
					respuestas[1] = questionDetails
									.get(QuestionTable.QUESTION_DESCRIPTION);
					respuestas[2] = questionDetails.get(QuestionTable.QUESTION);
					respuestas[3] = option;
					radioButton.setTag(respuestas);

					radioButtons.add(radioButton);
					radioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
										boolean isChecked) {
							if (isChecked) {
								for (RadioButton radioButton : radioButtons) {
									radioButton.setChecked(false);
								}
								buttonView.setChecked(true);
							}
						}
					});
					layoutParams = new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.WRAP_CONTENT);
					layoutParams.weight = 4;
					radioButton.setGravity(Gravity.CENTER);
					horizontalLinearLayout.addView(radioButton, layoutParams);

				}
			} else if (questionDetails.get(QuestionTable.QUESTION_TYPE).equals(
							AddNewQuestion.QUESTION_TYPE_OPEN)) {
				EditText multiline = new EditText(this);
				editTextList.add(multiline);
				multiline.setSingleLine(false);

				String[] respuestas = new String[4];
				respuestas[0] = questionDetails
								.get(QuestionTable.QUESTION_TITLE);
				respuestas[1] = questionDetails
								.get(QuestionTable.QUESTION_DESCRIPTION);
				respuestas[2] = questionDetails.get(QuestionTable.QUESTION);
				respuestas[3] = (String) multiline.getText().toString();
				multiline.setTag(respuestas);

				multiline.setHint(getResources().getString(
								R.string.write_your_answer_here));
				multiline.setOnFocusChangeListener(new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!hasFocus) {
							String[] respuestas = new String[4];
							respuestas[0] = questionDetails
											.get(QuestionTable.QUESTION_TITLE);
							respuestas[1] = questionDetails
											.get(QuestionTable.QUESTION_DESCRIPTION);
							respuestas[2] = questionDetails
											.get(QuestionTable.QUESTION);
							respuestas[3] = ((TextView) v).getText().toString();
							((TextView) v).setTag(respuestas);
						}

					}
				});
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);
				linearLayout.addView(multiline, layoutParams);

			}

			// iterate for each one of the questions, and create a widget according to each one.

			// create a button that captures the result of the selected options
			// separator
			linearLayout.addView((ImageView) LayoutInflater.from(this)
							.inflate(R.layout.separator, null)
							.findViewById(R.id.separator));
		}

		Button okButton = (Button) findViewById(R.id.okButton);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StringBuilder questionReportAnswers = new StringBuilder();
				
				for(RadioButton radioButton:radioButtonList){
					if(radioButton.isChecked()){
						String textToSend =getResources().getString(R.string.question_report_answer);
						textToSend=textToSend.replace("$question_title", ((String [])radioButton.getTag())[0]);
						textToSend=textToSend.replace("$question_description", ((String [])radioButton.getTag())[1]);
						textToSend=textToSend.replace("$question", ((String [])radioButton.getTag())[2]);
						textToSend=textToSend.replace("$answer", ((String [])radioButton.getTag())[3]);
						questionReportAnswers.append(textToSend);
					}
				}
				for(EditText editText:editTextList){
					String textToSend =getResources().getString(R.string.question_report_answer);
					textToSend=textToSend.replace("$question_title", ((String [])editText.getTag())[0]);
					textToSend=textToSend.replace("$question_description", ((String [])editText.getTag())[1]);
					textToSend=textToSend.replace("$question", ((String [])editText.getTag())[2]);
					textToSend=textToSend.replace("$answer", ((String [])editText.getTag())[3]);
					questionReportAnswers.append(textToSend);
				}
				for(SeekBar seekBar:seekBarList){
					String textToSend =getResources().getString(R.string.question_report_answer);
					textToSend=textToSend.replace("$question_title", ((String [])seekBar.getTag())[0]);
					textToSend=textToSend.replace("$question_description", ((String [])seekBar.getTag())[1]);
					textToSend=textToSend.replace("$question", ((String [])seekBar.getTag())[2]);
					textToSend=textToSend.replace("$answer", ((String [])seekBar.getTag())[3]);
					questionReportAnswers.append(seekBar);
				}
				Intent sendIntent = new Intent();
				sendIntent.putExtra("questionReportAnswers",
								questionReportAnswers.toString());
				setResult(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, sendIntent);
				finish();
				

			}
		});
		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_answer_questions, menu);
		return true;
	}
}
