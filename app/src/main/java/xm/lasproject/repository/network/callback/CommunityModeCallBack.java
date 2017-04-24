package xm.lasproject.repository.network.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import xm.lasproject.bean.CommunityMode;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/30
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public abstract class CommunityModeCallBack extends Callback<CommunityMode> {

    @Override
    public CommunityMode parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        CommunityMode communityMode = new Gson().fromJson(string, CommunityMode.class);
        return communityMode;
    }
}
