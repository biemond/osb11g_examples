package nl.amis.mw.reporting.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
  @NamedQuery(name = "Message.findAll"    , query = "select o from Message o"),
  @NamedQuery(name = "Message.findByConversationIdentifer", query = "select o from Message o where o.conversationIdentifier = :conversationIdentifier")
})
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MESSAGE_SEQ")
    @SequenceGenerator(sequenceName = "MESSAGE_SEQ", name="MESSAGE_SEQ", allocationSize = 1)
    private Long id;

    @Column(name="MESSAGE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date messageDate;


    @Column(name="MESSAGE_TYPE", length = 100)
    private String messageType;

    @Column(name="CONVERSATION_IDENTIFIER", length = 40)
    private String conversationIdentifier;

    @Column(name="STATE")
    private String state = "OK";

    @Column(name="SENDER", length = 200)
    private String sender;

    @Column(name="RECEIVER", length = 200)
    private String receiver;

    @OneToMany(mappedBy = "message", cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE })
    private List<MessageDetails> messageDetails;

    public Message() {
    }


    public List<MessageDetails> getMessageDetailsList() {
        if ( messageDetails == null ) {
            messageDetails = new ArrayList<MessageDetails>();
        }
        return messageDetails;
    }

    public void setMessageDetailsList(List<MessageDetails> messageDetails) {
        this.messageDetails = messageDetails;
    }

    public MessageDetails addMessageDetailsList(MessageDetails messageDetails) {
        getMessageDetailsList().add(messageDetails);
        messageDetails.setMessage(this);
        return messageDetails;
    }

    public MessageDetails removeMessageDetails(MessageDetails messageDetails) {
        getMessageDetailsList().remove(messageDetails);
        messageDetails.setMessage(null);
        return messageDetails;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setConversationIdentifier(String conversationIdentifier) {
        this.conversationIdentifier = conversationIdentifier;
    }

    public String getConversationIdentifier() {
        return conversationIdentifier;
    }
}
