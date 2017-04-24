package xm.lasproject.bean;

import java.io.Serializable;
import java.util.List;

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

public class RecordMode {

    private List<ResultsBean> results;

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean extends BmobObject implements Serializable {
        /**
         * createdAt : 2017-04-10 16:41:50
         * objectId : 8ac8fdee6d
         * recordContent : 你好，这是记录中的测试数据，第一条。
         * updatedAt : 2017-04-10 16:41:50
         * userObjectId : 7fc3c2eaf2
         * userPhotoUrl : http://bmob-cdn-10014.b0.upaiyun.com/2017/04/07/fe3eab78401b82f2809a68c2156917d6.jpg
         * username : 王明鼎
         */

//        private String createdAt;
//        private String objectId;
        private String recordContent;
//        private String updatedAt;
        private String userObjectId;
        private String userPhotoUrl;
        private String username;

//        public String getCreatedAt() {
//            return createdAt;
//        }
//
//        public void setCreatedAt(String createdAt) {
//            this.createdAt = createdAt;
//        }

//        public String getObjectId() {
//            return objectId;
//        }
//
//        public void setObjectId(String objectId) {
//            this.objectId = objectId;
//        }

        public String getRecordContent() {
            return recordContent;
        }

        public void setRecordContent(String recordContent) {
            this.recordContent = recordContent;
        }

//        public String getUpdatedAt() {
//            return updatedAt;
//        }
//
//        public void setUpdatedAt(String updatedAt) {
//            this.updatedAt = updatedAt;
//        }

        public String getUserObjectId() {
            return userObjectId;
        }

        public void setUserObjectId(String userObjectId) {
            this.userObjectId = userObjectId;
        }

        public String getUserPhotoUrl() {
            return userPhotoUrl;
        }

        public void setUserPhotoUrl(String userPhotoUrl) {
            this.userPhotoUrl = userPhotoUrl;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
