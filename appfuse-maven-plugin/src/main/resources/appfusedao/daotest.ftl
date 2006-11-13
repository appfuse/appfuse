package ${daopackagename};

// Generated on ${date} by Appfuse maven plugin generator using Hibernate Tools ${version}
import ${modelpackagename}.${pojo.getDeclarationName()};
<#assign classbody>
<#assign declarationName = pojo.importType(pojo.getDeclarationName())>
/**
 * An class that use hibernate to implement the ${declarationName}Dao interface.
 *
 * @author   $Author: $
 * @version  $Revision:$, $Date: $
 */
 <#if pojo.isComponent()>
 <#else>
public class ${declarationName}DaoTest extends BaseDAOTestCase
{
     private ${declarationName} createdValue;
     
     private String submitterName = "TESTER";
     
     /** This is the injected Dao we are testing. */
     private ${declarationName}Dao dao = null;
     
     /**
     * Setter for the ${declarationName}Dao.
     *
     * @param  inDao  value of the ${declarationName}Dao.
     */
    public void set${declarationName}Dao(${declarationName}Dao inDao)
    {
        this.dao = inDao;
    }

    /**
     * This method will test the add capability.
     */
    public void testAdd${declarationName}()
    {

        if (log.isDebugEnabled())
        {
            log.debug("Testing the add capability");
        }

        createdValue = create${declarationName}();

        // verify a primary key was assigned
        //assertNotNull(createdValue.get${declarationName}Id());

        // verify set fields are persisted after save
        assertEquals(submitterName,createdValue.getPrincipalModifiedBy());
        // @todo check other values here
        
        // Clean up the created value
        remove${declarationName}(createdValue);
        
    }

    /**
     * This method will test the retrieve capability.
     */
    public void testGet${declarationName}()
    {

        if (log.isDebugEnabled())
        {
            log.debug("Testing the retrieve capability");
        }

        createdValue = create${declarationName}();
        //@todo update the primary key here 
        //${declarationName} retrievedValue = dao.get${declarationName}(createdValue.getId());
        //assertNotNull(retrievedValue);
        //assertEquals(submitterName, retrievedValue.getPrincipalModifiedBy());
        // Clean up the created value
        remove${declarationName}(createdValue);
    }

    /**
     * This method will test the update capability.
     */
    public void testUpdate${declarationName}()
    {

        if (log.isDebugEnabled())
        {
            log.debug("Testing the update capability");
        }

        createdValue = create${declarationName}();
        // update a field and save
        String newSubmitterName = "TESTERUPDATE";
        createdValue.setPrincipalModifiedBy(newSubmitterName);
        Integer optimistic = createdValue.getOptimisticVersion();
        dao.save${declarationName}(createdValue);
        // check the saved value and make sure optimistic is working
        assertEquals(newSubmitterName, createdValue.getPrincipalModifiedBy());
        assertNotSame(optimistic, createdValue.getOptimisticVersion());
        // Clean up the created value
        remove${declarationName}(createdValue);
    }

    /**
     * This method will test the get values capability.
     */
    public void testGet${declarationName}s()
    {

        if (log.isDebugEnabled())
        {
            log.debug("Testing the get values capability");
        }

        createdValue = create${declarationName}();
        
        ${pojo.importType("java.util.List")} results = dao.get${declarationName}s();

        if (log.isDebugEnabled())
        {
            log.debug("results returned are " + results.size());
        }

        assertTrue(results.size() > 0);
        // Clean up the created value
        remove${declarationName}(createdValue);
    }

    /**
     * This method will test the get values by Example capability.
     */
    public void test${declarationName}sByExample()
    {

        if (log.isDebugEnabled())
        {
            log.debug("Testing the get values by Example capability");
        }

        
        createdValue = create${declarationName}();
        ${declarationName} queryValue = new ${declarationName}();
        queryValue.setlastModifiedDate = null;
        queryValue.setPrincipalModifiedBy(submitterName);

        ${pojo.importType("java.util.List")} results = dao.get${declarationName}sByExample(queryValue);

        if (log.isDebugEnabled())
        {
            log.debug("results returned for QBE are " + results.size());
        }

        assertTrue(results.size() == 1);
        // Clean up the created value
        remove${declarationName}(createdValue);
    }

    /**
     * This method will test the remove capability.
     */
    public void testRemove${declarationName}()
    {

        if (log.isDebugEnabled())
        {
            log.debug("Testing the remove capability");
        }

        createdValue = create${declarationName}();
        //@todo Add the primary key here
        //${declarationName}Id primaryKey = createdValue.getId();
        //dao.remove${declarationName}(primaryKey);

        //try
        //{
        //    dao.get${declarationName}(primaryKey);
        //    fail("${declarationName} found in database");
        //}
        //catch (${pojo.importType("org.springframework.orm.ObjectRetrievalFailureException")} ex)
        //{
        //   assertNotNull(ex.getMessage());
        //}
    }

    /**
     * This method will create an ${declarationName} for use in testing.
     *
     * @return  The value that was created.
     */
    public ${declarationName} create${declarationName}()
    {

        // Create an object
       createdValue = new ${declarationName}();
       createdValue.setPrincipalModifiedBy(submitterName);
       // @todo Set all required fields after here
       

        if (log.isDebugEnabled())
        {
            log.debug("Creating a ${declarationName} for testing");
        }

       dao.save${declarationName}(createdValue);

        return createdValue;
    }
    
    public void remove${declarationName}(${declarationName} in${declarationName})
    {
       //dao.remove${declarationName}(createdValue.getId());
    }
    
}
</#if>
</#assign>
${pojo.generateImports()
}
${classbody}

