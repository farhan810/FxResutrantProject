package tblcheck.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuongdm5 on 1/10/2016.
 */
public class Table {
    public class RECT {
        public double top;
        public double left;
        public double width;
        public double height;
    }

    public enum Type {
        TABLE,
        WALL,
        LED,
        LABEL,
        STAIRS,
        STOOL,
        PLANT,
        ARROW
    }

    public enum State {
        READY,
        IN_USE,
        DONE,
        RESERVE,
        STATIC
    }

    public enum Variant {
        RECTANGLE,
        CIRCLE,
        VERTICAL,
        STOOL,
        HORIZONTAL;

        public static EnumSet<Variant> parseValue(String val) {
            if (val.equalsIgnoreCase("rectangle-in-circle")) {
                return EnumSet.of(RECTANGLE, CIRCLE);
            }
            if(val.equalsIgnoreCase("half-circle")){
            	return EnumSet.of(CIRCLE);
            }
            return EnumSet.of(valueOf(val.toUpperCase()));
        }
    }

    protected long lastChanged;
    protected State state;
    protected EnumSet<Variant> variants;

    protected int section;
    protected String text;
    protected String tParameter;
    protected double rotation;
    protected boolean border; // for label
    protected String id;
    protected Type type;
    protected double top, left, width, height;
    protected int steps; // for stair

    protected int fontSize;
    protected int fontWeight;
   
    
    protected String onlyCircle;
    protected String subType;
//    private static ArrayList<String> savedStaffList;
    private static Map<Integer,String> mapSavedStaffList;
    private static int staffCount=0;
    private static boolean callFromAssignee;
	
    	
    
    
    
  
    public static Map<Integer, String> getMapSavedStaffList() {
    	if(mapSavedStaffList == null){
			mapSavedStaffList = new HashMap<Integer,String>();
		}
    	return mapSavedStaffList;
	}

	public static void setMapSavedStaffList(Map<Integer, String> mapSavedStaffList) {
		Table.mapSavedStaffList = mapSavedStaffList;
	}

	public static boolean isCallFromAssignee() {
		return callFromAssignee;
	}

	public static void setCallFromAssignee(boolean callFromAssignee) {
		Table.callFromAssignee = callFromAssignee;
	}

	public static int getStaffCount() {
		return staffCount;
	}

	public static void setStaffCount(int staffCount) {
		Table.staffCount = staffCount;
	}

//	public ArrayList<String> getSavedStaffList() {
//		if(savedStaffList == null){
//			savedStaffList = new ArrayList<String>();
//		}
//    	return savedStaffList;
//	}
//
//	public void setSavedStaffList(ArrayList<String> savedStaffList) {
//		this.savedStaffList = savedStaffList;
//	}

	/**
	 * @return the subType
	 */
	public String getSubType() {
		return subType;
	}

	/**
	 * @param subType the subType to set
	 */
	public void setSubType(String subType) {
		this.subType = subType;
	}

	/**
	 * @return the onlyCircle
	 */
	public String getOnlyCircle() {
		return onlyCircle;
	}

	/**
	 * @param onlyCircle the onlyCircle to set
	 */
	public void setOnlyCircle(String onlyCircle) {
		this.onlyCircle = onlyCircle;
	}

	public Table() {
        this.setState(State.STATIC);
        this.variants = EnumSet.of(Variant.RECTANGLE);
        this.type = Type.WALL;
    }
//
//    public Table(double top, double left) {
//        this();
//        this.top = top;
//        this.left = left;
//    }

    public Table(String text, double top, double left, double width, double height, double rotation) {
        this();
        this.text = text;
        this.top = top;
        this.left = left;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }

    public Table(String text, double top, double left, double width, double height, double rotation, Variant var) {
        this(text, top, left, width, height, rotation);
        this.variants.add(var);
    }

    public Table(String text, double top, double left, double width, double height, double rotation, EnumSet<Variant> vars) {
        this(text, top, left, width, height, rotation);
        this.variants.addAll(vars);
    }

    public long getLastChanged() {
        return lastChanged;
    }

    private void setLastChanged(long lastChanged) {
        this.lastChanged = lastChanged;
    }

    public void setLastChanged() {
        setLastChanged(System.currentTimeMillis());
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        if (state != this.state)
            setLastChanged();
        this.state = state;
    }

    public EnumSet<Variant> getVariants() {
        return variants;
    }

    public void setVariants(EnumSet<Variant> variants) {
        this.variants = variants;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        if (section != this.section)
            setLastChanged();
        this.section = section;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTParameter() {
        return tParameter;
    }

    public void setTParameter(String tParameter) {
        this.tParameter = tParameter;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public boolean isBorder() {
        return border;
    }

    public void setBorder(boolean border) {
        this.border = border;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getTop() {
        return top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getFontWeight() {
        return fontWeight;
    }

    public void setFontWeight(int fontWeight) {
        this.fontWeight = fontWeight;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - lastChanged;
    }

    public String getColor() {
        switch (state) {
            case READY:
                return "GREEN";
            case IN_USE:
                return "GRAY";
            case DONE:
                return "RED";
            case RESERVE:
                return "YELLOW";
        }
        return "";
    }

    public String encode(boolean withSection) {
        final String format = withSection ? "%s=%s|%d " : "%s=%s";
        return String.format(format, tParameter, getColor().toLowerCase(), section);
    }
}
