package xm.lasproject.bean;

import java.util.List;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/04/24
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class CommentMode {

    private List<ResultsBean> results;

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * commentContent : 好对\(^o^)/
         * commentUserId : 7fc3c2eaf2
         * commentUserPhoto : xm.jpg
         * commentUsername : 王明鼎
         * createdAt : 2017-04-24 22:26:27
         * objectId : eb2d9e6235
         * recordId : 6793488fde
         * updatedAt : 2017-04-24 22:26:27
         */

        private String commentContent;
        private String commentUserId;
        private String commentUserPhoto;
        private String commentUsername;
        private String createdAt;
        private String objectId;
        private String recordId;
        private String updatedAt;

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

        public String getCommentUserPhoto() {
            return commentUserPhoto;
        }

        public void setCommentUserPhoto(String commentUserPhoto) {
            this.commentUserPhoto = commentUserPhoto;
        }

        public String getCommentUsername() {
            return commentUsername;
        }

        public void setCommentUsername(String commentUsername) {
            this.commentUsername = commentUsername;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
