package kalinchih.my_ip;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class App {

    public static void main(String[] args) throws IpNotFoundError {
        App app = new App();
        app.logServerIp();
    }

    private void logServerIp() throws IpNotFoundError {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println(String.format("Server Local IP: %s", inetAddress.getHostAddress()));
        } catch (UnknownHostException e) {
            throw new IpNotFoundError(e);
        }
    }
}
