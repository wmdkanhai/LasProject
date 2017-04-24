package xm.lasproject.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/28
 *     desc   : 社区中每个模块中话题的bean
 *     version: 1.0
 * </pre>
 */

public class CommunityTheme extends BmobObject {
    private String modeType;

    private String userObjectId;
    private String username;
    private BmobFile userPhoto;
    private String sex;

    private String title;
    private String content;
    private BmobFile picture;
    private String commentNumber;
    private String time;

    public CommunityTheme() {
    }

    public CommunityTheme(String tableName) {
        super(tableName);
    }

    public String getUserObjectId() {
        return userObjectId;
    }

    public void setUserObjectId(String userObjectId) {
        this.userObjectId = userObjectId;
    }

    public String getModeType() {
        return modeType;
    }

    public void setModeType(String modeType) {
        this.modeType = modeType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BmobFile getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(BmobFile userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }

    public String getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(String commentNumber) {
        this.commentNumber = commentNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "CommunityTheme{" +
                "username='" + username + '\'' +
                ", userPhoto=" + userPhoto +
                ", sex='" + sex + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", picture=" + picture +
                ", commentNumber='" + commentNumber + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
