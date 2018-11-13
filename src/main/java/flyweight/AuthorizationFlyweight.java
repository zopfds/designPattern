package flyweight;

/**
 * 授权实体享元
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/11/13
 */
public class AuthorizationFlyweight implements Flyweight{

    private String operation;

    private String permit;

    /**
     * 构造函数构造享元的内部状态
     * @param state
     */
    public AuthorizationFlyweight(String state) {
        String[] ss = state.split(",");
        this.operation = ss[0];
        this.permit = ss[1];
    }

    public String getOperation() {
        return operation;
    }

    public String getPermit() {
        return permit;
    }

    @Override
    public boolean match(String operation, String permit) {
        return this.operation.equals(operation) && this.permit.equals(permit);
    }
}
