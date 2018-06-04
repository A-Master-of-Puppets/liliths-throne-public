package com.lilithsthrone.game.character.markings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.lilithsthrone.game.character.GameCharacter;
import com.lilithsthrone.game.inventory.AbstractCoreType;
import com.lilithsthrone.game.inventory.InventorySlot;
import com.lilithsthrone.game.inventory.enchanting.AbstractItemEffectType;
import com.lilithsthrone.game.inventory.enchanting.ItemEffectType;
import com.lilithsthrone.utils.Colour;
import com.lilithsthrone.utils.ColourListPresets;
import com.lilithsthrone.utils.Util;

/**
 * @since 0.2.6
 * @version 0.2.6
 * @author Innoxia
 */
public class AbstractTattooType extends AbstractCoreType {
	
	private static List<InventorySlot> standardInventorySlots = new ArrayList<>(InventorySlot.getClothingSlots());
	
	private boolean isMod;
	
	private int value;
	
	private int enchantmentLimit;
	
	private List<InventorySlot> slotAvailability;

	private String name;
	private String description;

	private List<Colour> availablePrimaryColours;
	private List<Colour> availableSecondaryColours;
	private List<Colour> availableTertiaryColours;
	
	private String pathName;
	private Map<Colour, Map<Colour, Map<Colour, String>>> SVGStringMap;
	
	public AbstractTattooType(
			String pathName,
			String name,
			String description,
			List<Colour> availablePrimaryColours,
			List<Colour> availableSecondaryColours,
			List<Colour> availableTertiaryColours,
			List<InventorySlot> slotAvailability) {

		this.isMod = false;
		
		value = 500;
		enchantmentLimit = 5;
		
		this.pathName = pathName;
		this.name = name;
		this.description = description;
		
		
		this.availablePrimaryColours = new ArrayList<>();
		if (availablePrimaryColours == null) {
			this.availablePrimaryColours.add(Colour.CLOTHING_BLACK);
		} else {
			this.availablePrimaryColours.addAll(availablePrimaryColours);
		}

		this.availableSecondaryColours = new ArrayList<>();
		if (availableSecondaryColours != null) {
			this.availableSecondaryColours.addAll(availableSecondaryColours);
		}

		this.availableTertiaryColours = new ArrayList<>();
		if (availableTertiaryColours != null) {
			this.availableTertiaryColours.addAll(availableTertiaryColours);
		}
		
		
		SVGStringMap = new HashMap<>();
		
		if(slotAvailability==null) {
			this.slotAvailability = standardInventorySlots;
		} else {
			this.slotAvailability = slotAvailability;
		}
	}
	

