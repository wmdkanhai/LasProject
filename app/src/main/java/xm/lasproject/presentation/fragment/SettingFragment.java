package xm.lasproject.presentation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import xm.lasproject.R;
import xm.lasproject.presentation.activity.AccountActivity;
import xm.lasproject.presentation.activity.MyMessageActivity;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/22
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class SettingFragment extends Fragment {

    public SettingFragment() {
    }

    public static SettingFragment newInstance(){
        return new SettingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_setting,container,false);
        getActivity().getFragmentManager().beginTransaction().replace(R.id.ll_fragment_container, new SettingContentFragment()).commit();
        return rootView;
    }

    public static class SettingContentFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {


        private Preference mAccount;
        private Preference mMyMessage;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_setting);
            mAccount = findPreference("account");
            mMyMessage = findPreference("myMessage");
            mAccount.setOnPreferenceClickListener(this);
            mMyMessage.setOnPreferenceClickListener(this);
        }


        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference == mAccount){
                Toast.makeText(getActivity(), "点击了账号", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(),AccountActivity.class));
                return true;
            }else if(preference == mMyMessage){
                Toast.makeText(getActivity(), "点击了我的信息", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MyMessageActivity.class));
                return true;
            }
            return false;
        }
    }
}
