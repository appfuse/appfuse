// There is no package since the Maven Archetype plugin doesn't support package expansion
// for multi-module archetypes: http://jira.codehaus.org/browse/ARCHETYPE-23.

public class Core {
    public static String getHello() {
        return "Hello";
    }
}
