package ${webpackagename};
/*
 * Copyright 2005-2006 The Apache Software Foundation.
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

// Generated on ${date} by Appfuse maven plugin generator using Hibernate Tools ${version}
<#assign classbody>
import java.io.Serializable;

import ${webpackagename}.BasePage;
import ${modelpackagename}.${pojo.getDeclarationName()};
import ${basepackagename}.${pojo.getDeclarationName()}Manager;

<#assign declarationName = pojo.importType(pojo.getDeclarationName())>
/**
 * A class that implements the ${declarationName}List JSF Backing Bean.
 *
 * @author   $Author: $
 * @version  $Revision:$, $Date: $
 */
 <#if pojo.isComponent()>
 <#else>
public class ${declarationName}Form extends BasePage implements Serializable {

    /**
     * @todo - regenerate new serial identifier
     */
    private static final long serialVersionUID = 1L;

    /**
     * JSF Backing Bean model class object
     */
    private ${declarationName} ${declarationName?lower_case} = new ${declarationName}();

    /**
     * JSF Backing Bean model service class object
     */
    private ${declarationName} ${declarationName?lower_case}Manager;

    /**
     * Form Bean id
     */
    private String id;

    /**
     * Getter for JSF Backing Bean model object
     */
    public ${declarationName} get${declarationName}() {
	    return ${declarationName?lower_case};
    }

    /**
     * Setter for JSF Backing Bean model object
     */
    public void set${declarationName}(${declarationName} ${declarationName?lower_case}) {
        this.${declarationName?lower_case} = ${declarationName?lower_case};
    }

    /**
     * Getter for JSF Backing Bean model object service class
     */
    public ${declarationName}Manager getManager() {
        return ${declarationName?lower_case}Manager;
    }

    /**
     * Setter for JSF Backing Bean model object service class
     */
    public void set${declarationName}Manager(${declarationName}Manager ${declarationName?lower_case}Manager) {
        this.${declarationName?lower_case}Manager = ${declarationName?lower_case}Manager;
    }

    /**
     * Getter for Form id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for Form id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Cancel Java Server Faces (JSF) action method
     */
    public String cancel() {
                                 
        if (log.isDebugEnabled()) {
            log.debug("Entering 'cancel' method");
        }

        if (!StringUtils.equals(getParameter("from"), "list")) {
            return "mainMenu";
        } else {
            return "cancel";
        }
    }

    /**
     * Edit Java Server Faces (JSF) action method
     */
    public String edit() {
        return "edit";
    }

    /**
     * Save Java Server Faces (JSF) action method
     */
    public String save() {
		return "saved";
    }

    /**
     * Delete Java Server Faces (JSF) action method
     */
    public String delete() {
        return "deleted";
    }

}
</#if>
</#assign>
${pojo.generateImports()
}
${classbody}
