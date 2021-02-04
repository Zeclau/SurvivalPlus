package net.coolsimulations.SurvivalPlus.api.item;

import net.coolsimulations.SurvivalPlus.api.SPTabs;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

public class SPItemIngot extends Item {

	public final boolean beacon;
	private boolean golem;
	private float health;

	public SPItemIngot(boolean isBeaconIngot, boolean healsIronGolem, float golemHealth) {
		super(new FabricItemSettings().group(SPTabs.tabMaterials));
		this.beacon = isBeaconIngot;
		this.golem = healsIronGolem;
		this.health = golemHealth;
	}
	
	public SPItemIngot(boolean isBeaconIngot) {
		super(new FabricItemSettings().group(SPTabs.tabMaterials));
		this.beacon = isBeaconIngot;
		this.golem = false;
		this.health = 0.0F;
	}
	
	/**
     * Get the amount of health that will be added to the Iron Golem's Health
     *
     * @return true if this Item can be used
     */
	public float getGolemHealth()
    {
        return this.health;
    }

	/**
     * Determines if this ingot will heal an Iron Golem
     *
     * @return true if this Item can be used
     */
	public boolean healsGolem()
    {
        return this.golem;
    }

}
