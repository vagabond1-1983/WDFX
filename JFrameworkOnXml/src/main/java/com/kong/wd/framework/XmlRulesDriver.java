package com.kong.wd.framework;

import java.io.File;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;

import com.kong.wd.model.Suite;


public class XmlRulesDriver {
	public static void main(String[] args) {
		Suite s = xml2Bean("UseCases/BaiduDWTest.xml", "baidu-rules.xml");
		s.toString();
	}
	
	public static Suite xml2Bean(String testXml, String ruleName) {
		Suite suite = null;
		try {

			File input = new File(testXml);
			final File rules = new File(ruleName);
			DigesterLoader loader = DigesterLoader.newLoader(new FromXmlRulesModule() {

				@Override
				protected void loadRules() {
					loadXMLRules(rules);
				}
				
			});
			Digester digester = loader.newDigester();

			suite = (Suite) digester.parse(input);
			System.out.println(suite.toString());
			
		} catch (Exception exc) {
			
			exc.printStackTrace();
		}
		return suite;
	}
}