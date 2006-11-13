package ${hibernatedaopackagename};
// Generated on ${date} by Appfuse Maven Plugin generator using Hibernate Tools ${version}
<#assign classbody>
import ${modelpackagename}.${pojo.getDeclarationName()};
<#assign declarationName = pojo.importType(pojo.getDeclarationName())>
/**
 * An class that use hibernate to implement the ${declarationName}Dao interface.
 *
 * @author   $Author: $
 * @version  $Revision:$, $Date: $
 */
 <#if pojo.isComponent()>
 <#else>
public class ${declarationName}DaoHibernate extends ${pojo.importType("org.appfuse.dao.hibernate.BaseDAOHibernate")}  implements ${declarationName}Dao
{
     /**
     * Retrieves all of the ${declarationName}s in the database.
     *
     *
     * @return  List of all ${declarationName} objects in the databaase.
     */
    public List get${declarationName}s()
    {
       return getHibernateTemplate().find("from ${declarationName}");
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
       log.info("TUNING OPPORTUNITY:Use of QBE can be optimized with specific query ",
                new Exception("QBE TUNING"));

            // filter on properties set in the incomming object.
            ${pojo.importType("org.springframework.orm.hibernate3.HibernateCallback")} callback = new HibernateCallback()
                {
                    public Object doInHibernate(${pojo.importType("org.hibernate.Session")} inSession) throws ${pojo.importType("org.hibernate.HibernateException")}
                    {
                        ${pojo.importType("org.hibernate.criterion.Example")} ex = Example.create(in${declarationName}).ignoreCase().enableLike(
                                ${pojo.importType("org.hibernate.criterion.MatchMode")}.ANYWHERE);

                        return inSession.createCriteria(${declarationName}.class).add(ex).list();
                    }
                };

            return (List) getHibernateTemplate().execute(callback);
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
    ${declarationName} returnValue = (${declarationName}) getHibernateTemplate().get(
                ${declarationName}.class, in${declarationName}Id);

        if (returnValue == null)
        {
            log.warn("uh oh, ${declarationName} with Identifier '" + in${declarationName}Id + "' not found...");
            throw new ${pojo.importType("org.springframework.orm.ObjectRetrievalFailureException")}(${declarationName}.class, in${declarationName}Id);
        }

        return returnValue;
    }

    /**
     * Saves a ${declarationName}'s information
     *
     * @param  in${declarationName}  the object to be saved
     */
    public void save${declarationName}(final ${declarationName} in${declarationName})
    {
    getHibernateTemplate().saveOrUpdate(in${declarationName});
    }

    /**
     * Removes a ${declarationName} from the database by ${declarationName}Id.
     *
     * @param  in${declarationName}Id  the ${declarationName}'s id
     */
    public void remove${declarationName}(final ${pojo.getJavaTypeName(clazz.identifierProperty, jdk5)} in${declarationName}Id)
    {
       getHibernateTemplate().delete(get${declarationName}(in${declarationName}Id));
    }
}
</#if>
</#assign>
${pojo.generateImports()
}
${classbody}

