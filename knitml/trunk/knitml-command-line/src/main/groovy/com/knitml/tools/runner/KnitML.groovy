package com.knitml.tools.runner

import org.apache.commons.lang.ArrayUtils

if (args.length < 1) {
	println '''Must specify command, which is one of:
validate, render, validateAndRender, convert, convertAndValidate, convertValidateAndRender'''
	System.exit(1)
}

String command = args[0]
String[] argsToPass
if (args.length < 2) {
	argsToPass = new String[0]
} else {
	argsToPass = ArrayUtils.subarray(args, 1, args.length)
}

switch (command.toLowerCase()) {
	case "render":
	case "r":
		RenderPattern.main(argsToPass)
		break
	case "validate":
	case "v":
		ValidatePattern.main(argsToPass)
		break
	case "validateandrender":
	case "validaterender":
	case "vr":
		ValidateAndRenderPattern.main(argsToPass)
		break
	case "convert":
	case "c":
		ConvertPattern.main(argsToPass)
		break
	case "convertandvalidate":
	case "convertvalidate":
	case "cv":
		ConvertAndValidatePattern.main(argsToPass)
		break
	case "convertvalidateandrender":
	case "convertvalidaterender":
	case "cvr":
		ConvertValidateAndRenderPattern.main(argsToPass)
		break
}

