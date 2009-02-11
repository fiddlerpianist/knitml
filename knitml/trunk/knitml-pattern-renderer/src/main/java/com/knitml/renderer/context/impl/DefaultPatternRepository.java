package com.knitml.renderer.context.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.math.IntRange;
import org.apache.commons.lang.math.Range;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.ResourceLoader;

import com.knitml.core.model.directions.block.Instruction;
import com.knitml.core.model.directions.block.Row;
import com.knitml.core.model.header.Needle;
import com.knitml.core.model.header.NeedleType;
import com.knitml.core.model.header.Yarn;
import com.knitml.renderer.context.InstructionInfo;
import com.knitml.renderer.context.PatternRepository;

public class DefaultPatternRepository implements PatternRepository {

	private Map<String, Yarn> yarns = new LinkedHashMap<String, Yarn>();
	private Map<String, Needle> needles = new LinkedHashMap<String, Needle>();
	private Map<String, NeedleType> needleTypes = new LinkedHashMap<String, NeedleType>();
	private Map<String, InstructionInfo> localInstructions = new HashMap<String, InstructionInfo>();
	private Map<String, InstructionInfo> globalInstructions = new HashMap<String, InstructionInfo>();

	private ReloadableResourceBundleMessageSource patternMessageSource;
	private Locale locale;
	private ResourceLoader messageSourceResourceLoader;

	public DefaultPatternRepository() {
		this.patternMessageSource = new ReloadableResourceBundleMessageSource();
		this.patternMessageSource
				.setResourceLoader(new FileSystemResourceLoader());
	}

	public void initialize() {
		if (messageSourceResourceLoader != null) {
			this.patternMessageSource
					.setResourceLoader(messageSourceResourceLoader);
		}
	}

	public Collection<Yarn> getYarns() {
		return yarns.values();
	}

	public Collection<Needle> getNeedles() {
		return needles.values();
	}

	public Collection<NeedleType> getNeedleTypes() {
		return needleTypes.values();
	}

	public Yarn getYarn(String id) {
		return yarns.get(id);
	}

	public void addYarn(Yarn yarn) {
		if (yarn != null) {
			yarns.put(yarn.getId(), yarn);
		}
	}

	public Needle getNeedle(String id) {
		return needles.get(id);
	}

	public NeedleType getNeedleType(String id) {
		return needleTypes.get(id);
	}

	public void addNeedle(Needle needle) {
		if (needle != null) {
			needles.put(needle.getId(), needle);
		}
	}

	public void addNeedleType(NeedleType needleType) {
		if (needleType != null) {
			needleTypes.put(needleType.getId(), needleType);
		}
	}

	public InstructionInfo getInstruction(String instructionId) {
		InstructionInfo instructionInfo = localInstructions.get(instructionId);
		if (instructionInfo == null) {
			instructionInfo = globalInstructions.get(instructionId);
		}
		return instructionInfo;
	}

	public void addLocalInstruction(Instruction instruction) {
		if (instruction != null) {
			InstructionInfo instructionInfo = new InstructionInfo(instruction);
			Range rowRange = deriveRowNumbers(instruction);
			if (rowRange != null) {
				instructionInfo.setRowRange(rowRange);
			}
			localInstructions.put(instruction.getId(), instructionInfo);
		}
	}

	public void addGlobalInstruction(Instruction instruction, String label) {
		InstructionInfo instructionInfo = new InstructionInfo(instruction,
				label);
		Range rowRange = deriveRowNumbers(instruction);
		if (rowRange != null) {
			instructionInfo.setRowRange(rowRange);
		}
		globalInstructions.put(instruction.getId(),
				instructionInfo);
	}

	private Range deriveRowNumbers(Instruction instruction) {
		if (!instruction.hasRows()) {
			return null;
		}
		List<Row> rows = instruction.getRows();
		SortedSet<Integer> rowNumberSet = new TreeSet<Integer>();
		for (Row row : rows) {
			if (row.getNumbers() == null) {
				// if at any point we encounter a null row number, return null
				return null;
			} else {
				for (Integer i : row.getNumbers()) {
					rowNumberSet.add(i);
				}
			}
		}
		return new IntRange(rowNumberSet.first(), rowNumberSet.last());
	}

	public void setPatternMessageSources(List<String> messageSourceLocations) {
		this.patternMessageSource.setBasenames(messageSourceLocations
				.toArray(new String[0]));
	}

	public String getPatternMessage(String key) {
		if (patternMessageSource != null) {
			return patternMessageSource.getMessage(key, null, getLocale());
		}
		return null;
	}

	public String getPatternMessage(String key, String defaultValue) {
		if (patternMessageSource != null) {
			return patternMessageSource.getMessage(key, null, defaultValue,
					getLocale());
		}
		return null;
	}

	// declaring for stub generation
	public Locale getLocale() {
		return this.locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setMessageSourceResourceLoader(
			ResourceLoader messageSourceResourceLoader) {
		this.messageSourceResourceLoader = messageSourceResourceLoader;
	}

	public void clearLocalInstructions() {
		this.localInstructions.clear();
	}

}
