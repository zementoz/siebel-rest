package sender.impl;

import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessServiceException;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import sender.Sender;

import java.util.Arrays;
import java.util.Enumeration;

public class GetSender implements Sender {
    private HttpClient httpClient;
    private HttpParams httpParams = new BasicHttpParams();

    public SiebelPropertySet send(SiebelPropertySet input) throws Exception {
        SiebelPropertySet output = new SiebelPropertySet();
        if (input.getProperty("URI") != null && !input.getProperty("URI").equals("")) {
            HttpGet request;
            if (input.getProperty("DynamicURI") != null && !input.getProperty("DynamicURI").equals("")) {
                request = new HttpGet(input.getProperty("URI") + input.getProperty("DynamicURI"));
            } else {
                request = new HttpGet(input.getProperty("URI"));
            }

            if (input.getProperty("Timeout") != null && !input.getProperty("Timeout").equals("")) {
                HttpConnectionParams.setConnectionTimeout(httpParams, Integer.valueOf(input.getProperty("Timeout")));
            } else {
                HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
            }

            if (input.getProperty("Content-Type") != null && !input.getProperty("Content-Type").equals("")) {
                request.setHeader("Content-type", input.getProperty("Content-Type"));
            }

            if (input.getProperty("Accept") != null && !input.getProperty("Accept").equals("")) {
                request.setHeader("Accept", input.getProperty("Accept"));
            }

            for (int i = 0; i < input.getChildCount(); i++) {
                if (input.getChild(i).getType().equals("Headers")) {
                    SiebelPropertySet headers = input.getChild(i);
                    Enumeration<String> headerNames = headers.getPropertyNames();
                    while (headerNames.hasMoreElements()) {
                        String headerName = headerNames.nextElement();
                        request.setHeader(headerName, headers.getProperty(headerName));
                    }
                }
            }

            httpClient = new DefaultHttpClient(httpParams);
            HttpResponse response = httpClient.execute(request);
            if (response.getHeaders("Content-Type")[0].getValue().equals("application/pdf"))
                output.setByteValue(IOUtils.toByteArray(response.getEntity().getContent()));
            else
                output.setValue(IOUtils.toString(response.getEntity().getContent()));
            output.setProperty("StatusCode", String.valueOf(response.getStatusLine().getStatusCode()));
            SiebelPropertySet responseHeaderProperties = new SiebelPropertySet();
            responseHeaderProperties.setType("Headers");
            for (Header header : Arrays.asList(response.getAllHeaders())) {
                responseHeaderProperties.setProperty(header.getName(), header.getValue());
            }
            output.addChild(responseHeaderProperties);
            return output;
        } else {
            throw new SiebelBusinessServiceException("SBL-JBS-002001", "Some parameters are missing");
        }
    }
}
