package xm.lasproject.repository.network.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;
import xm.lasproject.bean.CommunityList;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/29
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */
public abstract class CommunityListCallBack extends Callback<CommunityList> {

    @Override
    public CommunityList parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        CommunityList communityList = new Gson().fromJson(string, CommunityList.class);
        return communityList;
    }

}