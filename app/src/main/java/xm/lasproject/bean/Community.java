package xm.lasproject.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/05/15
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class Community extends BmobObject {
    private String modeType;
    private String modeDescribe;
    private BmobFile modePicture;
    private String modeTitle;

    public Community(String modeType, String modeDescribe, BmobFile modePicture) {
        this.modeType = modeType;
        this.modeDescribe = modeDescribe;
        this.modePicture = modePicture;
    }

    public Community(String tableName, String modeType, String modeDescribe, BmobFile modePicture) {
        super(tableName);
        this.modeType = modeType;
        this.modeDescribe = modeDescribe;
        this.modePicture = modePicture;
    }

    public Community() {
    }

    public Community(String tableName) {
        super(tableName);
    }

    public String getModeType() {
        return modeType;
    }

    public void setModeType(String modeType) {
        this.modeType = modeType;
    }

    public String getModeDescribe() {
        return modeDescribe;
    }

    public void setModeDescribe(String modeDescribe) {
        this.modeDescribe = modeDescribe;
    }

    public BmobFile getModePicture() {
        return modePicture;
    }

    public void setModePicture(BmobFile modePicture) {
        this.modePicture = modePicture;
    }

    public String getModeTitle() {
        return modeTitle;
    }

    public void setModeTitle(String modeTitle) {
        this.modeTitle = modeTitle;
    }
}
