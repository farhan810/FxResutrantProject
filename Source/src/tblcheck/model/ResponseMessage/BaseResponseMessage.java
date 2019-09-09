package tblcheck.model.ResponseMessage;

/**
 * Created by cuongdm5 on 1/24/2016.
 */
public class BaseResponseMessage<T> {

    public static final String STT_ERROR = "error";
    public static final String STT_OK = "ok";

    private String status;
    private T data;

    public String getStatus() {
        return this.status;
    }

    public T getData() {
        return this.data;
    }

    public BaseResponseMessage(){
    }
}
