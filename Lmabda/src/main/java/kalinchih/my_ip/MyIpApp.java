package kalinchih.my_ip;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MyIpApp implements RequestHandler<MyIpRequest, MyIpResponse> {

    public MyIpResponse handleRequest(MyIpRequest request, Context context) {
        String serverIp = "";
        try {
            serverIp = getServerIp();
        } catch (ServerIpNotFoundError e) {
            serverIp = e.getMessage();
        }
        MyIpResponse response = new MyIpResponse();
        response.serverIp = serverIp;
        response.myIp = request.sourceIp;
        context.getLogger().log(String.format("sourceIp: %s, xForwardedFor: %s", request.sourceIp, request.xForwardedFor));
        return response;
    }

    private String getServerIp() throws ServerIpNotFoundError {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            throw new ServerIpNotFoundError(e);
        }
    }
}