package com.hackncheese.glassl2vab;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardScrollView;
import com.google.android.glass.widget.Slider;
import com.hackncheese.glassl2vab.helper.DateHelper;
import com.hackncheese.glassl2vab.helper.EncodeHelper;
import com.hackncheese.glassl2vab.helper.NetHelper;

import java.util.Hashtable;
import java.util.Random;

/**
 * Opens the fridge:
 * - displays a progress bar while sending the order
 * - displays the result (success/error) message
 */
public class OpenFridgeActivity extends Activity {

    // for logs
    private static final String TAG = OpenFridgeActivity.class.getSimpleName();

    /**
     * {@link CardScrollView} to use as the main content view.
     */
    private CardScrollView mCardScroller;

    /**
     * Contains all the info collected when calling the API
     * I think we need a {@link Hashtable} because it is synchronized
     * and we will insert new entries in several threads.
     */
    private Hashtable<String, String> mTaskResult = new Hashtable<>();

    private CardAdapter mCardAdapter;
    private Slider mSlider;
    private Slider.Indeterminate mIndSlider;

    private String mEmail;
    private String mPassword;
    private String mSalt;
    private String mSaltCrypted;
    private SendOpenDoorOrderTask mSendOpenDoorOrderTask;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        mEmail = getString(R.string.l2vab_login);
        mPassword = getString(R.string.l2vab_password);
        mSalt = getString(R.string.l2vab_salt);
        mSaltCrypted = EncodeHelper.base64(EncodeHelper.sha512((new StringBuilder()).append(mPassword).append("{").append(mSalt).append("}").toString()));

        mCardAdapter = new CardAdapter(this, mTaskResult);
        mCardScroller = new CardScrollView(this);
        mCardScroller.setAdapter(mCardAdapter);
        setContentView(mCardScroller);

        mSlider = Slider.from(mCardScroller);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCardScroller.activate();

        openDoor();
    }

    @Override
    protected void onPause() {
        mCardScroller.deactivate();

        mTaskResult.clear();

        // hide the progress bar, if it was showing
        if (mIndSlider != null) {
            mIndSlider.hide();
            mIndSlider = null;
        }
        // cancel the async task if it exists
        if (mSendOpenDoorOrderTask != null) {
            mSendOpenDoorOrderTask.cancel(true); // true = force interruption
        }
        super.onPause();
    }

    private void openDoor() {
        mTaskResult.put("result", getString(R.string.fridge_opening));
        // notify that the card UI must be redrawn
        mCardAdapter.notifyDataSetChanged();
        // try to open the door
        mSendOpenDoorOrderTask = new SendOpenDoorOrderTask();
        mSendOpenDoorOrderTask.execute();
    }

    private String getSecuredHeader(String email, String saltCrypted) {
        int nonce = Math.abs((new Random(System.currentTimeMillis())).nextInt(9999));
        String created = DateHelper.getFormattedNow("yyyy-MM-dd'T'HH:mm:ssZ");
        String digest = EncodeHelper.base64(EncodeHelper.byteToHex(EncodeHelper.sha1((new StringBuilder()).append(nonce).append(created).append(saltCrypted).toString())));
        String securedHeader = (new StringBuilder()).append("UserToken email=\"").append(email).append("\", ").append("nonce=\"").append(nonce).append("\", ").append("created=\"").append(created).append("\", ").append("digest=\"").append(digest).append("\"").toString();

        Log.i(TAG, securedHeader);
        return securedHeader;
    }

    /**
     * an AsyncTask that will call the open door API URL
     */
    private class SendOpenDoorOrderTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... p) {
            Hashtable<String, String> headers = new Hashtable<>();
            headers.put("x-l2v-wsse", getSecuredHeader(mEmail, mSaltCrypted));

            return NetHelper.getDataFromUrl(getString(R.string.url_open_door), headers, "PUT");
        }

        protected void onPreExecute() {
            // show the progress bar
            mIndSlider = mSlider.startIndeterminate();
        }

        protected void onPostExecute(String result) {
            Log.i(TAG, result);

            // hide the progress bar
            if (mIndSlider != null) {
                mIndSlider.hide();
                mIndSlider = null;
            }

            int resultSound;
            if (result != null && !result.equals("{}")) {
                // play a nice sound
                resultSound = Sounds.SUCCESS;
                mTaskResult.put("result", getString(R.string.fridge_open_ok));
            } else {
                // play an error sound
                resultSound = Sounds.ERROR;
                mTaskResult.put("result", getString(R.string.fridge_open_error) + result);
            }

            // notify that the card UI must be redrawn
            mCardAdapter.notifyDataSetChanged();

            // play a sound
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            am.playSoundEffect(resultSound);

        }
    }

}
