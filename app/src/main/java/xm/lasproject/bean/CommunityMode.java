package xm.lasproject.bean;

import java.util.List;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/30
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class CommunityMode {

    private List<ResultsBean> results;

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * createdAt : 2017-03-30 12:58:06
         * modeDescribe : 爱情分享
         * modePicture : {"__type":"File","cdn":"upyun","filename":"分享 (1).png","url":"http://bmob-cdn-10014.b0.upaiyun.com/2017/03/30/ef9836904075ed7b808184c97c9c9cf7.png"}
         * modeType : 1
         * objectId : LtPRFFFJ
         * updatedAt : 2017-03-30 13:22:44
         */

        private String createdAt;
        private String modeDescribe;
        private ModePictureBean modePicture;
        private String modeType;
        private String objectId;
        private String updatedAt;

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getModeDescribe() {
            return modeDescribe;
        }

        public void setModeDescribe(String modeDescribe) {
            this.modeDescribe = modeDescribe;
        }

        public ModePictureBean getModePicture() {
            return modePicture;
        }

        public void setModePicture(ModePictureBean modePicture) {
            this.modePicture = modePicture;
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

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public static class ModePictureBean {
            /**
             * __type : File
             * cdn : upyun
             * filename : 分享 (1).png
             * url : http://bmob-cdn-10014.b0.upaiyun.com/2017/03/30/ef9836904075ed7b808184c97c9c9cf7.png
             */

            private String __type;
            private String cdn;
            private String filename;
            private String url;

            public String get__type() {
                return __type;
            }

            public void set__type(String __type) {
                this.__type = __type;
            }

            public String getCdn() {
                return cdn;
            }

            public void setCdn(String cdn) {
                this.cdn = cdn;
            }

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
