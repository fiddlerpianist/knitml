package com.knitml.core.xml;


public final class Elements {
	
	private Elements() {
	}

	public static final String PATTERN_NAME = "name";
	public static final String PATTERN_DESCRIPTION = "description";
	public static final String DIMENSIONS = "dimensions";
	
	public static final String UNTIL_DESIRED_LENGTH = "until-desired-length";
	public static final String UNTIL_STITCHES_REMAIN = "until-stitches-remain";
	public static final String UNTIL_STITCHES_REMAIN_ON_NEEDLES = "until-stitches-remain-on-needles";
	public static final String UNTIL_MEASURES = "until-measures";
	public static final String UNTIL_EQUALS = "until-equals";
	public static final String ADDITIONAL_TIMES = "additional-times";
	public static final String NEEDLES = "needles";
	public static final String NEEDLE_TYPE = "needle-type";
	public static final String NEEDLE = "needle";
	public static final String YARNS = "yarns";
	public static final String YARN = "yarn";
	public static final String YARN_TYPE = "yarn-type";
	public static final String SIZE = "size";
	public static final String LENGTH = "length";
	public static final String INSTRUCTION = "instruction";
	public static final String INSTRUCTION_GROUP = "instruction-group";
	public static final String STITCH_COUNT = "stitch-count";
	public static final String VALUE = "value";
	public static final String DIRECTIONS = "directions";
	public static final String PATTERN = "pattern";
	public static final String GAUGE = "gauge";
	public static final String STITCH_GAUGE = "stitch-gauge";
	public static final String ROW_GAUGE = "row-gauge";
	public static final String TECHNIQUES = "techniques";
	public static final String AUTHOR = "author";
	public static final String FIRST_NAME = "first-name";
	public static final String LAST_NAME = "last-name";
	public static final String COPYRIGHT_INFO = "copyright-info";
	
	
	public static final String TURN = "turn";
	public static final String ARRANGE_STITCHES_ON_NEEDLES = "arrange-stitches-on-needles";
	public static final String USE_NEEDLES = "use-needles";
	public static final String USING_NEEDLE = "using-needle";
	public static final String ROW = "row";
	public static final String DECREASE = "decrease";
	public static final String INCREASE = "increase";
	public static final String DESIGNATE_END_OF_ROW = "designate-end-of-row";
	public static final String PURL = "purl";
	public static final String DOUBLE_DECREASE = "double-decrease";
	public static final String INCREASE_INTO_NEXT_STITCH = "increase-into-next-stitch";
	public static final String KNIT = "knit";
	public static final String REMOVE_MARKER = "remove-marker";
	public static final String PICK_UP_STITCHES = "pick-up-stitches";
	public static final String CAST_ON = "cast-on";
	public static final String PLACE_MARKER = "place-marker";
	public static final String SLIP = "slip";
	public static final String REPEAT_INSTRUCTION = "repeat-instruction"; 
	public static final String MESSAGE = "message"; 
	public static final String BALL_LENGTH = "ball-length";
	public static final String BALL_WEIGHT = "ball-weight";
	public static final String TOTAL_LENGTH = "total-length";
	public static final String TOTAL_WEIGHT = "total-weight";
	public static final String COLOR = "color";
	
	public static final String[] KNITTING_OPERATIONS = { CAST_ON, KNIT, PURL, DECREASE, DOUBLE_DECREASE, INCREASE, INCREASE_INTO_NEXT_STITCH, SLIP, TURN, PLACE_MARKER, PICK_UP_STITCHES, DESIGNATE_END_OF_ROW, REMOVE_MARKER };
	
}
