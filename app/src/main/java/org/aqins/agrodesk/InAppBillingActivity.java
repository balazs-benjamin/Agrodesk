package org.aqins.agrodesk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.aqins.agrodesk.util.IabHelper;
import org.aqins.agrodesk.util.IabResult;
import org.aqins.agrodesk.util.Inventory;
import org.aqins.agrodesk.util.Purchase;

/**
 * Created by Axelle on 10/01/2017.
 */

public class InAppBillingActivity extends AppCompatActivity {
    private static final String TAG =
            "InAppBilling";
    IabHelper mHelper;
    static final String ITEM_SKU = "android.test.purchased";

    private Button clickButton;
    private Button buyButton;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_billing);

        buyButton = (Button)findViewById(R.id.buyButton);
        clickButton = (Button)findViewById(R.id.clickButton);
        clickButton.setEnabled(false);
        String base64EncodedPublicKey =
                "<YOUR_Key_Here>";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if(!result.isSuccess()) {
                    Log.d(TAG, "Le paiement a échoué: " +
                            result);
                } else {
                    Log.d(TAG, "Le paiement a réussi");
                }
            }

        });

    }

    public void buttonClicked (View view)
    {
        clickButton.setEnabled(false);
        buyButton.setEnabled(true);
    }

     public void buyClick(View view){
         mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001,
                 mPurchaseFinishedListener, "mypurchasetoken");
     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        if(!mHelper.handleActivityResult(requestCode,
                resultCode, data)){
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new  IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if(result.isFailure()){
                // Handle erron
                return;
            }
            else if (purchase.getSku().equals(ITEM_SKU)){
                consumeItem();
                buyButton.setEnabled(false);
            }

        }
    };

    public void consumeItem(){mHelper.queryInventoryAsync(mReceivedInventoryListener);}

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory)
        {
            if (result.isFailure()) {
                // handle error
                return;
            }else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                        mConsumeFinishedListener);
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    if (result.isSuccess()) {
                        // provision the in-app purchase to the user
                        // (for example, credit 50 gold coins to player's character)
                        clickButton.setEnabled(true);
                    }
                    else {
                        // handle error
                    }
                }
            };


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

}
