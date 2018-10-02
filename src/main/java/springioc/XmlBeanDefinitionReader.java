package springioc;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/7/31
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader{

    protected XmlBeanDefinitionReader(Map<String, BeanDefinition> registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    public void readerXML(String location) throws Exception{

        ResourceLoader resourceLoader = new ResourceLoader();
        //从资源中读取流
        InputStream inputStream = resourceLoader.getResource(location).getInputstream();
        //文件builder工厂
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //
        DocumentBuilder docBuilder = factory.newDocumentBuilder();

        Document doc = docBuilder.parse(inputStream);

        registerBeanDefinitions(doc);

        inputStream.close();
    }

    private void registerBeanDefinitions(Document doc) {

        Element root = doc.getDocumentElement();

        parseBeanDefinitions(root);
    }

    private void parseBeanDefinitions(Element root) {

        NodeList nl = root.getChildNodes();

        for (int i = 0; i < nl.getLength(); i++) {

            Node node = nl.item(i);

            if (node instanceof Element) {

                Element ele = (Element) node;

                processBeanDefinition(ele);
            }
        }
    }

    private void processBeanDefinition(Element ele) {

        String name = ele.getAttribute("name");

        String className = ele.getAttribute("class");

        BeanDefinition beanDefinition = new BeanDefinition();

        beanDefinition.setClassName(className);

        addPropertyValues(ele, beanDefinition);

        getRegistry().put(name, beanDefinition);
    }

    private void addPropertyValues(Element ele, BeanDefinition beanDefinition) {

        NodeList propertyNode = ele.getElementsByTagName("property");

        for (int i = 0; i < propertyNode.getLength(); i++) {

            Node node = propertyNode.item(i);

            if (node instanceof Element) {

                Element propertyEle = (Element) node;

                String name = propertyEle.getAttribute("name");

                String value = propertyEle.getAttribute("value");

                if (value != null && value.length() > 0) {
                    beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, value));
                } else {
                    String ref = propertyEle.getAttribute("ref");
                    if (ref == null || ref.length() == 0) {
                        throw new IllegalArgumentException(
                                "Configuration problem: <property> element for property '"
                                        + name + "' must specify a ref or value");
                    }
                    BeanReference beanRef = new BeanReference(name);
                    beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, beanRef));
                }
            }
        }
    }
}
