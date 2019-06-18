package com.dsource.idc.jellowintl.models;

import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class SecureKeysTest {

    @Test
    public void secureKeysModelTest(){
        String key = "ThisIsATestKeyUsedToForUnitTesting";
        String salt = UUID.randomUUID().toString();
        SecureKeys secureKeyIcon = new SecureKeys();
        secureKeyIcon.setSalt(salt);
        secureKeyIcon.setKey(key);
        assert secureKeyIcon.getSalt().equals(salt) && secureKeyIcon.getKey().equals(key);
    }
}
