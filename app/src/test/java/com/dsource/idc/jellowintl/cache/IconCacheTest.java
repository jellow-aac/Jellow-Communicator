package com.dsource.idc.jellowintl.cache;

import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

@RunWith(AndroidJUnit4.class)
public class IconCacheTest {

    @Test
    public void getExpressiveIconsTest(){
        String[] expressiveIcons = {
                "0101EE",
                "0102EE",
                "0103EE",
                "0104EE",
                "0105EE",
                "0106EE"};
        IconCache.setExpressiveIcons(expressiveIcons);
        assert IconCache.getExpressiveIcons() != null;
    }

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
        assert IconCache.getL1Icons() != null;
    }

    @Test
    public void getAllL2IconsTest(){
        String[] level2GreetFeelIcons = {
                "0101010000GG",
                "0101020000GG",
                "0101030000GG",
                "0101040000GG"};
        HashMap<String, String[]> map = new HashMap<>();
        map.put("0102", level2GreetFeelIcons);
        IconCache.setAllL2Icons(map);
        assert IconCache.getAllL2Icons() != null;
    }

    @Test
    public void getL3IconsTest(){
        String[] level3TimeWeatherTimeIcons = {
                "0101080001GG",
                "0101080002GG",
                "0101080003GG",
                "0101080004GG",
                "0101080005GG",
                "0101080006GG",
                "0101080007GG",
                "0101080008GG"};
        HashMap<String, String[]> map = new HashMap<>();
        map.put("0102", level3TimeWeatherTimeIcons);
        IconCache.setL3Icons(map);
        assert IconCache.getL3Icons() != null;
    }

    @Test
    public void getL3SeqIconsTest(){
        String[] level3SeqBrushingIcons = {
                "0101020001SS",
                "0101020002SS",
                "0101020003SS",
                "0101020004SS",
                "0101020005SS",
                "0101020006SS",
                "0101020007SS",
                "0101020008SS"};
        HashMap<String, String[]> map = new HashMap<>();
        map.put("0102", level3SeqBrushingIcons);
        IconCache.setL3SeqIcons(map);
        assert IconCache.getL3SeqIcons() != null;
    }

    @Test
    public void getMiscellaneousIconsTest(){
        String[] miscellaneousIcons = {
                "0101MS",
                "0102MS",
                "0103MS",
                "0104MS",
                "0105MS"};
        IconCache.setMiscellaneousIcons(miscellaneousIcons);
        assert IconCache.getMiscellaneousIcons() != null;
    }
}
