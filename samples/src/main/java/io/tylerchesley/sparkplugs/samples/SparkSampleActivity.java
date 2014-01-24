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

package io.tylerchesley.sparkplugs.samples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.tylerchesley.android.sparkplugs.SparkPlugFragmentActivity;
import io.tylerchesley.android.sparkplugs.plugins.ActivityLogPlugin;
import io.tylerchesley.android.sparkplugs.plugins.SupportFragmentPlugin;

/**
 * @author Tyler Chesley
 */
public class SparkSampleActivity extends SparkPlugFragmentActivity {

//------------------------------------------
//  Constants
//------------------------------------------

    private static final String TAG = "SparkSampleActivity";

//------------------------------------------
//  Overridden Methods
//------------------------------------------

    @Override
    protected void initializePlugins() {
        addActivityPlugin(new ActivityLogPlugin(TAG));
        addActivityPlugin(SupportFragmentPlugin.newPlugin(SampleFragment.class).build());
    }

//------------------------------------------
//  Inner Classes
//------------------------------------------

    public static final class SampleFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_sample, container, false);
        }

    }

}
