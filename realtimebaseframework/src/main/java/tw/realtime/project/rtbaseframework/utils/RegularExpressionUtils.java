package tw.realtime.project.rtbaseframework.utils;

import android.telephony.PhoneNumberUtils;
import android.util.Patterns;


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

}
