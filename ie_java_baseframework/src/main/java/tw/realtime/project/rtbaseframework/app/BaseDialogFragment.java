package tw.realtime.project.rtbaseframework.app;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


/**
 * 基礎對話框 Fragment
 */
public abstract class BaseDialogFragment extends DialogFragment {

    public enum DialogAction {
        /** 確定 */
        CONFIRM,
        /** 取消 */
        CANCEL
    }

    public interface OnDecisionMadeListener {
        /**
         * 當使用者點擊對話框按鈕時會呼叫
         * @param dialogFrag    本對話框實體
         * @param dialogAction  使用者所點擊的按鈕結果
         */
        void onNotification(@NonNull DialogFragment dialogFrag,
							@NonNull DialogAction dialogAction);
    }

    protected String getLogTag () {
        return this.getClass().getSimpleName();
    }


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }
	
	@Override
	public void onStart() {
		super.onStart();
		setWindowStyle();
	}

	/** 
	 * Set desired looks of the dialog, including background image/ color,
	 * window size, margin, etc.
	 * 
	 * Refs:
	 * https://gist.github.com/ishitcno1/9408188
	 */
	protected void setWindowStyle() {
		final Dialog theDialog = getDialog();
		if (null == theDialog) {
			return;
		}
		final Window window = getDialog().getWindow();
		if (null == window) {
			return;
		}
		final WindowManager.LayoutParams params = window.getAttributes();
		// Use dimAmount to control the amount of dim
		params.dimAmount = 0.6f;
		window.setAttributes(params);
		window.setBackgroundDrawableResource(android.R.color.transparent);
		if (doesLayoutNeedToBeMatchParent()) {
			window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		}
	}

	protected boolean doesLayoutNeedToBeMatchParent () {
		return false;
	}
}


