<%@ tag body-content="empty" %>
<%@ attribute name="group" required="false" %>
<%@ attribute name="type" required="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%! private static final String WRO_NAMESPACE = " xmlns=\"http://www.isdc.ro/wro\""; %> 
<c:set var="base" value="${pageContext.request.contextPath}"/>
<c:if test="${empty group}"><c:set var="group" value="main"/></c:if>
<c:if test="${empty type}"><c:set var="type" value="js"/></c:if>
<c:if test="${not empty param.debug}">
    <c:set var="debugAssets" value="${param.debug}" scope="session"/>
</c:if>

<c:set var="xml">
  <jsp:include page="/WEB-INF/wro.xml" />
</c:set>

<%-- xslt template to extract web resources by group and type(css|js) and print its link/script --%>
<c:set var="xslt">
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">">
  <xsl:output omit-xml-declaration="yes" indent="no"/>
  <xsl:param name="group" />
  <xsl:param name="type" />
  <xsl:variable name="base" select="'${base}'"/>


  <xsl:template match="/groups/group[@name=$group]">
      <xsl:if test="$type = 'js'">
       <xsl:apply-templates select="js"/>
      </xsl:if>
      <xsl:if test="$type = 'css'">
       <xsl:apply-templates select="css"/>
      </xsl:if>
  </xsl:template>

  <xsl:template match="js">
     <xsl:element name="script">
        <xsl:attribute name="type">text/javascript</xsl:attribute>
        <xsl:attribute name="src"><xsl:call-template name="url" /></xsl:attribute>
        <xsl:text> </xsl:text>
     </xsl:element>
  </xsl:template>

  <xsl:template match="css">
     <xsl:element name="link">
        <xsl:attribute name="type">text/css</xsl:attribute>
        <xsl:attribute name="rel">stylesheet</xsl:attribute>
        <xsl:attribute name="href"><xsl:call-template name="url" /></xsl:attribute>
     </xsl:element>
  </xsl:template>

  <xsl:template name="url">
    <xsl:choose>
      <xsl:when test="starts-with(., 'classpath:META-INF/resources/webjars')">
        <xsl:value-of select="concat($base, substring(., 29))" />
      </xsl:when>
      <xsl:when test="starts-with(., 'http://') or starts-with(., 'https://')">
        <xsl:value-of select="." />
      </xsl:when>      
      <xsl:otherwise>
        <xsl:value-of select="concat($base, .)" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:transform>
</c:set>


<c:choose>
    <c:when test="${sessionScope.debugAssets}">
      <%-- web resources by type (css|js) --%>
      <x:transform 
          doc='<%= ((String)jspContext.getAttribute("xml")).replace(WRO_NAMESPACE, "") %>'
          xslt='<%= jspContext.getAttribute("xslt") %>'>
        <x:param name="group"  value="${group}" />
        <x:param name="type"  value="${type}" />
      </x:transform>
    </c:when>
    <c:otherwise>
      <c:choose>
        <c:when test="${type == 'css'}">
          <link rel="stylesheet" type="text/css" href="${base}/assets/v/${applicationScope.assetsVersion}/${group}.css"/>
        </c:when>
        <c:otherwise>
          <script type="text/javascript" src="${base}/assets/v/${applicationScope.assetsVersion}/${group}.js"></script>
        </c:otherwise>
      </c:choose>
    </c:otherwise>
</c:choose>