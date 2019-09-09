package tblcheck.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import tblcheck.model.Config;
import tblcheck.model.Table;

/**
 * Created by cuongdm5 on 1/25/2016.
 */
public class LayoutHelper {

	public static final int TABLE_PADDING = 12;

	public static List<Table> getTables(String fileName) {
		List<Table> tables = null;
		Gson gson = new GsonBuilder().registerTypeAdapter(Table.class, new LayoutDeserializer()).serializeNulls()
				.create();
		Type type = new TypeToken<ArrayList<Table>>() {
		}.getType();
		try {
			// File f = Paths.get(LAYOUT_FOLDER_NAME, fileName).toFile();
			// System.out.println("File exist: " + f.exists());
			BufferedReader br = new BufferedReader(
					new FileReader(Paths.get(Config.getInstance().getLayoutFolder(), fileName).toFile()));
			// System.out.println(br.readLine());
			tables = gson.fromJson(br, type);
			tables.removeIf(tb -> tb == null);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		return tables;
	}

	public static List<Table> putTables(String fileName, List<Table> tables) {
		Gson gson = new GsonBuilder().registerTypeAdapter(Table.class, new LayoutDeserializer()).serializeNulls()
				.create();
		Type type = new TypeToken<ArrayList<Table>>() {
		}.getType();
		try {
			BufferedWriter br = new BufferedWriter(
					new FileWriter(new File(Paths.get(Config.getInstance().getLayoutFolder(), fileName).toString())));
			String tab = gson.toJson(tables, type);
			br.write(tab);
			br.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		return tables;
	}

	public static void reScaleTableLayout(List<Table> tables) {
		if (tables == null || tables.isEmpty())
			return;
		int left = Integer.MAX_VALUE, top = Integer.MAX_VALUE;
		for (Table tbl : tables) {
			if (tbl.getLeft() < left)
				left = (int) tbl.getLeft();
			if (tbl.getTop() < top)
				top = (int) tbl.getTop();
			if (tbl.getType() == Table.Type.TABLE) {
				tbl.setWidth(tbl.getWidth() + TABLE_PADDING);
				tbl.setHeight(tbl.getHeight() + TABLE_PADDING);
			}
		}
		if (top > 10)
			top -= 10;
		else if (top < 10)
			top = 10 - top;
		if (left > 10)
			left -= 10;
		else if (left < 10)
			left = 10 - left;
		Config.getInstance().setLayoutLeftAdjusted(left);
		Config.getInstance().setLayoutTopAdjusted(top);
		for (Table tbl : tables) {
			tbl.setLeft(tbl.getLeft() - left);
			tbl.setTop(tbl.getTop() - top);
		}
	}

	public static void clearTableLayout(List<Table> tables) {
		if (tables == null || tables.isEmpty())
			return;
		int left = Integer.MAX_VALUE, top = Integer.MAX_VALUE;
		for (Table tbl : tables) {
			if (tbl.getLeft() < left)
				left = (int) tbl.getLeft();
			if (tbl.getTop() < top)
				top = (int) tbl.getTop();
			if (tbl.getType() == Table.Type.TABLE) {
				tbl.setWidth(tbl.getWidth() + TABLE_PADDING);
				tbl.setHeight(tbl.getHeight() + TABLE_PADDING);
			}
		}
		if (top > 10)
			top -= 10;
		else if (top < 10)
			top = 10 - top;
		if (left > 10)
			left -= 10;
		else if (left < 10)
			left = 10 - left;

		for (Table tbl : tables) {
			tbl.setLeft(tbl.getLeft() - left);
			tbl.setTop(tbl.getTop() - top);
		}
	}

	public static void sortTables(List<Table> tables) {
		int min = 0;
		for (int i = 0; i < tables.size() - 1; i++) {
			if (tables.get(i).getType() != Table.Type.TABLE)
				continue;
			min = i;
			int tVal = Integer.parseInt(tables.get(min).getTParameter().substring(1));
			for (int j = i + 1; j < tables.size(); j++) {
				if (tables.get(j).getType() != Table.Type.TABLE)
					continue;
				int tP = Integer.parseInt(tables.get(j).getTParameter().substring(1));
				if (tP < tVal) {
					min = j;
					tVal = tP;
				}
			}
			if (min != i) {
				Table temp = tables.get(i);
				tables.set(i, tables.get(min));
				tables.set(min, temp);
			}
		}
	}

	public static String[] getLayoutFiles() {
		File f = new File(Config.getInstance().getLayoutFolder());
		return f.exists() ? f.list() : null;
	}
}
