package com.dsource.idc.jellowintl.factories;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.models.ExpressiveIcon;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.models.MiscellaneousIcon;
import com.dsource.idc.jellowintl.models.SeqNavigationButton;
import com.dsource.idc.jellowintl.utility.SessionManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;

@RunWith(AndroidJUnit4.class)
public class TextFactoryTest {
    private Context mContext;
    private SessionManager mSession;
    private File jsonFile;
    private String[] l1Icons = {
            "0101000000GG",
            "0102000000GG",
            "0103000000GG",
            "0104000000GG",
            "0105000000GG",
            "0106000000GG",
            "0107000000GG",
            "0108000000GG",
            "0109000000GG"};

    @Before
    public void setup(){
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mSession = new SessionManager(mContext);
        mSession.setLanguage(ENG_IN);
        jsonFile = createDummyFile();
    }

    @Test
    public void getIconObjectTest(){
       Icon[] icons = TextFactory.getIconObjects(jsonFile, l1Icons);
       assert  icons != null;
    }

    @Test
    public void getExpressiveIconObjectsTest(){
        String[] expressiveIcons = {"0101EE", "0102EE", "0103EE",
                                    "0104EE", "0105EE", "0106EE"};
        ExpressiveIcon[] icons = TextFactory.
                getExpressiveIconObjects(jsonFile, expressiveIcons);
        assert  icons != null;
    }

    @Test
    public void getMiscellaneousIconObjectsTest(){
        String[] miscellaneousIcons = {"0101MS", "0102MS", "0103MS"};
        MiscellaneousIcon[] icons = TextFactory.
                getMiscellaneousIconObjects(jsonFile, miscellaneousIcons);
        assert  icons != null;
    }

    @Test
    public void getSeqNavigationButtonObjectsTest(){
        String[] categoryNavButton = {"0104MS", "0105MS"};
        SeqNavigationButton[] icons = TextFactory.
                getSeqNavigationButtonObjects(jsonFile, categoryNavButton);
        assert  icons != null;
    }

    @Test
    public void getDisplayTextTest(){
        Icon[] icons = TextFactory.getIconObjects(jsonFile,
                l1Icons);
        assert  TextFactory.getDisplayText(icons).length != 0;
    }

    @Test
    public void getTitleTest(){
        String[] miscellaneousIcons =  {"0101MS", "0102MS", "0103MS"};
        MiscellaneousIcon[] icons = TextFactory.getMiscellaneousIconObjects(jsonFile,
                miscellaneousIcons);

        assert  TextFactory.getTitle(icons).length != 0;
    }

    @Test
    public void getExpressiveSpeechTextTest(){
        String[] expressiveIcons =  {"0101EE", "0102EE", "0103EE",
                                      "0104EE", "0105EE", "0106EE"};
        ExpressiveIcon[] expressiveIconObjects = TextFactory.
                getExpressiveIconObjects(jsonFile, expressiveIcons);

        assert TextFactory.getExpressiveSpeechText(expressiveIconObjects).length != 0;
    }

    @Test
    public void getSpeechTextTest(){
        Icon[] icons = TextFactory.getIconObjects(jsonFile,
                l1Icons);
        assert  TextFactory.getSpeechText(icons).length != 0;
    }

