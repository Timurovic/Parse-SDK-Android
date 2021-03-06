/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.fcm;

import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.RetryStrategy;

public class ParseFCM {

    static final String TAG = "ParseFCM";

    private static final String JOB_TAG_UPLOAD_TOKEN = "upload-token";

    /**
     * You can call this manually if you are overriding the {@link com.google.firebase.iid.FirebaseInstanceIdService}
     * @param context context
     */
    public static void scheduleTokenUpload(Context context) {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context.getApplicationContext()));
        Job job = dispatcher.newJobBuilder()
                .setRecurring(false)
                .setReplaceCurrent(true)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(
                        // only run on a network
                        Constraint.ON_ANY_NETWORK
                )
                .setService(ParseFirebaseJobService.class) // the JobService that will be called
                .setTag(JOB_TAG_UPLOAD_TOKEN)        // uniquely identifies the job
                .build();

        dispatcher.mustSchedule(job);
    }
}
