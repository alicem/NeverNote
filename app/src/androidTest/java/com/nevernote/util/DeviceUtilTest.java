package com.nevernote.util;

import android.support.test.runner.AndroidJUnit4;

import com.nevernote.BaseTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by ali on 15.03.18.
 */

@RunWith(AndroidJUnit4.class)
public class DeviceUtilTest extends BaseTest{


    @Test
    public void getDeviceUniqueIdTest(){
        assertNotNull(getTestComponent().deviceUtil().getDeviceUniqueId());
    }

}