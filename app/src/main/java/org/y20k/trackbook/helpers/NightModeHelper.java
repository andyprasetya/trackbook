/**
 * NightModeHelper.java
 * Implements the NightModeHelper class
 * A NightModeHelper can toggle and restore the state of the theme's Night Mode
 *
 * This file is part of
 * TRACKBOOK - Movement Recorder for Android
 *
 * Copyright (c) 2016-18 - Y20K.org
 * Licensed under the MIT-License
 * http://opensource.org/licenses/MIT
 *
 * Trackbook uses osmdroid - OpenStreetMap-Tools for Android
 * https://github.com/osmdroid/osmdroid
 */

package org.y20k.trackbook.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;


/**
 * NightModeHelper class
 */
public final class NightModeHelper implements TrackbookKeys {

    /* Define log tag */
    private static final String LOG_TAG = NightModeHelper.class.getSimpleName();


    /* Switches to opposite theme */
    public static void switchToOpposite(Context context) {
        switch (getCurrentNightModeState(context)) {
            case Configuration.UI_MODE_NIGHT_NO:
                // night mode is currently not active - turn on night mode
                activateNightMode(context);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // night mode is currently active - turn off night mode
                deactivateNightMode(context);
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                // don't know what mode is active - turn off night mode
                deactivateNightMode(context);
                break;
        }
    }


    /* Sets night mode / dark theme */
    public static void restoreSavedState(Context context) {
        int savedNightModeState = loadNightModeState(context);
        int currentNightModeState = getCurrentNightModeState(context);
        if (savedNightModeState != -1 && savedNightModeState != currentNightModeState) {
            switch (savedNightModeState) {
                case Configuration.UI_MODE_NIGHT_NO:
                    // turn off night mode
                    deactivateNightMode(context);
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    // turn on night mode
                    activateNightMode(context);
                    break;
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    // turn off night mode
                    deactivateNightMode(context);
                    break;
            }
        }
    }


    /* Returns state of night mode */
    private static int getCurrentNightModeState(Context context) {
        return context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
    }


    /* Activates Night Mode */
    private static void activateNightMode(Context context) {
        saveNightModeState(context, Configuration.UI_MODE_NIGHT_YES);

        // switch to Nighh Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }


    /* Deactivates Night Mode */
    private static void deactivateNightMode(Context context) {
        // save the new state
        saveNightModeState(context, Configuration.UI_MODE_NIGHT_NO);

        // switch to Day Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }


    /* Save state of night mode */
    private static void saveNightModeState(Context context, int currentState) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(PREF_NIGHT_MODE_STATE, currentState);
        editor.apply();
    }


    /* Load state of Night Mode */
    private static int loadNightModeState(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_NIGHT_MODE_STATE, -1);
    }

}
