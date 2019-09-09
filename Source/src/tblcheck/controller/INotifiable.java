package tblcheck.controller;

import tblcheck.model.MessageRequest;
import tblcheck.model.ResponseMessage.BaseResponseMessage;

/**
 * Created by cuongdm5 on 1/19/2016.
 */
public interface INotifiable {
	void onMessageResponse(Object resp, MessageRequest.RequestCommand reqCmd);
    void onMessageResponse(BaseResponseMessage resp, MessageRequest.RequestCommand reqCmd);
    void notifyFailRequest();
    void notifySuccessRequest();
}