	public AbstractTattooType(File tattooXMLFile) {

		if (tattooXMLFile.exists()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(tattooXMLFile);
				
				// Cast magic:
				doc.getDocumentElement().normalize();
				
				Element clothingElement = (Element) doc.getElementsByTagName("tattoo").item(0);
				
				Element coreAttributes = (Element) clothingElement.getElementsByTagName("coreAtributes").item(0);
				
				List<InventorySlot> slotAvailability = new ArrayList<>();
				Element slotAvailabilityElement = (Element)coreAttributes.getElementsByTagName("slotAvailability").item(0);
				try {
					if(slotAvailabilityElement.getElementsByTagName("slot").getLength()==0) {
						slotAvailability = standardInventorySlots;
						
					} else {
						for(int i=0; i<slotAvailabilityElement.getElementsByTagName("slot").getLength(); i++){
							Element e = ((Element)slotAvailabilityElement.getElementsByTagName("slot").item(i));
							slotAvailability.add(InventorySlot.valueOf(e.getTextContent()));
						}
					}
				} catch(Exception ex) {
					System.err.println("AbstractTattooType loading failed. Cause: 'slotAvailability' element unable to be parsed.");
				}
				this.slotAvailability = slotAvailability;
				
				this.isMod = true;
				
				this.value = Integer.valueOf(coreAttributes.getElementsByTagName("value").item(0).getTextContent());
				this.pathName = tattooXMLFile.getParentFile().getAbsolutePath() + "/" + coreAttributes.getElementsByTagName("imageName").item(0).getTextContent();
				this.name = coreAttributes.getElementsByTagName("name").item(0).getTextContent();
				this.description = coreAttributes.getElementsByTagName("description").item(0).getTextContent();
				
				try {
					enchantmentLimit = Integer.valueOf(coreAttributes.getElementsByTagName("enchantmentLimit").item(0).getTextContent());
				} catch(Exception ex) {
				}
				
				List<Colour> importedPrimaryColours = new ArrayList<>();
				try {
					if(((Element)coreAttributes.getElementsByTagName("primaryColours").item(0)).getAttribute("values").isEmpty()) {
						Element primaryColoursElement = ((Element)coreAttributes.getElementsByTagName("primaryColours").item(0));
						if(primaryColoursElement.getElementsByTagName("colour").getLength() > 0) {
							for(int i=0; i<primaryColoursElement.getElementsByTagName("colour").getLength(); i++){
								importedPrimaryColours.add(Colour.valueOf(((Element)primaryColoursElement.getElementsByTagName("colour").item(i)).getTextContent()));
							}
						}
					} else {
						importedPrimaryColours = ColourListPresets.valueOf(((Element)coreAttributes.getElementsByTagName("primaryColours").item(0)).getAttribute("values")).getPresetColourList();
					}
				} catch(Exception ex) {
					System.err.println("AbstractTattooType loading failed. Cause: 'primaryColours' element unable to be parsed.");
				}

				List<Colour> importedSecondaryColours = new ArrayList<>();
				try {
					if((Element)coreAttributes.getElementsByTagName("secondaryColours").item(0)!=null) {
						if(((Element)coreAttributes.getElementsByTagName("secondaryColours").item(0)).getAttribute("values").isEmpty()) {
							Element secondaryColoursElement = ((Element)coreAttributes.getElementsByTagName("secondaryColours").item(0));
							if(secondaryColoursElement.getElementsByTagName("colour").getLength() > 0) {
								for(int i=0; i<secondaryColoursElement.getElementsByTagName("colour").getLength(); i++){
									importedSecondaryColours.add(Colour.valueOf(((Element)secondaryColoursElement.getElementsByTagName("colour").item(i)).getTextContent()));
								}
							}
						} else {
							importedSecondaryColours = ColourListPresets.valueOf(((Element)coreAttributes.getElementsByTagName("secondaryColours").item(0)).getAttribute("values")).getPresetColourList();
						}
					}
				} catch(Exception ex) {
					System.err.println("AbstractTattooType loading failed. Cause: 'secondaryColours' element unable to be parsed.");
				}

				List<Colour> importedTertiaryColours = new ArrayList<>();
				try {
					if((Element)coreAttributes.getElementsByTagName("tertiaryColours").item(0)!=null) {
						if(((Element)coreAttributes.getElementsByTagName("tertiaryColours").item(0)).getAttribute("values").isEmpty()) {
							Element tertiaryColoursElement = ((Element)coreAttributes.getElementsByTagName("tertiaryColours").item(0));
							if(tertiaryColoursElement.getElementsByTagName("colour").getLength() > 0) {
								for(int i=0; i<tertiaryColoursElement.getElementsByTagName("colour").getLength(); i++){
									importedTertiaryColours.add(Colour.valueOf(((Element)tertiaryColoursElement.getElementsByTagName("colour").item(i)).getTextContent()));
								}
							}
						} else {
							importedTertiaryColours = ColourListPresets.valueOf(((Element)coreAttributes.getElementsByTagName("tertiaryColours").item(0)).getAttribute("values")).getPresetColourList();
						}
					}
				} catch(Exception ex) {
					System.err.println("AbstractTattooType loading failed. Cause: 'tertiaryColours' element unable to be parsed.");
				}
				
				this.availablePrimaryColours = new ArrayList<>(importedPrimaryColours);

				this.availableSecondaryColours = new ArrayList<>(importedSecondaryColours);

				this.availableTertiaryColours = new ArrayList<>(importedTertiaryColours);
				
				SVGStringMap = new HashMap<>();

			} catch(Exception ex) {
				System.err.println("ClothingType was unable to be loaded from file!");
			}
		}
	}
	
	@Override
	public boolean equals (Object o) {
		if(super.equals(o)) {
			return (o instanceof AbstractTattooType)
					&& ((AbstractTattooType)o).isMod()==isMod
					&& ((AbstractTattooType)o).getSlotAvailability().equals(this.getSlotAvailability())
					&& ((AbstractTattooType)o).getName().equals(this.getName())
					&& ((AbstractTattooType)o).getDescription().equals(this.getDescription())
					&& ((AbstractTattooType)o).getAvailablePrimaryColours().equals(this.getAvailablePrimaryColours())
					&& ((AbstractTattooType)o).getAvailableSecondaryColours().equals(this.getAvailableSecondaryColours())
					&& ((AbstractTattooType)o).getAvailableTertiaryColours().equals(this.getAvailableTertiaryColours())
					&& ((AbstractTattooType)o).getPathName().equals(this.getPathName());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (this.isMod() ? 1 : 0);
		
		result = 31 * result + getSlotAvailability().hashCode();
		result = 31 * result + getName().hashCode();
		result = 31 * result + getDescription().hashCode();
		
		result = 31 * result + getAvailablePrimaryColours().hashCode();
		result = 31 * result + getAvailableSecondaryColours().hashCode();
		result = 31 * result + getAvailableTertiaryColours().hashCode();
		
		result = 31 * result + getPathName().hashCode();
		
		return result;
	}
	
	public boolean isMod() {
		return isMod;
	}
	
	public int getValue() {
		return value;
	}


	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public List<Colour> getAvailablePrimaryColours() {
		return availablePrimaryColours;
	}

	public List<Colour> getAvailableSecondaryColours() {
		return availableSecondaryColours;
	}

	public List<Colour> getAvailableTertiaryColours() {
		return availableTertiaryColours;
	}

	public List<InventorySlot> getSlotAvailability() {
		return slotAvailability;
	}
	
	public String getId() {
		return TattooType.getIdFromTattooType(this);
	}
	
	public int getEnchantmentLimit() {
		return enchantmentLimit;
	}
	
	public AbstractItemEffectType getEnchantmentEffect() {
		return ItemEffectType.TATTOO;
	}
	
	private void addSVGStringMapping(Colour colour, Colour colourSecondary, Colour colourTertiary, String s) {
		if(SVGStringMap.get(colour)==null) {
			SVGStringMap.put(colour, new HashMap<>());
			SVGStringMap.get(colour).put(colourSecondary, new HashMap<>());
			
		} else if(SVGStringMap.get(colour).get(colourSecondary)==null) {
			SVGStringMap.get(colour).put(colourSecondary, new HashMap<>());
		}
		
		SVGStringMap.get(colour).get(colourSecondary).put(colourTertiary, s);
	}
	
	private String getSVGStringFromMap(Colour colour, Colour colourSecondary, Colour colourTertiary) {
		if(SVGStringMap.get(colour)==null) {
			return null;
		} else {
			if(SVGStringMap.get(colour).get(colourSecondary)==null) {
				return null;
			} else {
				return SVGStringMap.get(colour).get(colourSecondary).get(colourTertiary);
			}
		}
	}
	
	public String getSVGImage(GameCharacter character, Colour colour, Colour colourSecondary, Colour colourTertiary) {
		if (!availablePrimaryColours.contains(colour) || pathName==null || pathName.isEmpty()) {
			return "";
		}
		
		String stringFromMap = getSVGStringFromMap(colour, colourSecondary, colourTertiary);
		if (stringFromMap!=null) {
			return stringFromMap;
			
		} else {
			if (availablePrimaryColours.contains(colour)) {
				try {
					InputStream is;
					String s;
					if(isMod) {
						List<String> lines = Files.readAllLines(Paths.get(pathName));
						StringBuilder sb = new StringBuilder();
						for(String line : lines) {
							sb.append(line);
						}
						s = sb.toString();
						
					} else {
						is = this.getClass().getResourceAsStream("/com/lilithsthrone/res/tattoos/" + pathName + ".svg");
						s = Util.inputStreamToString(is);
						is.close();
					}
					
					s = Util.colourReplacement(this.getId(), colour, colourSecondary, colourTertiary, s);
					
					addSVGStringMapping(colour, colourSecondary, colourTertiary, s);
					
					return s;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
		return "";
	}

	public String getPathName() {
		return pathName;
	}
}
