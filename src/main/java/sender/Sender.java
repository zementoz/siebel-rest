package sender;

import com.siebel.data.SiebelPropertySet;

public interface Sender {
    SiebelPropertySet send(SiebelPropertySet input) throws Exception;
}
