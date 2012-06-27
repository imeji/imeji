/**
 * License: src/main/resources/license/escidoc.license
 */
package de.mpg.imeji.logic.vo.predefinedMetadata;

import java.net.URI;

import de.mpg.imeji.logic.util.DateFormatter;
import de.mpg.imeji.logic.vo.Metadata;
import de.mpg.j2j.annotations.j2jDataType;
import de.mpg.j2j.annotations.j2jId;
import de.mpg.j2j.annotations.j2jLiteral;
import de.mpg.j2j.annotations.j2jResource;

@j2jResource("http://imeji.org/terms/metadata")
@j2jDataType("http://imeji.org/terms/metadata#date")
@j2jId(getMethod = "getId", setMethod = "setId")
public class Date extends Metadata
{
    @j2jLiteral("http://imeji.org/terms/dateString")
    private String date;
    @j2jLiteral("http://imeji.org/terms/dateTime")
    private long dateTime;
    @j2jResource("http://imeji.org/terms/statement")
    private URI statement;

    public Date()
    {
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public long getDateTime()
    {
        return dateTime;
    }

    public void setDateTime(long dateTime)
    {
        this.dateTime = dateTime;
    }

    @Override
    public URI getStatement()
    {
        return statement;
    }

    @Override
    public void setStatement(URI namespace)
    {
        this.statement = namespace;
    }

    @Override
    public void init()
    {
        if (date != null)
        {
            dateTime = DateFormatter.getTime(date);
        }
        setSearchValue(date);
    }

    @Override
    public void copy(Metadata metadata)
    {
        if (metadata instanceof Date)
        {
            this.date = ((Date)metadata).getDate();
            this.dateTime = ((Date)metadata).getDateTime();
            copyMetadata(metadata);
        }
    }
}
