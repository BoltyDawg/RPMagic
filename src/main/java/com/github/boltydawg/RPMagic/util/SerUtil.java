package com.github.boltydawg.RPMagic.util;

import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.github.boltydawg.RPMagic.RPMagic;

public class SerUtil {
	public static void createFiles() {
		File fm = new File("plugins\\RPMagic\\mages.ser");
		File fb = new File("plugins\\RPMagic\\beacons.ser");
		try {
			if(!fm.exists()) {
				fm.createNewFile();
				FileOutputStream ifos = new FileOutputStream("plugins\\RPMagic\\mages.ser");
				ObjectOutputStream ioos = new ObjectOutputStream(ifos);
				ioos.reset();
				ioos.writeObject(new HashMap<UUID,ArrayList<String>>());
				ioos.flush();
				ioos.close();
				ifos.close();
				RPMagic.instance.getLogger().info("Created mage.ser");
			}
			if(!fb.exists()) {
				fb.createNewFile();
				FileOutputStream ifos = new FileOutputStream("plugins\\RPMagic\\beacons.ser");
				ObjectOutputStream ioos = new ObjectOutputStream(ifos);
				ioos.reset();
				ioos.writeObject(new HashMap<UUID,String>());
				ioos.flush();
				ioos.close();
				ifos.close();
				RPMagic.instance.getLogger().info("Created beacons.ser");
			}
		}
		catch(FileNotFoundException e){
			RPMagic.instance.getLogger().info("In initial setup");
			e.printStackTrace();
		}
		catch(IOException e) {
			RPMagic.instance.getLogger().info("In initial setup");
			e.printStackTrace();
		}
	}
	public static void loadValues() {
		try {
			FileInputStream fism = new FileInputStream("plugins\\RPMagic\\mages.ser");
			ObjectInputStream oism = new ObjectInputStream(fism);
			@SuppressWarnings("unchecked")
			HashMap<UUID,ArrayList<String>> readObjectm = (HashMap<UUID,ArrayList<String>>)oism.readObject();
			RPMagic.mages = readObjectm;
			oism.close();
			fism.close();
			
			FileInputStream fisb = new FileInputStream("plugins\\RPMagic\\beacons.ser");
			ObjectInputStream oisb = new ObjectInputStream(fisb);
			@SuppressWarnings("unchecked")
			HashMap<UUID,String> b = (HashMap<UUID,String>)oisb.readObject();
			for(Map.Entry<UUID, String> set : b.entrySet()) {
				RPMagic.beacons.put(set.getKey(), stringToLocation(set.getValue()));
			}
			oisb.close();
			fisb.close();
		}
		catch(FileNotFoundException e){
			Bukkit.broadcastMessage("File not found!");
			e.printStackTrace();
		}
		catch(IOException e) {
			Bukkit.broadcastMessage("IOException!");
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) {
			Bukkit.broadcastMessage("ClassNotFoundException!");
			e.printStackTrace();
		}
	}
	public static void storeValues() {
		try {
			FileOutputStream fosm = new FileOutputStream("plugins\\RPMagic\\mages.ser");
			ObjectOutputStream oosm = new ObjectOutputStream(fosm);
			
			oosm.reset();
			oosm.writeObject(RPMagic.mages);
			oosm.close();
			
			FileOutputStream fosb = new FileOutputStream("plugins\\RPMagic\\beacons.ser");
			ObjectOutputStream oosb = new ObjectOutputStream(fosb);
			
			oosb.reset();
			HashMap<UUID,String> temp = new HashMap<UUID,String>();
			for(Map.Entry<UUID, Location> set : RPMagic.beacons.entrySet()) {
				temp.put(set.getKey(), locationToString(set.getValue()));
			}
			oosb.writeObject(temp);
			oosb.close();
		}
		catch(FileNotFoundException e){
			Bukkit.broadcastMessage("File not found!");
			e.printStackTrace();
		}
		catch(IOException e) {
			Bukkit.broadcastMessage("IOException!");
			e.printStackTrace();
		}
	}
	
	
	
	public static String locationToString(Location loc) {
	    if (loc == null) {
	    	return "";
	    }
	    return loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ() ;  
	   }

	public static Location stringToLocation(String s) {
	  if (s == null || s.trim() == "") 
		  return null;
	  
	  final String[] parts = s.split(":");
	  
	  if (parts.length == 4) {
		  World w = Bukkit.getServer().getWorld(parts[0]);
		  double x = Double.parseDouble(parts[1]);
		  double y = Double.parseDouble(parts[2]);
		  double z = Double.parseDouble(parts[3]);
		  return new Location(w, x, y, z);
	  }
	  return null;
	  }
}
