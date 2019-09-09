package tblcheck.helper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.tablecheck.service.model.PingResponse;
import com.tablecheck.service.model.RestaurantResource;
import com.tablecheck.service.model.TableResponse;

import tblcheck.model.Section;
import tblcheck.model.ResponseMessage.BaseResponseMessage;

/**
 * Created by snoy on 7/24/16.
 */
public class TypeHelper {
    public static final Type RESP_STR = new TypeToken<BaseResponseMessage<String>>(){}.getType();
    public static final Type RESP_ARR_SEC = new TypeToken<BaseResponseMessage<ArrayList<Section>>>(){}.getType();
    public static final Type RESP_SEC = new TypeToken<BaseResponseMessage<Section>>(){}.getType();
    public static final Type RESP_PING = new TypeToken<BaseResponseMessage<PingResponse>>(){}.getType();
    public static final Type RESP_RESOURCES = new TypeToken<BaseResponseMessage<RestaurantResource>>(){}.getType();
    public static final Type RESP_TABLES = new TypeToken<List<TableResponse>>(){}.getType();
    public static final Type RESP_MAP_BOOL = new TypeToken<Map<String, Boolean>>(){}.getType();
}
