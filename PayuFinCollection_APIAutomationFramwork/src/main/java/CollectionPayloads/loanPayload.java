package CollectionPayloads;

import AppBase.baseClass;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashMap;
import java.util.Map;

public class loanPayload extends baseClass {

    public static String postPaymentBody(String reference_number, int amount, String payment_mode, int loan_id, String payment_type){
        String body="";
        Map<String, Object> pobj = new HashMap<String,Object>();
        pobj.put("reference_number",reference_number);
        pobj.put("processed_at",1640086683);
        pobj.put("remarks","Collection Automation Testing");
        pobj.put("amount",amount);
        pobj.put("payment_type",payment_type);
        pobj.put("state","success");
        pobj.put("loan",loan_id);
        pobj.put("source","gateway");
        pobj.put("payment_mode",payment_mode);
        try {
           body =  mapper.writeValueAsString(pobj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return body;

    }
}
