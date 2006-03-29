<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="activeUsers.title"/></title>
<content tag="heading"><fmt:message key="activeUsers.heading"/></content>
<body id="activeUsers"/>

<p><fmt:message key="activeUsers.message"/></p>

<div class="separator"></div>

<button type="button" onclick="location.href='mainMenu.html'">
    <fmt:message key="button.cancel"/>
</button>

<f:view>

<f:loadBundle var="text" basename="#{basePage.bundleName}"/>

<h:form id="activeUsersForm">   

<t:buffer into="#{table}">            
<t:dataTable id="activeUsers" var="user" value="#{activeUserList.users}"
     rows="20"
     sortColumn="#{activeUserList.sort}"
     sortAscending="#{activeUserList.ascending}"
     styleClass="activeUserList list"
     rowClasses="standardTable_Row1,standardTable_Row2"
     columnClasses="standardTable_Column,standardTable_Column">
    <t:column width="135px" 
        headerstyleClass="#{activeUserList.sort == 'username' ? 'standardTable_SortHeader' : 'standardTable_Header'}">
        <f:facet name="header">
            <t:commandSortHeader columnName="username" arrow="false">
                <f:facet name="ascending">
                    <t:graphicImage value="/images/arrow_up.png" rendered="true"
                        border="0" style="position: relative; left: -7px"/>
                </f:facet>
                <f:facet name="descending">
                    <t:graphicImage value="/images/arrow_down.png" rendered="true" 
                        border="0" style="position: relative; left: -7px"/>
                </f:facet>
                <h:outputText value="#{text['user.username']}" /> 
                <t:graphicImage value="/images/arrow_off.png" rendered="true" 
                    border="0" style="padding-left: 5px;"/>                
            </t:commandSortHeader>
        </f:facet>
        <h:outputText value="#{user.username}" />
    </t:column>
    <t:column
        headerstyleClass="#{activeUserList.sort == 'fullName' ? 'standardTable_SortHeader' : 'standardTable_Header'}">
        <f:facet name="header">
            <t:commandSortHeader columnName="fullName" arrow="false">
                <f:facet name="ascending">
                    <t:graphicImage value="/images/arrow_up.png" rendered="true"
                        border="0" style="position: relative; left: -7px"/>
                </f:facet>
                <f:facet name="descending">
                    <t:graphicImage value="/images/arrow_down.png" rendered="true" 
                        border="0" style="position: relative; left: -7px"/>
                </f:facet>
                <h:outputText value="#{text['activeUsers.fullName']}" />
                <t:graphicImage value="/images/arrow_off.png" rendered="true" 
                    border="0" style="padding-left: 5px;"/>              
            </t:commandSortHeader>
        </f:facet>
        <h:outputText value="#{user.fullName}" />
        <h:outputLink value="mailto:#{user.email}" rendered="#{not empty user.email}">
        <h:graphicImage style="padding-left: 10px;" alt="#{text['icon.email']}" 
            url="/images/iconEmail.gif" />
        </h:outputLink> 
    </t:column>
</t:dataTable>
</t:buffer>

<t:buffer into="#{scroller}"> 
<h:panelGrid columns="1" styleClass="scroller" columnClasses="standardTable_ColumnCentered" >
<t:dataScroller id="scroll"    for="activeUsers"
    fastStep="10" pageCountVar="pageCount"
    pageIndexVar="pageIndex" styleClass="scroller"
    paginator="true" paginatorMaxPages="9"
    paginatorTableClass="paginator"    paginatorActiveColumnClass="currentPage">
    <f:facet name="first" >
        <t:graphicImage url="/images/arrow-first.gif"/>
    </f:facet>
    <f:facet name="last">
        <t:graphicImage url="/images/arrow-last.gif"/>
    </f:facet>
    <f:facet name="previous">
        <t:graphicImage url="/images/arrow-previous.gif"/>
    </f:facet>
    <f:facet name="next">
        <t:graphicImage url="/images/arrow-next.gif"/>
    </f:facet>
    <f:facet name="fastforward">
        <t:graphicImage url="/images/arrow-ff.gif"/>
    </f:facet>
    <f:facet name="fastrewind">
        <t:graphicImage url="/images/arrow-fr.gif"/>
    </f:facet>
</t:dataScroller>
</h:panelGrid>
</t:buffer>

<t:buffer into="#{summary}"> 
<h:panelGrid columns="1" styleClass="scroller" columnClasses="standardTable_ColumnCentered" >
<t:dataScroller id="summary"
        for="activeUsers"
        rowsCountVar="rowsCount"
        displayedRowsCountVar="displayedRowsCountVar"
        firstRowIndexVar="firstRowIndex"
        lastRowIndexVar="lastRowIndex"
        pageCountVar="pageCount"
        pageIndexVar="pageIndex">
    <h:outputFormat value="#{text['activeUsers.summary']}">
        <f:param value="#{rowsCount}" />
        <f:param value="#{displayedRowsCountVar}" />
        <f:param value="#{firstRowIndex}" />
        <f:param value="#{lastRowIndex}" />
        <f:param value="#{pageIndex}" />
        <f:param value="#{pageCount}" />
    </h:outputFormat>
</t:dataScroller>  
</h:panelGrid>
</t:buffer>

<h:outputText value="#{summary}" escape="false"/>
<h:outputText value="#{table}" escape="false"/>
<h:outputText value="#{scroller}" escape="false"/>

</h:form>
</f:view>    