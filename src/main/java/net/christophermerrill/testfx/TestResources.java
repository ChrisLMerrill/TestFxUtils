package net.christophermerrill.testfx;

import java.io.*;
import java.net.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestResources
    {
    @SuppressWarnings("null")
    public static File getFile(String path, Class source_class)
        {
        try
            {
            URL resource = source_class.getClassLoader().getResource(path);
            if (resource == null)
                throw new RuntimeException("Unable to find " + path + ", or do not have authority to access");
            return new File(resource.toURI());
            }
        catch (URISyntaxException e)
            {
            throw new RuntimeException("didn't expect to get this from the classloader", e);
            }
        }
    }




