//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessService;
import com.siebel.eai.SiebelBusinessServiceException;
import sender.impl.GetSender;
import sender.impl.PostSender;
import sender.impl.PutSender;

public class SiebelOutboundREST extends SiebelBusinessService {
    public SiebelOutboundREST() {
    }

    public void doInvokeMethod(String methodName, SiebelPropertySet input, SiebelPropertySet output) throws SiebelBusinessServiceException {
        SiebelPropertySet buffer;
        if (methodName.equals("SendPost")) {
            try {
                buffer = new PostSender().send(input);
                output.setProperty("StatusCode", buffer.getProperty("StatusCode"));
                if (buffer.getValue().equals(""))
                    output.setByteValue(buffer.getByteValue());
                else
                    output.setValue(buffer.getValue());
                output.addChild(buffer.getChild(0));
            } catch (Exception var8) {
                throw new SiebelBusinessServiceException("SBL-JBS-001001", "Error invoking POST method: " + var8.getMessage());
            }
        } else if (methodName.equals("SendGet")) {
            try {
                buffer = new GetSender().send(input);
                output.setProperty("StatusCode", buffer.getProperty("StatusCode"));
                if (buffer.getValue().equals(""))
                    output.setByteValue(buffer.getByteValue());
                else
                    output.setValue(buffer.getValue());
                output.addChild(buffer.getChild(0));
            } catch (Exception var7) {
                throw new SiebelBusinessServiceException("SBL-JBS-001002", "Error invoking GET method: " + var7.getMessage());
            }
        } else if (methodName.equals("SendPut")) {
            try {
                buffer = new PutSender().send(input);
                output.setProperty("StatusCode", buffer.getProperty("StatusCode"));
                if (buffer.getValue().equals(""))
                    output.setByteValue(buffer.getByteValue());
                else
                    output.setValue(buffer.getValue());
                output.addChild(buffer.getChild(0));
            } catch (Exception var6) {
                throw new SiebelBusinessServiceException("SBL-JBS-001003", "Error invoking PUT method: " + var6.getMessage());
            }
        } else {
            throw new SiebelBusinessServiceException("SBL-JBS-001004", "Error invoking " + methodName + " method: Method doesn`t exist");
        }
    }
}