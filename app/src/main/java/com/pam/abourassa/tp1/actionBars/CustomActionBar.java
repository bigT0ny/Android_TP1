package com.pam.abourassa.tp1.actionBars;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pam.abourassa.tp1.R;

/**
 * Created by Anthony on 28/12/2016.
 */

public class CustomActionBar {

    /**
     * Methode permettant de personnaliser l'actionBar de chaque vue.
     */
    private static void getActionBar(Activity activity, String actionBarTitle) {
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Initialisation du titre de l'actionBar qui est centrer
        TextView countryNameTextview = (TextView) activity.findViewById(R.id.custom_action_bar_textview_title);
        countryNameTextview.setText(actionBarTitle);
        countryNameTextview.setGravity(Gravity.CENTER);
    }
    /**
     * Methode permettant d'injecter un layout personnaliser dans l'actionBar du fragment en cours
     * a l'aide d'un mipmap. Le resId du mipmap est recuperer afin de pouvoir afficher le mipmap.
     */
    public static void setActionBar(Activity activity, String actionBarTitle, int mipmapResId) {
        getActionBar(activity, actionBarTitle);
        ((ImageView) activity.findViewById(R.id.custom_action_bar_imageview_icon)).setImageResource(mipmapResId);
    }

    /**
     * Methode surchargee permettant de passer en parametre un bitmap plutot que le numero d'un
     * mipmap. Permet d'injecter un layout personnaliser dans l'actionBar du fragment en cours.
     * Si le bitmap est null, un drapeau (image mipmap) est afficher dans le imageview, sinon,
     * c'est le drapeau du pays qui est afficher a l'interieur.
     */
    public static void setActionBar(Activity activity, String actionBarTitle, Bitmap flagBitmap) {
        getActionBar(activity, actionBarTitle);

        if (flagBitmap != null) {
            ((ImageView) activity.findViewById(R.id.custom_action_bar_imageview_icon)).setImageBitmap(flagBitmap);
        }else {
            ((ImageView) activity.findViewById(R.id.custom_action_bar_imageview_icon)).setImageResource(R.mipmap.ic_flag);
        }
    }

    /**
     * Methode surchargee permettant de passer en parametre un bitmap plutot que le numero d'un
     * mipmap. Permet d'injecter un layout personnaliser dans l'actionBar du fragment en cours.
     * Si le bitmap est null, un drapeau (image mipmap) est afficher dans le imageview, sinon,
     * c'est le drapeau du pays qui est afficher a l'interieur. De plus, un boolean permet de verifier
     * l'etat de la preference show_flags. Si celle-ci est a false, dans le cityFragment, cela permet
     * de ne pas afficher d'image dans le imageview.
     */
    public static void setActionBar(Activity activity, String actionBarTitle, Bitmap flagBitmap, boolean showFlag) {
        getActionBar(activity, actionBarTitle);
        ImageView iconImageView = (ImageView) activity.findViewById(R.id.custom_action_bar_imageview_icon);

        // Si la preference show_flag est a true, le drapeaux est afficher, sinon, il ne l'est pas.
        if (showFlag) {
            if (flagBitmap != null) {
                iconImageView.setImageBitmap(flagBitmap);
            } else {
                iconImageView.setImageResource(R.mipmap.ic_flag);
            }
        }else {
            iconImageView.setVisibility(View.GONE);
        }
    }

}