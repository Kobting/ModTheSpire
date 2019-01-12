package com.evacipated.cardcrawl.modthespire;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.evacipated.cardcrawl.modthespire.lib.ConfigUtils;
import com.evacipated.cardcrawl.modthespire.ui.JModPanelCheckBoxList;
import com.evacipated.cardcrawl.modthespire.ui.ModPanel;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class LoadOrder {
    
    private static String CFG_FILE = ConfigUtils.CONFIG_DIR + File.separator + "mod_order.xml";
    
    public static class ModDescriptor {
        public File mod;
        public ModInfo info;
        public boolean checked;
        
        public ModDescriptor(File mod, ModInfo info, boolean checked) {
            this.mod = mod;
            this.info = info;
            this.checked = checked;
        }
    }
    
    public static void defaultLoad(DefaultListModel<ModPanel> model, File[] mods, ModInfo[] info, JModPanelCheckBoxList parent) {
        for (int i = 0; i < info.length; i++) {
            model.addElement(new ModPanel(info[i], mods[i], parent));
        }
        return;
    }

    public static ArrayList<ModDescriptor> getModsInOrder(ModInfo[] info){
        File cfg_file = new File(CFG_FILE);
        File[] mods = new File[info.length];
        for (int i=0; i<info.length; ++i) {
            if (info[i].jarURL == null) {
                System.out.println("ERROR: jarURL is null?: " + info[i].Name);
                continue;
            }
            try {
                mods[i] = new File(info[i].jarURL.toURI());
            } catch (URISyntaxException e) {
                System.out.println("Problem with: " + info[i].jarURL);
                e.printStackTrace();
            }
        }
        if (!cfg_file.exists()) {
            return new ArrayList<>();
        }

        Document d;

        try {
            d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(cfg_file));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            System.out.println("could not load config file: " + CFG_FILE);
            System.out.println("exception was: " + e.toString());
            e.printStackTrace();
            return new ArrayList<>();
        }

        ArrayList<ModDescriptor> loadOrder = new ArrayList<>();
        NodeList modsFromCfg = d.getElementsByTagName("mod");
        ArrayList<Integer> foundMods = new ArrayList<>();
        // O(n^2) will be unhappy with lots of mods
        for (int i = 0; i < modsFromCfg.getLength(); i++) {
            for (int j = 0; j < mods.length; j++) {
                if (modsFromCfg.item(i).getTextContent().equals(mods[j].getName())) {
                    loadOrder.add(new ModDescriptor(mods[j], info[j], true));
                    foundMods.add(i);
                }
            }
        }

        // give error messages about mods that weren't found
        for (int i = 0; i < modsFromCfg.getLength(); i++) {
            if (!foundMods.contains(i)) {
                System.out.println("could not find mod: " + modsFromCfg.item(i).getTextContent() + " even though it was specified in load order");
            }
        }

        // add the rest of the mods that didn't have an order specified
        for (int i = 0; i < mods.length; i++) {
            boolean found = false;
            for (int j = 0; j < loadOrder.size(); j++) {
                ModDescriptor descriptor = loadOrder.get(j);
                if (descriptor.mod == mods[i] && descriptor.info == info[i]) {
                    found = true;
                }
            }
            if (!found) {
                loadOrder.add(new ModDescriptor(mods[i], info[i], false));
            }
        }

        return loadOrder;

    }

    public static void loadModsInOrder(DefaultListModel<ModPanel> model, ModInfo[] info, JModPanelCheckBoxList parent) {
        File cfg_file = new File(CFG_FILE);

        File[] mods = new File[info.length];
        for (int i=0; i<info.length; ++i) {
            if (info[i].jarURL == null) {
                System.out.println("ERROR: jarURL is null?: " + info[i].Name);
                continue;
            }
            try {
                mods[i] = new File(info[i].jarURL.toURI());
            } catch (URISyntaxException e) {
                System.out.println("Problem with: " + info[i].jarURL);
                e.printStackTrace();
            }
        }
        
        if (!cfg_file.exists()) {
            defaultLoad(model, mods, info, parent);
            return;
        }
        
        Document d;
        
        try {
            d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(cfg_file));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            System.out.println("could not load config file: " + CFG_FILE);
            System.out.println("exception was: " + e.toString());
            e.printStackTrace();
            defaultLoad(model, mods, info, parent);
            return;
        }
        
        ArrayList<ModDescriptor> loadOrder = new ArrayList<>();
        NodeList modsFromCfg = d.getElementsByTagName("mod");
        ArrayList<Integer> foundMods = new ArrayList<>();
        // O(n^2) will be unhappy with lots of mods
        for (int i = 0; i < modsFromCfg.getLength(); i++) {
            for (int j = 0; j < mods.length; j++) {
                if (modsFromCfg.item(i).getTextContent().equals(mods[j].getName())) {
                    loadOrder.add(new ModDescriptor(mods[j], info[j], true));
                    foundMods.add(i);
                }
            }
        }
        
        // give error messages about mods that weren't found
        for (int i = 0; i < modsFromCfg.getLength(); i++) {
            if (!foundMods.contains(i)) {
                System.out.println("could not find mod: " + modsFromCfg.item(i).getTextContent() + " even though it was specified in load order");
            }
        }
        
        // add the rest of the mods that didn't have an order specified
        for (int i = 0; i < mods.length; i++) {
            boolean found = false;
            for (int j = 0; j < loadOrder.size(); j++) {
                ModDescriptor descriptor = loadOrder.get(j);
                if (descriptor.mod == mods[i] && descriptor.info == info[i]) {
                    found = true;
                }
            }
            if (!found) {
                loadOrder.add(new ModDescriptor(mods[i], info[i], false));
            }
        }
        
        // actually set them in order in the list
        for (ModDescriptor descriptor : loadOrder) {
            ModPanel toAdd = new ModPanel(descriptor.info, descriptor.mod, parent);
            if (toAdd.checkBox.isEnabled()) {
                toAdd.checkBox.setSelected(descriptor.checked);
            }
            model.addElement(toAdd);
        }


    }
    
    private static void closeWriter(BufferedWriter writer) {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("Exception during writer.close(), BufferedWriter may be leaked. " + e.toString());
        }
    }
    
    public static void saveCfg(File[] mods) {
        BufferedWriter br = null;
        try {
            File outFile = new File(CFG_FILE);
            br = new BufferedWriter(new FileWriter(outFile));
            br.write("<mts_cfg>\n");
            for (File mod : mods) {
                br.write("\t<mod>" + mod.getName() + "</mod>\n");
            }
            br.write("</mts_cfg>\n");
        } catch (IOException e) {
            System.out.println("could not save mod load order");
            System.out.println("exception was: " + e.toString());
            e.printStackTrace();
        } finally {
            closeWriter(br);
        }

    }
}
