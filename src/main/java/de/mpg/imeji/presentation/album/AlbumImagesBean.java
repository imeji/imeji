/**
 * License: src/main/resources/license/escidoc.license
 */
package de.mpg.imeji.presentation.album;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import de.mpg.imeji.logic.controller.AlbumController;
import de.mpg.imeji.logic.controller.ItemController;
import de.mpg.imeji.logic.search.SearchResult;
import de.mpg.imeji.logic.search.vo.SearchQuery;
import de.mpg.imeji.logic.search.vo.SortCriterion;
import de.mpg.imeji.logic.security.Operations.OperationsType;
import de.mpg.imeji.logic.security.Security;
import de.mpg.imeji.logic.util.ObjectHelper;
import de.mpg.imeji.logic.vo.Album;
import de.mpg.imeji.logic.vo.CollectionImeji;
import de.mpg.imeji.logic.vo.Item;
import de.mpg.imeji.presentation.beans.Navigation;
import de.mpg.imeji.presentation.image.ImagesBean;
import de.mpg.imeji.presentation.session.SessionBean;
import de.mpg.imeji.presentation.session.SessionObjectsController;
import de.mpg.imeji.presentation.util.BeanHelper;
import de.mpg.imeji.presentation.util.ObjectLoader;

/**
 * {@link ImagesBean} within an {@link Album}: Used to browse {@link Item} of an {@link Album}
 * 
 * @author saquet (initial creation)
 * @author $Author$ (last modification)
 * @version $Revision$ $LastChangedDate$
 */
public class AlbumImagesBean extends ImagesBean
{
    private String id = null;
    private Album album;
    private URI uri;
    private SessionBean session;
    private CollectionImeji collection;
    private Navigation navigation;

    /**
     * Constructor
     */
    public AlbumImagesBean()
    {
        super();
        session = (SessionBean)BeanHelper.getSessionBean(SessionBean.class);
        navigation = (Navigation)BeanHelper.getApplicationBean(Navigation.class);
    }

    @Override
    public String getInitPage()
    {
        getNavigationString();
        uri = ObjectHelper.getURI(Album.class, id);
        loadAlbum();
        browseInit();
        browseContext = getNavigationString() + id;
        return "";
    }

    @Override
    public String getNavigationString()
    {
        return "pretty:albumBrowse";
    }

    @Override
    public SearchResult search(SearchQuery searchQuery, SortCriterion sortCriterion)
    {
        ItemController controller = new ItemController(session.getUser());
        return controller.search(uri, searchQuery, sortCriterion, null);
    }

    /**
     * Load the current album
     */
    public void loadAlbum()
    {
        album = ObjectLoader.loadAlbumLazy(uri, session.getUser());
    }

    @Override
    public void initFacets()
    {
        // NO FACETs FOR ALBUMS
    }

    /**
     * Remove the selected {@link Item} from the current {@link Album}
     * 
     * @return
     * @throws Exception
     */
    public String removeFromAlbum() throws Exception
    {
        removeFromAlbum(session.getSelected());
        session.getSelected().clear();
        return "pretty:";
    }

    /**
     * Remove selected {@link Item} from active {@link Album}
     * 
     * @return
     * @throws Exception
     */
    public String removeFromActiveAlbum() throws Exception
    {
        removeFromActive(session.getSelected());
        session.getSelected().clear();
        return "pretty:";
    }

    /**
     * Remove all current {@link Item} from {@link Album}
     * 
     * @return
     * @throws Exception
     */
    public String removeAllFromAlbum() throws Exception
    {
        removeAllFromAlbum(album);
        return "pretty:";
    }

    /**
     * Remove all current {@link Item} from active {@link Album}
     * 
     * @return
     * @throws Exception
     */
    public String removeAllFromActiveAlbum() throws Exception
    {
        removeAllFromAlbum(session.getActiveAlbum());
        return "pretty:";
    }

    /**
     * Remove all {@link Item} from an {@link Album}
     * 
     * @param album
     * @throws Exception
     */
    private void removeAllFromAlbum(Album album) throws Exception
    {
        album.setImages(new ArrayList<URI>());
        AlbumController ac = new AlbumController();
        ac.update(album, session.getUser());
    }

