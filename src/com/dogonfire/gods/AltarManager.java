package com.dogonfire.gods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.Attachable;
import org.bukkit.material.MaterialData;

import com.dogonfire.gods.GodManager.GodType;

public class AltarManager
{
	private Gods plugin;
	private Random random = new Random();
	private HashMap<Integer, String> droppedItems = new HashMap();
	private HashMap<Material, List<GodManager.GodType>> altarBlockTypes = new HashMap();

	AltarManager(Gods p)
	{
		this.plugin = p;
	}

	public void resetAltarBlockTypes()
	{
		this.altarBlockTypes.clear();

		{
			ArrayList<GodManager.GodType> list = new ArrayList<GodType>();
			list.add(GodManager.GodType.BREATH);
			this.altarBlockTypes.put(Material.DIAMOND_BLOCK, list);
		}

		{
			ArrayList<GodManager.GodType> list = new ArrayList();
			list.add(GodManager.GodType.BLOOD);
			this.altarBlockTypes.put(Material.NETHER_WART_BLOCK, list);
		}

		{
			ArrayList<GodManager.GodType> list = new ArrayList();
			list.add(GodManager.GodType.LIGHT);
			this.altarBlockTypes.put(Material.GOLD_BLOCK, list);
		}

		{
			ArrayList<GodManager.GodType> list = new ArrayList();
			list.add(GodManager.GodType.VOID);
			this.altarBlockTypes.put(Material.OBSIDIAN, list);
		}

		{
			ArrayList<GodManager.GodType> list = new ArrayList();
			list.add(GodManager.GodType.TIME);
			this.altarBlockTypes.put(Material.REDSTONE_BLOCK, list);
		}

		{
			ArrayList<GodManager.GodType> list = new ArrayList();
			list.add(GodManager.GodType.SPACE);
			this.altarBlockTypes.put(Material.COAL_BLOCK, list);
		}

		{
			ArrayList<GodManager.GodType> list = new ArrayList();
			list.add(GodManager.GodType.HEART);
			this.altarBlockTypes.put(Material.STAINED_HARDENED_CLAY:6, list);
		}

		{
			ArrayList<GodManager.GodType> list = new ArrayList();
			list.add(GodManager.GodType.MIND);
			this.altarBlockTypes.put(Material.PRISMARINE, list);
		}

		{
			ArrayList<GodManager.GodType> list = new ArrayList();
			list.add(GodManager.GodType.HOPE);
			this.altarBlockTypes.put(Material.GLOWSTONE, list);
		}

		{
			ArrayList<GodManager.GodType> list = new ArrayList();
			list.add(GodManager.GodType.RAGE);
			this.altarBlockTypes.put(Material.PURPUR_BLOCK, list);
		}

		{
			ArrayList<GodManager.GodType> list = new ArrayList();
			list.add(GodManager.GodType.LIFE);
			this.altarBlockTypes.put(Material.EMERALD_BLOCK, list);
		}

		{
			ArrayList<GodManager.GodType> list = new ArrayList();
			list.add(GodManager.GodType.DOOM);
			this.altarBlockTypes.put(Material.SOUL_SAND, list);
		}

		if (this.plugin.werewolfEnabled)
		{
			ArrayList<GodManager.GodType> list = new ArrayList();
			list.add(GodManager.GodType.WEREWOLVES);
			this.altarBlockTypes.put(Material.WOOD, list);
		}
	}

	public void setAltarBlockTypeForGodType(GodManager.GodType godType, Material blockMaterial)
	{
		List<GodManager.GodType> godTypes = new ArrayList();
		if (this.altarBlockTypes.containsKey(blockMaterial))
		{
			godTypes = (List) this.altarBlockTypes.get(blockMaterial);
		}
		if (!godTypes.contains(godType))
		{
			godTypes.add(godType);
		}
		this.altarBlockTypes.put(blockMaterial, godTypes);
	}

	public GodManager.GodType getGodTypeForAltarBlockType(Material altarBlockType)
	{
		List<GodManager.GodType> godTypes = (List) this.altarBlockTypes.get(altarBlockType);
		if ((godTypes == null) || (godTypes.size() == 0))
		{
			this.plugin.logDebug("No god types available for block type " + altarBlockType.name() + "!");
			return null;
		}
		return (GodManager.GodType) godTypes.get(this.random.nextInt(godTypes.size()));
	}

