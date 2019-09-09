package tblcheck.helper;

import java.lang.reflect.Type;
import java.util.EnumSet;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import javafx.scene.text.FontWeight;
import tblcheck.model.Table;

/**
 * Created by cuongdm5 on 1/11/2016.
 */
public class LayoutDeserializer implements JsonDeserializer<Table>, JsonSerializer<Table> {

	static String tryGetString(JsonObject obj, String member) {
		return obj.has(member) ? obj.get(member).getAsString() : null;
	}

	static int tryGetInt(JsonObject obj, String member) {
		return tryGetInt(obj, member, 0);
	}

	static int tryGetInt(JsonObject obj, String member, int init) {
		if (obj.has(member)) {
			try {
				return obj.get(member).getAsInt();
			} catch (Exception e) {
			}
		}
		return init;
	}

	static boolean tryGetBoolean(JsonObject obj, String member, boolean def) {
		return obj.has(member) ? obj.get(member).getAsBoolean() : def;
	}

	@Override
	public Table deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext ctext)
			throws JsonParseException {
		Table table = null;
		try {
			JsonObject obj = jsonElement.getAsJsonObject();
			String tpe = tryGetString(obj, "type");
			String id = tryGetString(obj, "id");

			String text = tryGetString(obj, "label");
			if (text == null)
				text = tryGetString(obj, "text");
			int rotation = tryGetInt(obj, "rotation");
			String tParam = tryGetString(obj, "t-parameter");
			int top = tryGetInt(obj, "top");
			int left = tryGetInt(obj, "left");
			int width = tryGetInt(obj, "width");
			int height = tryGetInt(obj, "height");
			if (top < 0 || left < 0)
				return null;
			boolean border = tryGetBoolean(obj, "border", false);
			int fontSize = tryGetInt(obj, "font-size", 14);
			String fontWeight = tryGetString(obj, "font-weight");
			int steps = tryGetInt(obj, "steps", 3);

			String onlyCircle = tryGetString(obj, "onlyCircle");
			if (onlyCircle == null) {
				onlyCircle = "noCircle";
			}

			String subType = tryGetString(obj, "subType");
			if (subType == null) {
				subType = "noStool";
			}

			table = new Table(text, top, left, width, height, rotation);
			// table = new Table(id, top, left, width, height, rotation);

			table.setOnlyCircle(onlyCircle);
			table.setSubType(subType);

			table.setId(id);
			table.setTParameter(tParam);
			table.setBorder(border);
			table.setFontSize(fontSize);
			table.setSteps(steps);
			// table.setFontWeight(fontWeight.equals("bold") ?
			// FontWeight.BOLD.getWeight() : FontWeight.NORMAL.getWeight());
			if (fontWeight != null && !"".equals(fontWeight)) {
				table.setFontWeight(FontWeight.valueOf(fontWeight.toUpperCase()).getWeight());
			} else {
				table.setFontWeight(FontWeight.NORMAL.getWeight());
			}
			table.setType(Table.Type.valueOf(tpe.toUpperCase()));
			if (table.getType() == Table.Type.TABLE && !table.getSubType().equalsIgnoreCase("stool")) {
				table.setState(Table.State.READY);
			}

			if (table.getType() == Table.Type.TABLE && table.getSubType().equalsIgnoreCase("stool")) {
				table.setState(Table.State.STATIC);
			}

			if (table.getType() == Table.Type.STOOL || table.getType() == Table.Type.STAIRS) {
				table.setState(Table.State.STATIC);
			}
			// if (table.getType() == Table.Type.LABEL){
			// table.setState(Table.State.STATIC);
			// }

			String vars = tryGetString(obj, "variant");
			if (vars != null && !vars.isEmpty()) {
				table.getVariants().clear();
				for (String var : vars.split(",")) {
					table.getVariants().addAll(Table.Variant.parseValue(var.toUpperCase()));
					if (var.equalsIgnoreCase("rectangle-in-circle")) {
						table.setOnlyCircle("diamond");
					} else if (var.equalsIgnoreCase("half-circle")) {
						table.setOnlyCircle("arc");
					} else if (var.equalsIgnoreCase("circle")) {
						table.setOnlyCircle("circle");
					}
				}
			}
			// if (table.getType() == Table.Type.STOOL){//LABEL &&
			// table.getVariants().contains(Table.Variant.CIRCLE)) {
			// table.getVariants().add(Table.Variant.CIRCLE);
			//// table.setBorder(false);
			// }

		} catch (Exception ex) {
			System.out.println(jsonElement.toString());
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		return table;
	}

	public Table serialize(JsonElement jsonElement, Type type, JsonDeserializationContext ctext)
			throws JsonParseException {
		Table table = null;
		try {
			JsonObject obj = jsonElement.getAsJsonObject();
			String tpe = tryGetString(obj, "type");
			String id = tryGetString(obj, "id");
			// if (id.equalsIgnoreCase("oT61Div"))
			// System.out.println(id);
			String text = tryGetString(obj, "label");
			if (text == null)
				text = tryGetString(obj, "text");
			int rotation = tryGetInt(obj, "rotation");
			String tParam = tryGetString(obj, "t-parameter");
			int top = tryGetInt(obj, "top");
			int left = tryGetInt(obj, "left");
			int width = tryGetInt(obj, "width");
			int height = tryGetInt(obj, "height");
			if (top < 0 || left < 0)
				return null;
			boolean border = tryGetBoolean(obj, "border", false);
			int fontSize = tryGetInt(obj, "font-size", 14);
			String fontWeight = tryGetString(obj, "font-weight");
			int steps = tryGetInt(obj, "steps", 3);

			String onlyCircle = tryGetString(obj, "onlyCircle");
			if (onlyCircle == null) {
				onlyCircle = "noCircle";
			}

			String subType = tryGetString(obj, "subType");
			if (subType == null) {
				subType = "noStool";
			}

			table = new Table(text, top, left, width, height, rotation);
			// table = new Table(id, top, left, width, height, rotation);

			table.setOnlyCircle(onlyCircle);
			table.setSubType(subType);

			table.setId(id);
			table.setTParameter(tParam);
			table.setBorder(border);
			table.setFontSize(fontSize);
			table.setSteps(steps);
			// table.setFontWeight(fontWeight.equals("bold") ?
			// FontWeight.BOLD.getWeight() : FontWeight.NORMAL.getWeight());
			if (fontWeight != null)
				table.setFontWeight(FontWeight.valueOf(fontWeight.toUpperCase()).getWeight());

			table.setType(Table.Type.valueOf(tpe.toUpperCase()));
			if (table.getType() == Table.Type.TABLE && !table.getSubType().equalsIgnoreCase("stool")) {
				table.setState(Table.State.READY);
			}

			if (table.getType() == Table.Type.TABLE && table.getSubType().equalsIgnoreCase("stool")) {
				table.setState(Table.State.STATIC);
			}

			// if (table.getType() == Table.Type.LABEL){
			// table.setState(Table.State.STATIC);
			// }

			String vars = tryGetString(obj, "variant");
			if (vars != null && !vars.isEmpty())
				for (String var : vars.split(",")) {
					table.getVariants().addAll(Table.Variant.parseValue(var.toUpperCase()));
					if (var.equalsIgnoreCase("rectangle-in-circle")) {
						table.setOnlyCircle("diamond");
					} else if (var.equalsIgnoreCase("half-circle")) {
						table.setOnlyCircle("arc");
					}
				}

		} catch (Exception ex) {
			System.out.println(jsonElement.toString());
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		return table;
	}

	@Override
	public JsonElement serialize(Table arg0, Type arg1, JsonSerializationContext arg2) {
		JsonElement jsonElement = arg2.serialize(arg0, arg1);

		return jsonElement;
	}
}
