package de.mpg.escidoc.faces.upload;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import de.mpg.escidoc.faces.container.collection.CollectionVO;
import de.mpg.escidoc.faces.item.ImejiItemVO;
import de.mpg.escidoc.faces.item.ItemVO;

/**
 * 
 * @author saquet
 *
 */
public class Upload 
{
	private List<ImejiItemVO> items = null;
	private String contentModelId = null;
	private CollectionVO collection = null;
	private Date startDate = null;
	private Date endDate = null;
		
	public Upload(String contentModelId) 
	{
		items = new ArrayList<ImejiItemVO>();
		this.contentModelId = contentModelId;
	}

	public List<ImejiItemVO> getItems() 
	{
		return items;
	}

	public void setItems(List<ImejiItemVO> items) 
	{
		this.items = items;
	}

	public Date getStartDate() 
	{
		return startDate;
	}

	public void setStartDate(Date startDate) 
	{
		this.startDate = startDate;
	}

	public Date getEndDate() 
	{
		return endDate;
	}

	public void setEndDate(Date endDate) 
	{
		this.endDate = endDate;
	}

	
}
