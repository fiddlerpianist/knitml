package com.knitml.renderer.impl.helpers;

import static com.knitml.core.common.KnittingShape.ROUND;
import static com.knitml.validation.visitor.util.InstructionUtils.areRowsConsecutive;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.IntRange;
import org.apache.commons.lang.math.Range;
import org.apache.commons.lang.text.StrTokenizer;
import org.springframework.context.MessageSource;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.model.common.Yarn;
import com.knitml.renderer.context.RenderingContext;
import com.knitml.renderer.plural.PluralRule;
import com.knitml.renderer.plural.PluralRuleFactory;

public class MessageHelper {

	private MessageSource messageSource;
	private Locale locale;
	private PluralRule pluralRule;

	private static final String PLURAL_CODE_SUFFIX = ".$$$";
	private static final String PLURAL_CODE_SHORT_SUFFIX = ".$$";
	private static final String PLURAL_CODE_ORDER = ".$$order";
	private static final String PLURAL_CODE_ORDER_DEFAULT = "0123456789";

	public MessageHelper(MessageSource messageSource,
			PluralRuleFactory pluralRuleFactory, Locale locale) {
		this.messageSource = messageSource;
		this.locale = locale;
		try {
			int pluralRuleCode = Integer
					.parseInt(getMessage("knitml.pattern-renderer.plural-rule"));
			this.pluralRule = pluralRuleFactory
					.createPluralRule(pluralRuleCode);
		} catch (Exception ex) {
			throw new RuntimeException(
					"A plural rule code was not found for this resource bundle. The key must be 'knitml.pattern-renderer.plural-rule' and the value must be a non-negative integer.");
		}
	}

	public String getRowLabel(KnittingShape knittingShape, List<Integer> rows,
			String yarnId, RenderingContext context) {
		Yarn yarn = context.getPatternRepository().getYarn(yarnId);

		String rowType = knittingShape == ROUND ? "round" : "row";
		StringBuffer key = new StringBuffer("operation");
		List<Object> values = new ArrayList<Object>();

		// if rows looks like [3,4,5,...] where ... continues consecutive numbering scheme
		if (areRowsConsecutive(rows) && rows.size() > 2) {
			return buildRowRangeString(knittingShape, new IntRange(rows.get(0),
					rows.get(rows.size() - 1)), yarn);
		}

		String rowNumberString = buildList(rows, true);
		if (StringUtils.isBlank(rowNumberString)) {
			key.append(".next-").append(rowType);
		} else {
			key.append(".").append(rowType);
			values.add(rowNumberString);
		}
		if (yarn != null && yarn.getSymbol() != null) {
			key.append(".with-yarn");
			values.add(yarn.getSymbol());
		}
		return getPluralizedMessage(key.toString(), rows.size(), values
				.toArray());
	}

	public String buildList(List<?> objects, boolean allListSeparators) {
		if (objects == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < objects.size(); i++) {
			Object object = objects.get(i);
			sb.append(object);
			if (i + 2 < objects.size()) {
				sb.append(getMessage("operation.list-item-separator"));
			} else if (i + 1 < objects.size()) {
				if (allListSeparators) {
					sb.append(getMessage("operation.list-item-separator"));
				} else {
					sb
							.append(" ")
							.append(
									getMessage("operation.list-item-separator.before-last"))
							.append(" ");
				}
			}
		}
		return sb.toString();
	}

	public String buildRowRangeString(KnittingShape knittingShape,
			Range rowRange, Yarn yarn) {
		StringBuffer key = new StringBuffer();
		if (ROUND.equals(knittingShape)) {
			key.append("operation.round");
		} else {
			key.append("operation.row");
		}
		List<Object> values = new ArrayList<Object>(3);
		values.add(rowRange.getMinimumInteger());
		if (rowRange.getMinimumInteger() < rowRange.getMaximumInteger()) {
			key.append(".range");
			values.add(rowRange.getMaximumInteger());
		}

		if (yarn != null && yarn.getSymbol() != null) {
			key.append(".with-yarn");
			values.add(yarn.getSymbol());
		}

		int numberOfRows = rowRange.getMaximumInteger()
				- rowRange.getMinimumInteger() + 1;
		return getPluralizedMessageNoVarargs(key.toString(), numberOfRows, values.toArray());
	}

	public String getMessage(String code, Object... args) {
		return getMessageNoVarargs(code, args);
	}

	public String getMessageNoVarargs(String code, Object[] args) {
		return this.messageSource.getMessage(code, args, this.locale);
	}

	public String getMessageWithDefault(String code, String defaultMessage,
			Object... args) {
		return this.messageSource.getMessage(code, args, defaultMessage,
				this.locale);
	}

	public String getPluralizedMessageNoVarargs(String code, double number,
			Object[] args) {
		if (args.length == 0) {
			args = new Object[] { number };
		}
		StrTokenizer tokens = new StrTokenizer(getMessageNoVarargs(code
				+ PLURAL_CODE_SUFFIX, args), ";");
		if (tokens.size() == 1) {
			// one message for all plural forms
			return tokens.nextToken();
		}
		int tokenIndex = pluralRule.getPluralForm(number);
		try {
			return tokens.getTokenArray()[tokenIndex];
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new RuntimeException("Expected "
					+ pluralRule.getNumberOfForms() + " plural forms for code "
					+ code + " but was only " + tokens.size());
		}
	}

	public String getPluralizedMessage(String code, double number,
			Object... args) {
		return getPluralizedMessageNoVarargs(code, number, args);
	}

	public String getPluralizedMessage(String code, int[] numbers,
			Object... args) {
		if (args.length == 0) {
			args = ArrayUtils.toObject(numbers);
		}
		List<String> messages = new ArrayList<String>(numbers.length);
		for (int i = 0; i < numbers.length; i++) {
			String message;
			StrTokenizer tokens = new StrTokenizer(getMessageNoVarargs(code
					+ PLURAL_CODE_SHORT_SUFFIX + i, args), ";");
			if (tokens.size() == 1) {
				// one message for all plural forms
				message = tokens.nextToken();
			} else {
				int tokenIndex = pluralRule.getPluralForm(numbers[i]);
				try {
					message = tokens.getTokenArray()[tokenIndex];
				} catch (ArrayIndexOutOfBoundsException ex) {
					throw new RuntimeException("Expected "
							+ pluralRule.getNumberOfForms()
							+ " plural forms for code " + code
							+ " but was only " + tokens.size());
				}
			}
			messages.add(message);
		}
		StringBuffer finalMessage = new StringBuffer();
		String defaultCodeOrder = PLURAL_CODE_ORDER_DEFAULT.substring(0,
				numbers.length);
		char[] orderChars = getMessageWithDefault(code + PLURAL_CODE_ORDER,
				defaultCodeOrder).toCharArray();
		for (int i = 0; i < orderChars.length; i++) {
			int index = Integer.parseInt(String.valueOf(orderChars[i]));
			finalMessage.append(messages.get(index));
		}
		return finalMessage.toString();
	}

	public boolean shouldCapitalizeSentences() {
		return true;
	}

}
