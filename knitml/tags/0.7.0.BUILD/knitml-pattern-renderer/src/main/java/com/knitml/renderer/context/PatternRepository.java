package com.knitml.renderer.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.NullArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.knitml.core.model.common.Needle;
import com.knitml.core.model.common.NeedleType;
import com.knitml.core.model.common.Yarn;
import com.knitml.core.model.operations.inline.InlineInstruction;

public class PatternRepository {

	private static final Logger log = LoggerFactory
			.getLogger(PatternRepository.class);

	private Map<String, Yarn> yarns = new LinkedHashMap<String, Yarn>();
	private Map<String, Needle> needles = new LinkedHashMap<String, Needle>();
	private Map<String, NeedleType> needleTypes = new LinkedHashMap<String, NeedleType>();
	private Map<String, InstructionInfo> localInstructions = new HashMap<String, InstructionInfo>();
	private Map<String, InstructionInfo> globalInstructions = new HashMap<String, InstructionInfo>();
	private Map<String, InlineInstruction> inlineInstructions = new HashMap<String, InlineInstruction>();

	private ReloadableResourceBundleMessageSource patternMessageSource;
	private Locale locale;

	public PatternRepository(Options options) {
		if (options == null) {
			throw new NullArgumentException("options");
		}
		this.locale = options.getLocale();
		this.patternMessageSource = new ReloadableResourceBundleMessageSource();
		this.patternMessageSource.setResourceLoader(options
				.getPatternMessageResourceLoader());
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
		if (yarn != null && yarn.getId() != null) {
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

	public void addLocalInstruction(InstructionInfo instructionInfo) {
		if (instructionInfo == null) {
			throw new NullArgumentException("instructionInfo");
		}
		localInstructions.put(instructionInfo.getId(), instructionInfo);
		log.debug("Added local instruction [{}] to the pattern repository",
				instructionInfo.getId());
	}

	public void addGlobalInstruction(InstructionInfo instructionInfo) {
		if (instructionInfo == null) {
			throw new NullArgumentException("instructionInfo");
		}
		globalInstructions.put(instructionInfo.getId(), instructionInfo);
		log.debug("Added global instruction [{}] to the pattern repository",
				instructionInfo.getId());
	}

	public void addInlineInstruction(InlineInstruction instruction) {
		if (instruction != null) {
			inlineInstructions.put(instruction.getId(), instruction);
		}
	}

	public InlineInstruction getInlineInstruction(String id) {
		return inlineInstructions.get(id);
	}

	public void setPatternMessageSources(List<String> messageSourceLocations) {
		this.patternMessageSource.setBasenames(messageSourceLocations
				.toArray(new String[0]));
	}

	public String getPatternMessage(String key) {
		return patternMessageSource.getMessage(key, null, getLocale());
	}

	public String getPatternMessage(String key, String defaultValue) {
		return patternMessageSource.getMessage(key, null, defaultValue,
				getLocale());
	}

	public void clearLocalInstructions() {
		this.localInstructions.clear();
	}

	protected Locale getLocale() {
		return locale;
	}

}
