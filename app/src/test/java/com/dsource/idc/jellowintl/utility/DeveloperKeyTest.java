package com.dsource.idc.jellowintl.utility;

import org.junit.Test;

public class DeveloperKeyTest {

    @Test
    public void checkKey(){
        DeveloperKey developerKey = new DeveloperKey();
        assert DeveloperKey.DEVELOPER_KEY == "";
    }
}