	public List<String> getAltarBlockTypesFromGodType(GodManager.GodType godType)
	{
		List<String> list = new ArrayList();
		for (Material blockMaterial : this.altarBlockTypes.keySet())
		{
			if ((this.altarBlockTypes.get(blockMaterial) != null) && (((List) this.altarBlockTypes.get(blockMaterial)).contains(godType)))
			{
				list.add(blockMaterial.name());
			}
		}
		return list;
	}

	public Player getCursedPlayerFromAltar(Block block, String[] lines)
	{
		if ((block == null) || (block.getType() != Material.WALL_SIGN))
		{
			return null;
		}

		String cursesName = lines[0].trim();
		if (!cursesName.equalsIgnoreCase("curses"))
		{
			return null;
		}

		String playerName = lines[2];
		if ((playerName == null) || (playerName.length() < 1))
		{
			return null;
		}

		return plugin.getServer().getPlayer(playerName);
	}

	public Player getBlessedPlayerFromAltarSign(Block block, String[] lines)
	{
		if ((block == null) || (block.getType() != Material.WALL_SIGN))
		{
			return null;
		}
		String cursesName = lines[0].trim();
		if (!cursesName.equalsIgnoreCase("blessings"))
		{
			return null;
		}

		String playerName = lines[2];

		if ((playerName == null) || (playerName.length() < 1))
		{
			return null;
		}

		return plugin.getServer().getPlayer(playerName);
	}

	public GodManager.GodGender getGodGenderFromAltarBlock(Block block)
	{
		if (block.getRelative(BlockFace.UP).getType().equals(Material.REDSTONE_TORCH_ON))
		{
			return GodManager.GodGender.Female;
		}
		return GodManager.GodGender.Male;
	}

	public Block getAltarBlockFromSign(Block block)
	{
		if ((block == null) || (block.getType() != Material.WALL_SIGN))
		{
			return null;
		}
		MaterialData m = block.getState().getData();

		BlockFace face = BlockFace.DOWN;

		face = ((Attachable) m).getAttachedFace();

		Block altarBlock = block.getRelative(face);

		this.plugin.logDebug("getAltarBlockFromSign(): AltarBlock block is " + altarBlock.getType().name());
		if (getGodTypeForAltarBlockType(altarBlock.getType()) == null)
		{
			return null;
		}
		if ((!altarBlock.getRelative(BlockFace.UP).getType().equals(Material.TORCH)) && (!altarBlock.getRelative(BlockFace.UP).getType().equals(Material.REDSTONE_TORCH_ON)))
		{
			return null;
		}
		return altarBlock;
	}

	public boolean isAltarSign(Block block)
	{
		if ((block == null) || (block.getType() != Material.WALL_SIGN))
		{
			return false;
		}
		MaterialData m = block.getState().getData();

		BlockFace face = BlockFace.DOWN;

		face = ((Attachable) m).getAttachedFace();

		Block altarBlock = block.getRelative(face);

		this.plugin.logDebug("isAltarSign(): AltarBlock block is " + altarBlock.getType().name());
		if (getGodTypeForAltarBlockType(altarBlock.getType()) == null)
		{
			return false;
		}
		if ((!altarBlock.getRelative(BlockFace.UP).getType().equals(Material.TORCH)) && (!altarBlock.getRelative(BlockFace.UP).getType().equals(Material.REDSTONE_TORCH_ON)))
		{
			return false;
		}
		return true;
	}

