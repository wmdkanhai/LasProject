package xm.lasproject.presentation.contract;

import xm.lasproject.bean.CommunityList;
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

public interface ICommunityListContract {
    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void showError(String message);
        void success(CommunityList communityList);

    }

    interface Presenter extends BasePresenter {
        void getCommunityList(String mModeType);
    }
}
