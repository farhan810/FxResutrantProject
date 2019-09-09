package tblcheck.controller;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;

import com.google.gson.Gson;
import com.tablecheck.service.model.PingResponse;
import com.tablecheck.service.model.TableResponse;

import tblcheck.helper.HttpHelper;
import tblcheck.helper.TypeHelper;
import tblcheck.model.MessageRequest;
import tblcheck.model.MessageRequest.RequestCommand;
import tblcheck.model.ResponseMessage.BaseResponseMessage;

/**
 * Created by cuongdm5 on 1/19/2016.
 */
public class MessageTask implements Runnable {

    private static final Gson gson = new Gson();
    private LinkedList<MessageRequest> requests;
    private MessageRequest refreshReq;
    private INotifiable notifier;
    private boolean shouldExit = false;

    public MessageTask(INotifiable notifier) {
        this.requests = new LinkedList<MessageRequest>();
        this.notifier = notifier;
        shouldExit = true;
    }

    @Deprecated
    public synchronized void pushUpdate(String url, List<NameValuePair> data) {
        requests.add(new MessageRequest(url, RequestCommand.WRITE_TABLE, data));
        notifyAll();
    }

    @Deprecated
    public synchronized void pushRefresh(String url) {
        //System.out.println("Refresh is called");
        if (refreshReq == null || !refreshReq.getUrl().equals(url))
            refreshReq = new MessageRequest(url, RequestCommand.READ_TABLE);
        notifyAll();
    }

    @Deprecated
    public synchronized void pushRequest(MessageRequest req) {
        requests.add(req);
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        return requests.isEmpty();
    }

    private synchronized MessageRequest popUpdate() {
        if (requests.isEmpty())
            return null;
        return requests.pop();
    }

    public synchronized void flush() {
        requests.clear();
    }

    public synchronized void exit() {
        shouldExit = true;
        refreshReq = null;
        requests.clear();
        notifyAll();
    }

    @Override
    public void run() {
        while (!shouldExit) {
            synchronized (this) {
                while (refreshReq == null && requests.isEmpty())
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
            MessageRequest req;
            while ((req = popUpdate()) != null) {
                try {
                    RequestCommand type = req.getRequestCommand();
                    String data = null;
                    switch (type) {
                        case READ_STAFF:
                            data = HttpHelper.sendGet(req.getUrl());
                            break;
                        case READ_PUSHNOTIFICATION:
                            data = HttpHelper.sendGet(req.getUrl());
                            break;
                        case READ_STAFFNUMBERCHANGE:
                            data = HttpHelper.sendPost(req.getUrl(), req.getPostData());
                            break;
                        case READ_RESTROOM:
                            data = HttpHelper.sendGet(req.getUrl());
                            break;
                        case READ_MANAGER:
                            data = HttpHelper.sendGet(req.getUrl());
                            break;
                        case READ_STAFFSECTION:
                            data = HttpHelper.sendGet(req.getUrl());
                            break;
                        case WRITE_STAFF:
                            data = HttpHelper.sendPost(req.getUrl(), req.getStrData());
                            break;
                        case WRITE_TABLE:
                            data = HttpHelper.sendPost(req.getUrl(), req.getPostData());
                            break;
                        case WRITE_STAFF_LOG:
                            data = HttpHelper.sendPost(req.getUrl(), req.getPostData());
                            break;
                    }
                    if (data != null && notifier != null) {
                        notifier.notifySuccessRequest();
                        if (type != RequestCommand.READ_STAFF)
                            System.out.println("data: " + data);
                        BaseResponseMessage<String> resp = gson.fromJson(data, BaseResponseMessage.class);
                        notifier.onMessageResponse(resp, type);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    if (notifier != null) {
                        notifier.notifyFailRequest();
                    }
                }
            }
            synchronized (this) {
                if (refreshReq != null && notifier != null) {
                    //notify.messageFinish();
                    try {
                        String data = HttpHelper.sendGet(refreshReq.getUrl());
                        refreshReq = null;
                        notifier.notifySuccessRequest();
                        
                        PingResponse resp = gson.fromJson(data, TypeHelper.RESP_PING);
//                        List<TableResponse> resp = gson.fromJson(data, TypeHelper.RESP_PING);
                        
                        notifier.onMessageResponse(resp, RequestCommand.READ_RESOURCES);
                    } catch (Exception ex) {
                        System.out.println("ERROR");
                        notifier.notifyFailRequest();
                        ex.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

	public INotifiable getNotifier() {
		return notifier;
	}

	public boolean isShouldExit() {
		return shouldExit;
	}
}
