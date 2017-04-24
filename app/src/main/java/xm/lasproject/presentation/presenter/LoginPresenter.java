package xm.lasproject.presentation.presenter;

import android.content.Context;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import xm.lasproject.bean.User;
import xm.lasproject.presentation.contract.ILoginContract;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/24
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class LoginPresenter implements ILoginContract.Presenter {
    private ILoginContract.View mView;
    private Context mContext;

    public LoginPresenter(ILoginContract.View view,Context context) {
        this.mView = view;
        this.mContext = context;
//        this.mView.setPresenter(this);
    }



    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void login(final User user) {
        user.login(mContext,new SaveListener() {
            @Override
            public void onSuccess() {
                mView.success();
                User user1 = BmobUser.getCurrentUser(mContext, User.class);
                getUserInfo(user1.getObjectId());
            }

            @Override
            public void onFailure(int i, String s) {
                mView.showError(s);
            }

        });
    }

    @Override
    public void getUserInfo(String id) {
        BmobQuery<User> query = new BmobQuery<>();
        query.getObject(mContext, id, new GetListener<User>() {
            @Override
            public void onSuccess(User user) {
                mView.getUserInfoByObjectId(user);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}
