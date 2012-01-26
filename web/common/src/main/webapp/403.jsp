<%@ include file="/common/taglibs.jsp"%>

<page:applyDecorator name="default">

<head>
    <title><fmt:message key="403.title"/></title>
    <meta name="heading" content="<fmt:message key='403.title'/>"/>
</head>

<p>
    <fmt:message key="403.message">
        <fmt:param><c:url value="/"/></fmt:param>
    </fmt:message>
</p>
<p style="text-align: center">
    <a href="http://www.flickr.com/photos/mcginityphoto/6716289593/" title="Tetons view across meadow by McGinityPhoto, on Flickr">
        <img src="http://farm8.staticflickr.com/7019/6716289593_d3aa14b86f_z.jpg" width="640" height="427" 
        alt="Tetons view across meadow" style="margin: 20px; border: 1px solid black"></a>
</p>
</page:applyDecorator>