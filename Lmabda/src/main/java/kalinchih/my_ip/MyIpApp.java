package kalinchih.my_ip;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.commons.lang3.StringUtils;

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
        response.request = request;
        response.serverIp = serverIp;
        String[] xForwardedForIps = StringUtils.split(request.xForwardedFor, ",");
        if (xForwardedForIps.length > 0 && StringUtils.isNotBlank(xForwardedForIps[0])) {
            response.myIp = xForwardedForIps[0];
        } else {
            //response.myIp = request.sourceIp;
            response.myIp = "";
        }
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