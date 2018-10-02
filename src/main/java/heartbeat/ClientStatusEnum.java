package heartbeat;

public enum ClientStatusEnum {

    REGISTER(0,"注册"),
    UP(1,"上线"),
    DOWN(2,"下线");

    /**
     * 状态CODE
     */
    private int statusCode;

    /**
     * 状态描述
     */
    private String statusMessage;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    ClientStatusEnum(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    @Override
    public String toString() {
        return "ClientStatusEnum{" +
                "statusCode=" + statusCode +
                ", statusMessage='" + statusMessage + '\'' +
                '}';
    }
}
