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

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Implementation of the {@link SparkPluginableActivity} interface for the
 * {@link android.support.v7.app.ActionBarActivity} class.
 */
public class SparkPlugActionBarActivity extends ActionBarActivity implements
        SparkPluginableActivity {

//------------------------------------------
//  Variables
//------------------------------------------

    private SparkPlugActivityHelper mPluginHelper;

//------------------------------------------
//  Overridden Methods
//------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPluginHelper = new SparkPlugActivityHelper(this);
        initializePlugins();
        mPluginHelper.onCreate(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        mPluginHelper.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mPluginHelper.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPluginHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mPluginHelper.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mPluginHelper.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPluginHelper.onDestroy();
    }

//------------------------------------------
//  Methods
//------------------------------------------

    @Override
    public void removeActivityPlugin(SparkPlug plugin) {
        mPluginHelper.removeActivityPlugin(plugin);
    }

    @Override
    public void addActivityPlugin(SparkPlug plugin) {
        mPluginHelper.addActivityPlugin(plugin);
    }

    /**
     * Subclasses can override this method to initialize their plugins.
     */
    protected void initializePlugins() {

    }


}