	public boolean isAltarBlock(Block block)
	{
		if ((block == null) || (getGodTypeForAltarBlockType(block.getType()) == null))
		{
			return false;
		}
		if ((block.getRelative(BlockFace.UP).getTypeId() != Material.TORCH.getId()) && (block.getRelative(BlockFace.UP).getTypeId() != Material.REDSTONE_TORCH_ON.getId()))
		{
			return false;
		}
		if (block.getRelative(BlockFace.NORTH).getTypeId() == Material.WALL_SIGN.getId())
		{
			return true;
		}
		if (block.getRelative(BlockFace.SOUTH).getTypeId() == Material.WALL_SIGN.getId())
		{
			return true;
		}
		if (block.getRelative(BlockFace.WEST).getTypeId() == Material.WALL_SIGN.getId())
		{
			return true;
		}
		if (block.getRelative(BlockFace.EAST).getTypeId() == Material.WALL_SIGN.getId())
		{
			return true;
		}
		return false;
	}

	public boolean isAltarTorch(Block block)
	{
		if (block == null)
		{
			return false;
		}
		if ((block.getType() != Material.TORCH) && (block.getType() != Material.REDSTONE_TORCH_ON))
		{
			return false;
		}
		Block altarBlock = block.getRelative(BlockFace.DOWN);

		this.plugin.logDebug("isAltarTorch(): AltarBlock block is " + altarBlock.getType().name());
		if (getGodTypeForAltarBlockType(altarBlock.getType()) == null)
		{
			return false;
		}
		if (altarBlock.getRelative(BlockFace.EAST).getType() == Material.WALL_SIGN)
		{
			return true;
		}
		if (altarBlock.getRelative(BlockFace.WEST).getType() == Material.WALL_SIGN)
		{
			return true;
		}
		if (altarBlock.getRelative(BlockFace.NORTH).getType() == Material.WALL_SIGN)
		{
			return true;
		}
		if (altarBlock.getRelative(BlockFace.SOUTH).getType() == Material.WALL_SIGN)
		{
			return true;
		}
		return false;
	}

	public boolean isPrayingAltar(Block block)
	{
		return isAltarSign(block);
	}

	public boolean isCursingAltar(Block block, String[] lines)
	{
		if (!isAltarSign(block))
		{
			return false;
		}
		return getCursedPlayerFromAltar(block, lines) != null;
	}

	public boolean isBlessingAltar(Block block, String[] lines)
	{
		if (!isAltarSign(block))
		{
			return false;
		}
		return getBlessedPlayerFromAltarSign(block, lines) != null;
	}

	public boolean handleNewCursingAltar(SignChangeEvent event)
	{
		Player player = event.getPlayer();
		if ((!player.isOp()) && (!this.plugin.getPermissionsManager().hasPermission(player, "gods.altar.build")))
		{
			this.plugin.sendInfo(player.getUniqueId(), LanguageManager.LANGUAGESTRING.BuildAltarNotAllowed, ChatColor.DARK_RED, 0, "", 10);
			return false;
		}
		event.setLine(0, "Curses");
		event.setLine(1, "On");

		event.setLine(3, "");

		this.plugin.sendInfo(player.getUniqueId(), LanguageManager.LANGUAGESTRING.CursesHelp, ChatColor.AQUA, 0, "", 20);

		return true;
	}

	public boolean handleNewBlessingAltar(SignChangeEvent event)
	{
		Player player = event.getPlayer();

		if (!player.isOp() && !this.plugin.getPermissionsManager().hasPermission(player, "gods.altar.build"))
		{
			return false;
		}
		event.setLine(0, "Blessings");
		event.setLine(1, "On");

		event.setLine(3, "");

		this.plugin.sendInfo(player.getUniqueId(), LanguageManager.LANGUAGESTRING.BlessingsHelp, ChatColor.AQUA, 0, "", 10);

		return true;
	}

	public boolean handleNewSacrificingAltar(SignChangeEvent event)
	{
		Player player = event.getPlayer();
		if ((!player.isOp()) && (!this.plugin.getPermissionsManager().hasPermission(player, "gods.altar.build")))
		{
			this.plugin.sendInfo(player.getUniqueId(), LanguageManager.LANGUAGESTRING.BuildAltarNotAllowed, ChatColor.DARK_RED, 0, "", 10);
			return false;
		}
		return false;
	}

