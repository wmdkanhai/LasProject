package xm.lasproject.app;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;
import okhttp3.OkHttpClient;
import xm.lasproject.util.DemoMessageHandler;

import static xm.lasproject.config.SystemVar.BMOB_APPLICATION_ID;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/24
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class MyApplication extends Application {
    private static MyApplication INSTANCE;

    public static MyApplication INSTANCE() {
        return INSTANCE;
    }

    private void setInstance(MyApplication app) {
        setBmobIMApplication(app);
    }

    private static void setBmobIMApplication(MyApplication a) {
        MyApplication.INSTANCE = a;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, BMOB_APPLICATION_ID);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

        //只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())) {
            //im初始化
            BmobIM.init(this);
            //注册消息接收器
            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
        }


    }

    /**
     * 获取当前运行的进程名
     *
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
