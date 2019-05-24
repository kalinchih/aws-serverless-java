package kalinchih.my_ip;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;

public class MyIpApp implements RequestHandler<MyIpRequest, MyIpResponse> {

    public static final String AUTH_TOKEN_VALUE = "AuthTokenFromCloudFront";

    /***
     * Lambda entry method
     * @param request request which prepared by API Gateway request Mapping Template
     * @param context lambda context
     * @return MyIpResponse contains myIp(userIp), serverIp, lambdaExecutionMs and MyIpRequest object (for trouble shooting)
     */
    public MyIpResponse handleRequest(MyIpRequest request, Context context) {
        long startMs = Instant.now().toEpochMilli();
        MyIpResponse response = new MyIpResponse();
        if (!authorized(request.authToken)) {
            response.httpStatus = 403;
        } else {
            String serverIp = "";
            String userIp = "";
            try {
                serverIp = getServerIp();
                userIp = getUserIp(request.xForwardedFor);
            } catch (ServerIpNotFoundError e) {
                serverIp = e.getMessage();
            } catch (UserIpNotFoundError e) {
                userIp = e.getMessage();
            }
            // secure the authToken by removing it
            request.authToken = null;
            response.request = request;
            response.myIp = userIp;
            response.serverIp = serverIp;
            response.httpStatus = 200;
            long endMs = Instant.now().toEpochMilli();
            response.lambdaExecutionMs = endMs-startMs;
        }
        return response;
    }

    /***
     * Authorize the request by authToken
     * @param authToken authToken witch defined/passed from CloudFront Origin Custom Headers
     * @return true for authorized request
     */
    private boolean authorized(String authToken ){
        if (StringUtils.equals(authToken, AUTH_TOKEN_VALUE)) {
           return true;
        } else {
            return false;
        }
    }

    /***
     * Get server IP
     * @return server IP
     * @throws ServerIpNotFoundError
     */
    private String getServerIp() throws ServerIpNotFoundError {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            throw new ServerIpNotFoundError(e);
        }
    }

    /***
     * Get user IP
     * @param xForwardedFor X-Forwarded-Fro request header
     * @return user IP
     * @throws UserIpNotFoundError
     */
    private String getUserIp(String xForwardedFor) throws UserIpNotFoundError {
        // The web API endpoint type is 'Edge optimized'. So the header value must contains 3 IP addresses at least.
        // Split the header value by comma and get the 'third from last' IP address as the user IP.
        String[] xForwardedForIps = StringUtils.split(xForwardedFor, ",");
        if (xForwardedForIps != null && xForwardedForIps.length >= 3) {
            return xForwardedForIps[xForwardedForIps.length - 3];
        } else {
            throw new UserIpNotFoundError(String.format("Cannot find IP from X-Forwarded-For: %s", xForwardedForIps));
        }
    }
}