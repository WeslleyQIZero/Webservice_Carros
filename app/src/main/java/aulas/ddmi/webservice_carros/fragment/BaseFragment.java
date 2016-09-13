package aulas.ddmi.webservice_carros.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by vagner on 13/09/16.
 */
public class BaseFragment extends Fragment {
    //TAG para o LogCat
    protected static final String TAG = "web_service_carros";
    //uma caixa de progressão com uma animação de progressão
    private ProgressDialog mProgressDialog;

    //Mensagem de alerta com botão Ok
    public static void alertDialog(final Context context, final int title, final int mensagem) {
        try {
            AlertDialog dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(mensagem).create();
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    //Emite uma ProgressDialog
    //Uma caixa com uma mensagem de progressão e uma barra de progressão
    public void  showWait(final Context context, int title, int message){
        //cria e configura a caixa de diálogo e progressão
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(context.getResources().getString(message));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
    }

    //Faz Dismiss na ProgressDialog
    public void dismissWait(){
        mProgressDialog.dismiss();
    }

    /**
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     */
    public boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return false;
    }
}
