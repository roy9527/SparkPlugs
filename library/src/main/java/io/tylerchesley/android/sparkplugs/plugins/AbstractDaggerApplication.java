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

import android.app.Application;
import android.content.Context;

import dagger.ObjectGraph;

/**
 * @author Tyler Chesley (tylerchesley@gmail.com).
 */
public abstract class AbstractDaggerApplication extends Application implements
        DaggerPlugin.DaggerApplication {

//------------------------------------------
//  Static Methods
//------------------------------------------

    public static DaggerPlugin.DaggerApplication from(Context context) {
        return (DaggerPlugin.DaggerApplication) context.getApplicationContext();
    }

//------------------------------------------
//  Variables
//------------------------------------------

    private ObjectGraph mObjectGraph;

//------------------------------------------
//  Overridden Methods
//------------------------------------------

    @Override
    public void onCreate() {
        super.onCreate();

        mObjectGraph = onCreateObjectGraph();
        if (mObjectGraph == null) {
            throw new NullPointerException("Object graph may not be null.");
        }
        mObjectGraph.inject(this);
    }

//------------------------------------------
//  Methods
//------------------------------------------

    @Override
    public void inject(Object object) {
        mObjectGraph.inject(object);
    }

//------------------------------------------
//  Abstract Methods
//------------------------------------------

    abstract protected ObjectGraph onCreateObjectGraph();

}
