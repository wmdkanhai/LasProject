package xm.lasproject.bean;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/28
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class CommunityType {
    private String imageUrl;
    private String text;

    public CommunityType() {

    }
    public CommunityType(String imageUrl, String text) {
        this.imageUrl = imageUrl;
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "CommunityType{" +
                "imageUrl='" + imageUrl + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
