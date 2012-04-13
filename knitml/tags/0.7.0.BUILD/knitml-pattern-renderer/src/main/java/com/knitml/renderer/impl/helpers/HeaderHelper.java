package com.knitml.renderer.impl.helpers;

import static com.knitml.core.common.EnumUtils.fromEnum;
import static org.apache.commons.lang.StringUtils.isBlank;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.measure.Measurable;
import javax.measure.Measure;
import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.apache.commons.lang.StringUtils;

import com.knitml.core.common.NeedleStyle;
import com.knitml.core.model.common.Color;
import com.knitml.core.model.common.NeedleType;
import com.knitml.core.model.common.Yarn;
import com.knitml.core.model.common.YarnType;
import com.knitml.core.model.pattern.Author;
import com.knitml.core.model.pattern.Gauge;
import com.knitml.core.model.pattern.GeneralInformation;
import com.knitml.core.model.pattern.Supplies;
import com.knitml.core.units.RowGauge;
import com.knitml.core.units.StitchGauge;
import com.knitml.core.units.Units;
import com.knitml.renderer.context.Options;

public class HeaderHelper {

	private static final String INSTRUCTION_DEFS_KEY = "instructionDefs";

	private WriterHelper writerHelper;
	private Options options;

	private WriterHelper getWriterHelper() {
		return writerHelper;
	}

	public HeaderHelper(WriterHelper writerHelper, Options options) {
		this.writerHelper = writerHelper;
		this.options = options;
	}

	private void writeLine(String text) {
		getWriterHelper().writeLine(text);
	}

	private void write(String string) {
		getWriterHelper().write(string);
	}

