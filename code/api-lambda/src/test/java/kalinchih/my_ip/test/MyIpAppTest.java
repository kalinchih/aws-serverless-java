package kalinchih.my_ip.test;

import kalinchih.my_ip.MyIpApp;
import kalinchih.my_ip.MyIpRequest;
import kalinchih.my_ip.MyIpResponse;
import kalinchih.my_ip.UserIpNotFoundError;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({MyIpApp.class})
public class MyIpAppTest {

    @Test
    public void authorized() throws Exception {
        MyIpApp myIpApp = new MyIpApp();
        String methodName = "authorized";
        // correct authToken
        MyIpRequest request = new MyIpRequest();
        request.authToken = "AuthTokenFromCloudFront";
        Assert.assertEquals(true, Whitebox.invokeMethod(myIpApp, methodName, request));
        // null authToken
        MyIpRequest requestWithNullAuthToken = new MyIpRequest();
        requestWithNullAuthToken.authToken = null;
        Assert.assertEquals(false, Whitebox.invokeMethod(myIpApp, methodName, requestWithNullAuthToken));
        // incorrect authToken
        MyIpRequest requestWithIncorrectAuthToken = new MyIpRequest();
        requestWithIncorrectAuthToken.authToken = "incorrect";
        Assert.assertEquals(false, Whitebox.invokeMethod(myIpApp, methodName, requestWithIncorrectAuthToken));
    }

    @Test
    public void getServerIp() throws Exception {
        MyIpApp myIpApp = new MyIpApp();
        String methodName = "getServerIp";
        Assert.assertEquals(String.class.getName(), Whitebox.invokeMethod(myIpApp, methodName).getClass().getName());
    }

    @Test
    public void getUserIp() throws Exception {
        MyIpApp myIpApp = new MyIpApp();
        String methodName = "getUserIp";
        // X-Forwarded-For len >= 3
        Assert.assertEquals("ip2", Whitebox.invokeMethod(myIpApp, methodName, "ip1,ip2,ip3, ip4"));
        // X-Forwarded-For len < 3
        try {
            Assert.assertEquals(UserIpNotFoundError.class.getName(), Whitebox.invokeMethod(myIpApp, methodName, "ip1,ip2").getClass().getName());
        } catch (Exception e) {
        }
    }

    @Test
    public void handleRequest() throws Exception {
        MyIpApp myIpApp = new MyIpApp();
        // 200 response
        String methodName = "handleRequest";
        MyIpRequest request = new MyIpRequest();
        request.authToken = "AuthTokenFromCloudFront";
        request.xForwardedFor = "ip1,ip2,ip3, ip4";
        request.userAgent = "userAgent";
        MyIpResponse response = myIpApp.handleRequest(request, null);
        Assert.assertEquals(200, response.httpStatus);
        Assert.assertEquals("ip2", response.myIp);
        // 403 response
        MyIpRequest request403 = new MyIpRequest();
        request403.authToken = "invalidToken";
        MyIpResponse response403 = myIpApp.handleRequest(request403, null);
        Assert.assertEquals(403, response403.httpStatus);
        // UserIpNotFound response
        MyIpRequest requestUserIpNotFound = new MyIpRequest();
        requestUserIpNotFound.authToken = "AuthTokenFromCloudFront";
        requestUserIpNotFound.xForwardedFor = "ip1,ip2";
        MyIpResponse responseUserIpNotFound = myIpApp.handleRequest(requestUserIpNotFound, null);
        Assert.assertTrue(StringUtils.startsWith(responseUserIpNotFound.myIp, "Cannot find IP from X-Forwarded-For:"));
    }
}