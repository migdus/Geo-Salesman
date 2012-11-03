package com.diplomadoUNAL.geosalesman.util;

import java.util.HashMap;
import java.util.Iterator;

public class EditTextValidation {
	public static final int ALPHABETHIC_VALIDATION = 1; // only alphabethic and
	// spaces allowed
	public static final int NUMBER_VALIDATION = 2;
	public static final int NUMBER_WITH_SPACES_VALIDATION = 3;
	public static final int NO_PIPE_CHAR_VALIDATION = 4; // this character is
	// special, so it is
	// not allowed as input
	public static final int CHARACTER_VALIDATION = 8;

	/**
	 * 
	 * @param editText
	 * @param validationTest
	 *            k: validation test; v: error message to shown into edit test in case of error
	 * @return String with error, null if no error found
	 */
	public static String editTextValidation(String textToValidate,
					HashMap<Integer, String> validationTest) {

		int size = validationTest.size();
		StringBuilder stringBuilder = new StringBuilder();
		Iterator<Integer> keyIter = validationTest.keySet().iterator();
		while (keyIter.hasNext()) {
			Integer key = keyIter.next();

			boolean flagMatchCase = false;
			switch (key.intValue()) {
			case ALPHABETHIC_VALIDATION:
				if (!textToValidate.matches("[a-zA-Z ‡Ž’—œAƒêîò„–\\s']+"))
					flagMatchCase = true;
				break;
			case NUMBER_VALIDATION:
				if (!textToValidate.matches("[0-9]+"))
					flagMatchCase = true;
				break;

			case NUMBER_WITH_SPACES_VALIDATION:
				if (!textToValidate.matches("[0-9 ]+"))
					flagMatchCase = true;
				break;
			case NO_PIPE_CHAR_VALIDATION:
				if (textToValidate.contains("|"))
					flagMatchCase = true;
				break;
			case CHARACTER_VALIDATION:
				// TODO Change to a regex
				if (textToValidate.contains("'")
								|| textToValidate.contains("\"")
								|| textToValidate.contains("+")
								|| textToValidate.contains("*"))
					flagMatchCase = true;
				break;
			}
			if (flagMatchCase) {
				stringBuilder.append(validationTest.get(key.intValue()));
				stringBuilder.append('\n');
			}
		}
		if (stringBuilder.length() > 0) {
			stringBuilder.delete(stringBuilder.length() - 1,
							stringBuilder.length());
			return stringBuilder.toString();
		} else
			return null;
	}
}
