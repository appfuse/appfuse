package ${managerpackagename};

// Generated on ${date} by Appfuse Maven Plugin generator using Hibernate Tools ${version}
<#assign classbody>
import ${modelpackagename}.${pojo.getDeclarationName()};
<#assign declarationName = pojo.importType(pojo.getDeclarationName())>
/**
 * An interface that provides a transactional interface to the ${declarationName}
 * DAO.
 *
 * @author   $Author: $
 * @version  $Revision:$, $Date: $
 */
 <#if pojo.isComponent()>
 <#else>
public interface ${declarationName}Manager extends ${pojo.importType("com.appfuse.manager.Manager")} 
{
     /**
     * Retrieves all of the ${declarationName}s.
     *
     *
     * @return  List of all ${declarationName} objects in the database.
     */
    ${pojo.importType("java.util.List")} get${declarationName}s();
    
    /**
     * Retrieves all of the ${declarationName}s based on an example object.
     *
     * @param   in${declarationName}  A partially populated ${declarationName} object.
     *
     * @return  List of ${declarationName} objects that match the search criteria.
     */
    ${pojo.importType("java.util.List")} get${declarationName}sByExample(final ${declarationName} in${declarationName});

    /**
     * Gets ${declarationName} information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
     * found.
     *
     * @param   in${declarationName}Id  the ${declarationName}'s Id
     *
     * @return  ${declarationName} a populated ${declarationName} object
     */
    ${declarationName} get${declarationName}(final ${pojo.getJavaTypeName(clazz.identifierProperty, jdk5)} in${declarationName}Id);

    /**
     * Saves a ${declarationName}'s information
     *
     * @param  in${declarationName}  the object to be saved
     */
    void save${declarationName}(final ${declarationName} in${declarationName});

    /**
     * Removes a ${declarationName} from the database by ${declarationName}Id.
     *
     * @param  in${declarationName}Id  the ${declarationName}'s id
     */
    void remove${declarationName}(final ${pojo.getJavaTypeName(clazz.identifierProperty, jdk5)} in${declarationName}Id);
}
</#if>
</#assign>
${pojo.generateImports()
}
${classbody}

