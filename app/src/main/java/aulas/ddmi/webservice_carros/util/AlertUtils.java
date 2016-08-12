package aulas.ddmi.webservice_carros.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import aulas.ddmi.webservice_carros.R;

/**
 * Created by vagner on 15/05/16.
 */
public class AlertUtils{

    private final String TAG = "Webservice_Carros"; //TAG para o LogCat
    private static ProgressDialog mProgressDialog;

    //AlertDialog
    public static void showOk(final Context context, int title, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context); //cria um buider
        builder.setTitle(title).setMessage(message); //insere o título e a mensagem
        // Adiciona o botão e trata o evento onClick
        builder.setPositiveButton(R.string.alertdialog_buttom_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //fazer nada aqui neste projeto.
            }
        });
        AlertDialog dialog = builder.create(); //cria o alerta
        dialog.show(); //apresenta a caixa de diálogo
    }

    // ProgressDialog show
    public static void  showWait(final Context context, int title, int message){
        //cria e configura a caixa de progressão e diálogo
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(context.getResources().getString(message));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
    }

    // Progress Dismiss
    public static void dismissWait(){
        mProgressDialog.dismiss();
    }


}//fim da classe