	public boolean handleNewPrayingAltar(SignChangeEvent event)
	{
		Player player = event.getPlayer();
		if ((!player.isOp()) && (!this.plugin.getPermissionsManager().hasPermission(player, "gods.altar.build")))
		{
			this.plugin.sendInfo(player.getUniqueId(), LanguageManager.LANGUAGESTRING.BuildAltarNotAllowed, ChatColor.DARK_RED, 0, "", 10);
			return false;
		}

		String godName = "";
		int line = 0;
		int otherline = 0;
		while ((godName.isEmpty()) && (line < 4))
		{
			godName = event.getLine(line++);
		}

		line--;

		while (otherline < 4)
		{
			if (otherline == line)
			{
				otherline++;
			}
			else
			{
				String text = event.getLine(otherline);

				if ((text != null) && (text.length() > 0))
				{
					this.plugin.sendInfo(player.getUniqueId(), LanguageManager.LANGUAGESTRING.InvalidAltarSign, ChatColor.DARK_RED, 0, "", 10);
					return false;
				}
				otherline++;
			}
		}

		if (godName.length() <= 1)
		{
			this.plugin.sendInfo(player.getUniqueId(), LanguageManager.LANGUAGESTRING.InvalidGodName, ChatColor.DARK_RED, 0, "", 20);
			return false;
		}

		godName = godName.trim();
		godName = this.plugin.getGodManager().formatGodName(godName);

		if ((godName.length() <= 1) || (godName.contains(" ")))
		{
			this.plugin.sendInfo(player.getUniqueId(), LanguageManager.LANGUAGESTRING.InvalidGodName, ChatColor.DARK_RED, 0, "", 20);
			return false;
		}

		if ((this.plugin.isBlacklistedGod(godName)) || (!this.plugin.isWhitelistedGod(godName)))
		{
			this.plugin.sendInfo(player.getUniqueId(), LanguageManager.LANGUAGESTRING.PrayToBlacklistedGodNotAllowed, ChatColor.DARK_RED, 0, "", 1);
			return false;
		}

		if (!this.plugin.getGodManager().hasGodAccess(player.getUniqueId(), godName))
		{
			this.plugin.sendInfo(player.getUniqueId(), LanguageManager.LANGUAGESTRING.PrivateGodNoAccess, ChatColor.DARK_RED, 0, "", 1);
			return false;
		}

		String currentGodName = plugin.getBelieverManager().getGodForBeliever(player.getUniqueId());
		if (currentGodName!=null && !currentGodName.equals(godName))
		{
			this.plugin.sendInfo(player.getUniqueId(), LanguageManager.LANGUAGESTRING.CannotBuildAltarToOtherGods, ChatColor.DARK_RED, 0, godName, 1);
			return false;
		}
		
		Block altarBlock = this.plugin.getAltarManager().getAltarBlockFromSign(player.getWorld().getBlockAt(event.getBlock().getLocation()));

		if (altarBlock == null)
		{
			return false;
		}
		if (this.plugin.getGodManager().addAltar(event.getPlayer(), godName, event.getBlock().getLocation()))
		{
			if ((this.plugin.holyLandEnabled) && (this.plugin.getPermissionsManager().hasPermission(player, "gods.holyland")))
			{
				this.plugin.getLandManager().setPrayingHotspot(player.getName(), godName, altarBlock.getLocation());
			}
			this.plugin.getQuestManager().handleBuiltPrayingAltar(godName);

			event.setLine(0, "Altar");
			event.setLine(1, "of");
			event.setLine(2, godName);
			event.setLine(3, "");

			this.plugin.sendInfo(event.getPlayer().getUniqueId(), LanguageManager.LANGUAGESTRING.PrayAlterHelp, ChatColor.AQUA, 0, godName, 80);
		}
		else
		{
			event.setLine(0, "");
			event.setLine(1, "");
			event.setLine(2, "");
			event.setLine(3, "");

			return false;
		}
		
		return true;
	}

	public void addDroppedItem(int entityID, String playerName)
	{
		this.droppedItems.put(Integer.valueOf(entityID), playerName);
	}

	public String getDroppedItemPlayer(int entityID)
	{
		return (String) this.droppedItems.get(Integer.valueOf(entityID));
	}

	public void clearDroppedItems()
	{
		this.plugin.logDebug("Cleared " + this.droppedItems.size() + " dropped items");
		this.droppedItems.clear();
	}
}