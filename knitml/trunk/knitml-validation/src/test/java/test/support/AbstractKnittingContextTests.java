/**
 * 
 */
package test.support;

import java.io.StringReader;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.knitml.core.model.Pattern;
import com.knitml.core.model.Version;
import com.knitml.engine.KnittingEngine;
import com.knitml.engine.common.KnittingEngineException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.KnittingContextFactory;
import com.knitml.validation.context.impl.DefaultKnittingContextFactory;
import com.knitml.validation.visitor.instruction.Visitor;
import com.knitml.validation.visitor.instruction.VisitorFactory;
import com.knitml.validation.visitor.instruction.impl.DefaultVisitorFactory;

@RunWith(BlockJUnit4ClassRunner.class)
public abstract class AbstractKnittingContextTests {

	protected KnittingContextFactory knittingContextFactory;
	protected VisitorFactory visitorFactory;
	protected KnittingEngine engine;
	protected KnittingContext knittingContext;

	protected static final String PATTERN_START_TAG = "<pattern:pattern xmlns:pattern=\"http://www.knitml.com/schema/pattern\" xmlns:common=\"http://www.knitml.com/schema/common\" xmlns=\"http://www.knitml.com/schema/operations\" version=\"" + Version.getCurrentVersionId() + "\">"; //$NON-NLS-1$ //$NON-NLS-2$
	private static final String DIRECTIONS_START_TAG = "<pattern:directions>"; //$NON-NLS-1$
	private static final String INSTRUCTION_GROUP_START_TAG = "<pattern:instruction-group id=\"ig1\">"; //$NON-NLS-1$
	private static final String PATTERN_END_TAG = "</pattern:pattern>"; //$NON-NLS-1$
	private static final String DIRECTIONS_END_TAG = "</pattern:directions>"; //$NON-NLS-1$
	private static final String INSTRUCTION_GROUP_END_TAG = "</pattern:instruction-group>"; //$NON-NLS-1$

	private static final String ROW_PREFIX = PATTERN_START_TAG
			+ DIRECTIONS_START_TAG + INSTRUCTION_GROUP_START_TAG;
	private static final String ROW_SUFFIX = INSTRUCTION_GROUP_END_TAG + DIRECTIONS_END_TAG + PATTERN_END_TAG;
	private static final String INSTRUCTION_GROUP_PREFIX = PATTERN_START_TAG
			+ DIRECTIONS_START_TAG;
	private static final String INSTRUCTION_GROUP_SUFFIX = DIRECTIONS_END_TAG + PATTERN_END_TAG;
	private static final String DIRECTIONS_PREFIX = PATTERN_START_TAG;
	private static final String DIRECTIONS_SUFFIX = PATTERN_END_TAG;

	@Before
	public final void setUp() throws Exception {
		knittingContextFactory = new DefaultKnittingContextFactory();
		knittingContext = knittingContextFactory.createKnittingContext();
		engine = knittingContext.getEngine();
		visitorFactory = new DefaultVisitorFactory();
		onSetItUp();
	}

	protected void onSetItUp() throws Exception {
	}

	protected Pattern parseXml(String xml) throws JiBXException {
		StringBuffer xmlToUse = new StringBuffer();
		String testXml = xml.trim();
		if (testXml.startsWith("<row") || testXml.startsWith("<pattern:section") || testXml.startsWith("<instruction ")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			xmlToUse.append(ROW_PREFIX).append(xml).append(ROW_SUFFIX);
		} else if (testXml.startsWith("<pattern:instruction-group")) { //$NON-NLS-1$
			xmlToUse.append(INSTRUCTION_GROUP_PREFIX).append(xml).append(
					INSTRUCTION_GROUP_SUFFIX);
		} else if (testXml.startsWith("<pattern:directions")) { //$NON-NLS-1$
			xmlToUse.append(DIRECTIONS_PREFIX).append(xml).append(
					DIRECTIONS_SUFFIX);
		} else {
			xmlToUse.append(xml);
		}

		IBindingFactory factory = BindingDirectory.getFactory(Pattern.class);
		IUnmarshallingContext uctx = factory.createUnmarshallingContext();
		return (Pattern) uctx.unmarshalDocument(new StringReader(xmlToUse.toString()));
	}

	protected void visitDocument(Pattern pattern)
			throws KnittingEngineException {
		Visitor visitor = this.visitorFactory.findVisitorFromClassName(pattern);
		visitor.visit(pattern, this.knittingContext);
	}

	protected Pattern processXml(String xml) throws JiBXException,
			KnittingEngineException {
		Pattern pattern = parseXml(xml);
		visitDocument(pattern);
		return pattern;
	}

}