    private File createDummyFile() {
        File file = PathFactory.getJSONFile(mContext);
        try {
            file.createNewFile();
            if (file.exists()){
                OutputStream fo = null;
                fo = new FileOutputStream(file);
                fo.write(dummyJson.getBytes());
                fo.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private final String dummyJson = "{\n" +
            "    \"0101000000GG\": {\n" +
            "        \"Display_Label\": \"Greet and Feel…\",\n" +
            "        \"Speech_Label\": \"Greet and Feel\",\n" +
            "        \"Event_Tag\": \"GreetAndFeel\",\n" +
            "        \"L\": \"I like to greet others and talk about my feelings\",\n" +
            "        \"LL\": \"I really like to greet others and talk about my feelings\",\n" +
            "        \"Y\": \"I want to greet others and talk about my feelings\",\n" +
            "        \"YY\": \"I really want to greet others and talk about my feelings\",\n" +
            "        \"M\": \"I want to greet others and talk about my feelings some more\",\n" +
            "        \"MM\": \"I really want to greet others and talk about my feelings some more\",\n" +
            "        \"D\": \"I don't like to greet others and talk about my feelings\",\n" +
            "        \"DD\": \"I really don't like to greet others and talk about my feelings\",\n" +
            "        \"N\": \"I don't want to greet others and talk about my feelings\",\n" +
            "        \"NN\": \"I really don't want to greet others and talk about my feelings\",\n" +
            "        \"S\": \"I don't want to greet others and talk about my feelings anymore\",\n" +
            "        \"SS\": \"I really don't want to greet others and talk about my feelings any more\"\n" +
            "    },\n" +
            "    \"0102000000GG\": {\n" +
            "        \"Display_Label\": \"Daily Activities…\",\n" +
            "        \"Speech_Label\": \"Daily Activities\",\n" +
            "        \"Event_Tag\": \"DailyActivities\",\n" +
            "        \"L\": \"I like to do my daily activities\",\n" +
            "        \"LL\": \"I really like to do my daily activities\",\n" +
            "        \"Y\": \"I want to do my daily activities\",\n" +
            "        \"YY\": \"I really want to do my daily activities\",\n" +
            "        \"M\": \"I want to spend more time doing my daily activities\",\n" +
            "        \"MM\": \"I really want to spend some more time doing my daily activities\",\n" +
            "        \"D\": \"I don't like to do my daily activities\",\n" +
            "        \"DD\": \"I really don't like to do my daily activities\",\n" +
            "        \"N\": \"I don't want to do my daily activities\",\n" +
            "        \"NN\": \"I really don't want to do my daily activities\",\n" +
            "        \"S\": \"I don't want to spend more time doing my daily activities\",\n" +
            "        \"SS\": \"I really don't want to spend any more time doing my daily activities\"\n" +
            "    },\n" +
            "    \"0103000000GG\": {\n" +
            "        \"Display_Label\": \"Eating…\",\n" +
            "        \"Speech_Label\": \"Eating\",\n" +
            "        \"Event_Tag\": \"Eating\",\n" +
            "        \"L\": \"I like to eat\",\n" +
            "        \"LL\": \"I really like to eat\",\n" +
            "        \"Y\": \"I want to eat\",\n" +
            "        \"YY\": \"I really want to eat\",\n" +
            "        \"M\": \"I want to eat some more\",\n" +
            "        \"MM\": \"I really want to eat some more\",\n" +
            "        \"D\": \"I don't like to eat\",\n" +
            "        \"DD\": \"I really don't like to eat\",\n" +
            "        \"N\": \"I don't want to eat\",\n" +
            "        \"NN\": \"I really don't want to eat\",\n" +
            "        \"S\": \"I don't want to eat any more\",\n" +
            "        \"SS\": \"I really don't want to eat any more\"\n" +
            "    },\n" +
            "    \"0104000000GG\": {\n" +
            "        \"Display_Label\": \"Fun…\",\n" +
            "        \"Speech_Label\": \"Fun\",\n" +
            "        \"Event_Tag\": \"Fun\",\n" +
            "        \"L\": \"I like to have fun\",\n" +
            "        \"LL\": \"I really like to have fun\",\n" +
            "        \"Y\": \"I want to have fun\",\n" +
            "        \"YY\": \"I really want to have fun\",\n" +
            "        \"M\": \"I want to have more fun\",\n" +
            "        \"MM\": \"I want to have some more fun\",\n" +
            "        \"D\": \"I don't like to have fun\",\n" +
            "        \"DD\": \"I really don't like to have fun\",\n" +
            "        \"N\": \"I don't want to have fun\",\n" +
            "        \"NN\": \"I really don't want to have fun\",\n" +
            "        \"S\": \"I don't want to have more fun\",\n" +
            "        \"SS\": \"I really don't want to have any more fun\"\n" +
            "    },\n" +
            "    \"0105000000GG\": {\n" +
            "        \"Display_Label\": \"Learning…\",\n" +
            "        \"Speech_Label\": \"Learning\",\n" +
            "        \"Event_Tag\": \"Learning\",\n" +
            "        \"L\": \"I like to learn\",\n" +
            "        \"LL\": \"I really like to learn\",\n" +
            "        \"Y\": \"I want to learn\",\n" +
            "        \"YY\": \"I really want to learn\",\n" +
            "        \"M\": \"I want to learn more\",\n" +
            "        \"MM\": \"I really want to learn some more\",\n" +
            "        \"D\": \"I don't like to learn\",\n" +
            "        \"DD\": \"I really don't like to learn\",\n" +
            "        \"N\": \"I don't want to learn\",\n" +
            "        \"NN\": \"I really don't want to learn\",\n" +
            "        \"S\": \"I don't want to learn any more\",\n" +
            "        \"SS\": \"I really don't want to learn any more\"\n" +
            "    },\n" +
            "    \"0106000000GG\": {\n" +
            "        \"Display_Label\": \"People…\",\n" +
            "        \"Speech_Label\": \"People\",\n" +
            "        \"Event_Tag\": \"People\",\n" +
            "        \"L\": \"I like to talk to people\",\n" +
            "        \"LL\": \"I really like to talk to people\",\n" +
            "        \"Y\": \"I want to meet people\",\n" +
            "        \"YY\": \"I really want to meet people\",\n" +
            "        \"M\": \"I want to meet some more people\",\n" +
            "        \"MM\": \"I really want to meet some more people\",\n" +
            "        \"D\": \"I don't like to talk to people\",\n" +
            "        \"DD\": \"I really don't like to talk to people\",\n" +
            "        \"N\": \"I don't want to meet people\",\n" +
            "        \"NN\": \"I really don't want to meet people\",\n" +
            "        \"S\": \"I don't want to meet any more people\",\n" +
            "        \"SS\": \"I really don't want to meet any more people\"\n" +
            "    },\n" +
            "    \"0107000000GG\": {\n" +
            "        \"Display_Label\": \"Places…\",\n" +
            "        \"Speech_Label\": \"Places\",\n" +
            "        \"Event_Tag\": \"Places\",\n" +
            "        \"L\": \"I like to visit places\",\n" +
            "        \"LL\": \"I really like to visit places\",\n" +
            "        \"Y\": \"I want to visit different places\",\n" +
            "        \"YY\": \"I really want to visit different places\",\n" +
            "        \"M\": \"I want to visit some more places\",\n" +
            "        \"MM\": \"I really want to visit some more places\",\n" +
            "        \"D\": \"I don't like to visit places\",\n" +
            "        \"DD\": \"I really don't like to visit places\",\n" +
            "        \"N\": \"I don't want to visit different places\",\n" +
            "        \"NN\": \"I really don't want to visit different places\",\n" +
            "        \"S\": \"I don't want to visit any more places\",\n" +
            "        \"SS\": \"I really don't want to visit any more places\"\n" +
            "    },\n" +
            "    \"0108000000GG\": {\n" +
            "        \"Display_Label\": \"Time and Weather…\",\n" +
            "        \"Speech_Label\": \"Time and Weather\",\n" +
            "        \"Event_Tag\": \"TimeAndWeather\",\n" +
            "        \"L\": \"I like to talk about time and weather\",\n" +
            "        \"LL\": \"I really like to talk about time and weather\",\n" +
            "        \"Y\": \"I want to talk about time and weather\",\n" +
            "        \"YY\": \"I really want to talk about time and weather\",\n" +
            "        \"M\": \"I want to talk some more about time and weather\",\n" +
            "        \"MM\": \"I really want to talk some more about time and weather\",\n" +
            "        \"D\": \"I don't like to talk about time and weather\",\n" +
            "        \"DD\": \"I really don't like to talk about time and weather\",\n" +
            "        \"N\": \"I don't want to talk about time and weather\",\n" +
            "        \"NN\": \"I really don't want to talk about time and weather\",\n" +
            "        \"S\": \"I don't want to talk any more about time and weather\",\n" +
            "        \"SS\": \"I really don't want to talk any more about time and weather\"\n" +
            "    },\n" +
            "    \"0109000000GG\": {\n" +
            "        \"Display_Label\": \"Help…\",\n" +
            "        \"Speech_Label\": \"Help\",\n" +
            "        \"Event_Tag\": \"Help\",\n" +
            "        \"L\": \"I like to help\",\n" +
            "        \"LL\": \"I really like to help\",\n" +
            "        \"Y\": \"I need help\",\n" +
            "        \"YY\": \"I really need help\",\n" +
            "        \"M\": \"I need more help\",\n" +
            "        \"MM\": \"I really need some more help\",\n" +
            "        \"D\": \"I don't like to help\",\n" +
            "        \"DD\": \"I really don't like to help\",\n" +
            "        \"N\": \"I don't need help\",\n" +
            "        \"NN\": \"I really don't need help\",\n" +
            "        \"S\": \"I don't need more help\",\n" +
            "        \"SS\": \"I really don't need any more help\"\n" +
            "    },\n" +
            "    \"0101EE\": {\n" +
            "        \"L\": \"like\",\n" +
            "        \"LL\": \"really like\",\n" +
            "        \"Title\": \"LIKE\"\n" +
            "    },\n" +
            "    \"0102EE\": {\n" +
            "        \"L\": \"yes\",\n" +
            "        \"LL\": \"really yes\",\n" +
            "        \"Title\": \"YES\"\n" +
            "    },\n" +
            "    \"0103EE\": {\n" +
            "        \"L\": \"more\",\n" +
            "        \"LL\": \"really more\",\n" +
            "        \"Title\": \"MORE\"\n" +
            "    },\n" +
            "    \"0105EE\": {\n" +
            "        \"L\": \"no\",\n" +
            "        \"LL\": \"really no\",\n" +
            "        \"Title\": \"NO\"\n" +
            "    },\n" +
            "    \"0106EE\": {\n" +
            "        \"L\": \"less\",\n" +
            "        \"LL\": \"really less\",\n" +
            "        \"Title\": \"LESS\"\n" +
            "    },\n" +
            "    \"0104EE\": {\n" +
            "        \"L\": \"don’t like\",\n" +
            "        \"LL\": \"really don’t like\",\n" +
            "        \"Title\": \"DON’T LIKE\"\n" +
            "    },\n" +
            "    \"0101MS\": {\n" +
            "        \"Title\": \"Home\"\n" +
            "    },\n" +
            "    \"0102MS\": {\n" +
            "        \"Title\": \"BACK\"\n" +
            "    },\n" +
            "    \"0103MS\": {\n" +
            "        \"Title\": \"KEYBOARD\"\n" +
            "    },\n" +
            "    \"0104MS\": {\n" +
            "        \"Display_Label\": \"PREVIOUS\",\n" +
            "        \"Speech_Label\": \"PREVIOUS\"\n" +
            "    },\n" +
            "    \"0105MS\": {\n" +
            "        \"Display_Label\": \"NEXT\",\n" +
            "        \"Speech_Label\": \"NEXT\"\n" +
            "    }\n" +
            "}";
}