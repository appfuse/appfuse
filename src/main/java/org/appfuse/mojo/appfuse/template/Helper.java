package org.appfuse.mojo.appfuse.template;

/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This is a sample class to demonstrate how a helper class can be constructed to be used in conjunction with the
 * freemarker templates.
 * 
 * @author <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version $Id: $
 */
public class Helper
{

    /**
     * Creates a new Helper object.
     */
    public Helper()
    {
        super();

    }

    /**
     * This method will convert the first character of a string to lower case. This can be used to convert standard
     * object names to label or variable names.
     * 
     * @param inName
     *            The name to be converted.
     * 
     * @return The input name with the first character converted to lower case.
     */
    public String convertName( final String inName )
    {

        // If the first character is already lower case then just return.
        if ( Character.isLowerCase( inName.charAt( 0 ) ) )
        {
            return inName;
        }

        // Make a copy of the string so we don't mess up the input string
        String tempName = new String( inName );

        // Get the first character and convert it to lower case
        char firstchar = tempName.charAt( 0 );
        char replacechar = Character.toLowerCase( firstchar );

        // Replace the first character with the converted character.
        tempName = tempName.replaceFirst( Character.toString( firstchar ), Character.toString( replacechar ) );

        // Return the converted string
        return tempName;
    }
}
