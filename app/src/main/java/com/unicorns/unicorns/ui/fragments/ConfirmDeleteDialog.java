package com.unicorns.unicorns.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.unicorns.unicorns.database.Unicorn;
import com.unicorns.unicorns.utils.JsonFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * ConfirmDeleteDialog created by Andreas Pribitzer 12.01.2020
 */

public class ConfirmDeleteDialog extends DialogFragment {

    private static final String KEY_UNICORN = "jsonUnicorn";

    private IDeleteUnicorn listener;

    //Get a new instance of the dialog with serialized Unicorn
    public static ConfirmDeleteDialog newInstance(String jsonUnicorn) {
        ConfirmDeleteDialog fragment = new ConfirmDeleteDialog();
        Bundle args = new Bundle();
        args.putString(KEY_UNICORN, jsonUnicorn);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //Get deserialized Unicorn
        String jsonUnicorn = getArguments() != null ? getArguments().getString(KEY_UNICORN) : "";
        final Unicorn unicorn;
        try {
            unicorn = JsonFormatter.getUnicornFromLocalJsonObject(new JSONObject(jsonUnicorn));
            builder.setMessage("Are you sure you want to delete " + unicorn.getName() + "?")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(listener != null) {
                                listener.deleteUnicorn(unicorn);
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            dismiss();
        }

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (IDeleteUnicorn) context;
        } catch (ClassCastException ignored) {
        }
    }

    public interface IDeleteUnicorn {
        void deleteUnicorn(Unicorn unicorn);
    }
}
