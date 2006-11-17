package ${webpackagename};
/*
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
import ${modelpackagename}.${pojo.getDeclarationName()};
<#assign declarationName = pojo.importType(pojo.getDeclarationName())>
/**
 * A class that implements the ${declarationName}List JSF Backing Bean.
 *
 * @author   $Author: $
 * @version  $Revision:$, $Date: $
 */
 <#if pojo.isComponent()>
 <#else>
public class ${declarationName}List extends BasePage implements Serializable {

    /**
     * @todo - regenerate new serial identifier
     */
    private static final long serialVersionUID = 1L;

    /**
     * Java Server Faces (JSF) Backing Bean model service class object
     */
    private ${declarationName}Manager manager;

    /**
     * Setter for service class or Manager
     */
    public void set${declarationName}Manager(${declarationName}Manager manager) {
        this.manager = manager;
    }
    /**
     * Returns List of model objects that back Java Server Faces (JSF) page
     */
    public List get${declarationName}s() {
        return manager.get${declarationName}s(null);
    }
}
</#if>
</#assign>
${pojo.generateImports()
}
${classbody}

