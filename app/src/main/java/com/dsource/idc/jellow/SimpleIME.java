package com.dsource.idc.jellow;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import com.dsource.idc.jellow.Utility.SessionManager;

/**
 * Created by ekalpa on 11/22/2016.
 */
public class SimpleIME extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    private final int LANG_ENG = 0;
    private Keyboard keyboard;  // private Keyboard defaultKeyboard;
    private Keyboard keyboard1;
    private Keyboard keyboard2; // private Keyboard charKeyboard;
    private Keyboard ekeyboard;
    private Keyboard e1keyboard;
    //private boolean isCharKeyboard = false;
    private KeyboardView kv;
    private SessionManager mSession;

    private boolean caps = false;

    @Override   public void onPress(int primaryCode) {}

    @Override   public void onRelease(int primaryCode) {}

    @Override   public void onText(CharSequence text) {}

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null); // 2130968601
        keyboard = new Keyboard(this, R.xml.qwerty); //default
        keyboard1 = new Keyboard(this, R.xml.qwerty1); //num
        keyboard2 = new Keyboard(this, R.xml.qwerty2); //char
        ekeyboard = new Keyboard(this, R.xml.eqwerty);
        e1keyboard = new Keyboard(this, R.xml.eqwerty1);
        mSession = new SessionManager(this);
        if(mSession.getLanguage() == LANG_ENG)
            kv.setKeyboard(ekeyboard);
        else
            kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        kv.setPreviewEnabled(false);
        return kv;
    }

    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                ekeyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case -8:
                System.out.println("keyboard1");
            {
                /*if (this.isCharKeyboard) break;
                this.isCharKeyboard = true;*/
                this.kv.setKeyboard(this.keyboard1);
                return;
            }
            case -11:
                System.out.println("keyboard");
            {
                /*if (this.isCharKeyboard) break;
                this.isCharKeyboard = true;*/
                this.kv.setKeyboard(this.keyboard);
                return;
            }

            case -9:
                System.out.println("keyboard2");
            {
                /*if (this.isCharKeyboard) break;
                this.isCharKeyboard = true;*/
                this.kv.setKeyboard(this.keyboard2);
                return;
            }
            case -10:
                System.out.println("keyboarde");
            {
                /*if (this.isCharKeyboard) break;
                this.isCharKeyboard = true;*/
                this.kv.setKeyboard(this.ekeyboard);
                return;
            }
            case -0:
                System.out.println("keyboarde1");
            {
                /*if (this.isCharKeyboard) break;
                this.isCharKeyboard = true;*/
                this.kv.setKeyboard(this.e1keyboard);
                return;
            }

            default:
                char code = (char)primaryCode;
                if(Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code),1);
        }
    }

    @Override public void swipeDown() {}

    @Override public void swipeLeft() {}

    @Override public void swipeRight() {}

    @Override public void swipeUp() {}
}
