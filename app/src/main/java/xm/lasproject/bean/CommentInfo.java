package xm.lasproject.bean;

import cn.bmob.v3.BmobObject;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/30
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class CommentInfo extends BmobObject{
    //评论的记录的Id
    private String recordId;
    //评论的内容
    private String commentContent;

    //评论人的信息
    private String commentUserId;
    private String commentUsername;
    private String commentUserPhoto;
    private String commentUserSex;


    public CommentInfo() {
    }

    public CommentInfo(String recordId,String commentContent, String commentUserId, String commentUsername, String commentUserPhoto, String commentUserSex) {
        this.recordId = recordId;
        this.commentContent = commentContent;
        this.commentUserId = commentUserId;
        this.commentUsername = commentUsername;
        this.commentUserPhoto = commentUserPhoto;
        this.commentUserSex = commentUserSex;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getCommentUsername() {
        return commentUsername;
    }

    public void setCommentUsername(String commentUsername) {
        this.commentUsername = commentUsername;
    }

    public String getCommentUserPhoto() {
        return commentUserPhoto;
    }

    public void setCommentUserPhoto(String commentUserPhoto) {
        this.commentUserPhoto = commentUserPhoto;
    }

    public String getCommentUserSex() {
        return commentUserSex;
    }

    public void setCommentUserSex(String commentUserSex) {
        this.commentUserSex = commentUserSex;
    }
}
