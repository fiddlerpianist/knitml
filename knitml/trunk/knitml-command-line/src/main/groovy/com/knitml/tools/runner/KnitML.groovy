package com.knitml.tools.runner

import org.apache.commons.lang.ArrayUtils

if (args.length < 1) {
	println '''Must specify command, which is one of:
validate, render, convert, convertAndValidate, convertAndRender'''
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
	case "validateandrender":
	case "validaterender":
	case "r":
	case "vr":
	case "renderPattern":
		RenderPattern.main(argsToPass, command)
		break
	case "validate":
	case "validatePattern":
	case "v":
		ValidatePattern.main(argsToPass, command)
		break
	case "convert":
	case "c":
		ConvertPattern.main(argsToPass)
		break
	case "convertandvalidate":
	case "convertvalidate":
	case "cv":
		ConvertAndValidatePattern.main(argsToPass, command)
		break
	case "convertvalidateandrender":
	case "convertvalidaterender":
	case "cvr":
	case "convertandrender":
	case "convertrender":
	case "cr":
		ConvertAndRenderPattern.main(argsToPass, command)
		break
}

