package de.srsuders.chestlink.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Colorable;
import org.bukkit.material.Dye;
import org.bukkit.material.Skull;
import org.bukkit.potion.PotionEffect;

public class ItemBuilder {

	private ItemStack item;

	public ItemBuilder(Material material, int amount) {
		this.item = new ItemStack(material, amount);
	}

	public ItemBuilder(Material material) {
		this.item = new ItemStack(material);
	}

	@SuppressWarnings("deprecation")
	public ItemBuilder(Dye dye) {
		this.item = dye.toItemStack();
	}

	public ItemBuilder(ItemStack item) {
		this.item = item;
	}

	public ItemBuilder(Map<String, Object> deserialized) {
		this.item = ItemStack.deserialize(deserialized);
	}

	public ItemBuilder setColor(DyeColor color) {
		if (this.item.getData() instanceof Colorable) {
			final Colorable colorable = (Colorable) this.item;
			colorable.setColor(color);
		}
		return this;
	}

	public ItemBuilder setColor(Color color) {
		if (this.item.getItemMeta() instanceof LeatherArmorMeta) {
			final LeatherArmorMeta meta = (LeatherArmorMeta) this.item.getItemMeta();
			meta.setColor(color);
			this.item.setItemMeta(meta);
		}
		return this;
	}

