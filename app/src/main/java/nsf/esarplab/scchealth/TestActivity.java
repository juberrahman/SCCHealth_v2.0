package nsf.esarplab.scchealth;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class TestActivity extends AppCompatActivity {

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Log.i("Testactivity", "called");

        try {
            final String libPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/dexjuber.jar";
            final File tmpDir = getDir("dex", 0);
            Log.i("First", "Stage");
            final DexClassLoader classloader = new DexClassLoader(libPath, tmpDir.getAbsolutePath(), null, this.getClass().getClassLoader());
            final Class<Object> classToLoad = (Class<Object>) classloader.loadClass("com.example.MyClass");

            final Object myInstance  = classToLoad.newInstance();
            final Method doSomething = classToLoad.getMethod("MyClass");

            doSomething.invoke(myInstance);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
