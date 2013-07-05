package digester.sample;

import java.io.File;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;

public class XmlRulesDriver {
	public static void main(String[] args) {
		try {

			File input = new File("catalog.xml");
			final File rules = new File("rules.xml");
			DigesterLoader loader = DigesterLoader.newLoader(new FromXmlRulesModule() {

				@Override
				protected void loadRules() {
					loadXMLRules(rules);
				}
				
			});
			Digester digester = loader.newDigester();

			Catalog catalog = (Catalog) digester.parse(input);
			System.out.println(catalog.toString());

		} catch (Exception exc) {
			
			exc.printStackTrace();
		}
	}
}