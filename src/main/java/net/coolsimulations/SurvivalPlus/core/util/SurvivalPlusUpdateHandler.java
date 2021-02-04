package net.coolsimulations.SurvivalPlus.core.util;

import java.net.URL;
import java.util.Scanner;

import net.coolsimulations.SurvivalPlus.api.SPReference;
import net.minecraft.SharedConstants;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class SurvivalPlusUpdateHandler {
	
	private static String latestVersion;
	private static String latestVersionInfo;
	public static boolean isOld = false;
	public static TranslatableText updateInfo = null;
	public static LiteralText updateVersionInfo = null;
	
	public static void init() {
		
		try {
            URL url = new URL("https://coolsimulations.net/mcmods/survivalplus-fabric/versionchecker115.txt");
            Scanner s = new Scanner(url.openStream());
            latestVersion = s.next();
            s.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		
		try {
            URL url = new URL("https://coolsimulations.net/mcmods/survivalplus-fabric/updateinfo115.txt");
            Scanner s = new Scanner(url.openStream());
            latestVersionInfo = s.nextLine();
            s.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		
		if(latestVersion != null) {
			
			if(latestVersion.equals("ended")) {
				
				isOld = true;
				
				TranslatableText sp = new TranslatableText("sp.name");
				sp.getStyle().setColor(Formatting.BLUE);
				
				LiteralText MCVersion = new LiteralText(SharedConstants.getGameVersion().getName());
				MCVersion.getStyle().setColor(Formatting.BLUE);
				
				updateInfo = new TranslatableText("sp.update.display3", new Object[] {sp, MCVersion});
				updateInfo.getStyle().setColor(Formatting.YELLOW);
				
				updateInfo.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("sp.update.display2")));
				updateInfo.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://curseforge.com/minecraft/mc-mods/survivalplus-fabric"));
				
			}
			
			if(!latestVersion.equals(SPReference.VERSION) && !latestVersion.equals("ended")) {
				
				isOld = true;
				
				TranslatableText sp = new TranslatableText("sp.name");
				sp.getStyle().setColor(Formatting.BLUE);
				
				LiteralText version = new LiteralText(latestVersion);
				version.getStyle().setColor(Formatting.BLUE);
				
				updateInfo = new TranslatableText("sp.update.display1", new Object[] {sp, version});
				updateInfo.getStyle().setColor(Formatting.YELLOW);
				
				updateInfo.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("sp.update.display2")));
				updateInfo.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://curseforge.com/minecraft/mc-mods/survivalplus-fabric"));
				
				if(latestVersionInfo != null) {
					
					updateVersionInfo = new LiteralText(latestVersionInfo);
					updateVersionInfo.getStyle().setColor(Formatting.DARK_AQUA);
					updateVersionInfo.getStyle().setBold(true);
					
					updateVersionInfo.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("sp.update.display2")));
					updateVersionInfo.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://curseforge.com/minecraft/mc-mods/survivalplus-fabric"));
					
				}
				
			}
			
		}
	}

}
