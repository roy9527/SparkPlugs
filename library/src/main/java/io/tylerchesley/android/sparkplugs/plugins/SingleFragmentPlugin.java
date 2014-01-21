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
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import io.tylerchesley.android.sparkplugs.R;
import io.tylerchesley.android.sparkplugs.SparkPlugBase;

public class SingleFragmentPlugin<F extends Fragment> extends SparkPlugBase {

//------------------------------------------
//  Constants
//------------------------------------------

    public static final int INVALID_LAYOUT_RESOURCE_ID = -1;
    public static final String SINGLE_FRAGMENT_TAG = "single_fragment";
    public static final String ARG_URI = "_uri";

//------------------------------------------
//  Static Methods
//------------------------------------------

    /**
     * Converts an intent into a {@link Bundle} suitable for use as fragment arguments.
     */
    public static Bundle intentToFragmentArguments(Intent intent) {
        Bundle arguments = new Bundle();
        if (intent == null) {
            return arguments;
        }

        final Uri data = intent.getData();
        if (data != null) {
            arguments.putParcelable(ARG_URI, data);
        }

        final Bundle extras = intent.getExtras();
        if (extras != null) {
            arguments.putAll(intent.getExtras());
        }

        return arguments;
    }

    /**
     * Converts a fragment arguments bundle into an intent.
     */
    public static Intent fragmentArgumentsToIntent(Bundle arguments) {
        Intent intent = new Intent();
        if (arguments == null) {
            return intent;
        }

        final Uri data = arguments.getParcelable(ARG_URI);
        if (data != null) {
            intent.setData(data);
        }

        intent.putExtras(arguments);
        intent.removeExtra(ARG_URI);
        return intent;
    }

//------------------------------------------
//  Variables
//------------------------------------------

    private final Class<F> mFragmentClass;
    private final Bundle mArguments;
    private final String mTag;
    private final int mLayoutResourceId;
    private final int mContainerId;

    private F mFragment;

//------------------------------------------
//  Constructor
//------------------------------------------

    public SingleFragmentPlugin(Class<F> fragmentClass) {
        this(fragmentClass, null, SINGLE_FRAGMENT_TAG,
                R.layout.activity_single_fragment_plugin, R.id.single_fragment_container);
    }

    public SingleFragmentPlugin(Class<F> fragmentClass, Bundle arguments, String tag,
                                int layoutResourceId, int containerId) {
        mFragmentClass = fragmentClass;
        mArguments = arguments;
        mTag = tag;
        mLayoutResourceId = layoutResourceId;
        mContainerId = containerId;
    }

//------------------------------------------
//  Overridden Methods
//------------------------------------------

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {
        if (mLayoutResourceId != INVALID_LAYOUT_RESOURCE_ID) {
            activity.setContentView(mLayoutResourceId);
        }

        mFragment = (F) activity.getFragmentManager().findFragmentByTag(mTag);

        if (mFragment == null) {
            mFragment = (F) Fragment.instantiate(activity,
                    mFragmentClass.getName(), mArguments);
            activity.getFragmentManager()
                    .beginTransaction()
                    .add(mContainerId, mFragment, mTag)
                    .commit();
        }
    }

//------------------------------------------
//  Methods
//------------------------------------------

    public F getFragment() {
        return mFragment;
    }

}
