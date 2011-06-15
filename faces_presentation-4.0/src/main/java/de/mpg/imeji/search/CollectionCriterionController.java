package de.mpg.imeji.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import de.mpg.imeji.beans.SessionBean;
import de.mpg.imeji.util.BeanHelper;
import de.mpg.jena.controller.CollectionController;
import de.mpg.jena.controller.SearchCriterion;
import de.mpg.jena.controller.SearchCriterion.Filtertype;
import de.mpg.jena.controller.SearchCriterion.ImejiNamespaces;
import de.mpg.jena.controller.SearchCriterion.Operator;
import de.mpg.jena.search.SearchResult;
import de.mpg.jena.vo.CollectionImeji;
import de.mpg.jena.vo.complextypes.util.ComplexTypeHelper;

public class CollectionCriterionController implements Serializable 
{
	
	private List<CollectionCriterion> collectionCriterionList;
	private int collectionPosition;
    private SessionBean sb;

	private List<SelectItem> collectionList;
	private List<CollectionImeji> collections;	
 
	public CollectionCriterionController()
	{
        sb = (SessionBean)BeanHelper.getSessionBean(SessionBean.class);
        try 
        {
			getCollectionList(); 
		} 
        catch (Exception e) 
		{
			e.printStackTrace();
		} 
		collectionCriterionList = new ArrayList<CollectionCriterion>(); 
		CollectionCriterion newCollection = new CollectionCriterion((collections));
		collectionCriterionList.add(newCollection);
		
	}     
	
	public void getUserCollections() throws Exception
	{      
        if (collections == null)
        {
        	collections = new ArrayList<CollectionImeji>();
	        CollectionController cc = new CollectionController(sb.getUser());
	        SearchResult results = cc.search(new ArrayList<SearchCriterion>(), null, -1, 0);
			collections = (List<CollectionImeji>) cc.load(results.getResults(), -1, 0);
			if(collectionCriterionList != null)
			{
				for(int i=0; i<collectionCriterionList.size(); i++)
				{
					collectionCriterionList.get(i).setCollections(collections);
				}
			}
        }
	}
	
	public List<SelectItem> getCollectionList() throws Exception 
	{
		getUserCollections();
        collectionList = new ArrayList<SelectItem>();
        collectionList.add(new SelectItem(null, ((SessionBean)BeanHelper.getSessionBean(SessionBean.class)).getLabel("select_collection")));
        for (CollectionImeji ci : collections)
        {
            collectionList.add(new SelectItem(ci.getId().toString(), ci.getMetadata().getTitle()));
        }
		return collectionList;
	}
	
	public void setCollectionList(List<SelectItem> collectionList) {
		this.collectionList = collectionList;
	}
	
	public String addCollection() {
		CollectionCriterion newCollection = new CollectionCriterion(collections);
		collectionCriterionList.add(collectionPosition+1,newCollection);
		return "";
	}
	
	public String removeCollection(){

			collectionCriterionList.remove(collectionPosition);
		return "";
	}

	public int getCollectionPosition() {
		return collectionPosition;
	}

	public void setCollectionPosition(int collectionPosition) {
		this.collectionPosition = collectionPosition;
	}

	public CollectionCriterionController(List<CollectionCriterion> collectionCriterion){
		setCollectionCriterionList(collectionCriterion);
	}
	
	public List<CollectionCriterion> getCollectionCriterionList(){
		return collectionCriterionList;
	}
	
    public void setCollectionCriterionList(List<CollectionCriterion> collectionCriterionList){
        this.collectionCriterionList = collectionCriterionList;
    }

	public void clearAllForms() {
        for (CollectionCriterion vo : collectionCriterionList)
        {
        	vo.clearCriterion();
        }
    }
		
