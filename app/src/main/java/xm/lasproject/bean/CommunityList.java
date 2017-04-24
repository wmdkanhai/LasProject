package xm.lasproject.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/29
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class CommunityList {

    private List<ResultsBean> results;

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean implements Serializable {
        /**
         * content : 我非常喜欢你，但是你你喜欢喜欢喜欢喜欢还行吧，这是测试数据，哈哈哈哈
         * createdAt : 2017-03-28 17:14:56
         * modeType : 爱情分享
         * objectId : 6793488fde
         * sex : 男
         * time : 2017-03-28 05:14:54
         * title : 我喜欢你，你却不知道
         * updatedAt : 2017-03-28 17:14:56
         * username : 王明鼎
         */

        private String content;
        private String createdAt;
        private String modeType;
        private String objectId;
        private String sex;
        private String time;
        private String title;
        private String updatedAt;
        private String username;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getModeType() {
            return modeType;
        }

        public void setModeType(String modeType) {
            this.modeType = modeType;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
