package ce.tm4scholion.tm.persistency.xtm;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Topic Map Engine - XTM Persistency - ErrorHandler4XMLParser
 * 
 * ErrorHandlerXMLParser is, as the name says, used to handle errors which can occur while parsing a document
 *
 */
public class ErrorHandler4XMLParser implements ErrorHandler {

	public boolean valid = true;
	
	/**
	 * empty constructor
	 *
	 */
	public ErrorHandler4XMLParser(){}
	
	
	public void error(SAXParseException arg0){
		valid = false;
		arg0.printStackTrace();

	}

	public void fatalError(SAXParseException arg0) throws SAXException {
		valid = false;
		arg0.printStackTrace();

	}

	public void warning(SAXParseException arg0) throws SAXException {
		valid = false;
		arg0.printStackTrace();

	}

}
