package tblcheck.model.ResponseMessage;

/**
 * Created by snoy on 7/24/16.
 */
public class TableResponse {
    private String tParam;
    private String color;
    private int section;

    public String getTParam() {
        return tParam;
    }

    public String getColor() {
        return color;
    }

    public int getSection() {
        return section;
    }

    public TableResponse(String tParam, String color, int section){
        this.tParam = tParam;
        this.color = color;
        this.section = section;
    }
}
