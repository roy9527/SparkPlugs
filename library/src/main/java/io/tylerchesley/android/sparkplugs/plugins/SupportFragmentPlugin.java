/*
 * Copyright (c) 2014 Tyler Chesley.
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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import io.tylerchesley.android.sparkplugs.R;
import io.tylerchesley.android.sparkplugs.SparkPlugBase;
import io.tylerchesley.android.sparkplugs.util.FragmentUtils;

/**
 * @author Tyler Chesley (tylerchesley@gmail.com).
 */
public class SupportFragmentPlugin<F extends Fragment> extends SparkPlugBase {

//------------------------------------------
//  Constants
//------------------------------------------

    public static final int INVALID_LAYOUT_RESOURCE_ID = -1;
    public static final String SINGLE_FRAGMENT_TAG = "single_fragment";

//------------------------------------------
//  Static Methods
//------------------------------------------

    public static <B extends Fragment> Builder<B> newPlugin(Class<B> fragmentClass) {
        return new Builder<B>(fragmentClass);
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

    public SupportFragmentPlugin(Class<F> fragmentClass, Bundle arguments, String tag,
                          int layoutResourceId, int containerId) {
        if (fragmentClass == null) {
            throw new NullPointerException("Fragment class may not be null.");
        }

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
        if (!(activity instanceof FragmentActivity)) {
            throw new IllegalArgumentException("Activity must be FragmentActivity");
        }


        if (mLayoutResourceId != INVALID_LAYOUT_RESOURCE_ID) {
            activity.setContentView(mLayoutResourceId);
        }

        final FragmentActivity fragmentActivity = (FragmentActivity) activity;

        mFragment = (F) fragmentActivity.getSupportFragmentManager().findFragmentByTag(mTag);

        if (mFragment == null) {
            mFragment = (F) Fragment.instantiate(activity,
                    mFragmentClass.getName(), mArguments);
            fragmentActivity.getSupportFragmentManager()
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

//------------------------------------------
//  Inner Classes
//------------------------------------------

    public static final class Builder<F extends Fragment> {

        private final Class<F> mFragmentClass;

        private Bundle mArguments;
        private String mTag;
        private int mLayoutResourceId = R.layout.activity_single_fragment_plugin;
        private int mContainerId = R.id.single_fragment_container;

        Builder(Class<F> fragmentClass) {
            mFragmentClass = fragmentClass;
        }

        public Builder<F> arguments(Bundle arguments) {
            mArguments = arguments;
            return this;
        }

        public Builder arguments(Intent intent) {
            return arguments(FragmentUtils.intentToFragmentArguments(intent));
        }

        public Builder<F> tag(String tag) {
            mTag = tag;
            return this;
        }

        public Builder<F> layoutResourceId(int layoutResourceId) {
            mLayoutResourceId = layoutResourceId;
            return this;
        }

        public Builder<F> containerId(int containerId) {
            mContainerId = containerId;
            return this;
        }

        public Builder<F> layoutAlreadySet() {
            mLayoutResourceId = INVALID_LAYOUT_RESOURCE_ID;
            return this;
        }

        public SupportFragmentPlugin<F> build() {
            return new SupportFragmentPlugin<F>(mFragmentClass, mArguments, mTag,
                    mLayoutResourceId, mContainerId);
        }

    }

}
