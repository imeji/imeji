package de.mpg.escidoc.faces.item;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

import de.escidoc.schemas.contentstreams.x07.ContentStreamDocument.ContentStream;
import de.escidoc.schemas.item.x09.ItemDocument;
import de.escidoc.schemas.item.x09.ItemDocument.Item;
import de.escidoc.schemas.metadatarecords.x05.MdRecordDocument;
import de.escidoc.schemas.metadatarecords.x05.MdRecordDocument.MdRecord;
import de.escidoc.schemas.metadatarecords.x05.MdRecordsDocument.MdRecords;
import de.escidoc.schemas.relations.x03.RelationsDocument.Relations.Relation;
import de.mpg.escidoc.faces.metadata.MdsImejiItemVO;
import de.mpg.escidoc.faces.upload.helper.ImageHelper;
import de.mpg.escidoc.services.framework.PropertyReader;


public class ImejiItemVO {
	
    protected MdsImejiItemVO mdRecords = null;
    protected ItemDocument itemDoc = null;

	public ImejiItemVO(String title, String description){
		mdRecords = new MdsImejiItemVO(title, description);
    	itemDoc = ItemDocument.Factory.newInstance();
    	Item item = itemDoc.addNewItem();
    	System.out.println(item.getDomNode().getNamespaceURI());
    	
//    	MdRecord mdRec = (MdRecord)this.mdRecords;
    	MdRecordDocument mdRDoc = MdRecordDocument.Factory.newInstance();
    	mdRDoc.addNewMdRecord();
    	MdRecord mdRec = mdRDoc.getMdRecord();
    	mdRec.setName("escidoc");
    	
    	
    	XmlObject xmlObject = XmlObject.Factory.newInstance();
    	XmlCursor xmlCursor = xmlObject.newCursor();
    	xmlCursor.toNextToken();
    	xmlCursor.beginElement("imeji-metadata");
    	xmlCursor.toStartDoc();
    	xmlCursor.toNextToken();
    	
    	XmlCursor cursor = mdRec.newCursor();
    	cursor.toEndToken();
    	xmlCursor.moveXml(cursor);
    	cursor.dispose();
    	xmlCursor.dispose();
    	
    	MdRecords mdRecs = item.addNewMdRecords();
    	
    	mdRecs.addNewMdRecord();
    	mdRecs.setMdRecordArray(0,mdRec);
    	item.setMdRecords(mdRecs);
    	
    	item.addNewRelations();
    	//TODO: remove static collection id
//    	Relation rel = item.getRelations().addNewRelation();
//    	rel.setObjid("escidoc:201386");
//    	rel.setPredicate(XmlAnySimpleType.Factory.newValue("http://www.escidoc.de/ontologies/mpdl-ontologies/content-relations#isMemberOf"));
    	
    	//TODO remove static context id
    	item.addNewProperties().addNewContext();
    	item.getProperties().getContext().setObjid("escidoc:108013");

    	item.getProperties().addNewContentModel();
    	try {
			item.getProperties().getContentModel().setObjid(PropertyReader.getProperty("escidoc.faces.content-model.id"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}

	/**
	 * This method creates the three components (org, web, thumb resolution) for an imegji item
	 * @param inputStream
	 * @param fileName
	 * @param mimetype
	 * @param userHandle
	 * @throws Exception
	 */
	public void attachFile(BufferedImage bufferedImage, String fileName, String mimetype, String userHandle) throws Exception
	{
		ImageHelper imageHelper = new ImageHelper();
		itemDoc.getItem().addNewContentStreams();
		this.itemDoc.setItem(imageHelper.setComponent(imageHelper.getOrig(), itemDoc.getItem(), bufferedImage, fileName, mimetype, userHandle));
		this.itemDoc.setItem(imageHelper.setComponent(imageHelper.getWeb(), itemDoc.getItem(), bufferedImage, fileName, mimetype, userHandle));
		this.itemDoc.setItem(imageHelper.setComponent(imageHelper.getThumb(), itemDoc.getItem(), bufferedImage, fileName, mimetype, userHandle));
	}
	
	public MdsImejiItemVO getMdRecords() {
		return mdRecords;
	}

	public void setMdRecords(MdsImejiItemVO mdRecords) {
		this.mdRecords = mdRecords;
	}

	public ItemDocument getItemDocument() {
		return itemDoc;
	}

	public void setItem(ItemDocument item) {
		this.itemDoc = item;
	}
    
    
}
