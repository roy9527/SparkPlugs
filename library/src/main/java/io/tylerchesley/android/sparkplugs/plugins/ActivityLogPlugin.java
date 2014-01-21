/*
 * Copyright 2014 Tyler Chesley.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.tylerchesley.android.sparkplugs.plugins;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import io.tylerchesley.android.sparkplugs.SparkPlugBase;

/**
 * @author Tyler Chesley
 */
public class ActivityLogPlugin extends SparkPlugBase {

//------------------------------------------
//  Interface
//------------------------------------------

    public interface Logger {

        void log(String tag, String message);

    }

//------------------------------------------
//  Variables
//------------------------------------------

    private final String mTag;
    private final Logger mLogger;

//------------------------------------------
//  Constructor
//------------------------------------------

    public ActivityLogPlugin(String tag) {
        this(tag, new DefaultLogger());
    }

    public ActivityLogPlugin(String tag, Logger logger) {
        if (tag == null) {
            throw new NullPointerException("Tag cannot be null");
        }

        if (logger == null) {
            logger = new DefaultLogger();
        }

        mTag = tag;
        mLogger = logger;
    }

//------------------------------------------
//  Overridden Methods
//------------------------------------------

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {
        mLogger.log(mTag, "onCreate()");
    }

    @Override
    public void onStart(Activity activity) {
        mLogger.log(mTag, "onStart()");
    }

    @Override
    public void onRestart(Activity activity) {
        mLogger.log(mTag, "onRestart()");
    }

    @Override
    public void onResume(Activity activity) {
        mLogger.log(mTag, "onResume()");
    }

    @Override
    public void onPause(Activity activity) {
        mLogger.log(mTag, "onPause()");
    }

    @Override
    public void onStop(Activity activity) {
        mLogger.log(mTag, "onStop()");
    }

    @Override
    public void onDestroy(Activity activity) {
        mLogger.log(mTag, "onDestroy()");
    }

//------------------------------------------
//  Inner Classes
//------------------------------------------

    public static class DefaultLogger implements Logger {

        @Override
        public void log(String tag, String message) {
            Log.d(tag, message);
        }

    }

}
