package com.android.imgursample.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;

import com.android.imgursample.R;


public class DialogManager {

    private static DialogManager instance;

    private DialogManager() {
    }

    private DialogAnswerListener dialogAnswerListener;

    public interface DialogAnswerListener {
        void sendAnswer(boolean answer);
    }

    public void setDialogAnswerListener(DialogAnswerListener d) {
        dialogAnswerListener = d;
    }

    public synchronized static DialogManager getInstance() {
        if (instance == null) instance = new DialogManager();
        return instance;
    }

    public Dialog getProgressDialog(Context context, int messageResId) {
        Dialog progress = new Dialog(context);
        progress.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progress.setContentView(R.layout.progress_layout);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        return progress;
    }

    public Dialog getSimpleDialog(Context context, String title, String message) {
        Dialog dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogAnswerListener.sendAnswer(true);
                        dialog.dismiss();
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogAnswerListener.sendAnswer(false);
                        dialog.dismiss();
                    }
                }).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
