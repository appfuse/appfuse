<?xml version="1.0"?>
<!--
  Excerpted from the book, "Pragmatic Project Automation"
  ISBN 0-9745140-3-9
  Copyright 2004 The Pragmatic Programmers, LLC.  All Rights Reserved.
  Visit www.PragmaticProgrammer.com
 -->


<xsl:stylesheet
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
   version="1.0">
  
  <xsl:output method="xml" indent="yes"/>
  
  <xsl:template match="/">

    <rss version="2.0">    
      <channel>      

        <title>      
          AppFuse Build Status 
        </title>      

        <link>
          http://localhost:8080/cruisecontrol 
        </link>
      
        <description>
          CruiseControl build status feed for the AppFuse project.
        </description>

        <item>

          <xsl:variable name="project.name" 
	    select="cruisecontrol/info/property[@name='projectname']/@value"/>
          <xsl:variable name="build.date" 
            select="cruisecontrol/info/property[@name='builddate']/@value"/>
          <xsl:variable name="build.time" 
            select="cruisecontrol/build/@time"/>
          <xsl:variable name="modification.list"
            select="cruisecontrol/modifications/modification"/>

	  <xsl:variable name="testsuite.list" select="//testsuite"/>
	  <xsl:variable name="testcase.list" select="$testsuite.list/testcase"/>
          <title>
            <xsl:if test="cruisecontrol/build/@error">
              BUILD FAILED
            </xsl:if>
            <xsl:if test="not (cruisecontrol/build/@error)">
              Build Succeeded
            </xsl:if>
          </title>
          <link>
            http://localhost:8080/cruisecontrol/buildresults/<xsl:value-of select="$project.name"/>
          </link>
          <description>      
            <xsl:if test="cruisecontrol/build/@error">
              &lt;b&gt;Ant Error Message:&lt;/b&gt;&lt;br/&gt; 
              <xsl:value-of select="cruisecontrol/build/@error"/>
            </xsl:if>
            <xsl:if test="not (cruisecontrol/build/@error)">
              &lt;b&gt;Build:&lt;/b&gt; 
              <xsl:value-of select="cruisecontrol/info/property[@name='label']/@value"/>
            </xsl:if>

	    &lt;br/&gt;
	    &lt;br/&gt;

            &lt;b&gt;Date of build:&lt;/b&gt; 
	    <xsl:value-of select="$build.date"/>&lt;br/&gt;
            
	    &lt;b&gt;Time to build:&lt;/b&gt; 
	    <xsl:value-of select="$build.time"/>&lt;br/&gt;
	    
            <xsl:apply-templates select="$modification.list">
              <xsl:sort select="date" order="descending" data-type="text" />
            </xsl:apply-templates>
	
	    &lt;br/&gt;

	    &lt;b&gt;Unit Tests:&lt;/b&gt;
	    <xsl:value-of select="count($testcase.list)" />
    
          </description>  
        </item>
      </channel>
    </rss>
  </xsl:template>
  
  <xsl:template match="modification">
    <xsl:if test="position() = 1">
      
      &lt;br/&gt;

      &lt;b&gt;Last changed:&lt;/b&gt; 
      <xsl:value-of select="date"/>&lt;br/&gt;
     
      &lt;b&gt;Last changed by:&lt;/b&gt; 
      <xsl:value-of select="user"/>&lt;br/&gt;
       
      &lt;b&gt;Last log entry:&lt;/b&gt; 
      <xsl:value-of select="comment"/>&lt;br/&gt;
    
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>
