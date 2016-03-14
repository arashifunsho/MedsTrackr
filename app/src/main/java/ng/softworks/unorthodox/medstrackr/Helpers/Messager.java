package ng.softworks.unorthodox.medstrackr.Helpers;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;

import ng.softworks.unorthodox.medstrackr.R;

/**
 * Created by unorthodox on 14/03/16.
 * This is the helper class that is to handle every message passing including dialogs, snackbars
 * and toasts.
 */
public class Messager {
    private Context _context; private MaterialStyledDialog mDialog;

    public Messager(Context context){
        _context=context;
        mDialog= new MaterialStyledDialog(context);
    }

    public void showInfoDialog(int Message){
        mDialog.setTitle(_context.getResources().getString(R.string.diag_info_title))
                .setDescription(_context.getResources().getString(Message))
                .withDialogAnimation(true, Duration.NORMAL)
                .setIcon(ContextCompat.getDrawable(_context, R.drawable.ic_info))
                .build();

        mDialog.show();

    }

    public void dosageUseMsgDialog(String Message){
        mDialog.setTitle(_context.getResources().getString(R.string.diag_drug_usage))
                .setDescription(Message)
                .withDialogAnimation(true, Duration.NORMAL)
                .setScrollable(true)
                .setIcon(ContextCompat.getDrawable(_context, R.drawable.ic_drug_use))
                .build();

        mDialog.show();
    }

    public void snackbar(View view, int message){
        Snackbar.make(view,message,Snackbar.LENGTH_LONG).show();
    }
}
