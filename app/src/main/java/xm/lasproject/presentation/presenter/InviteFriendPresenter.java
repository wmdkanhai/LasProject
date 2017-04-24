package xm.lasproject.presentation.presenter;

import android.content.Context;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import xm.lasproject.bean.User;
import xm.lasproject.presentation.contract.IInviteFriendContract;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/24
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class InviteFriendPresenter implements IInviteFriendContract.Presenter {
    private IInviteFriendContract.View mView;
    private Context mContext;

    public InviteFriendPresenter(IInviteFriendContract.View view, Context context) {
        this.mView = view;
//        this.mView.setPresenter(this);
        this.mContext = context;
    }


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }


    @Override
    public void query(String username) {

        BmobQuery<User> query = new BmobQuery<>();

        //去掉当前用户
        try {
            BmobUser user = BmobUser.getCurrentUser(mContext);
            query.addWhereNotEqualTo("username",user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }

        query.addWhereEqualTo("username",username);
//        query.setLimit(limit);
        query.order("-createdAt");
        query.findObjects(mContext, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                mView.success(list);
            }

            @Override
            public void onError(int i, String s) {
                mView.showError(s);
            }
        });

    }
}
