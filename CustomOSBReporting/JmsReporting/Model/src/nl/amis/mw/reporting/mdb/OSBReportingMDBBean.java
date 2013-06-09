package nl.amis.mw.reporting.mdb;

import com.bea.wli.reporting.jmsprovider.runtime.ReportMessage;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import javax.jms.ObjectMessage;

import nl.amis.mw.reporting.services.OSBReportingSessionEJBLocal;

@MessageDriven(
    name = "OSBReportingMDBBean",
    activationConfig = {
      @ActivationConfigProperty(propertyName  = "destinationType", 
                                propertyValue = "javax.jms.Queue"),
      @ActivationConfigProperty(propertyName  = "connectionFactoryJndiName",
                                propertyValue = "wli.reporting.jmsprovider.ConnectionFactory"), 
      @ActivationConfigProperty(propertyName  = "destinationJndiName",
                                propertyValue = "wli.reporting.jmsprovider.queue") ,
      @ActivationConfigProperty(propertyName  ="initialContextFactory", 
                                propertyValue ="weblogic.jndi.WLInitialContextFactory"),
      @ActivationConfigProperty(propertyName  = "acknowledgeMode", 
                                propertyValue = "Auto-acknowledge"),

    }
)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)

public class OSBReportingMDBBean implements MessageListener {

    @EJB
    private OSBReportingSessionEJBLocal reportingService;

    public void onMessage(Message message) {

        ObjectMessage objMsg = (ObjectMessage)message;
        try {
            int dataType = message.getIntProperty("REPORTINGDATATYPE");
            ReportMessage repMessage = (ReportMessage)objMsg.getObject();
            reportingService.procesReportMessage(repMessage, dataType);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        
    }
}