	public String getSearchCriterion() 
	{
		List<SearchCriterion> scList = new ArrayList<SearchCriterion>();
		String errorMessage = "";
		boolean validSearch = false;

		for(CollectionCriterion collectionCriterion : collectionCriterionList)
		{ 
		    SearchCriterion subSC = new SearchCriterion(Operator.OR, new ArrayList<SearchCriterion>());
		    
			if(collectionCriterion.getSelectedCollectionId() != null && !"".equals(collectionCriterion.getSelectedCollectionId()))
			{    
			    SearchCriterion collSC = new SearchCriterion(Operator.AND, ImejiNamespaces.IMAGE_COLLECTION, collectionCriterion.getSelectedCollectionId(), Filtertype.URI);
			    subSC.getChildren().add(collSC);

			    for(MDCriterion mdc : collectionCriterion.getMdCriterionList())
    			{
    				if(mdc != null && mdc.getMdText() != null && !"".equals(mdc.getMdText().trim()) && mdc.getSelectedMdName()!= null && !"".equals(mdc.getSelectedMdName())) 
    				{ 
    					Operator op  = Operator.AND;
    					if (mdc.getLogicOperator() != null)
    					{
    						 op  = Operator.valueOf(mdc.getLogicOperator());
    					}
    					
    					//Create Search criterion
    					SearchCriterion mdSC = new SearchCriterion(op, new ArrayList<SearchCriterion>());

    					// Add metadata value
    					switch (ComplexTypeHelper.getComplexType(mdc.getSelectedStatement().getType()))
 				        {
 				        	case DATE:
 				        		String dop = mdc.getDateOperator();
 				        		Filtertype ft = Filtertype.valueOf(dop);
 				        		mdSC.getChildren().add(new SearchCriterion(Operator.AND, ImejiNamespaces.IMAGE_METADATA_DATE, mdc.getMdText(), ft));
 				        		break;
 				        	case GEOLOCATION:
 				        		//TODO to create a new searchcriterion with the 2 values as childs
 				        		mdSC.getChildren().add(new SearchCriterion(Operator.OR, ImejiNamespaces.IMAGE_METADATA_GEOLOCATION_LONGITUDE, mdc.getMdText(), Filtertype.REGEX));
 				        		mdSC.getChildren().add(new SearchCriterion(Operator.OR, ImejiNamespaces.IMAGE_METADATA_GEOLOCATION_LATITUDE, mdc.getMdText(), Filtertype.REGEX));
 				        		break;
 				        	case TEXT:
 				        		mdSC.getChildren().add(new SearchCriterion(Operator.AND, ImejiNamespaces.IMAGE_METADATA_TEXT, mdc.getMdText(), Filtertype.REGEX));
 				        		break;
 				        	case LICENSE:
 				        		//TODO
 				        		break;
 				        	case NUMBER:
 				        		String nop = mdc.getNumberOperator();
 				        		Filtertype ft1 = Filtertype.valueOf(nop);
 				        		mdSC.getChildren().add(new SearchCriterion(Operator.AND, ImejiNamespaces.IMAGE_METADATA_NUMBER, mdc.getMdText(), ft1));
 				        		break;
 				        	case PERSON:
 				        		mdSC.getChildren().add(new SearchCriterion(Operator.OR, ImejiNamespaces.IMAGE_METADATA_PERSON_FAMILY_NAME, mdc.getMdText(),  Filtertype.REGEX));
 				        		mdSC.getChildren().add(new SearchCriterion(Operator.OR,  ImejiNamespaces.IMAGE_METADATA_PERSON_GIVEN_NAME, mdc.getMdText(), Filtertype.REGEX));
 				        		mdSC.getChildren().add(new SearchCriterion(Operator.OR, ImejiNamespaces.IMAGE_METADATA_PERSON_ORGANIZATION_NAME, mdc.getMdText(),  Filtertype.REGEX));
 				        		break;
 				        	case PUBLICATION:
 				        		//TODO
 				        		break;
 				        	case URI:
 				        		//TODO
 				        		break;
 				        }
    					
    				    // Add metadata type
    					mdSC.getChildren().add(new SearchCriterion(Operator.AND, ImejiNamespaces.IMAGE_METADATA_NAMESPACE, mdc.getSelectedStatement().getName().toString(), Filtertype.URI));
    					subSC.getChildren().add(mdSC);
    					validSearch = true;
    				}
    				else
    				{
    					errorMessage = "No metadata selected!";
    				}
    			}
    		}
			else
			{
				errorMessage = "No collection selected!";
			}
		    scList.add(subSC);
		}
		if (!validSearch)
		{
			BeanHelper.error(errorMessage);
			return null;
		}
		String query = URLQueryTransformer.transform2URL(scList);
		return query;
	}
	
    protected String getNavigationString(){
        return "pretty:";
    }

    public void setCollections(List<CollectionImeji> collections)
    {
        this.collections = collections;
    }

    public List<CollectionImeji> getCollections()
    {
        return collections;
    }
    
    public int getSize()
    {
        return collectionCriterionList.size();
    }
	
}