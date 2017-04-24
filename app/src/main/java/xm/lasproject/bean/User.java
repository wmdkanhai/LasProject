package xm.lasproject.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/24
 *     desc   : 用户实体类
 *     version: 1.0
 * </pre>
 */

public class User extends BmobUser {
    private String nickName;//昵称
    private String birthday;//生日
    private String registTime;//注册时间
    private String pairing;//是否配对
    private String pairingInfo;//配对信息
    private String pairingTime;//配对时间
    private String sex;//性别
    private BmobFile photo;//头像
    private String loveTime;//恋爱日期

    public User() {
    }


    public User(String nickName, String pairing, String birthday, String registTime, String pairingInfo, String pairingTime, String sex, BmobFile photo, String loveTime) {
        this.nickName = nickName;
        this.birthday = birthday;
        this.pairing = pairing;
        this.registTime = registTime;
        this.pairingInfo = pairingInfo;
        this.pairingTime = pairingTime;
        this.sex = sex;
        this.photo = photo;
        this.loveTime = loveTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRegistTime() {
        return registTime;
    }

    public void setRegistTime(String registTime) {
        this.registTime = registTime;
    }

    public String getPairingInfo() {
        return pairingInfo;
    }

    public void setPairingInfo(String pairingInfo) {
        this.pairingInfo = pairingInfo;
    }

    public String getPairingTime() {
        return pairingTime;
    }

    public void setPairingTime(String pairingTime) {
        this.pairingTime = pairingTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public BmobFile getPhoto() {
        return photo;
    }

    public void setPhoto(BmobFile photo) {
        this.photo = photo;
    }

    public String getLoveTime() {
        return loveTime;
    }

    public void setLoveTime(String loveTime) {
        this.loveTime = loveTime;
    }

    public String getPairing() {
        return pairing;
    }

    public void setPairing(String pairing) {
        this.pairing = pairing;
    }

    @Override
    public String toString() {
        return "User{" +
                "nickName='" + nickName + '\'' +
                ", birthday='" + birthday + '\'' +
                ", registTime='" + registTime + '\'' +
                ", pairing='" + pairing + '\'' +
                ", pairingInfo='" + pairingInfo + '\'' +
                ", pairingTime='" + pairingTime + '\'' +
                ", sex='" + sex + '\'' +
                ", photo=" + photo +
                ", loveTime='" + loveTime + '\'' +
                '}';
    }
}
