package de.mpg.imeji.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

import de.mpg.escidoc.services.framework.PropertyReader;

/**
 *  JavaBean for static content of the Faces solution.
 * @author saquet
 *
 */

public class StaticContentBean
{
   private boolean about = true;
   private boolean legal = true;
   private boolean blog = true;
    
    public StaticContentBean() throws IOException, URISyntaxException
    {
        if ("".equals(PropertyReader.getProperty("escidoc.faces.about.url")))
        {
            about = false;
        }
        
        if ("".equals(PropertyReader.getProperty("escidoc.faces.legal.url")))
        {
            legal = false;
        }
        
        if ("".equals(PropertyReader.getProperty("escidoc.faces.blog.url")))
        {
            blog = false;
        }
    }
    
    public String getHeaderLogo()
    {
        try
        {
            String html = "background-image: url( " + PropertyReader.getProperty("escidoc.faces.logo.url") + ");";
            
            return html;
        }
        catch (Exception e)
        {
            return " ";
        }
    }
    
    public String getLogoLink()
    {
        try
        {            
            return PropertyReader.getProperty("escidoc.faces.logo.link.url");
        }
        catch (Exception e)
        {
            return "#";
        }
    }
    
    /**
     * Get the HTML content of the Help page.
     * URL of the Help page is defined in properties.
     * @return
     * @throws URISyntaxException 
     * @throws IOException 
     */
    public String getHelpContent() throws IOException, URISyntaxException
    {    
        String html = "";
        
        try
        {
            String helpProp = PropertyReader.getProperty("escidoc.faces.help.url");
            html = getContent(new URL(helpProp));
        }
        catch (Exception e)
        {
            html = PropertyReader.getProperty("escidoc.faces.help.url") + " couldn't be loaded. Url might be either wrong or protected."
                    + "<br/><br/>"
                    + "Error message:"
                    + "<br/><br/>"
                    + e.toString();
        }

        return html;
    }
    
    /**
     * Get the HTML content of the Home page.
     * URL of the Home page is defined in properties.
     * @return
     * @throws URISyntaxException 
     * @throws IOException 
     */
    public String getHomeContent() throws IOException, URISyntaxException 
    {
        String html = "";
       
        try
        {
            html = getContent(new URL(PropertyReader.getProperty("escidoc.faces.home.url")));
        }
        catch (Exception e)
        {
            html = PropertyReader.getProperty("escidoc.faces.help.url") + " couldn't be loaded. Url might be either wrong or protected."
                + "<br/><br/>"
                + "Error message:"
                + "<br/><br/>"
                + e.toString();
        }

        return html;
    }
    
    /**
     * Get the HTML content of the About page.
     * URL of the About page is defined in properties.
     * @return
     * @throws URISyntaxException 
     * @throws IOException 
     */
    public String getAboutContent() throws IOException, URISyntaxException
    {
        String html = "";
        
        try
        {
            html = getContent(new URL(PropertyReader.getProperty("escidoc.faces.about.url")));
        }
        catch (Exception e)
        {
            html = PropertyReader.getProperty("escidoc.faces.help.url") + " couldn't be loaded. Url might be either wrong or protected."
                + "<br/><br/>"
                + "Error message:"
                + "<br/><br/>"
                + e.toString();
        }
        
        return html;
    }
    
    /**
     * Get the HTML content of the Legal page.
     * URL of the Legal page is defined in properties.
     * @return
     * @throws URISyntaxException 
     * @throws IOException 
     */
    public String getLegalContent() throws IOException, URISyntaxException
    {
        String html = "";
        
        try
        {
            html = getContent(new URL(PropertyReader.getProperty("escidoc.faces.legal.url")));
        }
        catch (Exception e)
        {
            html = PropertyReader.getProperty("escidoc.faces.help.url") + " couldn't be loaded. Url might be either wrong or protected."
                + "<br/><br/>"
                + "Error message:"
                + "<br/><br/>"
                + e.toString();
        }
        
        return html;
    }
    
    public String getConfirmationContent() throws IOException, URISyntaxException
    {    
        String html = "";
        
        try
        {
            html = getContent(new URL(PropertyReader.getProperty("escidoc.faces.confirmation.url")));
        }
        catch (Exception e)
        {
            html = PropertyReader.getProperty("escidoc.faces.confirmation.url") + " couldn't be loaded. Url might be either wrong or protected."
                    + "<br/><br/>"
                    + "Error message:"
                    + "<br/><br/>"
                    + e.toString();
        }

        return html;
    }
    
    /**
     * Get the html content of an {@link URL}
     * @param url
     * @return
     * @throws Exception
     */
    private String getContent(URL url) throws Exception
    {
        BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            url.openStream()));

        String inputLine = "";
        String content = "";

        while (inputLine != null)
        {
            inputLine = in.readLine();
            if (inputLine != null)
            {
                content += inputLine + "  ";
            }
        }
        
        in.close();
        
        return content;
    }

    public boolean isAbout()
    {
        return about;
    }

    public void setAbout(boolean about)
    {
        this.about = about;
    }

    public boolean isLegal()
    {
        return legal;
    }

    public void setLegal(boolean legal)
    {
        this.legal = legal;
    }

    public boolean isBlog()
    {
        return blog;
    }

    public void setBlog(boolean blog)
    {
        this.blog = blog;
    }
    
}