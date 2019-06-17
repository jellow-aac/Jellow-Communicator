package com.dsource.idc.jellowintl.factories;


import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.utility.SessionManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static com.dsource.idc.jellowintl.factories.PathFactory.AUDIO_FOLDER;
import static com.dsource.idc.jellowintl.factories.PathFactory.DRAWABLE_FOLDER;
import static com.dsource.idc.jellowintl.factories.PathFactory.JSON_FILE;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;

@RunWith(AndroidJUnit4.class)
public class PathFactoryTest {
    private Context mContext;
    private SessionManager mSession;

    @Before
    public void setup(){
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mSession = new SessionManager(mContext);
        mSession.setLanguage(ENG_IN);
    }

    @Test
    public void getGlideIconPathTest(){
        String iconName ="0102000000GG.png";
        String path = PathFactory.getIconPath(mContext, iconName);
        assert  path.equals(
          mContext.getDir(ENG_IN, Context.MODE_PRIVATE).getAbsolutePath()
          .concat("//" + DRAWABLE_FOLDER + "/" + iconName));
    }

    @Test
    public void getBaseDirectoryPathTest(){
        String path = PathFactory.getBaseDirectoryPath(mContext);
        assert  path.equals(
                mContext.getDir(ENG_IN, Context.MODE_PRIVATE).getAbsolutePath()
                        .concat("/"));
    }

    @Test
    public void getAudioPathTest(){
        String path = PathFactory.getAudioPath(mContext);
        assert  path.equals(
                mContext.getDir(ENG_IN, Context.MODE_PRIVATE).getAbsolutePath()
                        .concat("/"+AUDIO_FOLDER+"/"));
    }

    @Test
    public void getIconDirectoryTest(){
        File path = PathFactory.getIconDirectory(mContext);
        File iconFile = new File(mContext.getDir(ENG_IN, Context.MODE_PRIVATE).getAbsolutePath()
                .concat("/"+DRAWABLE_FOLDER+"/"));
        assert  path.equals(iconFile);

    }

    @Test
    public void getJSONFileTest(){
        File path = PathFactory.getJSONFile(mContext);
        File iconFile = new File(mContext.getDir(ENG_IN, Context.MODE_PRIVATE).getAbsolutePath()
                .concat("/"+JSON_FILE));
        assert  path.equals(iconFile);

    }
}
