package ${managerimplpackagename};

// Generated on ${date} by Appfuse Maven Plugin generator using Hibernate Tools ${version}
<#assign classbody>
import ${modelpackagename}.${pojo.getDeclarationName()};
<#assign declarationName = pojo.importType(pojo.getDeclarationName())>
/**
 * An interface that provides a data management interface to the ${declarationName}
 * table.
 *
 * @author   $Author: $
 * @version  $Revision:$, $Date: $
 */
 <#if pojo.isComponent()>
 <#else>
public class ${declarationName}ManagerImpl extends ${pojo.importType("com.appfuse.manager.impl.BaseManager")} implements ${declarationName}Manager 
{
    /** The datamanagement DAO to access the ${declarationName} table. */
    private ${declarationName}Dao dao;

    /**
     * Set the DAO for communication with the data layer.
     *
     * @param  inDao
     */
    public void set${declarationName}Dao(final ${declarationName}Dao inDao)
    {
        this.dao = inDao;
    }

     /**
     * Retrieves all of the ${declarationName}s.
     *
     *
     * @return  List of all ${declarationName} objects in the database.
     */
    public ${pojo.importType("java.util.List")} get${declarationName}s()
    {
       return dao.get${declarationName}s();
    }
    
     /**
     * Retrieves all of the ${declarationName}s based on an example object.
     *
     * @param   in${declarationName}  A partially populated ${declarationName} object.
     *
     * @return  List of ${declarationName} objects that match the search criteria.
     */
    public ${pojo.importType("java.util.List")} get${declarationName}sByExample(final ${declarationName} in${declarationName})
    {
       return dao.get${declarationName}sByExample(in${declarationName});
    }

    /**
     * Gets ${declarationName} information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
     * found.
     *
     * @param   in${declarationName}Id  the ${declarationName}'s Id
     *
     * @return  ${declarationName} a populated ${declarationName} object
     */
    public ${declarationName} get${declarationName}(final ${pojo.getJavaTypeName(clazz.identifierProperty, jdk5)} in${declarationName}Id)
    {
       return dao.get${declarationName}(in${declarationName}Id);
    }

    /**
     * Saves a ${declarationName}'s information
     *
     * @param  in${declarationName}  the object to be saved
     */
    public void save${declarationName}(final ${declarationName} in${declarationName})
    {
      dao.save${declarationName}(in${declarationName});
    }

    /**
     * Removes a ${declarationName} from the database by ${declarationName}Id.
     *
     * @param  in${declarationName}Id  the ${declarationName}'s id
     */
    public void remove${declarationName}(final ${pojo.getJavaTypeName(clazz.identifierProperty, jdk5)} in${declarationName}Id)
    {
    dao.remove${declarationName}(in${declarationName}Id);
    }
}
</#if>
</#assign>
${pojo.generateImports()
}
${classbody}

