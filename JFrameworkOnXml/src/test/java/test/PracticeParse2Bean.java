package test;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.kong.wd.model.IBean;

public class PracticeParse2Bean {
	private static Document document = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File ucXMLFile = new File(System.getProperty("user.dir")
				+ File.separator + "UseCases" + File.separator
				+ "BaiduDWTest.xml");
		try {
			document = new SAXReader().read(ucXMLFile);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		Element root = document.getRootElement();
		IBean b = getElement(root, null);
		b.toString();
	}

	@SuppressWarnings("unchecked")
	private IBean getElement(Element element, IBean parentBean) {
		if (element == null) {
			return null;
		}

		// Build bean from element. Just consider current node
		IBean bean = null;
		try {
			bean = genBeanInstance(element);
			if (bean != null) {
				System.out.println(bean.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Set attributes
		if (element.attributeCount() > 1) {
			List<Attribute> attList = element.attributes();
			for (Iterator<Attribute> itsa = attList.iterator(); itsa.hasNext();) {
				Attribute att = (Attribute) itsa.next();
				if (bean != null) {
					setField(bean, att.getName(), att.getValue());

				}
			}
			System.out.println(bean.toString());
		}

		// If current element has children elements.
		List<Element> list = element.elements();
		List<IBean> childBeans = new ArrayList<IBean>();
		for (Iterator<Element> its = list.iterator(); its.hasNext();) {
			Element chileEle = (Element) its.next();
			System.out.println("节点：" + chileEle.getName() + ",内容：");
			IBean childBean = getElement(chileEle, bean);
			childBeans.add(childBean);
		}

		// Build children to bean
		if (childBeans.size() >= 1) {
			fullfilChild2Bean(bean, childBeans);
		} else {
			return bean;
		}

		if (parentBean != null) {
			fullfilChild2Bean(parentBean, bean);
		}

		return bean;
	}

	private <T> void fullfilChild2Bean(IBean bean, List<T> children) {
		setField(bean, null, children);
	}

	private void fullfilChild2Bean(IBean bean, Object child) {
		setField(bean, child.getClass().getName(), child);
	}

	private void setField(IBean bean, String fieldName, Object fieldValue) {
		Field field = null;
		try {
			if (fieldName != null) {
				field = bean.getClass().getDeclaredField(fieldName);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			if (field == null) {
				for (Field f : bean.getClass().getDeclaredFields()) {
					if (f.getType().equals(fieldValue.getClass())) {
						field = f;
						break;
					}
				}
			}
			if (field == null) {
				e.printStackTrace();
			}
		} finally {
			if (field != null) {
				field.setAccessible(true);
				try {
					field.set(bean,
							ConvertUtils.convert(fieldValue, field.getType()));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private IBean genBeanInstance(Element element)
			throws InstantiationException, IllegalAccessException {
		Class<?> clazz = null;
		try {
			clazz = Class.forName("com.kong.wd.model."
					+ ToCaseSentitive(element.getName()));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
		if (clazz == null) {
			return null;
		}
		return (IBean) clazz.newInstance();
	}

	private String ToCaseSentitive(String name) {
		if (name.matches("[A-Z]*")) {
			name = name.toLowerCase();
		}
		return String.valueOf(name.charAt(0)).toUpperCase()
				+ name.substring(1, name.length());
	}

}
