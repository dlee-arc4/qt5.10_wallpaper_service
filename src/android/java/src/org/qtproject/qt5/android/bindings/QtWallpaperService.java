/*
    Copyright (c) 2016, BogDan Vatra <bogdan@kde.org>
    Contact: http://www.qt.io/licensing/

    Commercial License Usage
    Licensees holding valid commercial Qt licenses may use this file in
    accordance with the commercial license agreement provided with the
    Software or, alternatively, in accordance with the terms contained in
    a written agreement between you and The Qt Company. For licensing terms
    and conditions see http://www.qt.io/terms-conditions. For further
    information use the contact form at http://www.qt.io/contact-us.

    BSD License Usage
    Alternatively, this file may be used under the BSD license as follows:
    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:

    1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
    2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.

    THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
    IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
    OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
    IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
    NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
    THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
    THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package org.qtproject.qt5.android.bindings;
import org.qtproject.qt5.android.QtWallpaperServiceDelegate;

import android.app.Service;
import android.view.SurfaceHolder; 
import android.service.wallpaper.WallpaperService;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;

public class QtWallpaperService extends WallpaperService
{
    public String APPLICATION_PARAMETERS = null; // use this variable to pass any parameters to your application,
                                                               // the parameters must not contain any white spaces
                                                               // and must be separated with "\t"
                                                               // e.g "-param1\t-param2=value2\t-param3\tvalue3"

    public String ENVIRONMENT_VARIABLES = "QT_USE_ANDROID_NATIVE_DIALOGS=1";
                                                               // use this variable to add any environment variables to your application.
                                                               // the env vars must be separated with "\t"
                                                               // e.g. "ENV_VAR1=1\tENV_VAR2=2\t"
                                                               // Currently the following vars are used by the android plugin:
                                                               // * QT_USE_ANDROID_NATIVE_DIALOGS - 1 to use the android native dialogs.

    public String[] QT_ANDROID_THEMES = null;     // A list with all themes that your application want to use.
                                                  // The name of the theme must be the same with any theme from
                                                  // http://developer.android.com/reference/android/R.style.html
                                                  // The most used themes are:
                                                  //  * "Theme" - (fallback) check http://developer.android.com/reference/android/R.style.html#Theme
                                                  //  * "Theme_Black" - check http://developer.android.com/reference/android/R.style.html#Theme_Black
                                                  //  * "Theme_Light" - (default for API <=10) check http://developer.android.com/reference/android/R.style.html#Theme_Light
                                                  //  * "Theme_Holo" - check http://developer.android.com/reference/android/R.style.html#Theme_Holo
                                                  //  * "Theme_Holo_Light" - (default for API 11-13) check http://developer.android.com/reference/android/R.style.html#Theme_Holo_Light
                                                  //  * "Theme_DeviceDefault" - check http://developer.android.com/reference/android/R.style.html#Theme_DeviceDefault
                                                  //  * "Theme_DeviceDefault_Light" - (default for API 14+) check http://developer.android.com/reference/android/R.style.html#Theme_DeviceDefault_Light

    public String QT_ANDROID_DEFAULT_THEME = null; // sets the default theme.

    QtWallpaperServiceLoader m_loader = new QtWallpaperServiceLoader(this);
    
    private QtWallpaperEngine m_engine;
    private ServiceInfo m_serviceInfo;
    private QtWallpaperServiceDelegate m_qtDelegate;

    public QtWallpaperService()
    {
      super();
    }
    /////////////////////////// forward all notifications ////////////////////////////
    /////////////////////////// Super class calls ////////////////////////////////////
    /////////////// PLEASE DO NOT CHANGE THE FOLLOWING CODE //////////////////////////
    //////////////////////////////////////////////////////////////////////////////////
    protected void onCreateHook() {
        m_loader.onCreate();
    }
    //---------------------------------------------------------------------------

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        if (!QtApplication.invokeDelegate(newConfig).invoked)
            super.onConfigurationChanged(newConfig);
    }
    public void super_onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }
    //---------------------------------------------------------------------------

    @Override
    public void onLowMemory()
    {
        if (!QtApplication.invokeDelegate().invoked)
            super.onLowMemory();
    }
    //---------------------------------------------------------------------------

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        QtApplication.InvokeResult res = QtApplication.invokeDelegate(intent, flags, startId);
        if (res.invoked)
            return (Integer) res.methodReturns;
        else
            return super.onStartCommand(intent, flags, startId);
    }
    public int super_onStartCommand(Intent intent, int flags, int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }
    //---------------------------------------------------------------------------

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        if (!QtApplication.invokeDelegate(rootIntent).invoked)
            super.onTaskRemoved(rootIntent);
    }
    public void super_onTaskRemoved(Intent rootIntent)
    {
        super.onTaskRemoved(rootIntent);
    }
    //---------------------------------------------------------------------------

    @Override
    public void onTrimMemory(int level)
    {
        if (!QtApplication.invokeDelegate(level).invoked)
            super.onTrimMemory(level);
    }
    public void super_onTrimMemory(int level)
    {
        super.onTrimMemory(level);
    }
    //---------------------------------------------------------------------------

    @Override
    public boolean onUnbind(Intent intent)
    {
        QtApplication.InvokeResult res = QtApplication.invokeDelegate(intent);
        if (res.invoked)
            return (Boolean) res.methodReturns;
        else
            return super.onUnbind(intent);
    }
    public boolean super_onUnbind(Intent intent)
    {
        return super.onUnbind(intent);
    }
    //---------------------------------------------------------------------------

    @Override
    public void onCreate()
    {
        super.onCreate();
        onCreateHook();
    }
    //---------------------------------------------------------------------------

    @Override
    public void onDestroy()
    {   
        super.onDestroy();
        m_qtDelegate.onDestroy();
    }   
    //---------------------------------------------------------------------------

    @Override
    public WallpaperService.Engine onCreateEngine()
    {   
        if (null == m_engine) {
            m_engine = new QtWallpaperEngine(m_qtDelegate);
        }
        return m_engine;
    }   
    //---------------------------------------------------------------------------

    public QtWallpaperServiceDelegate getWallpaperDelegate()
    {   
        return m_qtDelegate;
    }
    //---------------------------------------------------------------------------


   /*
    * This wallpaper engine is the only way to get access to an OpenGL
    * context as any kind of Android Service.
    */
    public class QtWallpaperEngine extends Engine {

        private QtWallpaperServiceDelegate m_qtDelegate;

        public QtWallpaperEngine(QtWallpaperServiceDelegate qtDelegate) {
            super();
            m_qtDelegate = qtDelegate;
        }
        //---------------------------------------------------------------------------
        @Override
        public void onCreate(SurfaceHolder surfaceholder)
        {
            super.onCreate(surfaceholder);
        }
        //---------------------------------------------------------------------------
        @Override
        public void onDestroy()
        {
            super.onDestroy();
        }
        //---------------------------------------------------------------------------
        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height)
        {
            super.onSurfaceChanged(holder, format, width, height);
            m_qtDelegate.onSurfaceChanged(holder, format, width, height);
        }
        //---------------------------------------------------------------------------
        @Override
        public void onSurfaceCreated(SurfaceHolder holder)
        {
            super.onSurfaceCreated(holder);
            m_qtDelegate.onSurfaceCreated(holder);
        }
        //---------------------------------------------------------------------------
        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder)
        {
            super.onSurfaceDestroyed(holder);
            m_qtDelegate.onSurfaceDestroyed(holder);
        }
        //---------------------------------------------------------------------------
        @Override
        public void onVisibilityChanged(boolean visible)
        {
            super.onVisibilityChanged(visible);
        }
        //---------------------------------------------------------------------------
    }
}
