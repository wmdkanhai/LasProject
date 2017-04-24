package xm.lasproject.config;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/22
 *     desc   : 存储系统的常量
 *     version: 1.0
 * </pre>
 */

public class SystemVar {
    //bomb的application_Id
    public static final String BMOB_APPLICATION_ID = "8dbaea8caa902586448121ce6313ab08";

    //是否是debug模式
    public static final boolean DEBUG=true;
    //好友请求：未读-未添加->接收到别人发给我的好友添加请求，初始状态
    public static final int STATUS_VERIFY_NONE=0;
    //好友请求：已读-未添加->点击查看了新朋友，则都变成已读状态
    public static final int STATUS_VERIFY_READED=2;
    //好友请求：已添加
    public static final int STATUS_VERIFIED=1;
    //好友请求：拒绝
    public static final int STATUS_VERIFY_REFUSE=3;
    //好友请求：我发出的好友请求-暂未存储到本地数据库中
    public static final int STATUS_VERIFY_ME_SEND=4;
}
