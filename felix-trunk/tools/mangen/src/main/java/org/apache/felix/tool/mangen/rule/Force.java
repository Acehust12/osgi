/*
 *   Copyright 2005 The Apache Software Foundation
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package org.apache.felix.tool.mangen.rule;

import java.util.List;
import java.util.Iterator;
import java.util.Set;

import java.util.regex.Pattern;

import org.apache.felix.tool.mangen.BundleJar;
import org.apache.felix.tool.mangen.OsgiPackage;

/**
 * Force a set of imports or exports globally.
 * <p>
 * Package names that match any of a list of regex based package name patterns
 * will be excluded from the list of possible imports. 
 *
 * @version $Revision: 29 $
 * @author <A HREF="mailto:robw@ascert.com">Rob Walker</A> 
 */
public class Force
        extends GenericRule
{
    //////////////////////////////////////////////////
    // STATIC VARIABLES
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    // STATIC PUBLIC METHODS
    //////////////////////////////////////////////////
    
    //////////////////////////////////////////////////
    // INSTANCE VARIABLES
    //////////////////////////////////////////////////
    
    //////////////////////////////////////////////////
    // CONSTRUCTORS
    //////////////////////////////////////////////////

    public Force()
    {
    }
    
    //////////////////////////////////////////////////
    // ACCESSOR METHODS
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    // PUBLIC INSTANCE METHODS
    //////////////////////////////////////////////////
    
    //////////////////////////////////////////////////
    // INTERFACE METHODS - Rule
    //////////////////////////////////////////////////
    
    /**
     * Iterate over the list of bundle jars, removing any imports
     * which match any of the specific package name Patterns
     */
    public void execute(List jarList)
    {
        for(Iterator i = jarList.iterator(); i.hasNext(); )
        {
            BundleJar bund = (BundleJar) i.next();
            rptOut.println("");            
            rptOut.println("> " + bund.getName() + " :");            
            
            forcePackages(bund.getPossibleImports(), "imports");
            forcePackages(bund.getPossibleExports(), "exports");
        }
    }
    
    /**
     * This rule can be used globally.
     */
    public boolean isUsableGlobally()
    {
        return true;
    }
    
    /**
     * This rule can be used locally.
     */
    public boolean isUsableLocally()
    {
        return true;
    }
    
    //////////////////////////////////////////////////
    // PROTECTED INSTANCE METHODS
    //////////////////////////////////////////////////
    
    /**
     * Force a set of packages into the list
     */
    public void forcePackages(Set set, String qualName)
    {
        Set pkgs = (Set) optionMap.get(qualName);
        if (set != null && pkgs != null)
        {
            for(Iterator i = pkgs.iterator(); i.hasNext(); )
            {
                Pattern patt = (Pattern) i.next();
                set.add(OsgiPackage.createStringPackage(patt.pattern()));
            }
        }
    }
    
    
    //////////////////////////////////////////////////
    // PRIVATE INSTANCE METHODS
    //////////////////////////////////////////////////
    
    //////////////////////////////////////////////////
    // STATIC INNER CLASSES
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    // NON-STATIC INNER CLASSES
    //////////////////////////////////////////////////
    
}
