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

package io.tylerchesley.android.sparkplugs;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class that implements the {@link SparkPluginableActivity} interface that contains all
 * the logic for calling plugin lifecycle methods.
 *
 * Different {@link android.app.Activity} classes can utilize the helper class without
 * duplicating the logic.
 */
public class SparkPlugActivityHelper implements SparkPluginableActivity {

//------------------------------------------
//  Variables
//------------------------------------------

    private List<SparkPlug> mPlugins = new ArrayList<SparkPlug>();

    private final Activity mActivity;

//------------------------------------------
//  Constructor
//------------------------------------------

    public SparkPlugActivityHelper(Activity activity) {
        mActivity = activity;
    }

//------------------------------------------
//  Methods
//------------------------------------------

    public void onCreate(Bundle savedInstanceState) {
        for (SparkPlug plugin : mPlugins) {
            plugin.onCreate(mActivity, savedInstanceState);
        }
    }

    public void onRestart() {
        for (SparkPlug plugin : mPlugins) {
            plugin.onRestart(mActivity);
        }
    }

    public void onStart() {
        for (SparkPlug plugin : mPlugins) {
            plugin.onStart(mActivity);
        }
    }

    public void onResume() {
        for (SparkPlug plugin : mPlugins) {
            plugin.onResume(mActivity);
        }
    }

    public void onPause() {
        for (SparkPlug plugin : mPlugins) {
            plugin.onPause(mActivity);
        }
    }

    public void onStop() {
        for (SparkPlug plugin : mPlugins) {
            plugin.onStop(mActivity);
        }
    }

    public void onDestroy() {
        for (SparkPlug plugin : mPlugins) {
            plugin.onDestroy(mActivity);
        }
    }

    @Override
    public void addActivityPlugin(SparkPlug plugin) {
        mPlugins.add(plugin);
    }

    @Override
    public void removeActivityPlugin(SparkPlug plugin) {
        mPlugins.remove(plugin);
    }

}
