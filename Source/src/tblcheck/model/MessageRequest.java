package tblcheck.model;

import java.util.List;

import org.apache.http.NameValuePair;

/**
 * Created by cuongdm5 on 1/18/2016.
 */
public class MessageRequest {
    public enum RequestCommand {
        READ_TABLE, WRITE_TABLE,
        READ_STAFF, WRITE_STAFF,
        WRITE_STAFF_LOG, READ_RESTROOM,
        READ_MANAGER, READ_STAFFSECTION,
        READ_PUSHNOTIFICATION, READ_STAFFNUMBERCHANGE, READ_RESOURCES
    }

    private String url;
    private RequestCommand requestCommand;
    private List<NameValuePair> postData;
    private String strData;

    public MessageRequest(String url) {
        this(url, RequestCommand.READ_TABLE);
    }

    public MessageRequest(String url, RequestCommand reqType) {
        this.url = url;
        this.requestCommand = reqType;
//        this.postData = new ArrayList<>();
    }

    public MessageRequest(String url, RequestCommand reqType, List<NameValuePair> data) {
        this.url = url;
        this.requestCommand = reqType;
        this.postData = data;
    }

    public MessageRequest(String url, RequestCommand reqType, String data) {
        this.url = url;
        this.requestCommand = reqType;
        this.strData = data;
    }

    public String getUrl() {
        return this.url;
    }

    public RequestCommand getRequestCommand() {
        return this.requestCommand;
    }

    public List<NameValuePair> getPostData() {
        return postData;
    }

    public String getStrData(){return strData;}
}
