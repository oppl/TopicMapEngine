package ce.tm4scholion.tm.persistency.xtm;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.dom4j.DocumentException;
import org.dom4j.io.XMLWriter;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ce.tm4scholion.tm.TopicMap;
import ce.tm4scholion.tm.persistency.TMPersistency;

/**
 * Topic Map Engine - XTM Persistency - TMxtmPersitency
 * 
 * TMxtmPersistency provides functionality to store Topic Maps as XTM 2.0 files and retrieve Topic Maps from XTM 2.0 files
 * 
 *
 */
public class TMxtmPersistency implements TMPersistency {

	private String tmResultXtm ;//= this.getClass().getResource("TMxtmResult.xml").getFile();//default value if someone does not open the connection
	
	/**
	 * default constructor
	 *
	 */
	public TMxtmPersistency(){}
		
	/**
	 * connect to the data source/sink, from which the TopicMap should be retrieved or should be stored in
	 * 
	 * @param connection a string which represents the path of an existing file in your file system
	 * @return true, if successfully connected, false otherwise
	 */	
	public boolean connect(String connection) {
		File f = new File(connection);
		if(f.exists()){
			this.tmResultXtm = connection;
			return true;
		}else
			return false;
	}

	/**
	 * retrieve a TopicMap from the connected data source
	 * 
	 * @return the retrieved TopicMap
	 */	
	public TopicMap retrieve() {
		//create XtmTopicMap
		XtmTopicMap tm = createXtmTMObject(tmResultXtm);
		
		//set proper values 4 superclass variables of XtmTopicMap
		tm.setTopicMapValues();
		
		return tm;
	}
	
	/**
	 * store the given TopicMap into the connected data sink
	 * 
	 * @param tm the TopicMap to be stored
	 * @return true, if the TopicMap was successfully stored, otherwise false
	 */	
	public boolean store(TopicMap tm) {
		//create w3c Document representation of tm
		Document doc = createDocument(tm);
        
		//add xmlns attribute to TopicMap element
		org.w3c.dom.Element r = (org.w3c.dom.Element)doc.getFirstChild();
		r.setAttribute("xmlns", "http://www.topicmaps.org/xtm/");		
		
		if(validateTMDocument(doc)){
	        //store TopicMap 
			storeDocument(doc, tmResultXtm);
			formatXMLFile(tmResultXtm);
			return true;
        }else{
        	storeDocument(doc, tmResultXtm);
        	formatXMLFile(tmResultXtm);
        	return false;
        }
	}
	
	/**
	 * stores a W3C document under a given path
	 * @param doc Document which is stored
	 * @param path Path of the file in which the document is stored
	 */
	private void storeDocument(org.w3c.dom.Document doc, String path) {
		OutputFormat format = new OutputFormat(doc);
		format.setEncoding("UTF-8");
		
		format.setIndenting(true);
		format.setPreserveSpace(true);

		XMLSerializer xmlSerial;
		try {
			File f = new File(path);
			OutputStreamWriter outWriter= new OutputStreamWriter(new BufferedOutputStream(new
					FileOutputStream(f),4096),"UTF-8");
			
			xmlSerial = new XMLSerializer(outWriter, format);
			xmlSerial.asDOMSerializer();
			xmlSerial.serialize(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void formatXMLFile(String path){
		try {
			org.dom4j.io.SAXReader reader = new org.dom4j.io.SAXReader();
			org.dom4j.Document doc = reader.read(path);
			
			File outFile = new File(path);
			OutputStreamWriter outWriter= new OutputStreamWriter(new BufferedOutputStream(new
					FileOutputStream(outFile),4096),"UTF-8");
			
			org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(outWriter, format);
			writer.write(doc);
			writer.close();
			
			
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * By using castor xml a W3C document is created out of a java object and the belonging mapping file 
	 * 
	 * @param mappingPath Path of the mapping file which is required to map a specific object to a W3C document 
	 * @param obj the object which should be mapped
	 * @return the W3C document which represents the xml description of obj, null if mapping was not successful
	 */
	private Document createDocument(Object obj) {
		StringWriter stringWriter = new StringWriter();
		
		Document doc = null;
		try {
			//------------------
			Mapping mapping = new Mapping(); 
			mapping.loadMapping(this.getClass().getResource("TMxtmMapping.xml"));
			
			Marshaller marsh = new Marshaller(stringWriter);
			
			marsh.setMapping(mapping);
			marsh.setEncoding("UTF-8");
			marsh.marshal(obj);
			doc = StringWriterToDocument(stringWriter);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MappingException e) {
			e.printStackTrace();
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}

		return doc;
	}
	
	/**
	 * This method is used to convert a StringWriter to a W3C document
	 * 
	 * @param stringWriter
	 * @return
	 * @throws IOException
	 */
	private Document StringWriterToDocument(StringWriter stringWriter)
			throws IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		InputSource inputSource = new InputSource();
		String str = null;
		str = stringWriter.toString();
		inputSource.setCharacterStream(new java.io.StringReader(str));

		try {
			doc = builder.parse(inputSource);
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	/**
	 * By using castor xml a XtmTopicMap Object is created out of a xml file
	 * 
	 * @param path
	 * @return a XtmTopicMap Object
	 */
	private XtmTopicMap createXtmTMObject(String path) {
		Unmarshaller unmarsh;
		XtmTopicMap xtmTM = null;
		try {
			Mapping mapping = new Mapping(); 
			mapping.loadMapping(this.getClass().getResource("TMxtmMapping.xml"));
			unmarsh = new Unmarshaller(mapping);
			xtmTM = (XtmTopicMap) unmarsh.unmarshal(new InputStreamReader(new FileInputStream(tmResultXtm), "UTF-8"));
		} catch (MappingException e) {
			e.printStackTrace();
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return xtmTM;
	}

	/**
	 * validates a document according to the xtm 2.0 schema
	 * 
	 * @param doc
	 * @return true if the document fits the xtm 2.0 schema, otherwise false
	 */
	private boolean validateTMDocument(Document doc){
    	try {
    		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    		
    		factory.setErrorHandler(new ErrorHandler4XMLParser());
    		StreamSource ss = new StreamSource(this.getClass().getResource("XTM_2_0.xsd").openStream());
    		javax.xml.validation.Schema schema = factory.newSchema(ss);
    	
    		Validator validator = schema.newValidator();
    		ErrorHandler4XMLParser errHandler = new ErrorHandler4XMLParser(); 
    		validator.setErrorHandler(errHandler);
    		
    		DOMSource source = new DOMSource(doc);
    		validator.validate(source);
    		
    		return errHandler.valid;
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}

	}
	
}
