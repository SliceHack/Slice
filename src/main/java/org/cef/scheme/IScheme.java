package org.cef.scheme;

import org.cef.browser.scheme.SchemePreResponse;
import org.cef.browser.scheme.SchemeResponseData;
import org.cef.browser.scheme.SchemeResponseHeaders;

/**
 * @author montoyo
 */
public interface IScheme {

    SchemePreResponse processRequest(String url);

    void getResponseHeaders(SchemeResponseHeaders resp);

    boolean readResponse(SchemeResponseData data);

}
