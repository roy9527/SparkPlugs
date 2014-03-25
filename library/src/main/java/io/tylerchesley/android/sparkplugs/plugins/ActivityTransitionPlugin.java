package io.tylerchesley.android.sparkplugs.plugins;

import android.app.Activity;
import android.os.Bundle;

import org.json.JSONObject;

import io.tylerchesley.android.sparkplugs.R;
import io.tylerchesley.android.sparkplugs.SparkPlugBase;

/**
 *
 */
public class ActivityTransitionPlugin extends SparkPlugBase {

//------------------------------------------
//  Static Methods
//------------------------------------------

    public static ActivityTransitionPlugin newSlideIn() {
        return new ActivityTransitionPlugin(R.anim.activity_open_translate,
                R.anim.activity_close_scale,
                R.anim.activity_open_scale,
                R.anim.activity_close_translate);
    }

    private static boolean shouldOverrideTransition(Activity activity) {
        return !(activity instanceof ActivityTransitionPluginActivity) ||
                ((ActivityTransitionPluginActivity) activity).shouldOverridePendingTransition();
    }

//------------------------------------------
//  Variables
//------------------------------------------

    private final int mCreateEnterAnimation;
    private final int mCreateExitAnimation;

    private final int mFinishEnterAnimation;
    private final int mFinishExitAnimation;

//------------------------------------------
//  Constructors
//------------------------------------------

    public ActivityTransitionPlugin(int createEnterAnimation, int createExitAnimation) {
        this(createEnterAnimation, createExitAnimation, createEnterAnimation, createExitAnimation);
    }

    public ActivityTransitionPlugin(int createEnterAnimation, int createExitAnimation,
                                    int finishEnterAnimation, int finishExitAnimation) {
        mCreateEnterAnimation = createEnterAnimation;
        mCreateExitAnimation = createExitAnimation;
        mFinishEnterAnimation = finishEnterAnimation;
        mFinishExitAnimation = finishExitAnimation;
    }

//------------------------------------------
//  Overridden Methods
//------------------------------------------

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {
        super.onCreate(activity, savedInstanceState);

        if (shouldOverrideTransition(activity)) {
            activity.overridePendingTransition(mCreateEnterAnimation, mCreateExitAnimation);
        }
    }

    @Override
    public void onPause(Activity activity) {
        super.onPause(activity);

        if (shouldOverrideTransition(activity)) {
            activity.overridePendingTransition(mFinishEnterAnimation, mFinishExitAnimation);
        }
    }

//------------------------------------------
//  Interfaces
//------------------------------------------

    public static interface ActivityTransitionPluginActivity {

        boolean shouldOverridePendingTransition();

    }

}
