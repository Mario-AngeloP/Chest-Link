package de.srsuders.chestlink.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 09.04.2021 Project: chestlink
 */
public class Serialize {

	public static Document fromItemStackToDocument(final ItemStack is) {
		final Map<String, Object> map = is.serialize();
		final Document doc = new Document();
		for (Entry<String, Object> values : map.entrySet()) {
			doc.put(values.getKey(), values.getValue());
		}
		return doc;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static ItemMeta deserializeItemMeta(Map<String, Object> map) {
		ItemMeta meta = null;
		try {
			Class[] craftMetaItemClasses = Class.forName("org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaItem")
					.getDeclaredClasses();
			for (Class craftMetaItemClass : craftMetaItemClasses) {
				if (!craftMetaItemClass.getSimpleName().equals("SerializableMeta"))
					continue;
				Method deserialize = craftMetaItemClass.getMethod("deserialize", Map.class);
				meta = (ItemMeta) deserialize.invoke(null, map);
				if (map.containsKey("enchants")) {
					String string = map.get("enchants").toString().replace("{", "").replace("}", "").replace("\"", "");
					for (String s : string.split(",")) {
						String[] split = s.split("=");
						Enchantment enchantment = Enchantment.getByName(split[0]);
						int level = Integer.parseInt(split[1]);
						if (enchantment != null)
							meta.addEnchant(enchantment, level, true);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return meta;

	}

	public static Document fromItemStackToDocument(final ItemStack is, final int slot) {
		final Map<String, Object> map = is.serialize();
		final Document doc = new Document();
		for (Entry<String, Object> values : map.entrySet()) {
			if (values.getKey().equals("meta")) {
				final Map<String, Object> imMap = is.getItemMeta().serialize();
				final Document metaDoc = new Document();
				for (Entry<String, Object> imValues : imMap.entrySet()) {
					metaDoc.put(imValues.getKey(), imValues.getValue());
				}
				doc.put("meta", metaDoc);
			} else
				doc.put(values.getKey(), values.getValue());
		}
		doc.put("slot", slot);
		return doc;
	}

	public static ItemStack fromDocumentToItemStack(final Document doc) {
		final Map<String, Object> map = new HashMap<>();
		ItemMeta meta = null;
		for (Entry<String, Object> docMap : doc.entrySet()) {
			if (docMap.getKey().equals("meta")) {
				final Document metaDoc = (Document) doc.get("meta");
				final Map<String, Object> metaMap = new HashMap<>();
				for (Entry<String, Object> metaEntry : metaDoc.entrySet()) {
					metaMap.put(metaEntry.getKey(), metaEntry.getValue());
				}
				meta = deserializeItemMeta(metaMap);
			} else
				map.put(docMap.getKey(), docMap.getValue());
		}
		final ItemStack is = ItemStack.deserialize(map);
		if (meta != null)
			is.setItemMeta(meta);
		return is;
	}
}
