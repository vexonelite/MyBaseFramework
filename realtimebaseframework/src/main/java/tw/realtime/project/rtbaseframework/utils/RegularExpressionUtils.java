package tw.realtime.project.rtbaseframework.utils;

import android.telephony.PhoneNumberUtils;
import android.util.Patterns;

import java.util.regex.Pattern;


public class RegularExpressionUtils {

	/**  
	 * Using matcher object's matches() to validate 
	 * if an input String of e-mail matches the specified regular expression.
	 */
	public static boolean checkEmail(String email) {
        //return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
		return Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	/**  
	 * Using matcher object's matches() to validate 
	 * if an input String of landline number matches the specified regular expression.
	 */
	public static boolean checkCellPhoneNumber(String cellPhoneNumber) {
		return PhoneNumberUtils.isGlobalPhoneNumber(cellPhoneNumber);
		//return Patterns.PHONE.matcher(cellPhoneNumber).matches();
	}

	/*
	 * Refs:
	 * http://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
	 * http://www.mkyong.com/regular-expressions/10-java-regular-expression-examples-you-should-know/
	 * http://www.mkyong.com/regular-expressions/how-to-validate-password-with-regular-expression/
	 * http://stackoverflow.com/questions/27119527/regex-pattern-allowing-certain-special-characters
	 */

	/**
	 * Using Java Regular Expression to specify the valid input String.
	 */
	private static final String ALPHABET_DIGIT_STRING = "^[a-zA-Z0-9@]*$";

	private static final Pattern ALPHABET_DIGIT_PATTERN = Pattern.compile(ALPHABET_DIGIT_STRING);

	/**  Using matcher object's matches() to validate
	 *   if an input String of password matches the specified regular expression.
	 */
	public static boolean checkAlphabetDigitString(String input) {
		return ALPHABET_DIGIT_PATTERN.matcher(input).matches();
	}

	public static boolean checkDpInput (String dpInput, int minDpLength, int maxDpLength) {
		if ( (null == dpInput) || (dpInput.length() < minDpLength) || (dpInput.length() > maxDpLength) ) {
			return false;
		}
		else {
			return checkAlphabetDigitString(dpInput);
		}
	}

}
