package de.mpg.imeji.presentation.session;

import java.net.URI;
import java.util.List;

import de.mpg.imeji.logic.controller.AlbumController;
import de.mpg.imeji.logic.controller.ItemController;
import de.mpg.imeji.logic.vo.Album;
import de.mpg.imeji.presentation.util.BeanHelper;

/**
 * SEt of methods to control objects that are stored in the {@link SessionBean}
 * 
 * @author saquet (initial creation)
 * @author $Author$ (last modification)
 * @version $Revision$ $LastChangedDate$
 */
public class SessionObjectsController
{
    private SessionBean session;

    /**
     * Default constructor: Initialize the {@link SessionBean}
     */
    public SessionObjectsController()
    {
        session = (SessionBean)BeanHelper.getSessionBean(SessionBean.class);
    }

    /**
     * Add the item to the {@link List} of selected {@link Item} stored in the {@link SessionBean}.
     * 
     * @param item
     */
    public void selectItem(URI itemURI)
    {
        if (!session.getSelected().contains(itemURI.toString()))
        {
            session.getSelected().add(itemURI.toString());
        }
    }

    /**
     * Remove the item from the {@link List} of selected {@link Item} stored in the {@link SessionBean}
     * 
     * @param item
     */
    public void unselectItem(URI itemURI)
    {
        if (session.getSelected().contains(itemURI.toString()))
        {
            session.getSelected().remove(itemURI.toString());
        }
    }

    /**
     * @param uris
     * @throws Exception
     */
    public void addToActiveAlbum(List<String> uris) throws Exception
    {
        AlbumController ac = new AlbumController();
        ac.addToAlbum(session.getActiveAlbum(), uris, session.getUser());
        reloadActiveAlbum();
    }

    /**
     * @param uris
     * @throws Exception
     */
    public void removeFromActiveAlbum(List<String> uris) throws Exception
    {
        AlbumController ac = new AlbumController();
        ac.removeFromAlbum(session.getActiveAlbum(), uris, session.getUser());
        reloadActiveAlbum();
    }

    /**
     * Reload active {@link Album} and set in the {@link SessionBean}
     */
    public void reloadActiveAlbum()
    {
        if (session.getActiveAlbum() != null)
        {
            ItemController ic = new ItemController();
            session.setActiveAlbum((Album)ic.loadContainerItems(session.getActiveAlbum(), session.getUser(), -1, 0));
        }
    }
}
