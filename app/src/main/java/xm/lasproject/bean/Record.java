package xm.lasproject.bean;

import cn.bmob.v3.BmobObject;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/04/10
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class Record extends BmobObject{
    private String userObjectId;
    private String username;
    private String recordContent;
    private String userPhotoUrl;

    public Record() {
    }

    public Record(String userObjectId, String username, String recordContent) {
        this.userObjectId = userObjectId;
        this.username = username;
        this.recordContent = recordContent;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getUserObjectId() {
        return userObjectId;
    }

    public void setUserObjectId(String userObjectId) {
        this.userObjectId = userObjectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRecordContent() {
        return recordContent;
    }

    public void setRecordContent(String recordContent) {
        this.recordContent = recordContent;
    }
}
