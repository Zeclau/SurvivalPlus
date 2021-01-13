package net.coolsimulations.SurvivalPlus.core.init;

import net.coolsimulations.SurvivalPlus.api.SPCompatibilityManager;
import net.coolsimulations.SurvivalPlus.api.SPItems;
import net.coolsimulations.SurvivalPlus.api.SPReference;
import net.coolsimulations.SurvivalPlus.api.item.SPArmorMaterial;
import net.coolsimulations.SurvivalPlus.api.item.SPItemArmor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SurvivalPlusArmor {

	public static void init(){

		SPItems.bronze_helmet = new SPItemArmor(SPArmorMaterial.bronzeArmorMaterial, EquipmentSlot.HEAD);
		SPItems.bronze_chestplate = new SPItemArmor(SPArmorMaterial.bronzeArmorMaterial, EquipmentSlot.CHEST);
		SPItems.bronze_leggings = new SPItemArmor(SPArmorMaterial.bronzeArmorMaterial, EquipmentSlot.LEGS);
		SPItems.bronze_boots = new SPItemArmor(SPArmorMaterial.bronzeArmorMaterial, EquipmentSlot.FEET);
		SPItems.bronze = DefaultedList.of();
		SPItems.bronze.add(0, new ItemStack(SPItems.bronze_helmet));
		SPItems.bronze.add(1, new ItemStack(SPItems.bronze_chestplate));
		SPItems.bronze.add(2, new ItemStack(SPItems.bronze_leggings));
		SPItems.bronze.add(3, new ItemStack(SPItems.bronze_boots));

		SPItems.stone_helmet = new SPItemArmor(SPArmorMaterial.stoneArmorMaterial, EquipmentSlot.HEAD);
		SPItems.stone_chestplate = new SPItemArmor(SPArmorMaterial.stoneArmorMaterial, EquipmentSlot.CHEST);
		SPItems.stone_leggings = new SPItemArmor(SPArmorMaterial.stoneArmorMaterial, EquipmentSlot.LEGS);
		SPItems.stone_boots = new SPItemArmor(SPArmorMaterial.stoneArmorMaterial, EquipmentSlot.FEET);
		SPItems.stone = DefaultedList.of();
		SPItems.stone.add(0, new ItemStack(SPItems.stone_helmet));
		SPItems.stone.add(1, new ItemStack(SPItems.stone_chestplate));
		SPItems.stone.add(2, new ItemStack(SPItems.stone_leggings));
		SPItems.stone.add(3, new ItemStack(SPItems.stone_boots));

		SPItems.titanium_helmet = new SPItemArmor(SPArmorMaterial.titaniumArmorMaterial, EquipmentSlot.HEAD);
		SPItems.titanium_chestplate = new SPItemArmor(SPArmorMaterial.titaniumArmorMaterial, EquipmentSlot.CHEST);
		SPItems.titanium_leggings = new SPItemArmor(SPArmorMaterial.titaniumArmorMaterial, EquipmentSlot.LEGS);
		SPItems.titanium_boots = new SPItemArmor(SPArmorMaterial.titaniumArmorMaterial, EquipmentSlot.FEET);
		SPItems.titanium = DefaultedList.of();
		SPItems.titanium.add(0, new ItemStack(SPItems.titanium_helmet));
		SPItems.titanium.add(1, new ItemStack(SPItems.titanium_chestplate));
		SPItems.titanium.add(2, new ItemStack(SPItems.titanium_leggings));
		SPItems.titanium.add(3, new ItemStack(SPItems.titanium_boots));

		SPItems.oak_helmet = new SPItemArmor(SPArmorMaterial.oakArmorMaterial, EquipmentSlot.HEAD);
		SPItems.oak_chestplate = new SPItemArmor(SPArmorMaterial.oakArmorMaterial, EquipmentSlot.CHEST);
		SPItems.oak_leggings = new SPItemArmor(SPArmorMaterial.oakArmorMaterial, EquipmentSlot.LEGS);
		SPItems.oak_boots = new SPItemArmor(SPArmorMaterial.oakArmorMaterial, EquipmentSlot.FEET);
		SPItems.oak = DefaultedList.of();
		SPItems.oak.add(0, new ItemStack(SPItems.oak_helmet));
		SPItems.oak.add(1, new ItemStack(SPItems.oak_chestplate));
		SPItems.oak.add(2, new ItemStack(SPItems.oak_leggings));
		SPItems.oak.add(3, new ItemStack(SPItems.oak_boots));

		SPItems.spruce_helmet = new SPItemArmor(SPArmorMaterial.spruceArmorMaterial, EquipmentSlot.HEAD);
		SPItems.spruce_chestplate = new SPItemArmor(SPArmorMaterial.spruceArmorMaterial, EquipmentSlot.CHEST);
		SPItems.spruce_leggings = new SPItemArmor(SPArmorMaterial.spruceArmorMaterial, EquipmentSlot.LEGS);
		SPItems.spruce_boots = new SPItemArmor(SPArmorMaterial.spruceArmorMaterial, EquipmentSlot.FEET);
		SPItems.spruce = DefaultedList.of();
		SPItems.spruce.add(0, new ItemStack(SPItems.spruce_helmet));
		SPItems.spruce.add(1, new ItemStack(SPItems.spruce_chestplate));
		SPItems.spruce.add(2, new ItemStack(SPItems.spruce_leggings));
		SPItems.spruce.add(3, new ItemStack(SPItems.spruce_boots));

		SPItems.birch_helmet = new SPItemArmor(SPArmorMaterial.birchArmorMaterial, EquipmentSlot.HEAD);
		SPItems.birch_chestplate = new SPItemArmor(SPArmorMaterial.birchArmorMaterial, EquipmentSlot.CHEST);
		SPItems.birch_leggings = new SPItemArmor(SPArmorMaterial.birchArmorMaterial, EquipmentSlot.LEGS);
		SPItems.birch_boots = new SPItemArmor(SPArmorMaterial.birchArmorMaterial, EquipmentSlot.FEET);
		SPItems.birch = DefaultedList.of();
		SPItems.birch.add(0, new ItemStack(SPItems.birch_helmet));
		SPItems.birch.add(1, new ItemStack(SPItems.birch_chestplate));
		SPItems.birch.add(2, new ItemStack(SPItems.birch_leggings));
		SPItems.birch.add(3, new ItemStack(SPItems.birch_boots));

		SPItems.jungle_helmet = new SPItemArmor(SPArmorMaterial.jungleArmorMaterial, EquipmentSlot.HEAD);
		SPItems.jungle_chestplate = new SPItemArmor(SPArmorMaterial.jungleArmorMaterial, EquipmentSlot.CHEST);
		SPItems.jungle_leggings = new SPItemArmor(SPArmorMaterial.jungleArmorMaterial, EquipmentSlot.LEGS);
		SPItems.jungle_boots = new SPItemArmor(SPArmorMaterial.jungleArmorMaterial, EquipmentSlot.FEET);
		SPItems.jungle = DefaultedList.of();
		SPItems.jungle.add(0, new ItemStack(SPItems.jungle_helmet));
		SPItems.jungle.add(1, new ItemStack(SPItems.jungle_chestplate));
		SPItems.jungle.add(2, new ItemStack(SPItems.jungle_leggings));
		SPItems.jungle.add(3, new ItemStack(SPItems.jungle_boots));

		SPItems.acacia_helmet = new SPItemArmor(SPArmorMaterial.acaciaArmorMaterial, EquipmentSlot.HEAD);
		SPItems.acacia_chestplate = new SPItemArmor(SPArmorMaterial.acaciaArmorMaterial, EquipmentSlot.CHEST);
		SPItems.acacia_leggings = new SPItemArmor(SPArmorMaterial.acaciaArmorMaterial, EquipmentSlot.LEGS);
		SPItems.acacia_boots = new SPItemArmor(SPArmorMaterial.acaciaArmorMaterial, EquipmentSlot.FEET);
		SPItems.acacia = DefaultedList.of();
		SPItems.acacia.add(0, new ItemStack(SPItems.acacia_helmet));
		SPItems.acacia.add(1, new ItemStack(SPItems.acacia_chestplate));
		SPItems.acacia.add(2, new ItemStack(SPItems.acacia_leggings));
		SPItems.acacia.add(3, new ItemStack(SPItems.acacia_boots));

		SPItems.dark_oak_helmet = new SPItemArmor(SPArmorMaterial.darkOakArmorMaterial, EquipmentSlot.HEAD);
		SPItems.dark_oak_chestplate = new SPItemArmor(SPArmorMaterial.darkOakArmorMaterial, EquipmentSlot.CHEST);
		SPItems.dark_oak_leggings = new SPItemArmor(SPArmorMaterial.darkOakArmorMaterial, EquipmentSlot.LEGS);
		SPItems.dark_oak_boots = new SPItemArmor(SPArmorMaterial.darkOakArmorMaterial, EquipmentSlot.FEET);
		SPItems.dark_oak = DefaultedList.of();
		SPItems.dark_oak.add(0, new ItemStack(SPItems.dark_oak_helmet));
		SPItems.dark_oak.add(1, new ItemStack(SPItems.dark_oak_chestplate));
		SPItems.dark_oak.add(2, new ItemStack(SPItems.dark_oak_leggings));
		SPItems.dark_oak.add(3, new ItemStack(SPItems.dark_oak_boots));
		
		if(SPCompatibilityManager.isBambooModsLoaded()) {
			SPItems.bamboo_helmet = new SPItemArmor(SPArmorMaterial.bambooArmorMaterial, EquipmentSlot.HEAD);
			SPItems.bamboo_chestplate = new SPItemArmor(SPArmorMaterial.bambooArmorMaterial, EquipmentSlot.CHEST);
			SPItems.bamboo_leggings = new SPItemArmor(SPArmorMaterial.bambooArmorMaterial, EquipmentSlot.LEGS);
			SPItems.bamboo_boots = new SPItemArmor(SPArmorMaterial.bambooArmorMaterial, EquipmentSlot.FEET);
			SPItems.bamboo = DefaultedList.of();
			SPItems.bamboo.add(0, new ItemStack(SPItems.bamboo_helmet));
			SPItems.bamboo.add(1, new ItemStack(SPItems.bamboo_chestplate));
			SPItems.bamboo.add(2, new ItemStack(SPItems.bamboo_leggings));
			SPItems.bamboo.add(3, new ItemStack(SPItems.bamboo_boots));
		}
		
		if(SPCompatibilityManager.isTraverseLoaded()) {
			TraverseArmor.init();
		}
		
		if(SPCompatibilityManager.isTerrestriaLoaded()) {
			TerrestriaArmor.init();
		}

	}

	public static void register()
	{
		registerItem(SPItems.bronze, "bronze");
		registerItem(SPItems.stone, "stone");
		registerItem(SPItems.titanium, "titanium");
		registerItem(SPItems.oak, "oak");
		registerItem(SPItems.spruce, "spruce");
		registerItem(SPItems.birch, "birch");
		registerItem(SPItems.jungle, "jungle");
		registerItem(SPItems.acacia, "acacia");
		registerItem(SPItems.dark_oak, "dark_oak");
		
		if(SPCompatibilityManager.isBambooModsLoaded()) {
			registerItem(SPItems.bamboo, "bamboo");
		}
		
		if(SPCompatibilityManager.isTraverseLoaded()) {
			TraverseArmor.register();
		}
		
		if(SPCompatibilityManager.isTerrestriaLoaded()) {
			TerrestriaArmor.register();
		}
	}
	
	public static void registerItem(DefaultedList<ItemStack> item, String materialName) {
		registerItem(item, materialName, "");
	}

	public static void registerItem(DefaultedList<ItemStack> item, String materialName, String modId) {

		for(int i = 0; i < item.size(); i++) {
			if(item.get(i).getItem() instanceof ArmorItem) {

				EquipmentSlot slotType = ((ArmorItem) item.get(i).getItem()).getSlotType();
				String lastName;

				switch (slotType) {
				case HEAD:
					lastName = "helmet";
					break;
				case CHEST:
					lastName = "chestplate";
					break;
				case LEGS:
					lastName = "leggings";
					break;
				case FEET:
					lastName = "boots";
					break;
				default:
					lastName = "";
					break;
				}

				if(modId != "") {
					Registry.register(Registry.ITEM, new Identifier(SPReference.MOD_ID, materialName + "_" + lastName + "_" + modId), item.get(i).getItem());
				} else {
					Registry.register(Registry.ITEM, new Identifier(SPReference.MOD_ID, materialName + "_" + lastName), item.get(i).getItem());
				}
			}
		}
	}

}