    /**
     * Remove a list of {@link Item} from the current {@link Album}
     * 
     * @param uris
     * @throws Exception
     */
    private void removeFromAlbum(List<String> uris) throws Exception
    {
        if (session.getActiveAlbum() != null
                && album.getId().toString().equals(session.getActiveAlbum().getId().toString()))
        {
            // if the current album is the active album as well
            removeFromActive(uris);
        }
        else
        {
            AlbumController ac = new AlbumController();
            album = (Album)ac.loadContainerItems(album, session.getUser(), -1, 0);
            int deletedCount = ac.removeFromAlbum(album, uris, session.getUser());
            BeanHelper.info(deletedCount + " " + session.getMessage("success_album_remove_images"));
        }
    }

    /**
     * Remove a list of {@link Item} from the active {@link Album}
     * 
     * @param uris
     * @throws Exception
     */
    private void removeFromActive(List<String> uris) throws Exception
    {
        SessionObjectsController soc = new SessionObjectsController();
        int before = session.getActiveAlbumSize();
        soc.removeFromActiveAlbum(uris);
        int deleted = before - session.getActiveAlbumSize();
        session.getSelected().clear();
        BeanHelper.info(deleted + " " + session.getMessage("success_album_remove_images"));
    }

    @Override
    public String getImageBaseUrl()
    {
        if (album == null || album.getId() == null)
            return "";
        return navigation.getApplicationUrl() + "album/" + this.id;
    }

    @Override
    public String getBackUrl()
    {
        return navigation.getBrowseUrl() + "/album" + "/" + this.id;
    }

    /**
     * Release current {@link Album}
     * 
     * @return
     */
    public String release()
    {
        ((AlbumBean)BeanHelper.getSessionBean(AlbumBean.class)).setId(id);
        ((AlbumBean)BeanHelper.getSessionBean(AlbumBean.class)).initView();
        ((AlbumBean)BeanHelper.getSessionBean(AlbumBean.class)).release();
        return "pretty:";
    }

    /**
     * Delete current {@link Album}
     * 
     * @return
     */
    public String delete()
    {
        ((AlbumBean)BeanHelper.getSessionBean(AlbumBean.class)).setId(id);
        ((AlbumBean)BeanHelper.getSessionBean(AlbumBean.class)).initView();
        ((AlbumBean)BeanHelper.getSessionBean(AlbumBean.class)).delete();
        return "pretty:albums";
    }

    /**
     * Withdraw current {@link Album}
     * 
     * @return
     * @throws Exception
     */
    public String withdraw() throws Exception
    {
        ((AlbumBean)BeanHelper.getSessionBean(AlbumBean.class)).setId(id);
        ((AlbumBean)BeanHelper.getSessionBean(AlbumBean.class)).initView();
        String dc = album.getDiscardComment();
        ((AlbumBean)BeanHelper.getSessionBean(AlbumBean.class)).getAlbum().setDiscardComment(dc);
        ((AlbumBean)BeanHelper.getSessionBean(AlbumBean.class)).withdraw();
        return "pretty:";
    }

    /**
     * Listener for the discard comment
     * 
     * @param event
     */
    @Override
    public void discardCommentListener(ValueChangeEvent event)
    {
        album.setDiscardComment(event.getNewValue().toString());
    }

    @Override
    public boolean isEditable()
    {
        Security security = new Security();
        return security.check(OperationsType.UPDATE, session.getUser(), album);
    }

    @Override
    public boolean isDeletable()
    {
        Security security = new Security();
        return security.check(OperationsType.DELETE, session.getUser(), album);
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
        // @Ye set session value to share with AlbumImageBean, another way is via injection
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("AlbumImagesBean.id", id);
    }

    public void setCollection(CollectionImeji collection)
    {
        this.collection = collection;
    }

    public CollectionImeji getCollection()
    {
        return collection;
    }

    public void setAlbum(Album album)
    {
        this.album = album;
    }

    public Album getAlbum()
    {
        return album;
    }
}
