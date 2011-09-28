package osb.book.soap.test;

import java.net.URISyntaxException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;

import osb.book.soap.jms.HelloWorldService;
import osb.book.soap.jms.HelloWorldServiceSoapHttpPortBindingQSService;
import osb.book.soap.jms.HelloWorldServiceSoapHttpPortBindingQSService_Impl;

import weblogic.wsee.connection.transport.jms.JmsTransportInfo;

public class TestService {
    public static void main(String[] args) throws ServiceException,
                                                  URISyntaxException,
                                                  RemoteException {
  
        HelloWorldServiceSoapHttpPortBindingQSService service = 
            new HelloWorldServiceSoapHttpPortBindingQSService_Impl();
        HelloWorldService port = 
            service.getHelloWorldServiceSoapHttpPortBindingQSPort();

        String uri = "jms://laptopedwin:7021?URI=SOAPoverJMS";    
        JmsTransportInfo ti =  new JmsTransportInfo(uri);    
        ((Stub)port)._setProperty("weblogic.wsee.connection.transportinfo", ti);    
         
        try {
            String result = null;
            System.out.println("start");
            result = port.sayHello();
            System.out.println("Got JMS result: " + result);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
