package com.dsource.idc.jellowintl;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import com.dsource.idc.jellowintl.utility.SessionManager;

import static com.dsource.idc.jellowintl.utility.SessionManager.BE_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_AU;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_UK;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_US;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;

/**
 * Created by ekalpa on 11/22/2016.
 */
public class SimpleIME extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {
    final int ENG_KEY_123 = -0;
    final int ENG_KEY_ABC = -10;
    final int KEY_LANG_SWITCH = -11;
    final int HI_KEY_SHIFT = -12;
    final int HI_KEY_ABC = -13;
    final int BN_KEY_SHIFT = -14;
    final int BN_KEY_123 = -15;
    final int BN_KEY_ABC = -16;
    final int KEY_ENTER = -17;

    private Keyboard keyHindiConsonants;
    private Keyboard keyHindiNumeric;
    private Keyboard keyHindiVowels;
    private Keyboard keyEnglishAlphabetSmallLetters;
    private Keyboard keyEnglishNumeric;
    private Keyboard keyBengaliConsonants;
    private Keyboard keyBengaliNumeric;
    private Keyboard keyBengaliVowels;
    private int mkeyCount = -1;
    private KeyboardView kv;

    private boolean caps = false;

    @Override   public void onPress(int primaryCode) {}

    @Override   public void onRelease(int primaryCode) {}

    @Override   public void onText(CharSequence text) {}

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyEnglishAlphabetSmallLetters = new Keyboard(this, R.xml.serial_english_alphabet_small_letters);
        keyEnglishNumeric = new Keyboard(this, R.xml.serial_english_numeric);
        keyHindiConsonants = new Keyboard(this, R.xml.serial_hindi_consonants);
        keyHindiVowels = new Keyboard(this, R.xml.serial_hindi_vowels);
        keyHindiNumeric = new Keyboard(this, R.xml.serial_hindi_numeric);
        keyBengaliConsonants = new Keyboard(this, R.xml.serial_bengali_consonants);
        keyBengaliVowels = new Keyboard(this, R.xml.serial_bengali_vowels);
        keyBengaliNumeric = new Keyboard(this, R.xml.serial_bengali_numeric);

        switch (new SessionManager(this).getLanguage()){
            case HI_IN:
                kv.setKeyboard(keyHindiConsonants);
                break;
            case BN_IN:
            case BE_IN:
                kv.setKeyboard(keyBengaliConsonants);
                break;
            case ENG_US:
            case ENG_UK:
            case ENG_AU:
            case ENG_IN:
            case MR_IN:
            default:
                kv.setKeyboard(keyEnglishAlphabetSmallLetters);
                break;
        }
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
            case KEY_ENTER:
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
                keyEnglishAlphabetSmallLetters.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case KEY_ENTER:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case -8:
                kv.setKeyboard(this.keyHindiNumeric);
                break;
            case -9:
                kv.setKeyboard(this.keyHindiVowels);
                break;
            case ENG_KEY_ABC:
                kv.setKeyboard(keyEnglishAlphabetSmallLetters);
                break;
            case ENG_KEY_123:
                kv.setKeyboard(this.keyEnglishNumeric);
                break;
            case HI_KEY_SHIFT:
                if(kv.getKeyboard().equals(keyHindiVowels))
                     kv.setKeyboard(keyHindiConsonants);
                else
                    kv.setKeyboard(keyHindiVowels);
                break;
            case HI_KEY_ABC:
                kv.setKeyboard(keyHindiConsonants);
                break;
            case BN_KEY_SHIFT:
                if(kv.getKeyboard().equals(keyBengaliVowels))
                    kv.setKeyboard(keyBengaliConsonants);
                else
                    kv.setKeyboard(keyBengaliVowels);
                break;
            case BN_KEY_123:
                kv.setKeyboard(keyBengaliNumeric);
                break;
            case BN_KEY_ABC:
                kv.setKeyboard(keyBengaliConsonants);
                break;
            case KEY_LANG_SWITCH:
                switch(++mkeyCount % 3){
                    case 1:
                        kv.setKeyboard(keyHindiConsonants);
                        break;
                    case 2:
                        kv.setKeyboard(keyBengaliConsonants);
                        break;
                    case 0:
                    default:
                        kv.setKeyboard(keyEnglishAlphabetSmallLetters);
                        break;
                }
                break;
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