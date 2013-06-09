package nl.amis.mw.reporting.services;

import com.bea.wli.reporting.jmsprovider.runtime.ReportMessage;

import javax.ejb.Local;



@Local
public interface OSBReportingSessionEJBLocal {

    void procesReportMessage ( ReportMessage repMessage, int dataType );
}
