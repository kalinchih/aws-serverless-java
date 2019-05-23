package kalinchih.my_ip;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;

public class MyIpApp implements RequestHandler<MyIpRequest, MyIpResponse> {

    public static final String AUTH_TOKEN_VALUE = "AuthTokenFromCloudFront";

    public MyIpResponse handleRequest(MyIpRequest request, Context context) {
        long startMs = Instant.now().toEpochMilli();
        MyIpResponse response = new MyIpResponse();
        if (!authorized(request)) {
            response.httpStatus = 403;
        } else {
            String serverIp = "";
            try {
                serverIp = getServerIp();
            } catch (ServerIpNotFoundError e) {
                serverIp = e.getMessage();
            }
            response.request = request;
            response.serverIp = serverIp;
            String[] xForwardedForIps = StringUtils.split(request.xForwardedFor, ",");
            if (xForwardedForIps.length >= 2) {
                response.myIp = xForwardedForIps[xForwardedForIps.length - 2];
            } else if (xForwardedForIps.length == 1) {
                response.myIp = xForwardedForIps[0];
            } else {
                //response.myIp = request.sourceIp;
                response.myIp = "";
            }
            response.httpStatus = 200;
            long endMs = Instant.now().toEpochMilli();
            response.lambdaExecutionMs = endMs-startMs;
        }
        return response;
    }

    private boolean authorized(MyIpRequest request){
        if (request != null && StringUtils.equals(request.authToken, AUTH_TOKEN_VALUE)) {
           return true;
        } else {
            return false;
        }
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