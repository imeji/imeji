/*
 *
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License"). You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or http://www.escidoc.de/license.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 */
/*
 * Copyright 2006-2007 Fachinformationszentrum Karlsruhe Gesellschaft
 * für wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Förderung der Wissenschaft e.V.
 * All rights reserved. Use is subject to license terms.
 */
package de.mpg.imeji.logic.storage.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.mpg.imeji.logic.storage.Storage;
import de.mpg.imeji.logic.storage.UploadResult;
import de.mpg.imeji.logic.storage.administrator.StorageAdministrator;
import de.mpg.imeji.logic.storage.internal.InternalStorageItem;
import de.mpg.imeji.logic.storage.internal.InternalStorageManager;
import de.mpg.imeji.logic.storage.util.StorageUtils;

/**
 * imeji internal {@link Storage}
 * 
 * @author saquet (initial creation)
 * @author $Author$ (last modification)
 * @version $Revision$ $LastChangedDate$
 */
public class InternalStorage implements Storage
{
    private final String name = "internal";
    private InternalStorageManager manager;

    /**
     * Default Constructor
     */
    public InternalStorage()
    {
        try
        {
            manager = new InternalStorageManager();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error initialising InternalStorageManager: ", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see de.mpg.imeji.logic.storage.Storage#upload(byte[])
     */
    @Override
    public UploadResult upload(String filename, File file, String collectionId)
    {
        InternalStorageItem item = manager.createItem(file, filename, collectionId);
        return new UploadResult(item.getId(), item.getOriginalUrl(), item.getWebUrl(), item.getThumbnailUrl());
    }

    /*
     * (non-Javadoc)
     * @see de.mpg.imeji.logic.storage.Storage#read(java.lang.String)
     */
    @Override
    public void read(String url, OutputStream out, boolean close)
    {
        try
        {
            FileInputStream fis = new FileInputStream(manager.transformUrlToPath(url));
            StorageUtils.writeInOut(fis, out, close);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error reading file " + manager.transformUrlToPath(url)
                    + " in internal storage: ", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see de.mpg.imeji.logic.storage.Storage#delete(java.lang.String)
     */
    @Override
    public void delete(String id)
    {
        manager.removeItem(new InternalStorageItem(id));
    }

    /*
     * (non-Javadoc)
     * @see de.mpg.imeji.logic.storage.Storage#update(java.lang.String, byte[])
     */
    @Override
    public void update(String url, File file)
    {
        try
        {
            manager.replaceFile(file, url);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error updating file " + manager.transformUrlToPath(url)
                    + " in internal storage: ", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see de.mpg.imeji.logic.storage.Storage#getName()
     */
    @Override
    public String getName()
    {
        return name;
    }

    /*
     * (non-Javadoc)
     * @see de.mpg.imeji.logic.storage.Storage#getAdminstrator()
     */
    @Override
    public StorageAdministrator getAdministrator()
    {
        return manager.getAdministrator();
    }

    /*
     * (non-Javadoc)
     * @see de.mpg.imeji.logic.storage.Storage#getCollectionId(java.lang.String)
     */
    @Override
    public String getCollectionId(String url)
    {
        return url.replace(manager.getStorageUrl(), "").split("/", 2)[0];
    }
}
