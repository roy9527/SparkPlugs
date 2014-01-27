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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import java.security.InvalidParameterException;

import io.tylerchesley.android.sparkplugs.R;
import io.tylerchesley.android.sparkplugs.SparkPlugBase;

public abstract class FragmentPlugin<F> extends SparkPlugBase {

//------------------------------------------
//  Constants
//------------------------------------------

    public static final int INVALID_RESOURCE_ID = -1;
    public static final String SINGLE_FRAGMENT_TAG = "single_fragment";
    public static final String ARG_URI = "_uri";

//------------------------------------------
//  Static Methods
//------------------------------------------

    public static <F> Builder<F> newPlugin(Class<F> fragmentClass) {
        final boolean isSupportFragment = fragmentClass.isAssignableFrom(
                android.support.v4.app.Fragment.class);

        if (isSupportFragment && Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            throw new InvalidParameterException("Fragment class must inherit from " +
                    "android.support.v4.app.Fragment on versions of Android " +
                    "before API 11 (HoneyComb)");
        }

        if (!isSupportFragment && fragmentClass.isAssignableFrom(Fragment.class)) {
            throw new InvalidParameterException("Fragment class inherit from either the " +
                    "android.app.Fragment or android.support.v4.app.Fragment");
        }

        return new Builder<F>(fragmentClass, isSupportFragment);
    }

    public static <F> FragmentPlugin<F> from(Class<F> fragmentClass){
        return newPlugin(fragmentClass).build();
    }

    public static <F> FragmentPlugin<F> from(Class<F> fragmentClass, Intent arguments) {
        return newPlugin(fragmentClass).arguments(arguments).build();
    }

    public static <F> FragmentPlugin<F> from(Class<F> fragmentClass, Bundle arguments) {
        return newPlugin(fragmentClass).arguments(arguments).build();
    }

    /**
     * Converts an intent into a {@link android.os.Bundle} suitable for use as fragment arguments.
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

//------------------------------------------
//  Constructor
//------------------------------------------

    public FragmentPlugin(Class<F> fragmentClass, Bundle arguments, String tag,
                          int layoutResourceId, int containerId) {
        if (fragmentClass == null) {
            throw new NullPointerException("Fragment class may not be null.");
        }

        if (TextUtils.isEmpty(tag)) {
            throw new NullPointerException("Tag may not be null.");
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
        if (mLayoutResourceId != INVALID_RESOURCE_ID) {
            activity.setContentView(mLayoutResourceId);
        }

        onCreateFragment(activity, mFragmentClass, mArguments, mTag, mContainerId);
    }

//------------------------------------------
//  Methods
//------------------------------------------

    protected abstract void onCreateFragment(Activity activity, Class<F> fragmentClass,
                                             Bundle arguments, String tag, int containerId);

    public abstract F getFragment();

//------------------------------------------
//  Inner Classes
//------------------------------------------

    public static final class Builder<F> {

        private final boolean mIsSupportFragment;
        private final Class<F> mFragmentClass;

        private Bundle mArguments;
        private String mTag = SINGLE_FRAGMENT_TAG;
        private int mLayoutResourceId = R.layout.activity_single_fragment_plugin;
        private int mContainerId = R.id.single_fragment_container;

        Builder(Class<F> fragmentClass, boolean isSupportFragment) {
            mFragmentClass = fragmentClass;
            mIsSupportFragment = isSupportFragment;
        }

        public Builder<F> arguments(Bundle arguments) {
            mArguments = arguments;
            return this;
        }

        public Builder<F> arguments(Intent intent) {
            return arguments(intentToFragmentArguments(intent));
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
            mLayoutResourceId = INVALID_RESOURCE_ID;
            return this;
        }

        public Builder<F> nonUiFragment() {
            mContainerId = INVALID_RESOURCE_ID;
            return this;
        }

        public FragmentPlugin<F> build() {
            if (mIsSupportFragment) {
                return new SupportFragmentPlugin(mFragmentClass, mArguments, mTag,
                        mLayoutResourceId, mContainerId);
            }

            return new FragmentPluginImpl(mFragmentClass, mArguments, mTag,
                    mLayoutResourceId, mContainerId);
        }

    }

    @TargetApi(11)
    static final class FragmentPluginImpl<F extends Fragment> extends FragmentPlugin<F> {

        private F mFragment;

        public FragmentPluginImpl(Class<F> fragmentClass, Bundle arguments, String tag,
                                  int layoutResourceId, int containerId) {
            super(fragmentClass, arguments, tag, layoutResourceId, containerId);
        }

        @Override
        @TargetApi(11)
        protected void onCreateFragment(Activity activity, Class<F> fragmentClass,
                                        Bundle arguments, String tag, int containerId) {
            mFragment = (F) activity.getFragmentManager().findFragmentByTag(tag);

            if (mFragment == null) {
                mFragment = (F) Fragment.instantiate(activity,
                        fragmentClass.getName(), arguments);
                final FragmentTransaction transaction = activity.getFragmentManager()
                        .beginTransaction();
                if (containerId > 0) {
                    transaction.add(containerId, mFragment, tag);
                }
                else {
                    transaction.add(mFragment, tag);
                }
                transaction.commit();
            }
        }

        @Override
        public F getFragment() {
            return null;
        }

    }

}
