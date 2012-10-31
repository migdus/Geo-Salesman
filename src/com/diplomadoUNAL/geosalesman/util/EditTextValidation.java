package com.diplomadoUNAL.geosalesman.util;

import java.util.Iterator;

import android.util.SparseArray;
import android.widget.EditText;

public class EditTextValidation {
    public static final int ALPHABETHIC_VALIDATION = 1; // only alphabethic and
							// spaces allowed
    public static final int NUMBER_VALIDATION = 2;
    public static final int NUMBER_WITH_SPACES_VALIDATION = 6;
    public static final int NO_PIPE_CHAR_VALIDATION = 3; // this character is
							 // special, so it is
							 // not allowed as input
    public static final int RANGE_VALIDATION = 4;
    public static final int CHARACTER_VALIDATION = 5;

    /**
     * 
     * @param editText
     * @param validationTest
     *            k: validation test; v: error message to shown into edit test
     *            in case of error
     * @return True if all tests are Ok. False otherwhise.
     */
    public static boolean editTextValidation(EditText editText,
	    SparseArray<String> validationTest) {
	String textToValidate = editText.getText().toString();
	int size = validationTest.size();
	int key = 0;
	for (int i = 0; i < size; i++) {
	    key = validationTest.keyAt(i);
	    switch (key) {
	    case ALPHABETHIC_VALIDATION:
		if (!textToValidate.matches("[a-zA-Z\\s']+")) {
		    editText.setError(validationTest.get(key));
		    return false;
		}
		break;
	    case NUMBER_VALIDATION:
		if (!textToValidate.matches("[0-9]+")) {
		    editText.setError(validationTest.get(key));
		    return false;
		}
		break;
	    case NUMBER_WITH_SPACES_VALIDATION:
		if (!textToValidate.matches("[0-9\\s']+")) {
		    editText.setError(validationTest.get(key));
		    return false;
		}
		break;
	    case NO_PIPE_CHAR_VALIDATION:
		if (!textToValidate.matches("\\|")) {
		    // TODO check this thing is ok
		    editText.setError(validationTest.get(key));
		    return false;
		}
		break;
	    case RANGE_VALIDATION:
		break;
	    case CHARACTER_VALIDATION:
		break;
	    }
	}
	return true;
    }
}
