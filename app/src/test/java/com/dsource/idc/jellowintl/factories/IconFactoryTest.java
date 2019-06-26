package com.dsource.idc.jellowintl.factories;


import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.cache.IconCache;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(AndroidJUnit4.class)
public class IconFactoryTest {

    @Test
    public void getL1IconsTest(){
        String[] l1Icons = {
            "0101000000GG",
            "0102000000GG",
            "0103000000GG",
            "0104000000GG",
            "0105000000GG",
            "0106000000GG",
            "0107000000GG",
            "0108000000GG",
            "0109000000GG"};
        IconCache.setL1Icons(l1Icons);
        assert IconFactory.getL1Icons(new File("dummy.txt"), "") != null;
    }

    @Test
    public void removeFileExtensionTest(){
        final String EXTENSION = ".png";
        String[] l1Icons = {
                "0101000000GG.png",
                "0102000000GG.png",
                "0103000000GG.png",
                "0104000000GG.png",
                "0105000000GG.png",
                "0106000000GG.png",
                "0107000000GG.png",
                "0108000000GG.png",
                "0109000000GG.png"};
        String[] l1IconsWithoutExt = IconFactory.removeFileExtension(l1Icons);
        for (String fileName : l1IconsWithoutExt) {
            assert !fileName.contains(EXTENSION);
        }
        assert true;
    }
}
