package xm.lasproject.presentation.contract;

import xm.lasproject.bean.User;
import xm.lasproject.presentation.BasePresenter;
import xm.lasproject.presentation.BaseView;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/24
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public interface ILoginContract {
    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void showError(String message);
        void success();
        void getUserInfoByObjectId(User user);
    }

    interface Presenter extends BasePresenter {
        void login(User user);
        void getUserInfo(String id);
    }
}