	private void writeNewLine() {
		getWriterHelper().writeNewLine();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void renderGeneralInformation(GeneralInformation generalInformation) {
		writeLine(generalInformation.getPatternName());
		Author author = generalInformation.getAuthor();
		if (author != null) {
			write("By ");
			write(author.getFirstName());
			write(" ");
			write(author.getLastName());
			writeNewLine();
		}
		String description = generalInformation.getDescription();
		description = writerHelper.convertSystemToOutputLines(description);
		if (!StringUtils.isBlank(description)) {
			writeLine(description);
		}
		String dimensions = generalInformation.getDimensions();
		if (!StringUtils.isBlank(dimensions)) {
			writeLine("Dimensions: " + dimensions);
		}
		String copyright = generalInformation.getCopyright();
		if (!StringUtils.isBlank(copyright)) {
			writeLine(copyright);
		}
		writeNewLine();
		Gauge gauge = generalInformation.getGauge();
		if (gauge != null) {
			if (options.isSquareGauge()) {
				renderSquareGauge(gauge);
			} else {
				DecimalFormat format = new DecimalFormat();
				format.setMaximumFractionDigits(1);
				if (gauge.getStitchGauge() != null) {
					Unit<StitchGauge> targetGaugeUnit;
					if (options.getStitchGaugeUnit() != null) {
						targetGaugeUnit = options.getStitchGaugeUnit();
					} else {
						targetGaugeUnit = ((Measure)gauge.getStitchGauge()).getUnit();
					}
					write("Stitch Gauge: ");
					double gaugeAsDouble = gauge.getStitchGauge().doubleValue(
							targetGaugeUnit);
					write(format.format(gaugeAsDouble));
					write(" ");
					write(targetGaugeUnit.toString());
					writeNewLine();
				}
				if (gauge.getRowGauge() != null) {
					Unit<RowGauge> targetGaugeUnit;
					write("Row Gauge: ");
					if (options.getRowGaugeUnit() != null) {
						targetGaugeUnit = options.getRowGaugeUnit();
					} else {
						targetGaugeUnit = ((Measure)gauge.getRowGauge()).getUnit();
					}
					double gaugeAsDouble = gauge.getRowGauge().doubleValue(
							targetGaugeUnit);
					write(format.format(gaugeAsDouble));
					write(" ");
					write(targetGaugeUnit.toString());
					writeNewLine();
				}
			}
		}
		writeNewLine();

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void renderSquareGauge(Gauge gauge) {
		int denominator = 1;
		Unit targetGaugeUnit = null;
		Unit<?> denominatorUnit;
		if (gauge.getStitchGauge() != null) {
			if (options.getStitchGaugeUnit() != null) {
				targetGaugeUnit = options.getStitchGaugeUnit();
			} else {
				targetGaugeUnit = ((Measure)gauge.getStitchGauge()).getUnit();
			}
			denominatorUnit = targetGaugeUnit.divide(Units.STITCH).inverse();
		} else if (gauge.getRowGauge() != null) {
			if (options.getRowGaugeUnit() != null) {
				targetGaugeUnit = options.getRowGaugeUnit();
			} else {
				targetGaugeUnit = ((Measure)gauge.getRowGauge()).getUnit();
			}
			denominatorUnit = targetGaugeUnit.divide(Units.ROW)
					.inverse();
		} else {
			// no gauge at all. Weird, and maybe not even possible
			return;
		}

		denominatorUnit.asType(Length.class);
		if (Units.CENTIMETER.equals(denominatorUnit)) {
			denominator = 10;
		} else if (Units.INCH.equals(denominatorUnit)) {
			denominator = 4;
		}

		write("Gauge:  ");
		if (gauge.getStitchGauge() != null) {
			write(String.valueOf(Math.round(gauge.getStitchGauge().doubleValue(
					targetGaugeUnit)
					* denominator)));
			write(" sts");
			if (gauge.getRowGauge() != null) {
				write(", ");
			} else {
				write(" ");
			}
		}
		if (gauge.getRowGauge() != null) {
			write(String.valueOf(Math.round(gauge.getRowGauge().doubleValue(
					targetGaugeUnit)
					* denominator)));
			write(" rows ");
		}
		write("= ");
		Measurable<?> size = Measure.valueOf(denominator, denominatorUnit);
		write(size.toString());
		writeNewLine();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void renderNeedles(Supplies supplies) {
		List<NeedleType> needleTypes = supplies.getNeedleTypes();
		if (needleTypes == null || needleTypes.size() == 0) {
			return;
		}
		writeLine("Needles:");
		getWriterHelper().incrementIndent();
		for (NeedleType needleType : needleTypes) {
			List<String> tokens = new ArrayList<String>(6);
			int needleCount = needleType.getNeedles().size();
			if (needleCount == 0) {
				break;
			}
			tokens.add(String.valueOf(needleCount));
			NeedleStyle style = needleType.getStyle();
			if (style == NeedleStyle.DPN) {
				tokens.add("double-pointed");
			} else {
				tokens.add(fromEnum(style));
			}
			tokens.add(needleCount == 1 ? "needle" : "needles");
			if (needleType.getNeedleSize() != null) {
				Measurable<Length> needleSizeMeasurable = needleType
						.getNeedleSize();
				tokens.add("size " + needleSizeMeasurable.toString());
				if (needleSizeMeasurable instanceof Measure) {
					Measure needleSize = (Measure) needleSizeMeasurable;
					Unit unit = needleSize.getUnit();
					// always display the standard unit
					if (!unit.equals(Units.NEEDLE_SIZE_MM)) {
						StringBuffer sb = new StringBuffer();
						sb.append("(").append(
								needleSize.to(Units.NEEDLE_SIZE_MM))
								.append(")");
						tokens.add(sb.toString());
					}
				}
			}

			// attributes in parentheses: consider making brand an optional
			// display?
			List<String> subtokens = new ArrayList<String>(2);
			if (needleType.getBrand() != null) {
				subtokens.add(needleType.getBrand());
			}
			if (needleType.getLength() != null) {
				subtokens.add(needleType.getLength().toString());
			}
			String nextAttributes = StringUtils.join(subtokens.toArray(), ", ");
			if (!StringUtils.isBlank(nextAttributes)) {
				// appends a colon to the last token
				int lastPosition = tokens.size() - 1;
				tokens.set(lastPosition, tokens.get(lastPosition) + ":");
				tokens.add(nextAttributes);
			}
			delimitAndFlushTokens(tokens);
		}
		getWriterHelper().decrementIndent();
		writeNewLine();
	}

	public void renderYarns(Supplies supplies) {
		List<YarnType> yarnTypes = supplies.getYarnTypes();
		if (yarnTypes == null || yarnTypes.isEmpty()) {
			return;
		}
		boolean multipleYarnsInProject = false;
		if (yarnTypes.size() > 1) {
			multipleYarnsInProject = true;
		}

		writeLine("Yarn:");
		getWriterHelper().incrementIndent();
		for (YarnType yarnType : yarnTypes) {
			List<String> tokens = new ArrayList<String>(12);
			int yarnCount = yarnType.getYarns().size();
			if (yarnCount == 1) {
				constructSingleYarnType(yarnType, tokens,
						multipleYarnsInProject);
				delimitAndFlushTokens(tokens);
			} else {
				boolean hasBrand = constructYarnTypeBrand(yarnType, tokens);
				if (yarnType.getWeight() != null) {
					StringBuffer sb = new StringBuffer(20);
					if (hasBrand) {
						sb.append("(").append(yarnType.getWeight()).append(
								" weight").append(")");
					} else {
						sb.append(yarnType.getWeight()).append(" weight");
					}
					tokens.add(sb.toString());
				}
				delimitAndFlushTokens(tokens);

				getWriterHelper().incrementIndent();
				for (Yarn yarn : yarnType.getYarns()) {
					Color color = yarn.getColor();
					if (color != null) {
						tokens.add("Color " + yarn.getSymbol() + ":");
						if (color.getName() != null) {
							tokens.add(color.getName());
						}
						if (color.getNumber() != null) {
							tokens.add(color.getNumber());
						}
					}
					List<String> subtokens = new ArrayList<String>(2);
					if (yarn.getTotalWeight() != null) {
						subtokens.add(yarn.getTotalWeight().toString());
					}
					if (yarn.getTotalLength() != null) {
						subtokens.add(yarn.getTotalLength().toString());
					}
					String nextAttributes = StringUtils.join(subtokens
							.toArray(), " / ");
					if (!StringUtils.isBlank(nextAttributes)) {
						StringBuffer sb = new StringBuffer();
						sb.append("(").append(nextAttributes).append(")");
						tokens.add(sb.toString());
					}
					delimitAndFlushTokens(tokens);
				}
				getWriterHelper().decrementIndent();
			}
		}
		getWriterHelper().decrementIndent();
		writeNewLine();
	}

	private List<String> constructSingleYarnType(YarnType yarnType,
			List<String> tokens, boolean multipleYarnsInProject) {

		boolean hasAmount = false;
		boolean hasBrand = false;
		boolean hasWeight = false;
		Yarn yarn = yarnType.getYarns().get(0);

		// symbol
		if (multipleYarnsInProject) {
			tokens.add(yarn.getSymbol() + ":");
		}

		// amount
		if (yarn.getTotalWeight() != null) {
			hasAmount = true;
			tokens.add(yarn.getTotalWeight().toString());
		}
		if (yarn.getTotalLength() != null) {
			hasAmount = true;
			if (yarn.getTotalWeight() != null) {
				tokens.add("(" + yarn.getTotalLength() + ")");
			} else {
				tokens.add(yarn.getTotalLength().toString());
			}
		}
		if (hasAmount) {
			tokens.add("of");
		}

		// brand
		hasBrand = constructYarnTypeBrand(yarnType, tokens);

		// weight
		if (!isBlank(yarnType.getWeight())) {
			hasWeight = true;
			StringBuffer sb = new StringBuffer();
			if (hasBrand) {
				sb.append("(");
				sb.append(yarnType.getWeight());
				sb.append(" weight");
				sb.append(")");
			} else {
				sb.append(yarnType.getWeight());
				sb.append(" weight yarn");
			}
			tokens.add(sb.toString());
		} else if (!hasBrand) {
			// no yarn weight, no brand, so write "yarn"
			tokens.add("yarn");
		}
		Color color = yarn.getColor();
		if (color != null) {
			if (hasAmount || hasBrand || hasWeight) {
				tokens.add("in");
			}
			if (color.getName() != null) {
				tokens.add(color.getName());
			}
			if (color.getNumber() != null) {
				tokens.add("(" + color.getNumber() + ")");
			}
		}
		return tokens;
	}

	private boolean constructYarnTypeBrand(YarnType yarnType,
			List<String> tokens) {
		int originalNumberOfTokens = tokens.size();
		String brand = yarnType.getBrand();
		if (!isBlank(brand)) {
			tokens.add(brand);
		}
		String category = yarnType.getCategory();
		String subcategory = yarnType.getSubcategory();
		if (!isBlank(category)) {
			if (!isBlank(subcategory)) {
				tokens.add(category + ",");
				tokens.add(subcategory);
			} else {
				tokens.add(category);
			}
		}
		return (tokens.size() > originalNumberOfTokens);
	}

	public void renderAccessories(Supplies supplies) {
		if (!supplies.hasAccessories()) {
			return;
		}
		writeLine("Accessories:");
		getWriterHelper().incrementIndent();
		if (supplies.getStitchHolders().size() > 0) {
			int n = supplies.getStitchHolders().size();
			// TODO internationalize
			writeLine(n + (n == 1 ? " stitch holder" : " stitch holders"));
		}
		getWriterHelper().decrementIndent();
		writeNewLine();
	}
	
	private void delimitAndFlushTokens(List<String> tokens) {
		writeLine(StringUtils.join(tokens.toArray(), " "));
		tokens.clear();
	}

	public void startStoringInstructionDefinitions() {
		getWriterHelper().startWritingToSegment(INSTRUCTION_DEFS_KEY);
	}

	public void stopStoringInstructionDefinitions() {
		getWriterHelper().stopWritingToSegment(INSTRUCTION_DEFS_KEY);
	}

	public void renderInstructionDefinitions() {
		getWriterHelper().writeSegmentToWriter(INSTRUCTION_DEFS_KEY);
	}

}