	@SuppressWarnings("deprecation")
	public ItemBuilder setUnbreakable(boolean unbreakable) {
		final ItemMeta meta = this.item.getItemMeta();
		meta.spigot().setUnbreakable(unbreakable);
		this.item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setAmount(int amount) {
		this.item.setAmount(amount);
		return this;
	}

	public ItemBuilder setDurability(int durability) {
		this.item.setDurability((short) durability);
		return this;
	}

	public ItemBuilder addItemFlag(ItemFlag flag) {
		final ItemMeta meta = this.item.getItemMeta();
		meta.addItemFlags(flag);
		this.item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder addItemFlags(ItemFlag[] flags) {
		final ItemMeta meta = this.item.getItemMeta();
		meta.addItemFlags(flags);
		this.item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder removeItemFlags() {
		final ItemMeta meta = this.item.getItemMeta();
		for (final ItemFlag flag : meta.getItemFlags()) {
			meta.removeItemFlags(flag);
		}
		this.item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
		final ItemMeta meta = this.item.getItemMeta();
		meta.addEnchant(enchantment, level, true);
		this.item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder addEnchantments(Enchantment[] enchantments, int[] level) {
		final ItemMeta meta = this.item.getItemMeta();
		for (int i = 0; i < enchantments.length; i++) {
			meta.addEnchant(enchantments[i], level[i], true);
		}
		this.item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder removeEnchantment(Enchantment enchantment) {
		final ItemMeta meta = this.item.getItemMeta();
		meta.removeEnchant(enchantment);
		this.item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder removeEnchantments(Enchantment[] enchantments) {
		final ItemMeta meta = this.item.getItemMeta();
		for (final Enchantment enchantment : enchantments) {
			meta.removeEnchant(enchantment);
		}
		this.item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder removeEnchantments() {
		final ItemMeta meta = this.item.getItemMeta();
		for (final Enchantment enchantment : meta.getEnchants().keySet()) {
			meta.removeEnchant(enchantment);
		}
		this.item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setLore(List<String> lore) {
		final ItemMeta meta = this.item.getItemMeta();
		meta.setLore(lore);
		this.item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setLore(String lore) {
		final ItemMeta meta = this.item.getItemMeta();
		meta.setLore(Arrays.asList(lore));
		this.item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setDisplayName(String name) {
		final ItemMeta meta = this.item.getItemMeta();
		meta.setDisplayName(name);
		this.item.setItemMeta(meta);
		return this;
	}

	@SuppressWarnings("deprecation")
	protected ItemBuilder setSkullOwner(Player player) {
		if (this.item.getData() instanceof Skull) {
			final SkullMeta meta = (SkullMeta) this.item.getItemMeta();
			meta.setOwner(player.getName());
			this.item.setItemMeta(meta);
		}
		return this;
	}

	protected ItemBuilder addEffect(PotionEffect effect) {
		if (this.item.getItemMeta() instanceof PotionMeta) {
			final PotionMeta meta = (PotionMeta) this.item.getItemMeta();
			meta.addCustomEffect(effect, true);
			this.item.setItemMeta(meta);
		}
		return this;
	}

	protected ItemBuilder addEffects(List<PotionEffect> effects) {
		if (this.item.getItemMeta() instanceof PotionMeta) {
			final PotionMeta meta = (PotionMeta) this.item.getItemMeta();
			for (final PotionEffect effect : effects) {
				meta.addCustomEffect(effect, true);
				this.item.setItemMeta(meta);
			}
		}
		return this;
	}

	protected ItemBuilder addEffects(PotionEffect... effects) {
		if (this.item.getItemMeta() instanceof PotionMeta) {
			final PotionMeta meta = (PotionMeta) this.item.getItemMeta();
			for (final PotionEffect effect : effects) {
				meta.addCustomEffect(effect, true);
				this.item.setItemMeta(meta);
			}
		}
		return this;
	}

	protected ItemBuilder setPages(List<String> pages) {
		if (this.item.getItemMeta() instanceof BookMeta) {
			final BookMeta meta = (BookMeta) this.item.getItemMeta();
			meta.setPages(pages);
			this.item.setItemMeta(meta);
		}
		return this;
	}

	protected ItemBuilder setPages(String... pages) {
		if (this.item.getItemMeta() instanceof BookMeta) {
			final BookMeta meta = (BookMeta) this.item.getItemMeta();
			meta.setPages(pages);
			this.item.setItemMeta(meta);
		}
		return this;
	}

	protected ItemBuilder setAuthor(String author) {
		if (this.item.getItemMeta() instanceof BookMeta) {
			final BookMeta meta = (BookMeta) this.item.getItemMeta();
			meta.setAuthor(author);
			this.item.setItemMeta(meta);
		}
		return this;
	}

	protected ItemBuilder setTitle(String title) {
		if (this.item.getItemMeta() instanceof BookMeta) {
			final BookMeta meta = (BookMeta) this.item.getItemMeta();
			meta.setTitle(title);
			this.item.setItemMeta(meta);
		}
		return this;
	}

	protected ItemBuilder addPage(String... page) {
		if (this.item.getItemMeta() instanceof BookMeta) {
			final BookMeta meta = (BookMeta) this.item.getItemMeta();
			meta.addPage(page);
			this.item.setItemMeta(meta);
		}
		return this;
	}

	protected ItemBuilder setPage(int page, String text) {
		if (this.item.getItemMeta() instanceof BookMeta) {
			final BookMeta meta = (BookMeta) this.item.getItemMeta();
			meta.setPage(page, text);
			this.item.setItemMeta(meta);
		}
		return this;
	}

	protected ItemBuilder setPatterns(List<Pattern> patterns) {
		if (this.item.getItemMeta() instanceof BannerMeta) {
			final BannerMeta meta = (BannerMeta) this.item.getItemMeta();
			meta.setPatterns(patterns);
			this.item.setItemMeta(meta);
		}
		return this;
	}

	protected ItemBuilder setPattern(int id, Pattern pattern) {
		if (this.item.getItemMeta() instanceof BannerMeta) {
			final BannerMeta meta = (BannerMeta) this.item.getItemMeta();
			meta.setPattern(id, pattern);
			this.item.setItemMeta(meta);
		}
		return this;
	}

	protected ItemBuilder addPattern(Pattern pattern) {
		if (this.item.getItemMeta() instanceof BannerMeta) {
			final BannerMeta meta = (BannerMeta) this.item.getItemMeta();
			meta.addPattern(pattern);
			this.item.setItemMeta(meta);
		}
		return this;
	}

	protected ItemBuilder setPower(int power) {
		if (this.item.getItemMeta() instanceof FireworkMeta) {
			final FireworkMeta meta = (FireworkMeta) this.item.getItemMeta();
			meta.setPower(power);
			this.item.setItemMeta(meta);
		}
		return this;
	}

	protected ItemBuilder addEffect(FireworkEffect effect) {
		if (this.item.getItemMeta() instanceof FireworkMeta) {
			final FireworkMeta meta = (FireworkMeta) this.item.getItemMeta();
			meta.addEffect(effect);
			this.item.setItemMeta(meta);
		}
		return this;
	}

	protected ItemBuilder addEffects(FireworkEffect... effects) {
		if (this.item.getItemMeta() instanceof FireworkMeta) {
			final FireworkMeta meta = (FireworkMeta) this.item.getItemMeta();
			meta.addEffects(effects);
			this.item.setItemMeta(meta);
		}
		return this;
	}

	protected ItemBuilder addEffects(Iterable<FireworkEffect> effects) {
		if (this.item.getItemMeta() instanceof FireworkMeta) {
			final FireworkMeta meta = (FireworkMeta) this.item.getItemMeta();
			meta.addEffects(effects);
			this.item.setItemMeta(meta);
		}
		return this;
	}

	protected ItemBuilder setScaling(boolean scaling) {
		if (this.item.getItemMeta() instanceof MapMeta) {
			final MapMeta meta = (MapMeta) this.item.getItemMeta();
			meta.setScaling(scaling);
			this.item.setItemMeta(meta);
		}
		return this;
	}

	public PotionBuilder toPotionBuilder() {
		return new PotionBuilder(this);
	}

	public SkullBuilder toSkullBuilder() {
		return new SkullBuilder(this);
	}

	public BookBuilder toBookBuilder() {
		return new BookBuilder(this);
	}

	public BannerBuilder toBannerBuilder() {
		return new BannerBuilder(this);
	}

	public FireworkBuilder toFireworkBuilder() {
		return new FireworkBuilder(this);
	}

	public MapBuilder toMapBuilder() {
		return new MapBuilder(this);
	}

	public SpawnEggBuilder toSpawnEggBuilder() {
		return new SpawnEggBuilder(this);
	}

	public ItemStack build() {
		return this.item;
	}

	public class PotionBuilder {

		public ItemBuilder item;

		public PotionBuilder(ItemBuilder item) {
			this.item = item;
		}

		public PotionBuilder addEffect(PotionEffect effect) {
			this.item.addEffect(effect);
			return this;
		}

		public PotionBuilder addEffects(List<PotionEffect> effects) {
			this.item.addEffects(effects);
			return this;
		}

		public PotionBuilder addEffects(PotionEffect... effects) {
			this.item.addEffects(effects);
			return this;
		}

		public ItemBuilder toItemBuilder() {
			return this.item;
		}

		public ItemStack build() {
			return this.item.build();
		}

	}

	public class SkullBuilder {

		private ItemBuilder item;

		public SkullBuilder(ItemBuilder item) {
			this.item = item;
		}

		public SkullBuilder setSkullOwner(Player player) {
			this.item.setSkullOwner(player);
			return this;
		}

		public ItemBuilder toItemBuilder() {
			return this.item;
		}

		public ItemStack build() {
			return this.item.build();
		}

	}

	public class BookBuilder {

		private ItemBuilder item;

		public BookBuilder(ItemBuilder item) {
			this.item = item;
		}

		public BookBuilder setPages(List<String> pages) {
			this.item.setPages(pages);
			return this;
		}

		public BookBuilder setPages(String... pages) {
			this.item.setPages(pages);
			return this;
		}

		public BookBuilder setAuthor(String author) {
			this.item.setAuthor(author);
			return this;
		}

		public BookBuilder setTitle(String title) {
			this.item.setTitle(title);
			return this;
		}

		public BookBuilder addPage(String... page) {
			this.item.addPage(page);
			return this;
		}

		public BookBuilder setPage(int page, String text) {
			this.item.setPage(page, text);
			return this;
		}

		public ItemBuilder toItemBuilder() {
			return this.item;
		}

		public ItemStack build() {
			return this.item.build();
		}

	}

	public class BannerBuilder {

		private ItemBuilder item;

		public BannerBuilder(ItemBuilder item) {
			this.item = item;
		}

		public BannerBuilder setPatterns(List<Pattern> patterns) {
			this.item.setPatterns(patterns);
			return this;
		}

		public BannerBuilder setPattern(int id, Pattern pattern) {
			this.item.setPattern(id, pattern);
			return this;
		}

		public BannerBuilder addPattern(Pattern pattern) {
			this.item.addPattern(pattern);
			return this;
		}

		public ItemBuilder toItemBuilder() {
			return this.item;
		}

		public ItemStack build() {
			return this.item.build();
		}

	}

	public class FireworkBuilder {

		private ItemBuilder item;

		public FireworkBuilder(ItemBuilder item) {
			this.item = item;
		}

		public FireworkBuilder setPower(int power) {
			this.item.setPower(power);
			return this;
		}

		public FireworkBuilder addEffect(FireworkEffect effect) {
			this.item.addEffect(effect);
			return this;
		}

		public FireworkBuilder addEffects(FireworkEffect... effects) {
			this.item.addEffects(effects);
			return this;
		}

		public FireworkBuilder addEffects(Iterable<FireworkEffect> effects) {
			this.item.addEffects(effects);
			return this;
		}

		public ItemBuilder toItemBuilder() {
			return this.item;
		}

		public ItemStack build() {
			return this.item.build();
		}

	}

	public class MapBuilder {

		private ItemBuilder item;

		public MapBuilder(ItemBuilder item) {
			this.item = item;
		}

		public MapBuilder setScaling(boolean scaling) {
			this.item.setScaling(scaling);
			return this;
		}

		public ItemBuilder toItemBuilder() {
			return this.item;
		}

		public ItemStack build() {
			return this.item.build();
		}

	}

	public class SpawnEggBuilder {

		public ItemBuilder item;

		public SpawnEggBuilder(ItemBuilder item) {
			this.item = item;
		}

		public ItemBuilder toItemBuilder() {
			return this.item;
		}

		public ItemStack build() {
			return this.item.build();
		}

	}

}