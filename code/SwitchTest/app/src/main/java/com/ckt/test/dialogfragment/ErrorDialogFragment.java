package com.ckt.test.dialogfragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.ckt.test.R;

/**
 * Created by wgp on 2017/8/21.
 * 错误对话框，条件不具备测试时候弹出
 */

public class ErrorDialogFragment extends DialogFragment {
    private static final String TAG = "ErrorDialogFragment";
    private AlertDialog mAlertDialog;

    public static ErrorDialogFragment newInstance() {
        ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
        return errorDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog: ");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.error_dialog_fragment, null);
        mAlertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setIcon(R.drawable.error)
                .setTitle("Error!")
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .create();
        mAlertDialog.setCanceledOnTouchOutside(true);//设置点击空白处对话框消失
        return mAlertDialog;
    }

    /**
     * 防止因用户点击过快Dialog多次弹出
     *
     * @param fragmentManager fragment管理者
     * @param activity        Activity上下文
     * @return
     */
    public static ErrorDialogFragment showDialog(FragmentManager fragmentManager, FragmentActivity activity) {
        ErrorDialogFragment errorDialogFragment =
                (ErrorDialogFragment) fragmentManager.findFragmentByTag(TAG);
        if (null == errorDialogFragment) {
            errorDialogFragment = newInstance();
        }
        if (!activity.isFinishing()
                && null != errorDialogFragment
                && !errorDialogFragment.isAdded()) {
            fragmentManager.beginTransaction()
                    .add(errorDialogFragment, TAG)
                    .commitAllowingStateLoss();
        }
        return errorDialogFragment;
    }
}
