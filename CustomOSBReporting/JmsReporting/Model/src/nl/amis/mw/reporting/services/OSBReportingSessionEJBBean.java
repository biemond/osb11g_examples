package nl.amis.mw.reporting.services;

import com.bea.wli.reporting.EndpointType;
import com.bea.wli.reporting.FaultType;
import com.bea.wli.reporting.MessageContextType;
import com.bea.wli.reporting.MessagecontextDocument;
import com.bea.wli.reporting.OriginType;
import com.bea.wli.reporting.ServiceType;
import com.bea.wli.reporting.jmsprovider.runtime.ReportMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.UnsupportedEncodingException;

import java.util.Calendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.xmlbeans.XmlOptions;

import nl.amis.mw.reporting.model.MessageDetails;
import nl.amis.mw.reporting.model.Message;


@Stateless(name = "OSBReportingSessionEJB", mappedName = "JmsReporting-Model-OSBReportingSessionEJB")
public class OSBReportingSessionEJBBean implements OSBReportingSessionEJBLocal {

    @PersistenceContext(unitName="mwoperations")
    private EntityManager em;

    public OSBReportingSessionEJBBean() {
    }
  
    public void procesReportMessage( ReportMessage repMessage, int dataType ) {
        MessagecontextDocument reportDoc = (MessagecontextDocument)repMessage.getMetadata();

        String contentencoding = reportDoc.getMessagecontext().getContentEncoding();

        byte[] binData = null;
        InputStream in = null;

        if (dataType == 1) {
          if ((repMessage.getStrPayload() != null) && (repMessage.getStrPayload().length() > 0)) {
              if ((contentencoding != null) && (contentencoding.length() > 0)) {
                try {
                    binData = repMessage.getStrPayload().getBytes(contentencoding);
                } catch (UnsupportedEncodingException e) {
                  e.printStackTrace();
                }
              } else {
                 binData = repMessage.getStrPayload().getBytes();
              }    
          }
        }
        else if (dataType == 2) {
          if (repMessage.getXmlPayload() != null) {
            if ((contentencoding != null) && (contentencoding.length() > 0)) {
              XmlOptions xmlOptions = new XmlOptions();
              xmlOptions.setCharacterEncoding(contentencoding);
              in = repMessage.getXmlPayload().newInputStream(xmlOptions);
            } else {
              in = repMessage.getXmlPayload().newInputStream();
            }

            try {
               binData = copyInputStreamtoByteArray(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        } else if ((dataType == 3) && (repMessage.getBinPayload() != null))
          binData = repMessage.getBinPayload();

        MessageContextType msgContextType = reportDoc.getMessagecontext();
        Calendar localhost_TS = msgContextType.getTimestamp();
        String msgLabels = msgContextType.getLabels();


        OriginType originType = msgContextType.getOrigin();
        String state = originType.getState().toString();
        String node = originType.getNode();
        String pipeline_Name = originType.getPipeline();
        String stage_Name = originType.getStage();

        String inbound_SvcName = msgContextType.getInboundEndpoint().getName();

        ServiceType svctype = msgContextType.getInboundEndpoint().getService();
         String inbound_SvcOperation = null;
         if (svctype != null) {
           inbound_SvcOperation = svctype.getOperation();
         }

         String inbound_SvcURI = msgContextType.getInboundEndpoint().getTransport().getUri();

         EndpointType outbound_Endpoint = msgContextType.getOutboundEndpoint();
         String outbound_SvcName = null;
         String outbound_SvcOperation = null;
         String outbound_SvcURI = null;

         if (outbound_Endpoint != null) {
           outbound_SvcName = outbound_Endpoint.getName();
           if (outbound_Endpoint.getService() != null) {
             outbound_SvcOperation = outbound_Endpoint.getService().getOperation();
           }
           if (outbound_Endpoint.getTransport() != null) {
             outbound_SvcURI = outbound_Endpoint.getTransport().getUri();
           }
         }

         FaultType fault = msgContextType.getFault();
         String error_Code = null;
         String error_Reason = null;
         String error_details = null;

         if (fault != null) {
           error_Code = fault.getErrorCode();

           if ((fault.getReason() != null) && (fault.getReason().length() > 0)) {
             error_Reason = fault.getReason();
           }
           if (fault.getDetails() != null) {
             error_details = fault.getDetails().xmlText();
           }
         }

        // store the reporting data

        String conversationIdentifier = null;
        final String conversationIdentifierKey = "conversationidentifier";
        String componentIdentifier = null;
        final String componentIdentifierKey = "componentidentifier";
        String messageType = null;
        final String messageTypeKey = "messagetype";
        String sender = null;
        final String senderKey = "sender";
        String receiver = null;
        final String receiverKey = "receiver";
        
        final String errorState = "ERROR";

        Map<String, String> keys = null;
        if ( msgLabels != null ) {
            keys = getMsgKey(msgLabels);
        }
        List<Message> foundInstances = null;
        // check if the conversationId already exists
        if ( keys != null &&  keys.get(conversationIdentifierKey) != null){
            conversationIdentifier =  keys.get(conversationIdentifierKey);
            if (conversationIdentifier != null && !"".equalsIgnoreCase(conversationIdentifier))
              foundInstances = em.createNamedQuery("Message.findByConversationIdentifer")
                    .setParameter("conversationIdentifier", conversationIdentifier).getResultList();             
        }     
 
        // component or proxy messageID
        if ( keys != null &&  keys.get(componentIdentifierKey) != null)
            componentIdentifier = keys.get(componentIdentifierKey);    

        if ( keys != null &&  keys.get(messageTypeKey) != null)
            messageType = keys.get(messageTypeKey);     

        if ( keys != null &&  keys.get(senderKey) != null)
            sender = keys.get(senderKey);                     

        if ( keys != null &&  keys.get(receiverKey) != null)
            receiver = keys.get(receiverKey);     
        
        Message message = null;
        // hergebruik instance of maak een nieuwe aan
        if ( foundInstances != null && foundInstances.size() > 0 ) {
          message = foundInstances.get(0);
        } else {
          message = new Message();         
          if ( localhost_TS != null )
            message.setMessageDate(localhost_TS.getTime()) ;   

        }
        if ( message.getMessageType() == null && messageType != null )
          message.setMessageType(messageType);


        if ( message.getConversationIdentifier() == null && conversationIdentifier != null )
           message.setConversationIdentifier(conversationIdentifier);             

        if ( message.getSender() == null && sender != null )
           message.setSender(sender); 

        if ( message.getReceiver() == null && receiver != null )
             message.setReceiver(receiver); 
        
        if ( error_Code != null && !"".equalsIgnoreCase(error_Code) )
          message.setState(errorState);  

        message = em.merge(message);
        em.flush();
        
        
        MessageDetails messageDetail = new MessageDetails();

        if ( localhost_TS != null )
          messageDetail.setDetailDate(localhost_TS.getTime()) ;   

        // payload
        if ( binData != null ){  
            if (dataType == 3 ) {
                messageDetail.setPayloadBinair(binData);
            }
            else {
                messageDetail.setPayloadString(new String(binData));
            }
        }

        if (  componentIdentifier != null )
           messageDetail.setComponentIdentifier(componentIdentifier); 

        messageDetail.setState(state);
        messageDetail.setNode(truncate(node, 128));
        messageDetail.setPipelineName(truncate(pipeline_Name, 128));
        messageDetail.setStageName(truncate(stage_Name, 128));
        messageDetail.setInboundServiceName(truncate(inbound_SvcName, 256));
        messageDetail.setInboundOperation(truncate(inbound_SvcOperation, 64));
        messageDetail.setInboundServiceUri(truncate(inbound_SvcURI, 128));
        messageDetail.setOutboundOperation(truncate(outbound_SvcOperation, 64));
        messageDetail.setOutboundServiceName(truncate(outbound_SvcName, 256)); 
        messageDetail.setOutboundServiceUri(truncate(outbound_SvcURI, 256));
        messageDetail.setErrorCode(truncate(error_Code, 64));
        messageDetail.setErrorReason(truncate(error_Reason, 1024));
        messageDetail.setErrorDetails(truncate(error_details, 2048));
        messageDetail.setMsgLabels(truncate(msgLabels, 2048));
        // set Mutual references
        messageDetail.setMessage(message);
        em.persist(messageDetail);
        em.flush();

    }

    private byte[] copyInputStreamtoByteArray(InputStream in) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      byte[] buffer = new byte[4096];
      int n;
      while ((n = in.read(buffer)) > 0) {
        baos.write(buffer, 0, n);
      }
      byte[] data = baos.toByteArray();
      in.close();
      baos.flush();
      baos.close();
      return data;
    }

    private Map<String, String> getMsgKey(String input) {
        Map<String, String> m = new HashMap<String, String>();
        for (String s : input.split(";")) {
            s = s.trim();
            int valstart = s.indexOf("=");
            String value   = s.substring(valstart+1);
            String key   = s.substring(0,valstart);
            m.put(key.toLowerCase(), value);
        }
        return m;
    }


    private String truncate(String data, int length) {
      if ((data != null) && (data.length() > length)) {
        return data.substring(0, length - 1);
      }
      return data;
    }

}
