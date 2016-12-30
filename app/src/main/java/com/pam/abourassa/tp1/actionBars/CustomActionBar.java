package com.pam.abourassa.tp1.actionBars;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.pam.abourassa.tp1.R;

/**
 * Created by Anthony on 28/12/2016.
 */

public class CustomActionBar {
    /**
     * Methode permettant d'injecter un layout personnaliser dans l'actionBar du fragment en cours
     * a l'aide d'un mipmap. Le resId du mipmap est recuperer afin de pouvoir afficher le mipmap.
     */
    public static void setActionBar(Activity activity, String title, int resId) {
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ((ImageView) activity.findViewById(R.id.custom_action_bar_imageview_icon)).setImageResource(resId);
        TextView countryName_textiew = (TextView) activity.findViewById(R.id.custom_action_bar_textview_title);
        countryName_textiew.setText(title);
        countryName_textiew.setGravity(Gravity.CENTER);
    }

    /**
     * Methode surchargee permettant de passer en parametre un bitmap plutot que le numero d'un
     * mipmap. Permet d'injecter un layout personnaliser dans l'actionBar du fragment en cours.
     * Si le bitmap est null, un drapeau (image mipmap) est afficher dans le imageview, sinon,
     * c'est le drapeau du pays qui est afficher a l'interieur.
     */
    public static void setActionBar(Activity activity, String title, Bitmap bitmap) {
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.custom_action_bar);

        TextView countryName_textiew = (TextView) activity.findViewById(R.id.custom_action_bar_textview_title);
        countryName_textiew.setText(title);
        countryName_textiew.setGravity(Gravity.CENTER);

        if (bitmap != null) {
            ((ImageView) activity.findViewById(R.id.custom_action_bar_imageview_icon)).setImageBitmap(bitmap);
        }else {
            ((ImageView) activity.findViewById(R.id.custom_action_bar_imageview_icon)).setImageResource(R.mipmap.ic_flag);
        }
    }

}
