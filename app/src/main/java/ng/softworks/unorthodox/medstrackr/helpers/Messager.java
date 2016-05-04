package ng.softworks.unorthodox.medstrackr.helpers;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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

    public void showInfoDialog(int title,int Message){
        mDialog
                .setTitle(_context.getResources().getString(title))
                .setDescription(_context.getResources().getString(Message))
                .withIconAnimation(true)
                .setCancelable(false)
                .withDialogAnimation(true,Duration.NORMAL)
                .setIcon(ContextCompat.getDrawable(_context, R.drawable.ic_info))
                .setPositive(_context.getResources().getString(R.string.diag_OK),null)
                .build();

        mDialog.show();

    }

    public void showAboutDialog(){
        LayoutInflater inflater=(LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDialog
                .setTitle(_context.getResources().getString(R.string.about_app))
                .setCustomView(inflater.inflate(R.layout.dialog_about_app,null))
                .withIconAnimation(true)
                .setCancelable(false)
                .withDialogAnimation(true,Duration.NORMAL)
                .setIcon(ContextCompat.getDrawable(_context, R.drawable.ic_info))
                .setPositive(_context.getResources().getString(R.string.diag_OK),null)
                .build();

        mDialog.show();
    }

    public void dosageUseMsgDialog(String Message){
        mDialog.setTitle(_context.getResources().getString(R.string.diag_drug_usage))
                .setDescription(Message)
                .withDialogAnimation(true, Duration.NORMAL)
                .setScrollable(true)
                .setIcon(ContextCompat.getDrawable(_context, R.drawable.ic_drug_use))
                .setPositive(_context.getResources().getString(R.string.diag_OK),new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
//                        Log.d("MaterialStyledDialogs", "Do something!");
                    }
                })
                .build();

        mDialog.show();
    }

    public void snackbar(View view, int message){
        Snackbar.make(view,message,Snackbar.LENGTH_LONG).show();
    }

    public void snackbar(View view, String message){
        Snackbar.make(view,message,Snackbar.LENGTH_LONG).show();
    }
}
