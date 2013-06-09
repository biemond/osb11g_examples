package nl.amis.mw.reporting.model;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
  @NamedQuery(name = "MessageDetails.findAll", query = "select o from MessageDetails o")
})
@Table(name = "MESSAGE_DETAILS")
public class MessageDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name="DETAIL_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date detailDate;
    @Column(name="ERROR_CODE", length = 64)
    private String errorCode;
    @Column(name="ERROR_DETAILS", length = 2048)
    private String errorDetails;
    @Column(name="ERROR_REASON", length = 1024)
    private String errorReason;

    @Column(name="COMPONENT_IDENTIFIER", length = 40)
    private String componentIdentifier;

    @Id
    @SequenceGenerator(sequenceName = "MESSAGE_DETAILS_SEQ", name="MESSAGE_DETAILS_SEQ" , allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MESSAGE_DETAILS_SEQ")
    @Column(name = "ID")
    private Long id;


    @Column(name="INBOUND_OPERATION", length = 64)
    private String inboundOperation;
    @Column(name="INBOUND_SERVICE_NAME", length = 256)
    private String inboundServiceName;
    @Column(name="INBOUND_SERVICE_URI", length = 128)
    private String inboundServiceUri;

    @Column(name="MSG_LABELS", length = 2048)
    private String msgLabels;
    @Column(length = 128)
    private String node;
    @Column(name="OUTBOUND_OPERATION", length = 64)
    private String outboundOperation;
    @Column(name="OUTBOUND_SERVICE_NAME", length = 256)
    private String outboundServiceName;
    @Column(name="OUTBOUND_SERVICE_URI", length = 256)
    private String outboundServiceUri;
    @Column(name="PAYLOAD_BINAIR")
    @Lob
    private byte[] payloadBinair;
    @Column(name="PAYLOAD_STRING")
    @Lob
    private String payloadString;
    @Column(name="PIPELINE_NAME", length = 128)
    private String pipelineName;
    @Column(name="STAGE_NAME", length = 128)
    private String stageName;
    @Column(length = 8)
    private String state;

    @ManyToOne(cascade = {  CascadeType.REFRESH})
    @JoinColumn(name = "MESSAGE_ID")
    private Message message;

    public MessageDetails() {
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInboundOperation() {
        return inboundOperation;
    }

    public void setInboundOperation(String inboundOperation) {
        this.inboundOperation = inboundOperation;
    }

    public String getInboundServiceName() {
        return inboundServiceName;
    }

    public void setInboundServiceName(String inboundServiceName) {
        this.inboundServiceName = inboundServiceName;
    }

    public String getInboundServiceUri() {
        return inboundServiceUri;
    }

    public void setInboundServiceUri(String inboundServiceUri) {
        this.inboundServiceUri = inboundServiceUri;
    }

    public String getMsgLabels() {
        return msgLabels;
    }

    public void setMsgLabels(String msgLabels) {
        this.msgLabels = msgLabels;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getOutboundOperation() {
        return outboundOperation;
    }

    public void setOutboundOperation(String outboundOperation) {
        this.outboundOperation = outboundOperation;
    }

    public String getOutboundServiceName() {
        return outboundServiceName;
    }

    public void setOutboundServiceName(String outboundServiceName) {
        this.outboundServiceName = outboundServiceName;
    }

    public String getOutboundServiceUri() {
        return outboundServiceUri;
    }

    public void setOutboundServiceUri(String outboundServiceUri) {
        this.outboundServiceUri = outboundServiceUri;
    }

    public byte[] getPayloadBinair() {
        return payloadBinair;
    }

    public void setPayloadBinair(byte[] payloadBinair) {
        this.payloadBinair = payloadBinair;
    }

    public String getPayloadString() {
        return payloadString;
    }

    public void setPayloadString(String payloadString) {
        this.payloadString = payloadString;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public void setDetailDate(Date detailDate) {
        this.detailDate = detailDate;
    }

    public Date getDetailDate() {
        return detailDate;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setComponentIdentifier(String componentIdentifier) {
        this.componentIdentifier = componentIdentifier;
    }

    public String getComponentIdentifier() {
        return componentIdentifier;
    }
}
