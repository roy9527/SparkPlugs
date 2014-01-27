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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.security.InvalidParameterException;


/**
 * @author Tyler Chesley (tylerchesley@gmail.com).
 */
class SupportFragmentPlugin<F extends Fragment> extends FragmentPlugin<F> {

//------------------------------------------
//  Variables
//------------------------------------------

    private F mFragment;

//------------------------------------------
//  Constructor
//------------------------------------------

    public SupportFragmentPlugin(Class<F> fragmentClass, Bundle arguments, String tag,
                          int layoutResourceId, int containerId) {
        super(fragmentClass, arguments, tag, layoutResourceId, containerId);
    }

//------------------------------------------
//  Methods
//------------------------------------------


    @Override
    protected void onCreateFragment(Activity activity, Class<F> fragmentClass, Bundle arguments,
                                    String tag, int containerId) {
        if (!(activity instanceof FragmentActivity)) {
            throw new InvalidParameterException("Activity must be instance of FragmentActivity " +
                    "to use support fragments.");
        }

        final FragmentActivity fragmentActivity = (FragmentActivity) activity;
        final FragmentManager manager = fragmentActivity.getSupportFragmentManager();
        mFragment = (F) manager.findFragmentByTag(tag);

        if (mFragment == null) {
            mFragment = (F) Fragment.instantiate(activity, fragmentClass.getName(), arguments);
            final FragmentTransaction transaction = manager.beginTransaction();
            if (containerId > 0) {
                transaction.add(containerId, mFragment, tag);
            }
            else {
                transaction.add(mFragment, tag);
            }
            transaction.commit();
        }

    }

    public F getFragment() {
        return mFragment;
    }

}
