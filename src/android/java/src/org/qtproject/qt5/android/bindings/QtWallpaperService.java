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
//
import org.qtproject.qt5.android.QtWallpaperServiceDelegate;
import android.view.SurfaceHolder; 
import android.service.wallpaper.WallpaperService;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.ComponentName;
import android.app.PendingIntent;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
//
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Log;
public class QtWallpaperService extends WallpaperService
{
    private static final int ONGOING_NOTIFICATION_ID = 10112;
    QtWallpaperServiceLoader m_loader = new QtWallpaperServiceLoader(this);
    private QtWallpaperEngine        m_engine = null;
    private QtWallpaperServiceDelegate m_qtDelegate;
    /////////////////////////// forward all notifications ////////////////////////////
    /////////////////////////// Super class calls ////////////////////////////////////
    /////////////// PLEASE DO NOT CHANGE THE FOLLOWING CODE //////////////////////////
    //////////////////////////////////////////////////////////////////////////////////
    protected void onCreateHook() {
        Log.d("Qt", "QtWallpaperService:onCreateHook");

        m_loader.onCreate();
        Intent notificationIntent = new Intent(getBaseContext(), QtWallpaperServiceLoader.class);
        int drawableID = getBaseContext().getResources().getIdentifier("arc4", "drawable", getPackageName());
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, notificationIntent, 0);   
        Notification notification = new Notification.Builder(this).setContentTitle("Qt Wallpaper Service")
                                                                  .setTicker("Qt Wallpaper Started")
                                                                  .setSmallIcon(drawableID)
                                                                  .setContentIntent(pendingIntent)
                                                                  .setOngoing(true)
                                                                  .build() ;
        startForeground(ONGOING_NOTIFICATION_ID, notification);

        QtApplication.invokeDelegate(notificationIntent);
    }
    @Override
    public void onCreate()
    {
        Log.d("Qt", "QtWallpaperService:onCreate");
        super.onCreate();
        onCreateHook();
    }
    //---------------------------------------------------------------------------

    @Override
    public void onDestroy()
    {
        Log.d("Qt", "QtWallpaperService:onDestroy");
        super.onDestroy();
        QtApplication.invokeDelegate();
        Process.killProcess(Process.myPid());
    }
    //onBind is final in wallpaperService. Which is upsetting because we 
    //       might be loosing functionality by not calling QtApplication.invokeDelegate
    //---------------------------------------------------------------------------

    //public IBinder onBind(Intent intent)
    //{
    //    QtApplication.InvokeResult res = QtApplication.invokeDelegate(intent);
    //    if (res.invoked)
    //        return (IBinder)res.methodReturns;
    //    else
    //        return null;
    //}
    //---------------------------------------------------------------------------

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        Log.d("Qt", "QtWallpaperService:onConfigurationChanged");
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
        Log.d("Qt", "QtWallpaperService:onLowMemory");
        if (!QtApplication.invokeDelegate().invoked)
            super.onLowMemory();
    }
    //---------------------------------------------------------------------------

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("Qt", "QtWallpaperService:onStartCommand");
        QtApplication.InvokeResult res = QtApplication.invokeDelegate(intent, flags, startId);
        if (res.invoked)
            return (Integer) res.methodReturns;
        else
            return super.onStartCommand(intent, flags, startId);
    }
    public int super_onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("Qt", "QtWallpaperService:super_onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
    //---------------------------------------------------------------------------

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        Log.d("Qt", "QtWallpaperService:onTaskRemoved");
        if (!QtApplication.invokeDelegate(rootIntent).invoked)
            super.onTaskRemoved(rootIntent);
    }
    public void super_onTaskRemoved(Intent rootIntent)
    {
        Log.d("Qt", "QtWallpaperService:super_onTaskRemoved");
        super.onTaskRemoved(rootIntent);
    }
    //---------------------------------------------------------------------------

    @Override
    public void onTrimMemory(int level)
    {
        Log.d("Qt", "QtWallpaperService:onTrimMemory");
        if (!QtApplication.invokeDelegate(level).invoked)
            super.onTrimMemory(level);
    }
    public void super_onTrimMemory(int level)
    {
        Log.d("Qt", "QtWallpaperService:super_onTrimMemory");
        super.onTrimMemory(level);
    }
    //---------------------------------------------------------------------------

    @Override
    public boolean onUnbind(Intent intent)
    {
        Log.d("Qt", "QtWallpaperService:onUnbind");
        QtApplication.InvokeResult res = QtApplication.invokeDelegate(intent);
        if (res.invoked)
            return (Boolean) res.methodReturns;
        else
            return super.onUnbind(intent);
    }
    public boolean super_onUnbind(Intent intent)
    {
        Log.d("Qt", "QtWallpaperService:super_onUnbind");
        return super.onUnbind(intent);
    }
    //---------------------------------------------------------------------------
    //------------------------Engine---------------------------------------------

    @Override
    public WallpaperService.Engine onCreateEngine()
    {   
        Log.d("Qt", "QtWallpaperService:onCreateEngine");
        if (null == m_engine) {
            if ( null == m_qtDelegate)
                m_qtDelegate = new QtWallpaperServiceDelegate();
            m_engine = new QtWallpaperEngine(m_qtDelegate);
        }
        return m_engine;
    }   
    //---------------------------------------------------------------------------

    public QtWallpaperServiceDelegate getWallpaperDelegate()
    {   
        Log.d("Qt", "QtWallpaperService:getWallpaperDelegate");
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
            Log.d("Qt", "QtWallpaperEngine.Engine:Engine");
            m_qtDelegate = qtDelegate;
        }
        //---------------------------------------------------------------------------
        @Override
        public void onCreate(SurfaceHolder surfaceholder)
        {
            Log.d("Qt", "QtWallpaperEngine.Engine:onCreate");
            super.onCreate(surfaceholder);
        }
        //---------------------------------------------------------------------------
        @Override
        public void onDestroy()
        {
            Log.d("Qt", "QtWallpaperEngine.Engine:onDestroy");
            super.onDestroy();
        }
        //---------------------------------------------------------------------------
        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height)
        {
            Log.d("Qt", "QtWallpaperEngine.Engine:onSurfaceChanged");
            super.onSurfaceChanged(holder, format, width, height);
            if ( null != m_qtDelegate )
                m_qtDelegate.onSurfaceChanged(holder, format, width, height);
        }
        //---------------------------------------------------------------------------
        @Override
        public void onSurfaceCreated(SurfaceHolder holder)
        {
            Log.d("Qt", "QtWallpaperEngine.Engine:onSurfaceCreated");
            super.onSurfaceCreated(holder);
            if ( null != m_qtDelegate ){
                m_qtDelegate.onSurfaceCreated(holder);
            }
        }
        //---------------------------------------------------------------------------
        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder)
        {
            Log.d("Qt", "QtWallpaperEngine.Engine:onSurfaceDestroyed");
            super.onSurfaceDestroyed(holder);
            if ( null != m_qtDelegate )
                m_qtDelegate.onSurfaceDestroyed(holder);
        }
        //---------------------------------------------------------------------------
        @Override
        public void onVisibilityChanged(boolean visible)
        {
            Log.d("Qt", "QtWallpaperEngine.Engine:onVisibilityChanged");
            super.onVisibilityChanged(visible);
        }
        //---------------------------------------------------------------------------
    }

}
