package com.kong.wd.dao.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.kong.wd.model.LaunchAppParamsBean;
import com.kong.wd.model.StepBean;
import com.kong.wd.model.TestBean;

public class XmlParse {
	private static final Logger logger = Logger.getLogger(XmlParse.class);
	private Document document = null;

	public XmlParse(File ucXMLFile) {
		try {
			logger.debug("Pause XML document start: " + ucXMLFile.getName());
			document = new SAXReader().read(ucXMLFile);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public Element findElementUnderRoot(String targetElement) {
		Element target = null;
		Element suiteElm = document.getRootElement();
		// settings element
		if (targetElement != null) {
			target = suiteElm.element(targetElement);
			if (target != null) {
				logger.debug("find your target element: " + targetElement);
				return target;
			}
		}
		logger.debug("Did not find your target element: " + targetElement);
		return null;
	}

	public LaunchAppParamsBean parseSettings(Element settings) {
		if (settings == null) {
			return null;
		}
		return new LaunchAppParamsBean(
				settings.attributeValue("application_url"),
				settings.attributeValue("browser"),
				settings.attributeValue("seleniumServer"),
				settings.attributeValue("seleniumPort"));

	}

	public TestBean parseTest(Element testElm) {
		// TEST element
		// Element testElm = suiteElm.element("TEST");
		TestBean testBean = new TestBean(testElm.attributeValue("name"));
		if (testElm.attributeValue("timeout") != null) {
			// Set timeout
			testBean.setTimeout(new Integer(testElm.attributeValue("timeout"))
					.intValue());
		}
		logger.debug("test name: " + testBean);

		List<Element> stepNodes = testElm.element("STEPS").elements();
		List<StepBean> steps = new ArrayList<StepBean>();

		Element elm = null;
		// Iterator steps nodes
		Iterator<Element> iterator = null;
		iterator = stepNodes.iterator();
		while (iterator != null && iterator.hasNext()) {
			elm = iterator.next();
			StepBean stepBean = new StepBean();
			stepBean.setIndex(new Integer(elm.attributeValue("index"))
					.intValue());
			stepBean.setName(elm.attributeValue("name"));
			stepBean.setType(elm.attributeValue("type"));
			if (elm.element("XPATH") != null) {
				stepBean.setXpath(elm.element("XPATH").getText());
			}
			if (elm.element("VALUE") != null) {
				stepBean.setValue(elm.element("VALUE").getText());
			}
			logger.debug("------------------------------>");
			logger.debug(stepBean.toString());
			String type = stepBean.getType();
			if (type == null) {
				throw new RuntimeException("type of step is null!");
			}
			steps.add(stepBean);

		}
		testBean.setSteps(steps);
		return testBean;
	}

}
