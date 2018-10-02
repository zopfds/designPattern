package heartbeat;

import java.io.Serializable;
import java.util.Date;

/**
 * 心跳检测包
 */
public class HeartBeatData implements Serializable{

    /**
     * 客户端标识
     */
    private String clientId;

    /**
     * 客户端ip
     */
    private String ip;

    /**
     * 状态
     */
    private int status;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 心跳检测数据
     */
    private String heartBeatData;

    /**
     * 数据创建时间
     */
    private Date createTime;

    /**
     * 最新更新时间
     */
    private Date lastUpdateTime;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getHeartBeatData() {
        return heartBeatData;
    }

    public void setHeartBeatData(String heartBeatData) {
        this.heartBeatData = heartBeatData;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HeartBeatData(String clientId, String ip, String clientName, String heartBeatData) {
        this.clientId = clientId;
        this.ip = ip;
        this.clientName = clientName;
        this.heartBeatData = heartBeatData;
    }

    @Override
    public String toString() {
        return "HeartBeatData{" +
                "clientId='" + clientId + '\'' +
                ", ip='" + ip + '\'' +
                ", status=" + status +
                ", clientName='" + clientName + '\'' +
                ", heartBeatData='" + heartBeatData + '\'' +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
