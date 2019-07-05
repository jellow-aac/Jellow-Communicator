package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;

import com.dsource.idc.jellowintl.TalkBack.TalkbackHints_SingleClick;

public class DialogActivity extends LevelBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelx_layout);
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog(DialogActivity.this);
                *//*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*//*
            }
        });*/
    }

    public void showCustomDialog(Context context) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_layout, null);

        final Button enterCategory = mView.findViewById(R.id.enterCategory);
        final Button closeDialog = mView.findViewById(R.id.btnClose);
        ImageView ivLike = mView.findViewById(R.id.ivlike);
        ImageView ivYes = mView.findViewById(R.id.ivyes);
        ImageView ivAdd = mView.findViewById(R.id.ivadd);
        ImageView ivDisLike = mView.findViewById(R.id.ivdislike);
        ImageView ivNo = mView.findViewById(R.id.ivno);
        ImageView ivMinus = mView.findViewById(R.id.ivminus);
        ImageView ivBack = mView.findViewById(R.id.back);
        ImageView ivHome = mView.findViewById(R.id.home);
        ImageView ivKeyboard = mView.findViewById(R.id.keyboard);
        ViewCompat.setAccessibilityDelegate(ivLike, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivYes, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivAdd, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivDisLike, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivNo, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivMinus, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivBack, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivHome, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivKeyboard, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(enterCategory, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(closeDialog, new TalkbackHints_SingleClick());
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        final ImageView[] expressiveBtns = {ivLike, ivYes, ivAdd, ivDisLike, ivNo, ivMinus};

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DialogActivity.this, "like", Toast.LENGTH_SHORT).show();
                //mIvLike.performClick();
                setBorderToExpression(0, expressiveBtns);
            }
        });
        ivYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DialogActivity.this, "Yes", Toast.LENGTH_SHORT).show();
                //mIvYes.performClick();
                setBorderToExpression(1, expressiveBtns);
            }
        });
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DialogActivity.this, "Add", Toast.LENGTH_SHORT).show();
                //mIvMore.performClick();
                setBorderToExpression(2, expressiveBtns);
            }
        });
        ivDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DialogActivity.this, "Dislike", Toast.LENGTH_SHORT).show();
                //mIvDontLike.performClick();
                setBorderToExpression(3, expressiveBtns);
            }
        });
        ivNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DialogActivity.this, "No", Toast.LENGTH_SHORT).show();
                //mIvNo.performClick();
                setBorderToExpression(4, expressiveBtns);
            }
        });
        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DialogActivity.this, "Less", Toast.LENGTH_SHORT).show();
                //mIvLess.performClick();
                setBorderToExpression(5, expressiveBtns);
            }
        });

        ivBack.setEnabled(false);
        ivBack.setAlpha(0.5f);
        ivBack.setOnClickListener(null);
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DialogActivity.this, "Home", Toast.LENGTH_SHORT).show();
                //mIvHome.performClick();
                dialog.dismiss();
            }
        });
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DialogActivity.this, "Keyboard", Toast.LENGTH_SHORT).show();
                /*mUec.sendEventIfAny("");
                mIvKeyboard.performClick();*/
                dialog.dismiss();
            }
        });

        enterCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DialogActivity.this, "category", Toast.LENGTH_SHORT).show();
                // create event bundle for firebase
                /*Bundle bundle = new Bundle();
                bundle.putString("Icon", "Opened " + mSpeechText[position]);
                bundleEvent("Grid", bundle);

                Intent intent = new Intent(MainActivity.this, LevelTwoActivity.class);
                intent.putExtra("mLevelOneItemPos", position);
                intent.putExtra("selectedMenuItemPath", title + "/");
                startActivityForResult(intent, REQ_HOME);*/
                dialog.dismiss();
            }
        });

        enterCategory.setAccessibilityDelegate(new View.AccessibilityDelegate(){
            @Override
            public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
                super.onPopulateAccessibilityEvent(host, event);
                if(event.getEventType() != AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
                    mView.findViewById(R.id.txTitleHidden).
                            setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
                }
            }
        });

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DialogActivity.this, "Clsoe", Toast.LENGTH_SHORT).show();
                //clear all selection
                clearSelectionAfterAccessibilityDialogClose();
                //dismiss dialog
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //disabledView.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2; //style id
        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
    }

    private void setBorderToExpression(int btnPos, ImageView[] diagExprBtns) {
        // clear previously selected any expressive button or home button
        diagExprBtns[0].setImageResource(R.drawable.like);
        diagExprBtns[1].setImageResource(R.drawable.yes);
        diagExprBtns[2].setImageResource(R.drawable.more);
        diagExprBtns[3].setImageResource(R.drawable.dontlike);
        diagExprBtns[4].setImageResource(R.drawable.no);
        diagExprBtns[5].setImageResource(R.drawable.less);
        // set expressive button or home button to pressed state
        switch (btnPos){
            case 0: diagExprBtns[0].setImageResource(R.drawable.like_pressed); break;
            case 1: diagExprBtns[1].setImageResource(R.drawable.yes_pressed); break;
            case 2: diagExprBtns[2].setImageResource(R.drawable.more_pressed); break;
            case 3: diagExprBtns[3].setImageResource(R.drawable.dontlike_pressed); break;
            case 4: diagExprBtns[4].setImageResource(R.drawable.no_pressed); break;
            case 5: diagExprBtns[5].setImageResource(R.drawable.less_pressed); break;
            default: break;
        }
    }

    private void clearSelectionAfterAccessibilityDialogClose() {
        //resetRecyclerMenuItemsAndFlags();
        //Send pending grid event
      //  mUec.sendEventIfAny("");
    //    resetExpressiveButtons(-1);
  //      mShouldReadFullSpeech = false;
//        mFlgImage = -1;
    }
}
