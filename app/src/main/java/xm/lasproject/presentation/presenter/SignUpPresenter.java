package xm.lasproject.presentation.presenter;

import android.content.Context;

import cn.bmob.v3.listener.SaveListener;
import xm.lasproject.bean.User;
import xm.lasproject.presentation.contract.ISignUpContract;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/24
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class SignUpPresenter implements ISignUpContract.Presenter {
    private ISignUpContract.View mView;
    private Context mContext;

    public SignUpPresenter(ISignUpContract.View view, Context context) {
        this.mView = view;
        this.mContext = context;
//        this.mView.setPresenter(this);
    }

    @Override
    public void signUp(User user) {
        //这里调用的bmob提供的方法，对用户进行注册
        user.signUp(mContext,new SaveListener() {
            @Override
            public void onSuccess() {
                mView.success();
            }

            @Override
            public void onFailure(int i, String s) {
                mView.showError(s);
            }

        });

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }
}
